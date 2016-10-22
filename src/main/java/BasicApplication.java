import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
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

		final BasicResource basicResource = new BasicResource();

		environment.jersey().register(basicResource);
		environment.healthChecks().register("base", new BasicHealthCheck());
	}
}
