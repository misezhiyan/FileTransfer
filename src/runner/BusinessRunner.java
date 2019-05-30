package runner;

import java.lang.reflect.Method;

import business.BasicDealer;
import business.FileDealer;
import http.Request;
import http.Response;

public class BusinessRunner {

	public static void Service(Request request, Response response) {

		String urL = request.getUrL();
		String dealerUrl = "";

		// 查找处理器
		BasicDealer basicDealer = null;
		if (urL.startsWith("/file")) {
			basicDealer = new FileDealer();
			dealerUrl = "/file";
		}

		// 匹配方法
		String subUrl = urL.substring(dealerUrl.length());
		if ("/listRoots".equals(subUrl)) {
			Class<? extends BasicDealer> clazz = basicDealer.getClass();
			try {
				Method declaredMethod = clazz.getDeclaredMethod("listRoots", request.getClass(), response.getClass());
				declaredMethod.invoke(basicDealer, request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
