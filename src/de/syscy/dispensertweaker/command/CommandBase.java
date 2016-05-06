package de.syscy.dispensertweaker.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import lombok.Getter;

public abstract class CommandBase {
	private @Getter String command;
	private @Getter String description;
	private @Getter String usage;
	private @Getter List<String> aliases;

	public CommandBase(String command) {
		this(command, "", command);
	}

	public CommandBase(String command, String description) {
		this(command, description, command);
	}

	public CommandBase(String command, String description, String usage, String... aliases) {
		this.command = command;
		this.description = description;
		this.usage = usage;
		this.aliases = new ArrayList<>();
		
		for(String alias : aliases) {
			this.aliases.add(alias.toLowerCase());
		}
	}

	public abstract boolean onCommand(CommandSender sender, String[] args);

	protected String arrayToString(String[] array, int startingIndex, boolean useCommas) {
		String string = "";

		for(int i = startingIndex; i < array.length; i++) {
			if(i == array.length - 1) {
				string = string + array[i];
			} else {
				string = string + array[i] + (useCommas ? ", " : " ");
			}
		}

		return string;
	}

	protected boolean isAuthorized(CommandSender sender) {
		return (sender.hasPermission("dispensertweaks." + getCommand().trim())) || (sender.isOp());
	}

	protected boolean isExempt(CommandSender sender) {
		return sender.hasPermission("dispensertweaks." + getCommand().trim() + ".exempt");
	}

	protected String getExemptMessage(String player) {
		return ChatColor.DARK_RED + player + ChatColor.RED + " is exempt from this command!";
	}

	protected String getUsageMessage() {
		return ChatColor.RED + "Usage: " + ChatColor.GRAY + "/dispensertweaks " + this.usage;
	}

	protected String getNotFoundMessage(String player) {
		return ChatColor.RED + "Player \"" + ChatColor.DARK_RED + player + ChatColor.RED + "\" was not found!";
	}

	protected Player getPlayer(String name) {
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(player.getName().equalsIgnoreCase(name)) {
				return player;
			}
		}
		return null;
	}

	protected boolean isValidPlayer(Player player) {
		return (player != null) && (player.isOnline());
	}

	protected void denyAccess(CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "You do not have access to this command!");
	}
}