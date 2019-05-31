package business;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

import javax.swing.filechooser.FileSystemView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import http.Request;
import http.Response;

public class FileDealer implements BasicDealer {

	public void listRoots(Request request, Response response) throws IOException {

		JSONArray listRoots = listRoots();

		String result = JSONObject.toJSONString(listRoots);

		String returnMessage = "HTTP/1.1 400 OK\r\n";
		returnMessage += "Content-Type: text/html\r\n";
		returnMessage += "Content-Length: " + result.length() + "\r\n";
		returnMessage += "\r\n";
		// returnMessage += "<h1>" + result + "</h1>";
		returnMessage += result;

		System.out.println(returnMessage);

		// OutputStream outputStream = response.getOutputStream();
		// outputStream.write(returnMessage.getBytes());
		// outputStream.flush();
		// outputStream.close();

		PrintWriter writer = response.getWriter();
		writer.println(returnMessage);
		writer.flush();
		writer.close();
	}

	public static void main(String[] args) {
		listRoots();
	}

	// 展示盘根目录
	public static JSONArray listRoots() {

		JSONArray jsonArr = new JSONArray();

		// 当前文件系统类
		FileSystemView fsv = FileSystemView.getFileSystemView();
		// 列出所有windows 磁盘
		File[] fs = File.listRoots();
		// 显示磁盘卷标
		for (int i = 0; i < fs.length; i++) {
			String panf = fsv.getSystemDisplayName(fs[i]);
			String total = FormetFileSize(fs[i].getTotalSpace());
			String free = FormetFileSize(fs[i].getFreeSpace());

			JSONObject json = new JSONObject();
			json.put("panf", panf);
			json.put("total", total);
			json.put("free", free);

			jsonArr.add(json);
		}

		return jsonArr;
	}

	// 查询盘容量
	public static String FormetFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}
}
