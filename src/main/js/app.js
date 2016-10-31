(function(angular){

	'use strict';

	var localHost = 'http://localhost:8090';

	var app = angular.module('timmybank', ['ngRoute']);

	app.config(function ($httpProvider) {
	  $httpProvider.defaults.headers.common = {};
	  $httpProvider.defaults.headers.post = {};
	  $httpProvider.defaults.headers.put = {};
	  $httpProvider.defaults.headers.patch = {};
	});

	app.config(['$routeProvider', function($routeProvider) {

		$routeProvider
			.when('/tasks/:userId', {
				template: '<task-list></task-list>'
			})
			.when('/accountable/:userId', {
				template: '<accountable-list></accountable-list>'
			})
			.when('/task/:userId', {
				template: '<create-task></create-task>'
			})
			.otherwise({
				redirectTo: '/tasks/:userId'
			});
	}]);

	app.service('userService', function(){
		var user = {
			id: '1'
		}

		return user;
	})

	app.service('tabNumber', function() {
		var tab = {
			id: ''
		}
		return tab;
	})

	app.component('navBar', {
		templateUrl: '/src/main/components/nav-bar.html',
		controller: ['tabNumber', 'userService', function(tabNumber, userService){
			var vm = this;
			vm.selectedTab = tabNumber;
			vm.user = userService;
		}]
	})

	app.component('taskList', {
		templateUrl: '/src/main/components/task-list.html',
		controller: [
			'$http', 
			'tabNumber', 
			'userService', 
			'$routeParams', 
			function(
				$http, 
				tabNumber, 
				userService, 
				$routeParams
			){
				var vm = this;
				tabNumber.id = 1;
				userService.id = $routeParams.userId;

				$http.get(localHost + '/api/task/user/' + userService.id)
					.then(function(response) {
						vm.tasks = response.data;
					});
			}]
	});

	app.component('accountableList', {
		templateUrl: '/src/main/components/accountable-list.html',
		controller: [
			'$http', 
			'tabNumber', 
			'userService', 
			'$routeParams', 
			function(
				$http, 
				tabNumber, 
				userService, 
				$routeParams
			){
				var vm = this;
				userService.id = $routeParams.userId;

				vm.updateTaskStatus = updateTaskStatus;

				tabNumber.id = 2;

				function updateTaskStatus(id) {
					$http.put(localHost + '/api/task/' + id)
						.then(function(){
							document.getElementById('task_' + id).innerHTML = '<svg class="done" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 32 40"><path d="M28.245,4.86C20.391,9.676,14.691,15.751,12.131,18.8L5.86,13.887L3.09,16.12l10.836,11.02  c1.865-4.777,7.771-14.113,14.983-20.746L28.245,4.86z"/></svg>';
						})
				}

				$http.get(localHost + '/api/task/partner/' + userService.id)
					.then(function(response) {
						vm.accountableTasks = response.data;
					});
			}]
	});

	app.component('createTask', {
		templateUrl: '/src/main/components/create-task.html',
		controller: [
			'$http', 
			'tabNumber', 
			'$routeParams', 
			'userService', 
			'$location', 
			function(
				$http, 
				tabNumber, 
				$routeParams, 
				userService, 
				$location
			){
				var vm = this;

				tabNumber.id = 3;
				userService.id = $routeParams.userId;
				vm.createTask = createTask;

				function createTask() {
					var postObj = {
						userId: userService.id,
						accountableId: vm.accountableId,
						completed: false,
						message: vm.message,
						amount: parseInt(vm.amount)
					}

					$http({
						method: 'POST',
						url: localHost + '/api/task',
						data: JSON.stringify(postObj),
						headers: {
							'Content-Type': 'application/json'
					}}).then(function(){
						$location.path( '/tasks/' + postObj.userId );
					});
				}
			}]
	})

})(angular);