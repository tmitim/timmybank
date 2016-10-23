package com.tmitim.timmybank;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.skife.jdbi.v2.DBI;

import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class BasicApplication extends Application<BasicConfiguration> {

	public static void main(String[] args) throws Exception {
		new BasicApplication().run(args);
	}

	@Override
	public String getName() {
		return "dropwizard-base-project";
	}

	@Override
	public void initialize(Bootstrap<BasicConfiguration> bootstrap) {
		bootstrap.setConfigurationSourceProvider(
			new SubstitutingSourceProvider(
				bootstrap.getConfigurationSourceProvider(),
				new EnvironmentVariableSubstitutor()
			)
		);
	}

	@Override
	public void run(BasicConfiguration configuration, Environment environment) {
		// Enable CORS headers
		final FilterRegistration.Dynamic cors = environment.servlets().addFilter("CORS", CrossOriginFilter.class);

		// Configure CORS parameters
		cors.setInitParameter("allowedOrigins", "*");
		cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
		cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

		// Add URL mapping
		cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

		final DBIFactory factory = new DBIFactory();
		final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "mysql");

		final TaskDao taskDao = jdbi.onDemand(TaskDao.class);
		final BasicResource basicResource = new BasicResource(taskDao);

		environment.jersey().register(basicResource);
		environment.healthChecks().register("base", new BasicHealthCheck());

		System.out.println("Running Server");
	}
}
