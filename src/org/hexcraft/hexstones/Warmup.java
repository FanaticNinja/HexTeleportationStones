package org.hexcraft.hexstones;

import java.util.*;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.hexcraft.HexTeleportationStones;

public class Warmup {

	private HexTeleportationStones plugin;
	private HashMap<String, TeleportState> waitingPlayers = new HashMap<String, TeleportState>();
	
	public Warmup(HexTeleportationStones plugin)
	{
		this.plugin = plugin;
		startCounter();
	}

	public void addPlayer(Player player, TeleportState teleportState)
	{
		this.waitingPlayers.put(player.getName(), teleportState);
	}

	private boolean isSameBlock(Location loc, Location loc2)
	{
		if (plugin.config.bCancelOnMove == false) {
			return true;
		}
		if ((loc.getBlockX() == loc2.getBlockX()) && (loc.getBlockY() == loc2.getBlockY()) && (loc.getBlockZ() == loc2.getBlockZ())) {
			return true;
		}
		return false;
	}

	private void startCounter()
	{
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable()
		{
			public void run()
			{
				for (Iterator<TeleportState> iter = Warmup.this.waitingPlayers.values().iterator(); iter.hasNext();)
				{
					TeleportState state = (TeleportState)iter.next();
					if (!state.isProcessing())
					{
						state.setProcessing(true);
 
						Player player = state.getPlayer();
						if (player != null)
						{
							if (state.isTeleportTime())
							{
								if (isSameBlock(player.getLocation(), state.getLocation()))
								{
									Location loc = state.getDestination();

									//int x = loc.getBlockX();
									//int z = loc.getBlockZ();

									player.teleport(new Location(loc.getWorld(), loc.getBlockX() + 0.5D, loc.getBlockY(), loc.getBlockZ() + 0.5D));
									state.getPlayer().sendMessage(ChatColor.GOLD + "Old Magics have brought you here...");
								}
								else
								{
									state.getPlayer().sendMessage(ChatColor.RED + "Teleport cancelled upon moving...");
								}
								iter.remove();
							}
							else if (!isSameBlock(player.getLocation(), state.getLocation()))
							{
								state.getPlayer().sendMessage(ChatColor.RED + "Teleport cancelled upon moving...");
								iter.remove();
							}
							else
							{
								state.getPlayer().sendMessage(ChatColor.GOLD + "" + state.getCounter());
							}
						}
						else {
							iter.remove();
						}
						state.setProcessing(false);
					}
				}
			}
		}, 0L, 20L);
	}
}