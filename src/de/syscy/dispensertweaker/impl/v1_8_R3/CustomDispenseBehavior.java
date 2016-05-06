package de.syscy.dispensertweaker.impl.v1_8_R3;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;

import de.syscy.dispensertweaker.behavior.DTDispenseBehavior;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.IDispenseBehavior;
import net.minecraft.server.v1_8_R3.ISourceBlock;
import net.minecraft.server.v1_8_R3.ItemStack;

@AllArgsConstructor
public class CustomDispenseBehavior implements IDispenseBehavior {
	private @Getter DTDispenseBehavior dispenseBehavior;

	@Override
	public ItemStack a(ISourceBlock dispenserBlock, ItemStack itemStack) {
		World world = dispenserBlock.getWorld().getWorld();
		Block craftDispenserBlock = world.getBlockAt((int) dispenserBlock.getBlockPosition().getX(), (int) dispenserBlock.getBlockPosition().getY(), (int) dispenserBlock.getBlockPosition().getZ());
		
		CraftItemStack craftItemStack = CraftItemStack.asCraftMirror(itemStack);
		NMSSourceBlockContainer_v1_8_R3 sourceBlockContainer = new NMSSourceBlockContainer_v1_8_R3(craftDispenserBlock, dispenserBlock);
		
		CraftItemStack returnItemStack = (CraftItemStack) dispenseBehavior.dispense(sourceBlockContainer, craftItemStack);
		
		ItemStack nmsReturnItemStack = CraftItemStack.asNMSCopy(returnItemStack);
		
		return nmsReturnItemStack == null ? new ItemStack(Blocks.AIR, 0) : nmsReturnItemStack;
	}
}