package runner;

import java.net.InetAddress;

import config.ServerConfigure;
import http.HttpServer;
import util.StringUtil;

public class ServerRunner {

	public static void initServer() {

		// ����IP
		try {
			// windows��ȡ����IP
			InetAddress localHost = InetAddress.getLocalHost();
			ServerConfigure.ip = localHost.getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (StringUtil.isEmpty(ServerConfigure.ip)) {
			System.err.println("û�л�ȡ������IP");
			System.exit(1);
		}

		ServerConfigure.port = 8088;
	}

	public static void startServer() {

		HttpServer.start();
	}

}
