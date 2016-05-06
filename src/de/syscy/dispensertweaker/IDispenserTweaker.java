package de.syscy.dispensertweaker;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import de.syscy.dispensertweaker.behavior.DTDispenseBehavior;
import de.syscy.dispensertweaker.behavior.NMSSourceBlockContainer;

public interface IDispenserTweaker {
	/**
	 * Overrides a Dispense Behavior
	 */
	public void overrideBehavior(int itemId, DTDispenseBehavior dispenseBehavior);
	
	/**
	 * @return If the Dispense Behavior could be executed
	 */
	public boolean executeDispenseBehavior(ItemStack itemStack, NMSSourceBlockContainer dispenserBlock);
	
	/**
	 * Calls the shoot method on a projectile.
	 * Used for shooting arrows and other projectiles in DTDispenseBehaviorProjectile
	 * 
	 * @return If the entity was a projectile
	 */
	public boolean shootProjectile(Entity entity, Vector velocity, float power, float a);
}