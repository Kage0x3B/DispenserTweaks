package de.syscy.dispensertweaker.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class PlayerCommandBase extends CommandBase {
	public PlayerCommandBase(String command) {
		super(command, "", command);
	}

	public PlayerCommandBase(String command, String description) {
		super(command, description, command);
	}

	public PlayerCommandBase(String command, String description, String usage, String... aliases) {
		super(command, description, usage, aliases);
	}

	public boolean onCommand(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You must be a player to use this command!");

			return true;
		}
		
		return onPlayerCommand((Player) sender, args);
	}

	public abstract boolean onPlayerCommand(Player sender, String[] args);
}