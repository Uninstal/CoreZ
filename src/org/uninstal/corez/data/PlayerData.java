package org.uninstal.corez.data;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.uninstal.corez.utils.Values;

public class PlayerData {

	private UUID uuid;
	private BossBar bb;
	
	private HashMap<String, Bar> bars;

	public PlayerData(UUID uuid) {
		
		this.uuid = uuid;
		this.bars = new HashMap<>();
		
		Bar bar = new Bar("thirst", 0, 100, 100);
		bars.put("thirst", bar);
		
		bb = Bukkit.createBossBar(Values.BOSSBAR_TITLE.replace("<thirst>", String.valueOf(100)), Values.BARCOLOR, BarStyle.SOLID, BarFlag.CREATE_FOG);
		
		double progress = bar.getValue() * 0.01;
		bb.setProgress(progress);
		bb.setVisible(true);
		
        if(Bukkit.getPlayer(uuid) != null) {
			
			Player player = Bukkit.getPlayer(uuid);
			bb.addPlayer(player);
		}
	}
	
    public PlayerData(UUID uuid, int thirst) {
		
		this.uuid = uuid;
		this.bars = new HashMap<>();
		
		Bar bar = new Bar("thirst", 0, 100, thirst);
		bars.put("thirst", bar);
		
		bb = Bukkit.createBossBar(Values.BOSSBAR_TITLE.replace("<thirst>", String.valueOf(bar.getValue())), Values.BARCOLOR, BarStyle.SOLID, BarFlag.CREATE_FOG);
		
		double progress = bar.getValue() * 0.01;
		bb.setProgress(progress);
		bb.setVisible(true);
		
		if(Bukkit.getPlayer(uuid) != null) {
			
			Player player = Bukkit.getPlayer(uuid);
			bb.addPlayer(player);
		}
	}
	
    public UUID getUUID() {
		return uuid;
	}
    
    public BossBar getBossBar() {
    	return bb;
    }
	
	public void putBar(String name, Bar bar) {
		bars.put(name, bar);
	}
	
	public Bar getBar(String name) {
		return bars.get(name);
	}
}
