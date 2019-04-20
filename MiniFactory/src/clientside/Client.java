package clientside;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {
	private static Socket socket;
	private static Listener client;
	private static boolean playing = true;
	
	public static void main(String[] args) {
		System.out.println("Clientside");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			System.out.println("Введите ip адрес сервера:");
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
		

		try(PrintStream dos = new PrintStream(socket.getOutputStream())) {
			while(!socket.isClosed() && playing) {
				String cmd = br.readLine();
				if (cmd.toLowerCase().equals("/help")) {
					System.out.println("Клиентские команды:");
					System.out.println("/onlymenu - глушитель чата, при отключении отобразит пропущенные сообщения");
                }
				if (cmd.toLowerCase().equals("/onlymenu")) {
					if (client.isMenuMode()) {
						client.MenuModeOff();
						System.out.println("Чат заглушён. Если хотите разглушить, напишите ещё раз.");
					}
					else {
						client.MenuModeOn();
						System.out.println("Чат разглушён.");
					}
				} else {
					dos.println(cmd);
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
	    if (playing) {
	        if (client != null) {
	            client.askToStop();
	            client.interrupt();
	        }
			playing = false;
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
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

		
		try(Scanner dis = new Scanner(socket.getInputStream())) {
			while (!socket.isClosed() && !needToStop) {
				String msg = dis.nextLine();
				if (msg.indexOf("$MENU$") == 0) {
					msg = msg.replace("$MENU$", "");
					System.out.println("<"+msg);
				} else if (menuMode){
					needToShow.add(msg);
				} else System.out.println("<"+msg);
			}
		} catch (IOException | NoSuchElementException e) {
			e.printStackTrace();
		}
	}
}
