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
 * ��дsocket
 */
public class NIOServer {

	private ServerSocketChannel server;
	private ByteBuffer sendBuffer;
	private ByteBuffer recvBuffer;
	private Selector selector;
	private int port = 9012;

	// ��ʼ��������
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
			System.out.println("����������������ض˿ںţ�{}" + this.port);

		} catch (IOException e) {
			System.out.println("����ʱ����IO�쳣");
		}

	}

	NIOServer() {
		this(9012);
	}

	// ��ʼ���������˿�
	public void start() {

		try {

			listener();

		} catch (IOException e) {

			System.out.println("�����˿�IO�쳣");
		}

	}

	// һ����ѭ��һֱ�ڼ���������˿��¼�
	public void listener() throws IOException {
		while (true) {
			System.out.println("-----------------------------------------------------");
			System.out.println("1.selectedKeys��ֵ:{}" + selector.selectedKeys().size());
			System.out.println("1.registe��ֵ��{}" + selector.keys().size());
			int n = selector.select();
			System.out.println("----------------------fengexian1---------------------");
			System.out.println("2.select����ֵ:{}" + n);
			System.out.println("2.selectedKeys��ֵ:{}" + selector.selectedKeys().size());
			System.out.println("2.registe��ֵ��{}" + selector.keys().size());
			System.out.println("-------------------------------------------------------");
			// û��׼���õ�ͨ������ʵ�Ҿ��ø������ᵽ�����Ϊ���û��ͨ��׼���ã�
			// Ӧ��select����һֱ�����š�
			if (n == 0) {
				continue;
			}
			Set<SelectionKey> eventKeys = selector.selectedKeys();
			Iterator<SelectionKey> it = eventKeys.iterator();
			while (it.hasNext()) {
				SelectionKey eventKey = it.next();
				it.remove();
				// ׼���õ�ͨ����ȡ����ͨ����ѡ�����Ķ�Ӧ��ϵ�����ô˹�ϵ���Եõ�ͨ������ѡ������
				// ��ʼ���崦��ͨ��������ݣ����ӣ�����д�ȣ�
				handleKey(eventKey);
			}
		}
	}

	// ����IO�����ӣ���д�Ⱥ���
	public void handleKey(SelectionKey eventKey) throws IOException {
		if (eventKey.isAcceptable()) {
			SocketChannel sc = server.accept();
			System.out.println("�µĿͻ����Ѿ����ӳɹ�");
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
				System.out.println("�ͻ���{}�Ѿ��رա�" + scc.socket().getRemoteSocketAddress());
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
			System.out.println("sendBuffer��д");
			SocketChannel sc = (SocketChannel) eventKey.channel();
			if (sendBuffer.remaining() > 0) {
				sc.write(sendBuffer);
				System.out.println("sendBufferʣ���С:{}" + sendBuffer.remaining());
			}
		}

	}

	// ��������������
	public static void main(String[] args) {
		new NIOServer().start();
	}
}
