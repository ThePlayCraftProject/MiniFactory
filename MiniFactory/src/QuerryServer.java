import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class QuerryServer {
	private QuerryListener listener;
	private ServerSocket querry;
	
	public QuerryServer() {
		info("Старт.");
		try {
			querry = new ServerSocket(25566);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		listener = new QuerryListener(querry, this);
		listener.start();
	}
	
	public void stop() {
		listener.askToStop();
		try {
			querry.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		info("Стоп.");
	}
	
	protected void info(String s) {
		System.out.println("QS>"+s);
	}
}


class QuerryListener extends Thread {
	private ServerSocket querry;
	private QuerryServer qs;
	private boolean running = true;
	
	public QuerryListener(ServerSocket querry, QuerryServer qs) {
		this.querry = querry;
		this.qs = qs;
	}
	
	@Override
	public void run() {
		info("Обслуживает запросы игроков.");
		while (!querry.isClosed() && running) {
			try {
				Socket client = querry.accept();
				PrintStream dos = new PrintStream(client.getOutputStream());
				dos.println(Game.getPlayerManager().getPlayers().size()+"/"+Game.getPlayerManager().max);
				client.close();
			} catch (IOException e) {
			}
		}
		info("Перестал обслуживать запросы игроков.");
	}

	protected void info (String s) {
		qs.info("Listener>"+s);
	}
	
	public void askToStop() {
		running = false;
	}
}