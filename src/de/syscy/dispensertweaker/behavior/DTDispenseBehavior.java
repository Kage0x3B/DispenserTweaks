package de.syscy.dispensertweaker.behavior;

import org.bukkit.inventory.ItemStack;

public abstract interface DTDispenseBehavior {
	public static final DTDispenseBehavior NONE = new DTDispenseBehavior() {
		public ItemStack dispense(NMSSourceBlockContainer dispenserBlock, ItemStack itemStack) {
			return itemStack;
		}
	};

	public abstract ItemStack dispense(NMSSourceBlockContainer dispenserBlock, ItemStack itemStack);
}