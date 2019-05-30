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

		// ���Ҵ�����
		BasicDealer basicDealer = null;
		if (urL.startsWith("/file")) {
			basicDealer = new FileDealer();
			dealerUrl = "/file";
		}

		// ƥ�䷽��
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
