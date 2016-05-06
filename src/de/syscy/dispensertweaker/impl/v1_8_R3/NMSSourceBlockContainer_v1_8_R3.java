package de.syscy.dispensertweaker.impl.v1_8_R3;

import org.bukkit.block.Block;

import de.syscy.dispensertweaker.behavior.NMSSourceBlockContainer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.ISourceBlock;

@AllArgsConstructor
public class NMSSourceBlockContainer_v1_8_R3 implements NMSSourceBlockContainer {
	private @Getter Block block;
	private @Getter ISourceBlock sourceBlock;
}