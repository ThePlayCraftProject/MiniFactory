import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class PlayerManager {
	private Map<String, Player> players = new HashMap<String, Player>();
	private Listener findingNewPlayers;
	private boolean isFinding = false;
	private ServerSocket server;
	public final int max = 2;
	
	public PlayerManager() {
		info("Запуск.");
		try {
			server = new ServerSocket(25565);
		} catch (IOException e) {
			e.printStackTrace();
		}
		startFindingPlayers();
	}
	protected void info(String s) {
		System.out.println("PM>"+s);
	}
	public void add(Player p) {
		players.put(p.getName(), p);
		if (players.size() >= max) stopFindingPlayers();
	}
	
	public Player getPlayer(String name) {
		return players.get(name);
	}
	
	public void stop() {
		stopFindingPlayers();
		broadcast("Сервер выключается!");
		try {
			server.close();
			for (Player p : players.values()) {
				p.stop();
			}
			System.out.println("ОООП!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		info("Стоп.");
	}
	
	public Map<String, Player> getPlayers() {
		return new HashMap<String, Player>(players);
	}
	
	private void startFindingPlayers() {
		if (!isFinding) {
			findingNewPlayers = new Listener(server, this);
			findingNewPlayers.start();
		}
		isFinding = true;
	}
	private void stopFindingPlayers() {
		findingNewPlayers.askToStop();
		isFinding = false;
	}
	
	public String getKeysAsString() {
		String s = "";
		boolean b = false;
		for (String ss : players.keySet()) {
			if (b) s += ", "; 
			else b = true;
			s += ss;
		}
		return s;
	}
	
	public void broadcast(String msg) {
		for (Player p : players.values()) {
			p.sendMessage(msg);
		}
	}
	
	public void disconnectPlayer(Player p) {
		p.stop();
		if (players.remove(p.getName()) != null) {
			info("Игрок "+p.getName()+" отключился!");
			broadcast("Игрок "+p.getName()+" отключился!");
		}
		if (players.size() < max) startFindingPlayers();
	}

	public boolean changePlayerName(Player p, String newName) {
		if (players.get(newName) == null) {
			players.put(newName, p);
			players.remove(p.getName());
			return true;
		}
		return false;
	}
}



class Listener extends Thread {
	private ServerSocket server;
	private boolean needToStop = false;
	private PlayerManager pm;

	public Listener(ServerSocket server, PlayerManager pm) {
		this.server = server;
		this.pm = pm;
	}
	
	public void askToStop() {
		needToStop = true;
	}
	protected void info (String s) {
		pm.info("Listener>"+s);
	}
	@Override
	public void run() {
		super.run();
		info("Начат поиск игроков.");
		while (!needToStop && !server.isClosed()) {
			try {
				Socket client = server.accept();
				if (client != null) {
					Player p = new Player("UnnamedSocket"+(pm.getPlayers().size()+1), client);
					pm.add(p);
					info("Игрок "+p.getName()+" подключился!");
					Game.getPlayerManager().broadcast("Игрок "+p.getName()+" подключился!");
					info("Список игроков: "+pm.getKeysAsString());
				}
			} catch (IOException e) {
				if (!needToStop) e.printStackTrace();
			}
		}
		info("Прекращён поиск игроков.");
	}
}