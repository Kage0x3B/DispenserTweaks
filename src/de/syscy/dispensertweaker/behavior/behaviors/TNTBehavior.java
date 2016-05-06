package de.syscy.dispensertweaker.behavior.behaviors;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import de.syscy.dispensertweaker.DispenserSetting;
import de.syscy.dispensertweaker.DispenserSetting.Setting;
import de.syscy.dispensertweaker.DispenserTweaker;
import de.syscy.dispensertweaker.behavior.DTDispenseBehaviorProjectile;

public class TNTBehavior extends DTDispenseBehaviorProjectile {
	@Override
	protected void spawnProjectile(World world, Block dispenser, BlockFace dispenserDirection, Location location, ItemStack itemStack, Vector velocity) {
		Location tntLocation = new Location(location.getWorld(), location.getBlockX() + 0.5, location.getBlockY() + 0.5, location.getBlockZ() + 0.5);

		TNTPrimed primedTNT = (TNTPrimed) world.spawnEntity(tntLocation, EntityType.PRIMED_TNT);
		primedTNT.setVelocity(velocity.multiply(getPower(dispenser)));
		primedTNT.setFuseTicks((int) DispenserSetting.get(dispenser, Setting.TNT_FUSE_TICKS));
		primedTNT.setIsIncendiary(DispenserSetting.get(dispenser, Setting.FIERY_EXPLOSIONS) == 1);

		double explosionStrenght = DispenserSetting.get(dispenser, Setting.EXPLOSION_STRENGTH);

		if(explosionStrenght > 0) {
			DispenserTweaker.getInstance().getEntityExplosionStrength().put(primedTNT.getEntityId(), explosionStrenght);
		}
	}

	@Override
	protected Vector getVelocity(Block dispenser, Location dropLocation, BlockFace dispenserDirection) {
		return getPower(dispenser) > 1.0 ? super.getVelocity(dispenser, dropLocation, dispenserDirection) : new Vector();
	}
}