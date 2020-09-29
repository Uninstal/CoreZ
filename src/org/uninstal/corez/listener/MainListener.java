package org.uninstal.corez.listener;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.uninstal.corez.CoreZ;
import org.uninstal.corez.data.Bar;
import org.uninstal.corez.data.PlayerData;
import org.uninstal.corez.event.ThirstChangeEvent;
import org.uninstal.corez.utils.Values;

public class MainListener implements Listener{
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		
		PlayerData data = CoreZ.getData(e.getEntity().getUniqueId());
		if(data != null) {
			data.getBossBar().removePlayer(e.getEntity());
			CoreZ.removeData(data.getUUID());
		}
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		
		if(!Values.BLOCKED_WORLDS.contains(e.getRespawnLocation().getWorld().getName()))
			CoreZ.putData(e.getPlayer());
	}
	
	@EventHandler
	public void onIngite(EntityCombustEvent e) {
		
		if(!Values.ZOMBIE_ENABLE) return;
		if(e.getEntityType() == EntityType.ZOMBIE) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onConsume(PlayerItemConsumeEvent e) {
		
		Player player = e.getPlayer();
		ItemStack item = e.getItem();
		if(Values.BLOCKED_WORLDS.contains(player.getWorld().getName())) return;
		
		if(Values.HEALTH_ITEMS.containsKey(item.getType())) {
			
			double health = player.getHealth() + Values.HEALTH_ITEMS.get(item.getType());
			player.setHealth(Math.min(20.0, Math.max(0.0, health)));
		}
		
		if(Values.THIRST_ITEMS.containsKey(item.getType())) {
			
			int thirst = Values.THIRST_ITEMS.get(item.getType());
			PlayerData data = CoreZ.getData(player.getUniqueId());
			Bar bar = data.getBar("thirst");
			
			ThirstChangeEvent event = new ThirstChangeEvent(player, data, Math.min(bar.getValue() + thirst, 100), bar.getValue());
			Bukkit.getPluginManager().callEvent(event);
			
			if(!event.isCancelled()) {
				
				bar.setValue(bar.getValue() + thirst);
				data.putBar("thirst", bar);
			}
		}
	}
	
	@EventHandler
	public void onRegenHealth(EntityRegainHealthEvent e) {
		
		if(!Values.BLOCKED_WORLDS.contains(e.getEntity().getWorld().getName())) {
			
			if(e.getEntity() instanceof Player
					&& e.getRegainReason() == RegainReason.SATIATED) {
				
				if(Values.DISABLE_AUTOREGEN) {
					
					e.setCancelled(true);
					return;
				}
			}
		}
	}
	
	@EventHandler
	public void onTeleport(PlayerTeleportEvent e) {
		
		Player player = e.getPlayer();
		World from = e.getFrom().getWorld();
		World to = e.getTo().getWorld();
		
		if(Values.BLOCKED_WORLDS.contains(to.getName()) 
				&& !Values.BLOCKED_WORLDS.contains(from.getName())) {
			
			PlayerData data = CoreZ.getData(player.getUniqueId());
			data.getBossBar().removePlayer(e.getPlayer());
			
			return;
		}
		
		else if(!Values.BLOCKED_WORLDS.contains(to.getName()) 
				&& Values.BLOCKED_WORLDS.contains(from.getName())) {
			
			PlayerData data = CoreZ.canGetData(player.getUniqueId()) ? CoreZ.getData(player.getUniqueId()) : new PlayerData(player.getUniqueId());
			data.getBossBar().addPlayer(e.getPlayer());
			
			return;
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		
		Player player = e.getPlayer();
		UUID uuid = player.getUniqueId();
		String worldname = player.getWorld().getName();
		
		if(!Values.BLOCKED_WORLDS.contains(worldname)) {
			
			PlayerData data = CoreZ.canGetData(uuid) ? CoreZ.getData(uuid) : new PlayerData(uuid);
			CoreZ.putData(player, data);
			
			data.getBossBar().addPlayer(player);
		}
	}

	@EventHandler
	public void onThirst(ThirstChangeEvent e) {
		
		Player player = e.getPlayer();
		PlayerData data = e.getData();
		
		double progress = (double) e.getNewValue() * 0.01;
		data.getBossBar().setProgress(progress);
		data.getBossBar().setTitle(Values.BOSSBAR_TITLE.replace("<thirst>", String.valueOf(e.getNewValue())));
		
		if(Values.THIRST_WARNINGS.containsKey(e.getNewValue()) && e.getNewValue() < e.getOldValue()) {
			
			String message = Values.THIRST_WARNINGS.get(e.getNewValue()).replace("&", "§");
			player.sendMessage(message);
		}
	}
}
