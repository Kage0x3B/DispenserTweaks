package de.syscy.dispensertweaker.behavior.behaviors;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import de.syscy.dispensertweaker.DispenserSetting;
import de.syscy.dispensertweaker.DispenserSetting.Setting;
import de.syscy.dispensertweaker.DispenserTweaker;
import de.syscy.dispensertweaker.behavior.DTDispenseBehaviorItem;
import de.syscy.dispensertweaker.behavior.NMSSourceBlockContainer;

public class SmallFireballBehavior extends DTDispenseBehaviorItem {
	private static Random random = new Random();

	@Override
	protected ItemStack dispenseItem(NMSSourceBlockContainer block, ItemStack itemStack, BlockFace dispenserDirection) {
		World world = block.getBlock().getWorld();
		Location location = getDropLocation(block.getBlock());
		location.setX(location.getX() + dispenserDirection.getModX() * 0.3f);
		location.setY(location.getY() + dispenserDirection.getModY() * 0.3f);
		location.setZ(location.getZ() + dispenserDirection.getModZ() * 0.3f);

		Vector velocity = getVelocity(block.getBlock(), location, dispenserDirection);
		System.out.println(velocity);
		
		ItemStack itemStack2 = new ItemStack(itemStack);
		itemStack2.setAmount(itemStack2.getAmount() - 1);
		BlockDispenseEvent event = new BlockDispenseEvent(block.getBlock(), itemStack2, velocity);

		Bukkit.getServer().getPluginManager().callEvent(event);

		if(event.isCancelled()) {
			itemStack.setAmount(itemStack.getAmount() + 1);
			
			return itemStack;
		}

		if(!event.getItem().getType().equals(itemStack.getType())) {
			if(DispenserTweaker.executeDispenseBehavior(event.getItem(), block)) {
				itemStack.setAmount(itemStack.getAmount() + 1);
				
				return itemStack;
			}
		}
		
		SmallFireball fireball = (SmallFireball) world.spawnEntity(location, EntityType.SMALL_FIREBALL);
		fireball.setVelocity(event.getVelocity());
		System.out.println("Event Velocity: " + velocity);

		double explosionStrenght = DispenserSetting.get(block.getBlock(), Setting.EXPLOSION_STRENGTH);

		if(explosionStrenght > 0) {
			DispenserTweaker.getInstance().getEntityExplosionStrength().put(fireball.getEntityId(), explosionStrenght);
		}

		return itemStack;
	}

	protected Vector getVelocity(Block dispenser, Location dropLocation, BlockFace dispenserDirection) {
		double x = random.nextGaussian() * 0.05 + dispenserDirection.getModX();
		double y = random.nextGaussian() * 0.05 + dispenserDirection.getModY();
		double z = random.nextGaussian() * 0.05 + dispenserDirection.getModZ();

		return new Vector(x, y, z).multiply(DispenserSetting.get(dispenser, Setting.VELOCITY_MULTIPLIER));
	}
}