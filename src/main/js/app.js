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
		controller: ['$http', 'tabNumber', '$routeParams', function($http, tabNumber, $routeParams){
			var vm = this;
			vm.selectedTab = tabNumber.id;
			vm.userId = $routeParams.userId;
		}]
	})

	app.component('taskList', {
		templateUrl: '/src/main/components/task-list.html',
		controller: ['$http', 'tabNumber', '$routeParams', function($http, tabNumber, $routeParams){
			var vm = this;
			tabNumber.id = 1;
			vm.userId = $routeParams.userId;

			$http.get(localHost + '/api/task/user/' + vm.userId)
				.then(function(response) {
					vm.tasks = response.data;
				});
		}]
	});

	app.component('accountableList', {
		templateUrl: '/src/main/components/accountable-list.html',
		controller: ['$http', 'tabNumber', '$routeParams', function($http, tabNumber, $routeParams){
			var vm = this;
			vm.userId = $routeParams.userId;

			vm.updateTaskStatus = updateTaskStatus;

			tabNumber.id = 2;

			function updateTaskStatus(id) {
				$http.put(localHost + '/api/task/' + id)
					.then(function(){
						document.getElementById('task_' + id).innerHTML = '<svg class="done" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 32 40"><path d="M28.245,4.86C20.391,9.676,14.691,15.751,12.131,18.8L5.86,13.887L3.09,16.12l10.836,11.02  c1.865-4.777,7.771-14.113,14.983-20.746L28.245,4.86z"/></svg>';
					})
			}

			$http.get(localHost + '/api/task/partner/' + vm.userId)
				.then(function(response) {
					vm.accountableTasks = response.data;
					console.log(response.data);
				});
		}]
	});

	app.component('createTask', {
		templateUrl: '/src/main/components/create-task.html',
		controller: ['$http', 'tabNumber', '$routeParams', '$location', function($http, tabNumber, $routeParams, $location){
			var vm = this;

			tabNumber.id = 3;
			vm.userId = $routeParams.userId;
			vm.createTask = createTask;
			vm.message = '';
			vm.accountableId = '';
			vm.amount = '';

			var postObj = {
				userId: vm.userId,
				accountableId: vm.accountableId,
				completed: false,
				message: vm.message,
				amount: vm.amount
			}

			function createTask() {
				postObj.message = vm.message;
				postObj.accountableId = vm.accountableId;
				postObj.amount = parseInt(vm.amount);

				console.log(postObj);

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