package org.uninstal.corez.utils;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.configuration.file.YamlConfiguration;

public class Values {
	
	public static void load(YamlConfiguration config) {
		
		BOSSBAR_TITLE = config.getString("thirst.bossbar.title", "§fThirst: §a<thirst>").replace("&", "§");
		BLOCKED_WORLDS = config.getStringList("blocked-worlds");
		config.getConfigurationSection("messages.warnings").getKeys(false).forEach(value -> THIRST_WARNINGS.put(Integer.valueOf(value), config.getString("messages.warnings." + value).replace("&", "§")));
		BARCOLOR = BarColor.valueOf(config.getString("thirst.bossbar.color", "blue").toUpperCase());
		NOT_PERMISSON = config.getString("messages.not-permission", "&cYou don`t have permission.").replace("&", "§");
		
		for(String value : config.getConfigurationSection("health.items").getKeys(false)) {
			
			if(Material.getMaterial(value.toUpperCase()) == null) {
				Bukkit.getConsoleSender().sendMessage("§c[CoreZ] Item '" + value.toUpperCase() + "' is null!");
			}
			
			else {
				HEALTH_ITEMS.put(Material.getMaterial(value.toUpperCase()), config.getDouble("health.items." + value));
			}
		}
		
        for(String value : config.getConfigurationSection("thirst.items").getKeys(false)){
        	
			if(Material.getMaterial(value.toUpperCase()) == null) {
				Bukkit.getConsoleSender().sendMessage("§c[CoreZ] Item '" + value + "' is null!");
			}
			
			else {
				THIRST_ITEMS.put(Material.getMaterial(value.toUpperCase()), config.getInt("thirst.items." + value));
			}
		}
		
		DISABLE_AUTOREGEN = config.getBoolean("health.disable-autoregen", true);
		THIRST_RATE = config.getInt("thirst.change-rate", 15);
		ZOMBIE_ENABLE = config.getBoolean("zombie.enable", false);
		ZOMBIE_SPAWN_RATE = config.getInt("zombie.spawn-rate", 30);
		MAX_ENTITIES = config.getInt("zombie.max-entities", 15);
		DAMAGE_RATE = config.getInt("thirst.damage.rate", 3);
		DAMAGE_VALUE = config.getInt("thirst.damage.value", 4);
		ZOMBIE_HEALTH = config.getDouble("zombie.modifications.health", 20.0);
		ZOMBIE_SPEED = config.getInt("zombie.modifications.speed", 1);
	}
	
	public static List<String> BLOCKED_WORLDS;
	
	public static String BOSSBAR_TITLE;
	public static String NOT_PERMISSON;
	public static HashMap<Integer, String> THIRST_WARNINGS = new HashMap<>();
	public static HashMap<Material, Integer> THIRST_ITEMS = new HashMap<>();
	public static BarColor BARCOLOR;
	public static int THIRST_RATE;
	public static int DAMAGE_RATE;
	public static int DAMAGE_VALUE;
	
	public static HashMap<Material, Double> HEALTH_ITEMS = new HashMap<>();
	public static boolean DISABLE_AUTOREGEN;
	
	public static boolean ZOMBIE_ENABLE;
	public static int ZOMBIE_SPAWN_RATE;
	public static int MAX_ENTITIES;
	public static int ZOMBIE_SPEED;
	public static double ZOMBIE_HEALTH;
	
}
