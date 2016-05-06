package de.syscy.dispensertweaker;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import de.syscy.dispensertweaker.behavior.DTDispenseBehavior;
import de.syscy.dispensertweaker.behavior.NMSSourceBlockContainer;
import de.syscy.dispensertweaker.behavior.behaviors.ArrowBehavior;
import de.syscy.dispensertweaker.behavior.behaviors.SmallFireballBehavior;
import de.syscy.dispensertweaker.behavior.behaviors.TNTBehavior;
import de.syscy.dispensertweaker.command.CommandManager;
import de.syscy.dispensertweaker.command.commands.DispenserSettingCommand;
import de.syscy.dispensertweaker.impl.v1_7_R4.DispenserTweaker_v1_7_R4;
import de.syscy.dispensertweaker.impl.v1_8_R3.DispenserTweaker_v1_8_R3;
import de.syscy.dispensertweaker.impl.v1_9_R1.DispenserTweaker_v1_9_R1;
import de.syscy.dispensertweaker.listener.DTListener;
import lombok.Getter;

public class DispenserTweaker extends JavaPlugin {
	private static @Getter DispenserTweaker instance;
	
	private static @Getter FileConfiguration pluginConfig;
	private static @Getter IDispenserTweaker nmsDispenserTweaker;
	
	private @Getter Map<Player, DispenserSetting> currentSettingChanges = new HashMap<>();
	
	private @Getter Map<Integer, Double> entityExplosionStrength = new HashMap<>();

	@Override
	public void onEnable() {
		instance = this;
		
		String packageName = Bukkit.getServer().getClass().getPackage().getName();
		packageName = packageName.substring(packageName.lastIndexOf('.') + 1);

		switch(packageName) {
			case "v1_7_R4":
				nmsDispenserTweaker = new DispenserTweaker_v1_7_R4();
				break;
			case "v1_8_R3":
				nmsDispenserTweaker = new DispenserTweaker_v1_8_R3();
				break;
			case "v1_9_R1":
				nmsDispenserTweaker = new DispenserTweaker_v1_9_R1();
				break;
		}

		if(nmsDispenserTweaker == null) {
			getLogger().severe("Incompatible with NMS version " + packageName + "!");
			getPluginLoader().disablePlugin(this);

			return;
		}
		
		getServer().getPluginManager().registerEvents(new DTListener(), this);
		
		CommandManager commandManager = new CommandManager(this);
		commandManager.addCommand(new DispenserSettingCommand(DispenserSetting.Setting.RESET));
		commandManager.addCommand(new DispenserSettingCommand(DispenserSetting.Setting.VELOCITY_MULTIPLIER));
		commandManager.addCommand(new DispenserSettingCommand(DispenserSetting.Setting.TNT_FUSE_TICKS));
		commandManager.addCommand(new DispenserSettingCommand(DispenserSetting.Setting.EXPLOSION_STRENGTH));
		commandManager.addCommand(new DispenserSettingCommand(DispenserSetting.Setting.FIERY_EXPLOSIONS));
		
		getCommand("dispensertweaker").setExecutor(commandManager);
		getCommand("dt").setExecutor(commandManager);

		loadConfig();
		pluginConfig = getConfig();

		overrideConfigurableBehaviors();
	}

	/**
	 * Overrides a Dispense Behavior
	 */
	public static void registerCustomDispenseBehavior(int itemId, DTDispenseBehavior dispenseBehavior) {
		nmsDispenserTweaker.overrideBehavior(itemId, dispenseBehavior);
	}

	/**
	 * @return If the Dispense Behavior could be executed
	 */
	public static boolean executeDispenseBehavior(ItemStack itemStack, NMSSourceBlockContainer dispenserBlock) {
		return nmsDispenserTweaker.executeDispenseBehavior(itemStack, dispenserBlock);
	}
	
	public static void setDispenserValue() {
		
	}

	private void overrideConfigurableBehaviors() {
		registerCustomDispenseBehavior(262, new ArrowBehavior());
		registerCustomDispenseBehavior(46, new TNTBehavior());
		registerCustomDispenseBehavior(385, new SmallFireballBehavior());
	}

	private void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
}