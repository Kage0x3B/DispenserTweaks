package de.syscy.dispensertweaker.impl.v1_8_R3;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import de.syscy.dispensertweaker.IDispenserTweaker;
import de.syscy.dispensertweaker.behavior.DTDispenseBehavior;
import de.syscy.dispensertweaker.behavior.NMSSourceBlockContainer;
import net.minecraft.server.v1_8_R3.BlockDispenser;
import net.minecraft.server.v1_8_R3.DispenseBehaviorItem;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.IDispenseBehavior;
import net.minecraft.server.v1_8_R3.IProjectile;
import net.minecraft.server.v1_8_R3.ISourceBlock;
import net.minecraft.server.v1_8_R3.Item;

public class DispenserTweaker_v1_8_R3 implements IDispenserTweaker {
	@Override
	public void overrideBehavior(int itemId, DTDispenseBehavior dispenseBehavior) {
		Item item = null;

		try {
			item = Item.getById(itemId);
		} catch(Exception ex) {
			return;
		}

		if(item == null) {
			return;
		}

		BlockDispenser.REGISTRY.a(item, new CustomDispenseBehavior(dispenseBehavior));
	}

	@Override
	public boolean executeDispenseBehavior(ItemStack itemStack, NMSSourceBlockContainer dispenserBlock) {
		ISourceBlock nmsBlock = ((NMSSourceBlockContainer_v1_8_R3) dispenserBlock).getSourceBlock();
		net.minecraft.server.v1_8_R3.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);

		IDispenseBehavior dispenseBehavior = (IDispenseBehavior) BlockDispenser.REGISTRY.get(nmsItemStack.getItem());

		if((dispenseBehavior != IDispenseBehavior.NONE) && (dispenseBehavior.getClass() != DispenseBehaviorItem.class)) {
			dispenseBehavior.a(nmsBlock, nmsItemStack);

			return true;
		}

		return false;
	}
	
	@Override
	public boolean shootProjectile(org.bukkit.entity.Entity entity, Vector velocity, float power, float a) {
		Entity nmsEntity = ((CraftEntity) entity).getHandle();
		
		if(nmsEntity instanceof IProjectile) {
			((IProjectile) nmsEntity).shoot(velocity.getX(), velocity.getY(), velocity.getZ(), power, a);
			return true;
		} else {
			return false;
		}
	}
}