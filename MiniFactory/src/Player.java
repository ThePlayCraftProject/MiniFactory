import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Player {
	private String name;
	private Socket socket;
	private ServerSideListener listener;
	
	public String getName() {
		return name;
	}
	
	public Player(String name, Socket socket) {
		this.name = name;
		this.socket = socket;
		
		listener = new ServerSideListener(this, socket);
		listener.start();
	}
	
	public void changeName(String newName) {
		if (Game.getPlayerManager().changePlayerName(this, newName)) name = newName;
	}
	
	public int sendMessage(String s) {
		return sendMessage(s, "");
	}
	
	public int sendMessage(String s, String type) {
		type = type.toUpperCase();
		if (type.equals("MENU")) s = "$MENU$"+s;
		
		if (!socket.isClosed()) {
			try {
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
				
				dos.writeUTF(s);
				dos.flush();
			} catch (IOException e) {
				e.printStackTrace();
				return 1;
			}
			return 0;
		}
		return 1;
	}
	public void stop() {
		try {
			socket.close();
			listener.askToStop();
			System.out.println("����!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}



class ServerSideListener extends Thread {
	private Socket client;
	private Player p;
	private boolean needToStop = false;
	
	public ServerSideListener(Player p, Socket client) {
		this.p = p;
		this.client = client;
	}
	
	public void askToStop() {
		needToStop = true;
	}
	
	@Override
	public void run() {
		super.run();
		
		int tries = 1;
		while (tries <= 3) {
			try(DataInputStream dis = new DataInputStream(client.getInputStream())) {
				
				
				while (!client.isClosed() && !needToStop) {
					String cmd = dis.readUTF();
					System.out.println("�������: "+cmd+" �� "+p.getName());
					Game.getCommandManager().gotCommand(p, cmd);
				}
			} catch (IOException e) {
				if (needToStop) break;
				e.printStackTrace();
				Game.getPlayerManager().info("� ������� "+p.getName()+" �������� ����������.. "+tries+" �������.");
				tries++;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		Game.getPlayerManager().disconnectPlayer(p);
	}
}