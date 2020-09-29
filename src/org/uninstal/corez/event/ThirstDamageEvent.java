package org.uninstal.corez.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.uninstal.corez.data.PlayerData;

public class ThirstDamageEvent extends Event implements Cancellable {
	
	private static HandlerList handlers = new HandlerList();
	private boolean cancel = false;
	
	private Player player;
	private PlayerData data;
	private int damage;
	
	public ThirstDamageEvent(Player player, PlayerData data, int damage) {
		
		this.player = player;
		this.data = data;
		this.damage = damage;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public PlayerData getData() {
		return data;
	}
	
	public int getDamage() {
		return damage;
	}
	
	public void setDamage(int damage) {
		this.damage = damage;
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean arg0) {
		this.cancel = arg0;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
