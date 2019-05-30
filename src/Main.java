import config.Configure;
import runner.CustomerRunner;
import runner.ServerRunner;

public class Main {

	public static void main(String[] args) {

		init();

		if ("SERVER".equals(Configure.serviceType)) {
			ServerRunner.initServer();
			ServerRunner.startServer();
		} else if ("CUSTOMER".equals(Configure.serviceType)) {
			CustomerRunner.initCustomer();
			CustomerRunner.startCustomer();
		}
	}

	private static void init() {
		Configure.serviceType = "SERVER";
		// // Configure.fromFilePackagePath = "C:\\Users\\Administrator\\Desktop\\filesplit\\catalina.out";
		// Configure.toFilePackagePath = "C:\\Users\\Administrator\\Desktop\\filesplit\\toFile";
	}

}
