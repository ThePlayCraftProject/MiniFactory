public class CommandManager {
	public CommandManager() {
		info("Старт.");
	}

	protected void info(String s) {
		System.out.println("CM>"+s);
	}
	
	public void gotCommand(Player p, String cmd) {
		if (cmd.length() == 0 || cmd == null) return; 
		if (cmd.indexOf("/") != 0) {
			refineChat(p, cmd);
			return;
		}
		String[] sa = cmd.split(" ");
		cmd = sa[0].replace("/", "").toLowerCase();
		String[] args = new String[sa.length-1];
		for (int i = 1; i < sa.length; i++) {
			args[i-1] = sa[i];
		}
		refineCommand(p, cmd, args);
	}
	
	private void refineChat(Player p, String message) {
		Game.getPlayerManager().broadcast("<"+p.getName()+"> "+message);
	}
	
	private void refineCommand(Player p, String cmd, String[] args) {
		if (cmd.equals("help")) {
			p.sendMessage("Общие команды:");
			p.sendMessage("/help - помощь");
			p.sendMessage("Серверные команды:");
			p.sendMessage("/changenick <ник> - смена ника");
		}
		else if (cmd.equals("changenick")) {
			if (args.length >= 1) {
				String nick = args[0];
				for (int i = 1; i < args.length; i++) {
					nick+=" "+args[i];
				}
				if (Game.getPlayerManager().getPlayer(nick) != null) {
					p.sendMessage("Игрок с именем "+nick+" уже существует.", "MENU");
					return;
				}
				p.sendMessage("Ваше старое имя "+p.getName()+" было сменено на "+nick+".", "MENU");
				p.changeName(nick);
			}
			else p.sendMessage("Не указано новое имя.", "MENU");
			return;
		}
		else p.sendMessage("Такой команды не существует. /help", "MENU");
	}
}
