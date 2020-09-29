package org.uninstal.corez;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.uninstal.corez.data.Bar;
import org.uninstal.corez.data.PlayerData;
import org.uninstal.corez.event.ThirstChangeEvent;
import org.uninstal.corez.event.ThirstDamageEvent;
import org.uninstal.corez.utils.Values;

public class Task implements Runnable {
	
	private Random rand = new Random();
	private int id;
	
	private int thirstCounter = 0;
	private int zombieCounter = 0;
	private int damageCounter = 0;

	public Task(CoreZ plugin) {
		this.id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 0, 20);
	}

	@Override
	public void run() {
		
		if(thirstCounter >= Values.THIRST_RATE) {
			
			for(Player player : Bukkit.getOnlinePlayers()) {
				
				if(player.getGameMode() != GameMode.SURVIVAL && player.getGameMode() != GameMode.ADVENTURE) continue;
				if(!CoreZ.canGetData(player.getUniqueId())) continue;
				if(Values.BLOCKED_WORLDS.contains(player.getWorld().getName())) continue;
				
				PlayerData data = CoreZ.getData(player.getUniqueId());
				Bar bar = data.getBar("thirst");
				if(bar.getValue() == 0) continue;
				
				ThirstChangeEvent event = new ThirstChangeEvent(player, data, Math.max(bar.getValue() - 1, 0), bar.getValue());
				Bukkit.getPluginManager().callEvent(event);
				
				if(!event.isCancelled()) {
					
					bar.setValue(bar.getValue() - 1);
					data.putBar("thirst", bar);
				}
				
				continue;
			}
			
			thirstCounter = 0;
		}
		
		thirstCounter++;
		
		if(damageCounter >= Values.DAMAGE_RATE) {
			
			for(Player player : Bukkit.getOnlinePlayers()) {
				
				if(player.getGameMode() != GameMode.SURVIVAL && player.getGameMode() != GameMode.ADVENTURE) continue;
				if(!CoreZ.canGetData(player.getUniqueId())) continue;
				if(Values.BLOCKED_WORLDS.contains(player.getWorld().getName())) continue;
				
				PlayerData data = CoreZ.getData(player.getUniqueId());
				Bar bar = data.getBar("thirst");
				if(bar.getValue() == 0) {
					
					ThirstDamageEvent event = new ThirstDamageEvent(player, data, Values.DAMAGE_VALUE);
					Bukkit.getPluginManager().callEvent(event);
					
					if(!event.isCancelled()) {
						
						int damage = event.getDamage();
						player.damage(damage);
					}
				}
			}
			
			damageCounter = 0;
		}
		
		damageCounter++;
		
		if(Values.ZOMBIE_ENABLE) {
			
			if(zombieCounter >= Values.ZOMBIE_SPAWN_RATE) {
				
				for(Player player : Bukkit.getOnlinePlayers()) {
					
					int nearby = player.getNearbyEntities(75, 10, 75).size();
					if(nearby >= Values.MAX_ENTITIES) continue;
					
					int spawn = rand.nextInt(11);
					int spawned = 0;
					
					for(int i = 0; i <= spawn; i++) {
						
						int xCount = rand.nextInt(100) - 50;
						int zCount = rand.nextInt(100) - 50;
						
						if(Math.abs(xCount) < 20 && Math.abs(zCount) < 20) {
							
							int r = rand.nextInt(2);
							
							if(r == 1) xCount = xCount < 0 ? xCount - 15 : xCount + 15;
							else zCount = zCount < 0 ? zCount - 15 : zCount + 15;
						}
						
						int x = player.getLocation().getBlockX() + xCount;
						int z = player.getLocation().getBlockZ() + zCount;
						int y = player.getWorld().getHighestBlockYAt(x, z);
						
						Location loc = new Location(player.getWorld(), x, y, z);
						
						if(loc.getBlock().getRelative(0, -1, 0).getType() == Material.LEAVES ||
						   loc.getBlock().getRelative(0, -1, 0).getType() == Material.LEAVES_2) continue;
						if(loc.getBlock().getLightFromBlocks() > 9) continue;
						
						Zombie zombie = (Zombie) player.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
						zombie.setBaby(false);
						
						if(Values.ZOMBIE_SPEED > 1) zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200000, Values.ZOMBIE_SPEED - 2, false, false));
						if(Values.ZOMBIE_HEALTH != 20.0) zombie.setHealth(Values.ZOMBIE_HEALTH);
						
						++spawned;
					}
					
					System.out.println("[CoreZ] " + spawned + "/" + spawn + " zombie has spawned!");
					zombieCounter = 0;
				}
			}
			
			zombieCounter++;
			return;
		}
	}
	
	public void cancel() {
		Bukkit.getScheduler().cancelTask(id);
	}
}
