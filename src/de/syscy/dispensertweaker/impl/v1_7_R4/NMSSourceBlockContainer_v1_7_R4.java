package de.syscy.dispensertweaker.impl.v1_7_R4;

import org.bukkit.block.Block;

import de.syscy.dispensertweaker.behavior.NMSSourceBlockContainer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.server.v1_7_R4.ISourceBlock;

@AllArgsConstructor
public class NMSSourceBlockContainer_v1_7_R4 implements NMSSourceBlockContainer {
	private @Getter Block block;
	private @Getter ISourceBlock sourceBlock;
}