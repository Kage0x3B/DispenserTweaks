package de.syscy.dispensertweaker.command.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.syscy.dispensertweaker.DispenserSetting;
import de.syscy.dispensertweaker.DispenserSetting.Setting;
import de.syscy.dispensertweaker.DispenserTweaker;
import de.syscy.dispensertweaker.command.PlayerCommandBase;
import lombok.Getter;

public class DispenserSettingCommand extends PlayerCommandBase {
	private @Getter DispenserSetting.Setting setting;

	public DispenserSettingCommand(DispenserSetting.Setting setting) {
		super(setting.getCommandName(), setting.getDescription(), setting.getCommandName() + (setting == Setting.RESET ? "" : " [value]"), setting.getCommandAlias());

		this.setting = setting;
	}

	@Override
	public boolean onPlayerCommand(Player sender, String[] args) {
		if(!isAuthorized(sender)) {
			return false;
		}

		double value = setting.getDefaultValue();

		if(args.length > 0) {
			try {
				value = Double.parseDouble(args[0]);
			} catch(NumberFormatException ex) {
			}
		}

		DispenserTweaker.getInstance().getCurrentSettingChanges().put(sender, new DispenserSetting(setting, value));

		if(setting == Setting.RESET) {
			sender.sendMessage(ChatColor.GOLD + "Now right click a dispenser to reset all values.");
		} else {
			if(value == setting.getDefaultValue()) {
				sender.sendMessage(ChatColor.GOLD + "Now right click a dispenser to reset the " + setting.getFancyName() + " value.");
			} else {
				sender.sendMessage(ChatColor.GOLD + "Now right click a dispenser to set the " + setting.getFancyName() + " value to " + value + ".");
			}
		}

		return true;
	}
}