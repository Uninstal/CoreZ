package org.uninstal.corez.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.uninstal.corez.data.PlayerData;

public class ThirstChangeEvent extends Event implements Cancellable {
	
	private static HandlerList handlers = new HandlerList();
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

	private Player player;
	private PlayerData data;
	private int newValue;
	private int oldValue;
	
	private boolean cancel = false;

	public ThirstChangeEvent(Player player, PlayerData data, int newValue, int oldValue) {
		
		this.player = player;
		this.data = data;
		this.newValue = newValue;
		this.oldValue = oldValue;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public PlayerData getData() {
		return data;
	}
	
	public int getOldValue() {
		return oldValue;
	}
	
	public int getNewValue() {
		return newValue;
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
}
