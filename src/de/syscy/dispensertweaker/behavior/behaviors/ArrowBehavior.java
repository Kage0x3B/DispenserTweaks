package de.syscy.dispensertweaker.behavior.behaviors;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import de.syscy.dispensertweaker.behavior.DTDispenseBehaviorProjectile;

public class ArrowBehavior extends DTDispenseBehaviorProjectile {
	@Override
	protected void spawnProjectile(World world, Block dispenser, BlockFace dispenserDirection, Location location, ItemStack itemStack, Vector velocity) {
		world.spawnArrow(location, velocity, getPower(dispenser), getA());
	}
}