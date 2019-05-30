package http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class Response {

	public static final int BUFFER_SIZE = 2048;
	// ���������D�̵��ļ�
	private static final String WEB_ROOT = "D:";
	private Request request;
	private OutputStream output;

	public OutputStream getOutputStream() {

		return output;
	}

	public PrintWriter getWriter() {

		return new PrintWriter(output);
	}

	public Response(OutputStream output) {
		this.output = output;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public void sendStaticResource() throws IOException {
		byte[] bytes = new byte[BUFFER_SIZE];
		FileInputStream fis = null;
		try {
			// ƴ�ӱ���Ŀ¼��������˿ںź����Ŀ¼
			File file = new File(WEB_ROOT, request.getUrL());
			// ����ļ����ڣ��Ҳ��Ǹ�Ŀ¼
			if (file.exists() && !file.isDirectory()) {
				fis = new FileInputStream(file);
				int ch = fis.read(bytes, 0, BUFFER_SIZE);
				while (ch != -1) {
					output.write(bytes, 0, ch);
					ch = fis.read(bytes, 0, BUFFER_SIZE);
				}
			} else {
				// �ļ������ڣ����ظ��������Ӧ��ʾ,�������ƴ��HTML�κ�Ԫ��
				String retMessage = "<h1>" + file.getName() + " file or directory not exists</h1>";
				String returnMessage = "HTTP/1.1 404 File Not Found\r\n" + "Content-Type: text/html\r\n" + "Content-Length: " + retMessage.length() + "\r\n" + "\r\n" + retMessage;
				output.write(returnMessage.getBytes());
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if (fis != null)
				fis.close();
		}
	}
}