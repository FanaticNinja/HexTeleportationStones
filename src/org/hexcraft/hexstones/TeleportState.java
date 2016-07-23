package org.hexcraft.hexstones;

import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TeleportState
{
	private UUID playerUniqueId;
	//private String playerName;
	private Location playerLocation;
	private Location destination;
	private int counter;
	private Player player;
	//private double health;
	
	// -- stone uses..
	public int numUsesLeft;

	private boolean processing;

	public TeleportState(Player player, Location dest, int counter, int numUsesLeft)
	{
		this.destination = dest;
		this.playerLocation = player.getLocation();
		//this.playerName = player.getName();
		this.player = player;
		
		this.numUsesLeft = numUsesLeft;
                  
		//this.health = player.getHealth();

		// -- Counter 3
		this.counter = counter;
		this.playerUniqueId = player.getUniqueId();
	}

	public Location getLocation()
	{
		return this.playerLocation;
	}

	public boolean isTeleportTime()
	{
		if (this.counter > 1)
		{
			this.counter -= 1;
			return false;
		}
		return true;
	}
	public Player getPlayer()
	{
		return this.player;
	}

	public int getCounter()
	{
		return this.counter;
	}

	public void setCounter(int counter)
	{
		this.counter = counter;
	}

	public Location getDestination()
	{
		return this.destination;
	}
 
	public boolean isProcessing()
	{
		return this.processing;
	}
 
	public void setProcessing(boolean processing)
	{
		this.processing = processing;
	}

	public UUID getUniqueId()
	{
		return this.playerUniqueId;
	}
}