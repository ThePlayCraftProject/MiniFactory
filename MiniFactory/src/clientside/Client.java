package clientside;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {
	private static Socket socket;
	private static Listener client;
	private static boolean playing = true;
	
	public static void main(String[] args) {
		System.out.println("Clientside");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			System.out.println("������� ip ����� �������:");
			String ip = br.readLine();
			if (ip.toLowerCase().equals("marvel")) ip = "178.140.215.233";
			socket = new Socket(ip, 25565);
		} catch (IOException e) {
			e.printStackTrace();
			if (client != null) {
				client.askToStop();
			}
		}
		client = new Listener(socket);
		client.start();
		

		try(DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {
			while(!socket.isClosed() && playing) {
				String cmd = br.readLine();
				if (cmd.toLowerCase().equals("/help")) {
					System.out.println("���������� �������:");
					System.out.println("/onlymenu - ��������� ����, ��� ���������� ��������� ����������� ���������");
                }
				if (cmd.toLowerCase().equals("/onlymenu")) {
					if (client.isMenuMode()) {
						client.MenuModeOff();
						System.out.println("��� ��������. ���� ������ ����������, �������� ��� ���.");
					}
					else {
						client.MenuModeOn();
						System.out.println("��� ���������.");
					}
				} else {
					System.out.println(cmd);
					dos.writeUTF(cmd);
					if (!br.ready()) {
						dos.flush();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void stop() {
		client.askToStop();
		playing = false;
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class Listener extends Thread {
	private Socket socket;
	private boolean needToStop = false;
	private List<String> needToShow = new ArrayList<String>();
	private boolean menuMode = false;
	
	public Listener(Socket socket) {
		this.socket = socket;
	}
	
	public boolean isMenuMode() {
		return menuMode;
	}
	public void MenuModeOn() {
		menuMode = true;
	}
	public void MenuModeOff() {
		menuMode = false;
		for (String s : needToShow) {
			System.out.println("<"+s);
		}
	}
	
	protected void info(String s) {
		System.out.println("Listener>"+s);
	}
	
	public void askToStop() {
		needToStop = true;
	}
	
	
	@Override
	public void run() {
		super.run();

		
		try(DataInputStream dis = new DataInputStream(socket.getInputStream())) {
			while (!socket.isClosed() && !needToStop) {
				String msg = dis.readUTF();
				if (msg.indexOf("$MENU$") == 0) {
					msg = msg.replace("$MENU$", "");
					System.out.println("<"+msg);
				} else if (menuMode){
					needToShow.add(msg);
				} else System.out.println("<"+msg);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
