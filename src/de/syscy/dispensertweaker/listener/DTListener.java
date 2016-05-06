package de.syscy.dispensertweaker.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Explosive;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import de.syscy.dispensertweaker.DispenserSetting;
import de.syscy.dispensertweaker.DispenserSetting.Setting;
import de.syscy.dispensertweaker.DispenserTweaker;

public class DTListener implements Listener {
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(event.getClickedBlock().getType() == Material.DISPENSER && DispenserTweaker.getInstance().getCurrentSettingChanges().containsKey(event.getPlayer())) {
				DispenserSetting dispenserSetting = DispenserTweaker.getInstance().getCurrentSettingChanges().get(event.getPlayer());
				dispenserSetting.apply(event.getClickedBlock());

				if(dispenserSetting.getSetting() == Setting.RESET) {
					event.getPlayer().sendMessage(ChatColor.GREEN + "Reset all values!");
				} else {
					event.getPlayer().sendMessage(ChatColor.GREEN + "Changed the " + dispenserSetting.getSetting().getFancyName() + " setting to " + dispenserSetting.getValue() + "!");
				}

				DispenserTweaker.getInstance().getCurrentSettingChanges().remove(event.getPlayer());

				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {
		if(DispenserTweaker.getInstance().getEntityExplosionStrength().containsKey(event.getEntity().getEntityId())) {
			double explosionStrength = DispenserTweaker.getInstance().getEntityExplosionStrength().get(event.getEntity().getEntityId());
			boolean setFire = false;

			if(event.getEntity() instanceof Explosive) {
				setFire = ((Explosive) event.getEntity()).isIncendiary();
			}

			if(explosionStrength > 0) {
				event.setCancelled(true);
				event.getLocation().getWorld().createExplosion(event.getLocation(), (float) explosionStrength, setFire);
			}
			
			DispenserTweaker.getInstance().getEntityExplosionStrength().remove(event.getEntity().getEntityId());
		}
	}
	
	@EventHandler
	public void onBlockIgnite(BlockIgniteEvent event) {
		if(event.getCause() == IgniteCause.FIREBALL) {
			if(DispenserTweaker.getInstance().getEntityExplosionStrength().containsKey(event.getIgnitingEntity().getEntityId())) {
				double explosionStrength = DispenserTweaker.getInstance().getEntityExplosionStrength().get(event.getIgnitingEntity().getEntityId());
				boolean setFire = false;

				if(event.getIgnitingEntity() instanceof Explosive) {
					setFire = ((Explosive) event.getIgnitingEntity()).isIncendiary();
				}

				if(explosionStrength > 0) {
					event.setCancelled(true);
					event.getBlock().getLocation().getWorld().createExplosion(event.getBlock().getLocation(), (float) explosionStrength, setFire);
				}
				
				DispenserTweaker.getInstance().getEntityExplosionStrength().remove(event.getIgnitingEntity().getEntityId());
			}
		}
	}
}