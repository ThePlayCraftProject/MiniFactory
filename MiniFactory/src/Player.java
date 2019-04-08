import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

public class Player {
	private String name;
	private Socket socket;
	private ServerSideListener listener;
	
	public String getName() {
		return name;
	}
	
	
	
	@Override
	public int hashCode() {
		return Objects.hash(listener, name, socket);
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		return Objects.equals(listener, other.listener) && Objects.equals(name, other.name)
				&& Objects.equals(socket, other.socket);
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
			System.out.println("ОООП!");
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
					System.out.println("получил: "+cmd+" от "+p.getName());
					Game.getCommandManager().gotCommand(p, cmd);
				}
			} catch (IOException e) {
				if (needToStop) break;
				e.printStackTrace();
				Game.getPlayerManager().info("С игроком "+p.getName()+" потеряно соединение.. "+tries+" попытка.");
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