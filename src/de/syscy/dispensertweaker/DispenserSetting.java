package de.syscy.dispensertweaker;

import org.bukkit.block.Block;
import org.bukkit.metadata.FixedMetadataValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class DispenserSetting {
	private final @Getter Setting setting;
	private @Getter double value;

	public DispenserSetting(Setting setting) {
		this.setting = setting;
		this.value = setting.getDefaultValue();
	}

	public DispenserSetting setValue(double value) {
		this.value = value;

		return this;
	}

	public void apply(Block dispenser) {
		if(setting == Setting.RESET) {
			for(Setting setting : Setting.values()) {
				if(dispenser.hasMetadata(setting.name())) {
					dispenser.removeMetadata(setting.name(), DispenserTweaker.getInstance());
				}
			}
		} else {
			dispenser.setMetadata(setting.name(), new FixedMetadataValue(DispenserTweaker.getInstance(), value));
		}
	}

	public static double get(Block dispenser, Setting setting) {
		if(dispenser.hasMetadata(setting.name())) {
			return dispenser.getMetadata(setting.name()).get(0).asDouble();
		} else {
			return setting.getDefaultValue();
		}
	}

	@AllArgsConstructor
	public enum Setting {
		//@formatter:off
		RESET(-1, "Resets all values"),
		VELOCITY_MULTIPLIER(1.0, "Velocity multiplier for projectiles and more"),
		TNT_FUSE_TICKS(40, "How long TNT takes to explode in ticks"),
		EXPLOSION_STRENGTH(0, "Strength of the explosion"),
		FIERY_EXPLOSIONS(0, "If the explosion creates fire");
		//@formatter:on

		private @Getter double defaultValue;
		private @Getter String description;

		public String getCommandAlias() {
			String commandAlias = "";
			boolean first = true;

			for(String namePart : getNameParts()) {
				if(namePart.isEmpty())
					continue;

				if(first) {
					commandAlias += namePart.substring(0, 1);
					first = false;
				} else {
					commandAlias += namePart.substring(0, 1).toUpperCase();
				}
			}

			return commandAlias;
		}

		public String getCommandName() {
			String commandName = "";
			boolean first = true;

			for(String namePart : getNameParts()) {
				if(first) {
					commandName += namePart;
					first = false;
				} else {
					commandName += uppercaseFirstLetter(namePart);
				}
			}

			return commandName;
		}

		public String getFancyName() {
			String fancyName = "";

			for(String namePart : getNameParts()) {
				fancyName += uppercaseFirstLetter(namePart) + " ";
			}

			if(fancyName.length() > 0) {
				fancyName = fancyName.substring(0, fancyName.length() - 1);
			}

			return fancyName;
		}

		private String uppercaseFirstLetter(String string) {
			return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
		}

		private String[] getNameParts() {
			return name().toLowerCase().split("_");
		}
	}
}