package de.syscy.dispensertweaker.behavior;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import de.syscy.dispensertweaker.DispenserTweaker;

public class DTDispenseBehaviorItem implements DTDispenseBehavior {
	private static BlockFace[] blockFaces = new BlockFace[] { BlockFace.DOWN, BlockFace.UP, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST, BlockFace.EAST };

	private static Random random = new Random();

	@SuppressWarnings("deprecation")
	public final ItemStack dispense(NMSSourceBlockContainer block, ItemStack itemStack) {
		BlockFace dispenserDirection = getDispenserDirectionFromData(block.getBlock().getData());
		itemStack.setAmount(itemStack.getAmount() - 1);

		ItemStack leftItemStack = dispenseItem(block, itemStack, dispenserDirection);
		playDispenserSound(block.getBlock());
		playSmokeEffect(block.getBlock(), dispenserDirection == BlockFace.UP ? dispenserDirection : dispenserDirection.getOppositeFace());

		return leftItemStack;
	}

	protected ItemStack dispenseItem(NMSSourceBlockContainer block, ItemStack itemStack, BlockFace dispenserDirection) {
		ItemStack itemStack2 = itemStack.clone();

		if(!tryDispensingItem(block.getBlock().getWorld(), itemStack2, 6, dispenserDirection, block)) {
			itemStack2.setAmount(itemStack.getAmount() + 1);
		}

		return itemStack2;
	}

	public static boolean tryDispensingItem(World world, ItemStack itemStack, int i, BlockFace dispenserDirection, NMSSourceBlockContainer block) {
		Location dropLocation = getDropLocation(block.getBlock());

		double x = dropLocation.getX();
		double y = dropLocation.getY();
		double z = dropLocation.getZ();

		if(dispenserDirection == BlockFace.UP || dispenserDirection == BlockFace.DOWN) {
			y -= 0.125;
		} else {
			y -= 0.15625;
		}

		Item itemEntity = world.dropItem(new Location(world, x, y, z), itemStack);

		double horizontalMotionFactor = random.nextDouble() * 0.1 + 0.2;

		double motX = (dispenserDirection.getModX() * horizontalMotionFactor);
		double motY = 0.2;
		double motZ = (dispenserDirection.getModZ() * horizontalMotionFactor);

		motX += random.nextGaussian() * 0.0074 * i;
		motY += random.nextGaussian() * 0.0074 * i;
		motZ += random.nextGaussian() * 0.0074 * i;

		itemEntity.setVelocity(new Vector(motX, motY, motZ));

		BlockDispenseEvent event = new BlockDispenseEvent(block.getBlock(), itemStack.clone(), itemEntity.getVelocity());

		Bukkit.getServer().getPluginManager().callEvent(event);

		if(event.isCancelled()) {
			itemEntity.remove();

			return false;
		}

		itemEntity.setItemStack(event.getItem());
		itemEntity.setVelocity(event.getVelocity().clone());

		if(!event.getItem().getType().equals(itemStack.getType())) {
			if(DispenserTweaker.executeDispenseBehavior(event.getItem(), block)) {
				itemEntity.remove();
			}

			return false;
		}

		return true;
	}

	protected void playDispenserSound(Block block) {
		block.getWorld().playEffect(block.getLocation(), Effect.CLICK1, 0);
	}

	protected void playSmokeEffect(Block block, BlockFace blockFace) {
		if(blockFace == BlockFace.DOWN) {
			return;
		}

		Location location = block.getLocation();

		if(blockFace == BlockFace.UP) {
			location = location.add(0, 1, 0);
		}

		block.getWorld().playEffect(block.getLocation(), Effect.SMOKE, blockFace);
	}

	protected static BlockFace getDispenserDirectionFromData(int blockData) {
		return blockFaces[blockData & 0x7];
	}

	@SuppressWarnings("deprecation")
	protected static Location getDropLocation(Block dispenserBlock) {
		BlockFace dispenserDirection = getDispenserDirectionFromData(dispenserBlock.getData());

		double x = dispenserBlock.getX() + 0.5;
		double y = dispenserBlock.getY() + 0.5;
		double z = dispenserBlock.getZ() + 0.5;

		x += 0.7 * dispenserDirection.getModX();
		y += 0.7 * dispenserDirection.getModY();
		z += 0.7 * dispenserDirection.getModZ();

		return new Location(dispenserBlock.getWorld(), x, y, z);
	}
}