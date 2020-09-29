package org.uninstal.corez;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.uninstal.corez.command.CommandCoreZ;
import org.uninstal.corez.data.PlayerData;
import org.uninstal.corez.listener.MainListener;
import org.uninstal.corez.utils.Values;

public class CoreZ extends JavaPlugin {
	
	private static Files f;
	private static HashMap<UUID, PlayerData> datas = new HashMap<>();

	@Override
	public void onEnable() {
		
		f = new Files(this);
		YamlConfiguration config = f.registerNewFile("config");
		YamlConfiguration config_data = f.registerNewFile("data");
		
		Values.load(config);
		
		for(String key : config_data.getKeys(false)) {
			
			UUID uuid = UUID.fromString(key);
			PlayerData data = new PlayerData(uuid, config_data.getInt(key));
			
			datas.put(uuid, data);
			config_data.set(key, null);
		}
		
		new Task(this);
		getCommand("corez").setExecutor(new CommandCoreZ());
		Bukkit.getPluginManager().registerEvents(new MainListener(), this);
	}
	
	@Override
	public void onDisable() {
		
		YamlConfiguration config_data = f.getYaml("data");
		for(PlayerData data : datas.values()) {
			
			config_data.set(data.getUUID().toString(), data.getBar("thirst").getValue());
			
			if(Bukkit.getPlayer(data.getUUID()) != null) {
				
				Player player = Bukkit.getPlayer(data.getUUID());
				data.getBossBar().removePlayer(player);
			}
		}
		
		f.save("data", config_data);
	}
	
	public static Collection<PlayerData> getDatas() {
		return datas.values();
	}
	
    public static void putData(Player player) {
		datas.put(player.getUniqueId(), new PlayerData(player.getUniqueId()));
	}
	
	public static void putData(Player player, PlayerData data) {
		datas.put(player.getUniqueId(), data);
	}
	
	public static boolean canGetData(UUID uuid) {
		return datas.containsKey(uuid);
	}
	
	public static PlayerData getData(UUID uuid) {
		return datas.get(uuid);
	}
	
	public static void removeData(UUID uuid) {
		datas.remove(uuid);
	}
	
	public static Files files() {
		return f;
	}
}
