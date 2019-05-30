package http;

import java.io.IOException;
import java.io.InputStream;

public class Request {
	private InputStream is;
	private String url;

	public Request(InputStream input) {
		this.is = input;
		parse();
	}

	public void parse() {

		// ��socket�ж�ȡһ��2048�����ַ�
		StringBuffer request = new StringBuffer(Response.BUFFER_SIZE);
		int i;
		byte[] buffer = new byte[Response.BUFFER_SIZE];
		try {
			i = is.read(buffer);
		} catch (IOException e) {
			e.printStackTrace();
			i = -1;
		}
		for (int j = 0; j < i; j++) {
			request.append((char) buffer[j]);
		}
		// ��ӡ��ȡ��socket�е�����
		System.out.print(request.toString());
		url = parseUrL(request.toString());
		System.out.println(url);
	}

	private String parseUrL(String requestString) {
		int index1, index2;
		index1 = requestString.indexOf(' ');// ��socket��ȡ����ͷ�Ƿ���ֵ
		if (index1 != -1) {
			index2 = requestString.indexOf(' ', index1 + 1);
			if (index2 > index1)
				return requestString.substring(index1 + 1, index2);
		}

		return null;
	}

	public String getUrL() {
		return url;
	}

}