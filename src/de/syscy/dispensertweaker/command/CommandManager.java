package de.syscy.dispensertweaker.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import de.syscy.dispensertweaker.util.NumberUtil;
import de.syscy.dispensertweaker.util.Theme;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommandManager implements CommandExecutor {
	private final @Getter JavaPlugin plugin;

	private List<CommandBase> commands = new ArrayList<>();

	private int cmdPerPage = 6;
	private int totalPages;

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 0) {
			if(sender.hasPermission("dispensertweaker.help")) {
				sendHelpPage(sender, 1);

				return true;
			}

			sender.sendMessage(ChatColor.RED + "You do not have access to this command!");

			return false;
		}

		if(args[0].equalsIgnoreCase("help")) {
			if(sender.hasPermission("dispensertweaker.help")) {
				if(args.length == 1) {
					sendHelpPage(sender, 1);

					return true;
				}

				if(args.length == 2) {
					if(NumberUtil.isNumber(args[1])) {
						int page = Integer.parseInt(args[1]);

						if((page > 0) && (page <= this.totalPages)) {
							sendHelpPage(sender, page);

							return true;
						}

						sendHelpPage(sender, 1);

						return true;
					}

					CommandBase command;

					if((command = getCommand(args[1])) != null) {
						sender.sendMessage(ChatColor.GOLD + "Command: " + ChatColor.GRAY + command.getCommand());
						sender.sendMessage(ChatColor.GOLD + "Aliases: " + ChatColor.GRAY + command.getAliases().toString());
						sender.sendMessage(ChatColor.GOLD + "Description: " + ChatColor.GRAY + command.getDescription());
						return true;
					}

					sender.sendMessage(ChatColor.RED + " page/command \"" + args[1] + "\".");

					return false;
				}

				sender.sendMessage(ChatColor.RED + "Usage: " + ChatColor.GRAY + "/dispensertweaker help [page]");

				return false;
			}

			sender.sendMessage(ChatColor.RED + "You do not have access to this command!");

			return false;
		}

		CommandBase command = getCommand(args[0]);

		if(command == null) {
			sender.sendMessage(ChatColor.RED + " DispenserTweaker command: " + ChatColor.DARK_RED + args[0].toLowerCase());
		} else {
			String[] cmdArgs = new String[args.length - 1];

			if(args.length > 1) {
				System.arraycopy(args, 1, cmdArgs, 0, args.length - 1);
			}

			return command.onCommand(sender, cmdArgs);
		}

		return false;
	}
	
	public void addCommand(CommandBase command) {
		commands.add(command);
	}

	private void sendHelpPage(CommandSender sender, int page) {
		int size = commands.size();
		this.totalPages = (size / this.cmdPerPage);

		if(size - this.cmdPerPage * this.totalPages > 0) {
			this.totalPages += 1;
		}

		if((page < 1) || (page > this.totalPages)) {
			page = 1;
		}

		sender.sendMessage("------DispenserTweaker Help------");

		int startIndex = (page - 1) * this.cmdPerPage;
		int endIndex = this.cmdPerPage * page;

		for(int i = startIndex; i < endIndex; i++) {
			if(commands.size() == i) {
				break;
			}

			sender.sendMessage(Theme.COMMAND + "/dispensertweaker " + (String) commands.get(i).getCommand() + Theme.SPLITTER + " | " + Theme.DESCRIPTION + (String) commands.get(i).getDescription() + " ");
		}

		sender.sendMessage(ChatColor.DARK_GRAY + "Type \"/dispensertweaker help <command>\" to get information on a command.");
		sender.sendMessage(ChatColor.GOLD + "Page number: " + ChatColor.DARK_BLUE + page + "/" + this.totalPages);
	}

	public CommandBase getCommand(String commandString) {
		for(CommandBase command : commands) {
			if(command.getCommand().equalsIgnoreCase(commandString) || command.getAliases().contains(commandString.toLowerCase())) {
				return command;
			}
		}

		return null;
	}
}