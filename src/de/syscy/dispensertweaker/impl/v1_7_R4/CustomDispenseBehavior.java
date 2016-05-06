package de.syscy.dispensertweaker.impl.v1_7_R4;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;

import de.syscy.dispensertweaker.behavior.DTDispenseBehavior;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.server.v1_7_R4.Blocks;
import net.minecraft.server.v1_7_R4.IDispenseBehavior;
import net.minecraft.server.v1_7_R4.ISourceBlock;
import net.minecraft.server.v1_7_R4.ItemStack;

@AllArgsConstructor
public class CustomDispenseBehavior implements IDispenseBehavior {
	private @Getter DTDispenseBehavior dispenseBehavior;

	@Override
	public ItemStack a(ISourceBlock dispenserBlock, ItemStack itemStack) {
		World world = dispenserBlock.k().getWorld();
		Block craftDispenserBlock = world.getBlockAt((int) dispenserBlock.getBlockX(), (int) dispenserBlock.getBlockY(), (int) dispenserBlock.getBlockZ());

		CraftItemStack craftItemStack = CraftItemStack.asCraftMirror(itemStack);
		NMSSourceBlockContainer_v1_7_R4 sourceBlockContainer = new NMSSourceBlockContainer_v1_7_R4(craftDispenserBlock, dispenserBlock);

		CraftItemStack returnItemStack = (CraftItemStack) dispenseBehavior.dispense(sourceBlockContainer, craftItemStack);

		ItemStack nmsReturnItemStack = CraftItemStack.asNMSCopy(returnItemStack);

		return nmsReturnItemStack == null ? new ItemStack(Blocks.AIR, 0) : nmsReturnItemStack;
	}
}