import java.util.Scanner;

public class Game {
	private final static EnterpriseManager em = new EnterpriseManager();
	private final static CommandManager cm = new CommandManager();
	private final static PlayerManager pm = new PlayerManager();
	private static boolean playing = true;
	
	public static void main (String[] args) throws InterruptedException {
		System.out.println("Serverside");
		start();
	}
	
	private static void start() {
		System.out.println("Старт.");
		Scanner in = new Scanner(System.in);
		String msg = in.nextLine();
		while (playing) {
			if (msg.equals("/stop")) {
				askToStop();
				stop();
			}
		}
		in.close();
	}
	
	public static void askToStop() {
		playing = false;
	}
	
	private static void stop() {
		System.out.println("Стоп.");
		pm.stop();
	}

	public static EnterpriseManager getEnterpriseManager() {
		return em;
	}
	public static CommandManager getCommandManager() {
		return cm;
	}
	public static PlayerManager getPlayerManager() {
		return pm;
	}
}