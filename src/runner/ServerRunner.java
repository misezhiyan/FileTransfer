package runner;

import java.net.InetAddress;

import config.ServerConfigure;
import http.HttpServer;
import util.StringUtil;

public class ServerRunner {

	public static void initServer() {

		// 服务IP
		try {
			// windows获取本地IP
			InetAddress localHost = InetAddress.getLocalHost();
			ServerConfigure.ip = localHost.getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (StringUtil.isEmpty(ServerConfigure.ip)) {
			System.err.println("没有获取到本地IP");
			System.exit(1);
		}

		ServerConfigure.port = 8088;
	}

	public static void startServer() {

		HttpServer.start();
	}

}
