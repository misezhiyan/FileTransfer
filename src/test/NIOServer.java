package test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 手写socket
 */
public class NIOServer {

	private ServerSocketChannel server;
	private ByteBuffer sendBuffer;
	private ByteBuffer recvBuffer;
	private Selector selector;
	private int port = 9012;

	// 初始化服务器
	NIOServer(int port) {
		this.port = port;
		try {
			recvBuffer = ByteBuffer.allocate(1024);
			sendBuffer = ByteBuffer.allocate(1024);
			server = ServerSocketChannel.open();
			server.socket().bind(new InetSocketAddress(port));
			server.configureBlocking(false);
			selector = Selector.open();
			server.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("服务器已启动，监控端口号：{}" + this.port);

		} catch (IOException e) {
			System.out.println("连接时出现IO异常");
		}

	}

	NIOServer() {
		this(9012);
	}

	// 初始化，监听端口
	public void start() {

		try {

			listener();

		} catch (IOException e) {

			System.out.println("监听端口IO异常");
		}

	}

	// 一个死循环一直在监听，处理端口事件
	public void listener() throws IOException {
		while (true) {
			System.out.println("-----------------------------------------------------");
			System.out.println("1.selectedKeys的值:{}" + selector.selectedKeys().size());
			System.out.println("1.registe的值：{}" + selector.keys().size());
			int n = selector.select();
			System.out.println("----------------------fengexian1---------------------");
			System.out.println("2.select返回值:{}" + n);
			System.out.println("2.selectedKeys的值:{}" + selector.selectedKeys().size());
			System.out.println("2.registe的值：{}" + selector.keys().size());
			System.out.println("-------------------------------------------------------");
			// 没有准备好的通道，其实我觉得根本不会到这里，因为如果没有通道准备好，
			// 应该select函数一直阻塞着。
			if (n == 0) {
				continue;
			}
			Set<SelectionKey> eventKeys = selector.selectedKeys();
			Iterator<SelectionKey> it = eventKeys.iterator();
			while (it.hasNext()) {
				SelectionKey eventKey = it.next();
				it.remove();
				// 准备好的通道中取得了通道和选择器的对应关系，利用此关系可以得到通道或者选择器。
				// 开始具体处理通道相关内容，连接，读，写等；
				handleKey(eventKey);
			}
		}
	}

	// 处理IO口连接，读写等函数
	public void handleKey(SelectionKey eventKey) throws IOException {
		if (eventKey.isAcceptable()) {
			SocketChannel sc = server.accept();
			System.out.println("新的客户端已经连接成功");
			sc.configureBlocking(false);
			sc.register(selector, SelectionKey.OP_READ);
		}

		if (eventKey.isReadable()) {
			SocketChannel sc = (SocketChannel) eventKey.channel();
			String content = "";
			int n;
			recvBuffer.clear();
			try {
				while ((n = sc.read(recvBuffer)) > 0) {

					content = content + new String(recvBuffer.array(), 0, n);
				}
			} catch (IOException e) {
				eventKey.cancel();
				sc.close();
				return;
			}
			if (n == -1) {
				SocketChannel scc = (SocketChannel) eventKey.channel();
				eventKey.channel().close();
				eventKey.cancel();
				System.out.println("客户端{}已经关闭。" + scc.socket().getRemoteSocketAddress());
				return;
			}
			System.out.println("receive client input Stirng : {}" + content);
			// content = "yanzh";
			if (content.length() > 0) {
				sendBuffer.clear();
				sendBuffer.put(content.getBytes());
				sendBuffer.flip();
				sc.write(sendBuffer);
			}
			// sc.configureBlocking(false);
			// sc.register(selector, SelectionKey.OP_WRITE);
			// eventKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
		}
		if (eventKey.isWritable()) {
			System.out.println("sendBuffer可写");
			SocketChannel sc = (SocketChannel) eventKey.channel();
			if (sendBuffer.remaining() > 0) {
				sc.write(sendBuffer);
				System.out.println("sendBuffer剩余大小:{}" + sendBuffer.remaining());
			}
		}

	}

	// 主函数启动服务
	public static void main(String[] args) {
		new NIOServer().start();
	}
}
