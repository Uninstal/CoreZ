package org.uninstal.corez.command;

import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.uninstal.corez.CoreZ;
import org.uninstal.corez.data.PlayerData;
import org.uninstal.corez.utils.Values;

public class CommandCoreZ implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!sender.isOp()) {
			
			sender.sendMessage(Values.NOT_PERMISSON);
			return false;
		}
		
		else {
			
			if(args.length >= 1 && args[0].equalsIgnoreCase("reload")) {
				
				CoreZ.files().reload();
				YamlConfiguration config = CoreZ.files().getYaml("config");
				Values.load(config);
				
				for(PlayerData data : CoreZ.getDatas()) {
					
					BossBar bb = data.getBossBar();
					bb.setColor(Values.BARCOLOR);
					bb.setTitle(Values.BOSSBAR_TITLE.replace("<thirst>", String.valueOf(data.getBar("thirst").getValue())));
				}
				
				return true;
			}
			
			else {
				
				sender.sendMessage(" §a/corez reload §7- Reload plugin configuration.");
				return false;
			}
		}
	}
}
