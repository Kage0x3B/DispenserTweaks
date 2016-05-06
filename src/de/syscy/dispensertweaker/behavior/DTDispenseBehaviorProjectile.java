package de.syscy.dispensertweaker.behavior;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import de.syscy.dispensertweaker.DispenserSetting;
import de.syscy.dispensertweaker.DispenserSetting.Setting;
import de.syscy.dispensertweaker.DispenserTweaker;

public abstract class DTDispenseBehaviorProjectile extends DTDispenseBehaviorItem {
	@Override
	protected ItemStack dispenseItem(NMSSourceBlockContainer dispenser, ItemStack itemStack, BlockFace dispenserDirection) {
		World world = dispenser.getBlock().getWorld();
		Location dropLocation = getDropLocation(dispenser.getBlock());

		ItemStack itemStack2 = itemStack.clone();
		itemStack2.setAmount(itemStack2.getAmount() - 1);

		BlockDispenseEvent event = new BlockDispenseEvent(dispenser.getBlock(), itemStack2.clone(), getVelocity(dispenser.getBlock(), dropLocation, dispenserDirection));
		Bukkit.getServer().getPluginManager().callEvent(event);

		if(event.isCancelled()) {
			itemStack.setAmount(itemStack.getAmount() + 1);

			return itemStack;
		}

		if(!event.getItem().equals(itemStack2)) {
			itemStack.setAmount(itemStack.getAmount() + 1);

			DispenserTweaker.executeDispenseBehavior(event.getItem(), dispenser);

			return itemStack;
		}
		
		spawnProjectile(world, dispenser.getBlock(), dispenserDirection, dropLocation, itemStack, event.getVelocity());
		
		return itemStack;
	}
	
	@Override
	protected void playDispenserSound(Block block) {
		block.getWorld().playEffect(block.getLocation(), Effect.BOW_FIRE, 0);
	}

	protected abstract void spawnProjectile(World world, Block dispenser, BlockFace dispenserDirection, Location location, ItemStack itemStack, Vector velocity);

	protected float getA() {
		return 6.0f;
	}

	protected float getPower(Block dispenser) {
		return (float) DispenserSetting.get(dispenser, Setting.VELOCITY_MULTIPLIER);
	}
	
	protected Vector getVelocity(Block dispenser, Location dropLocation, BlockFace dispenserDirection) {
		return new Vector(dispenserDirection.getModX(), dispenserDirection.getModY() + 0.1F, dispenserDirection.getModZ());
	}
}