package de.syscy.dispensertweaker.impl.v1_9_R1;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;

import de.syscy.dispensertweaker.behavior.DTDispenseBehavior;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.server.v1_9_R1.Blocks;
import net.minecraft.server.v1_9_R1.IDispenseBehavior;
import net.minecraft.server.v1_9_R1.ISourceBlock;
import net.minecraft.server.v1_9_R1.ItemStack;

@AllArgsConstructor
public class CustomDispenseBehavior implements IDispenseBehavior {
	private @Getter DTDispenseBehavior dispenseBehavior;

	@Override
	public ItemStack a(ISourceBlock dispenserBlock, ItemStack itemStack) {
		World world = dispenserBlock.getWorld().getWorld();
		Block craftDispenserBlock = world.getBlockAt((int) dispenserBlock.getBlockPosition().getX(), (int) dispenserBlock.getBlockPosition().getY(), (int) dispenserBlock.getBlockPosition().getZ());

		CraftItemStack craftItemStack = CraftItemStack.asCraftMirror(itemStack);
		NMSSourceBlockContainer_v1_9_R1 sourceBlockContainer = new NMSSourceBlockContainer_v1_9_R1(craftDispenserBlock, dispenserBlock);

		CraftItemStack returnItemStack = (CraftItemStack) dispenseBehavior.dispense(sourceBlockContainer, craftItemStack);

		ItemStack nmsReturnItemStack = CraftItemStack.asNMSCopy(returnItemStack);

		return nmsReturnItemStack == null ? new ItemStack(Blocks.AIR, 0) : nmsReturnItemStack;
	}
}