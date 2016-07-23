package org.hexcraft.listeners;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.hexcraft.HexTeleportationStones;
import org.hexcraft.hexstones.ConfigStone;
import org.hexcraft.hexstones.TeleportState;
import org.hexcraft.util.Cooldown;
import org.hexcraft.util.DiscUtil;
import org.hexcraft.util.PlayerCooldown;

public class Stone implements Listener {

	private HexTeleportationStones plugin;
	
	private Integer cooldownTime;
	
	public Stone(HexTeleportationStones _plugin)
	{
		this.plugin = _plugin;
		
		cooldownTime = (plugin.config.cooldownTimeInSeconds * 1000);
		
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	public boolean checkhand(ItemStack item) {
		
		if (item == null) {
			return false;
		}
		
		if (item.getData().getItemType() == plugin.config.Material && item.getItemMeta().hasLore()) {
			return true;
		}
		else {
			return false;
		}
		
	}
	
	@EventHandler(ignoreCancelled = false, priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent event) {
		// -- check for our special emerald...
		//plugin.AddLogEntry("ItemID: " + event.getItem().getTypeId());
		//plugin.AddLogEntry("Damage: " + event.getItem().getData());
		
		if(event.getAction() == Action.PHYSICAL) {
			return;
		}
		
		if (event.getItem() == null) {
			return;
		}
		
		ItemStack mainHand = event.getPlayer().getInventory().getItemInMainHand();
		ItemStack offHand = event.getPlayer().getInventory().getItemInOffHand();
		
		boolean bMainHand = checkhand(mainHand);
		ItemStack stone = (bMainHand) ? mainHand : offHand;
		
		
		if (checkhand(stone)) {
			
			// -- get id of thingy
			  String temp = stone.getItemMeta().getLore().get(0);
			  //plugin.AddLogEntry("Lore: " + temp);
			  
			  //minVal = (a < b) ? a : b;
			  
			  if (!temp.equals("Unidentified Stone"))
			  {
				  // -- check if has 2nd line,
				  //List<String> Lores = event.getPlayer().getItemInHand().getItemMeta().getLore();
				  
				  if (stone.getItemMeta().getLore().size() > 1)
				  {
					  if (stone.getItemMeta().getLore().get(1).contains("Originator:"))
					  {
						  TeleportState teleportState = GetLocationFromLore(temp, event.getPlayer());
							if (teleportState != null)
							{
								PlayerCooldown pc = Cooldown.getCooldown("TeleportationStone" + temp, event.getPlayer().getName());
								if(pc==null) {
									// -- Use allowed
									// -- Init warmup!
									event.getPlayer().sendMessage(ChatColor.GOLD + "Summoning Old Magics, stand still for " + plugin.config.warmupTimeInSeconds + " seconds.");
									plugin.wManager.addPlayer(event.getPlayer(), teleportState);
									
									//name for cooldown, name of player, length of cooldown in milliseconds
									Cooldown.addCooldown("TeleportationStone" + temp, event.getPlayer().getName(), cooldownTime);
									//event.getPlayer().teleport(loc);
									
									//event.getPlayer().sendMessage(ChatColor.GOLD + "Old Magics have brought you here...");
									
									// -- check the actual item...
									if (teleportState.numUsesLeft <= 0 && plugin.config.numUses >= 0) {
										// -- destroy item..
										if (bMainHand) {
					    					event.getPlayer().getInventory().setItemInMainHand(null);
					    				}
					    				else {
					    					event.getPlayer().getInventory().setItemInOffHand(null);
					    				}
									}
								}
								else{
									if(pc.isOver()) {
										// -- Teleport allowed!							
										pc.reset();
										//event.getPlayer().teleport(loc);
										//event.getPlayer().sendMessage(ChatColor.GOLD + "Old Magics have brought you here...");
										// -- Use allowed
										// -- Init warmup!
										event.getPlayer().sendMessage(ChatColor.GOLD + "Summoning Old Magics, stand still for " + plugin.config.warmupTimeInSeconds + " seconds.");
										plugin.wManager.addPlayer(event.getPlayer(), teleportState);
										
										// -- check the actual item...
										if (teleportState.numUsesLeft <= 0 && plugin.config.numUses >= 0) {
											// -- destroy item..
											if (bMainHand) {
						    					event.getPlayer().getInventory().setItemInMainHand(null);
						    				}
						    				else {
						    					event.getPlayer().getInventory().setItemInOffHand(null);
						    				}
										}
									}
									else
										event.getPlayer().sendMessage(ChatColor.GOLD + "It seems the stone is still regenerating." );
								}
							}
							else
							{
								event.getPlayer().sendMessage(ChatColor.RED + "Un-linked Teleportation Stone.");
							}
					  }
				  }
				  
				  
			  }
			  else
			  {
				// -- set the stone location etc
				SaveConfig();
				
				
				
				// -- save out stuff...
				//SaveStoneToFile(Integer.toString(plugin.config.uniqueID), event.getPlayer());
				SaveStoneToFile(Integer.toString(plugin.config.uniqueID), event.getPlayer());
				
				List<String> lore = new ArrayList<String>();
				
				lore.add(Integer.toString(plugin.config.uniqueID));
				lore.add(ChatColor.GRAY + "Originator: " + event.getPlayer().getName());
				
				ItemStack TeleportationStoneItem = plugin.CreateItem(plugin.config.Material, 1, (short)0, ChatColor.DARK_AQUA + plugin.config.Name, lore);
				
				//event.getPlayer().setItemInHand(TeleportationStoneItem);
				
				if (bMainHand) {
					event.getPlayer().getInventory().setItemInMainHand(TeleportationStoneItem);
				}
				else {
					event.getPlayer().getInventory().setItemInOffHand(TeleportationStoneItem);
				}
				
				event.getPlayer().sendMessage(ChatColor.GOLD + "The Stone has been imbued with information.");
			  }

	    }
	}
	
	public void SaveStoneToFile(String Identifier, Player p)
	{
		
		String configWeaponFolderPath = plugin.dataLayerFolderPath + File.separator + "stones";
		String configFilePath = configWeaponFolderPath + File.separator + Identifier + ".json";
		
		ConfigStone newStone = new ConfigStone();
    	
		newStone.Creator = p.getName();
		newStone.World = p.getWorld().getName();
		newStone.x = p.getLocation().getBlockX();
		newStone.y = p.getLocation().getBlockY();
		newStone.z = p.getLocation().getBlockZ();
		newStone.yaw = p.getLocation().getYaw();
		newStone.pitch = p.getLocation().getPitch();
		
		newStone.numUses = plugin.config.numUses;
		
		String save = plugin.gson.toJson(newStone);
		DiscUtil.writeCatch(new File(configFilePath), save);
  
	}
	
	public void SaveConfig() {
		
		// -- Save
		plugin.config.uniqueID += 1;
		
		String save = plugin.gson.toJson(plugin.config);
		DiscUtil.writeCatch(new File(plugin.configFilePath), save);
	    
	}
    
	public TeleportState GetLocationFromLore(String Identifier, Player player)
	{
		
		
		String configWeaponFolderPath = this.plugin.dataLayerFolderPath + File.separator + "stones";
		String configFilePath = configWeaponFolderPath + File.separator + Identifier + ".json";
		
		// -- load from json config
		String content = DiscUtil.readCatch(new File(configFilePath));
	    
	    if (content == null)
	    {
	    	return null;
	    }
	    
	    // -- load config from content
		ConfigStone configStone = plugin.gson.fromJson(content, ConfigStone.class);
	    
	    // -- set base location..
 		if (configStone.Creator == null)
 		{
 			return null;
 		}
 		
 		if (configStone.numUses == null) {
 			configStone.numUses = plugin.config.numUses - 1;
 		}
 		else {
 			configStone.numUses --;
 		}
    	
    	Location loc = null;
		
		// -- Location
		Double x = (double) configStone.x;
		Double y = (double) configStone.y;
		Double z = (double) configStone.z;
		float yaw = (float) configStone.yaw;
		float pitch = (float) configStone.pitch;
		String world = configStone.World;
		
		// -- set base location..
		if (configStone.x != null)
		{
			loc = new Location(plugin.getServer().getWorld(world), x, y, z, yaw, pitch);
			return new TeleportState(player, loc, plugin.config.warmupTimeInSeconds, configStone.numUses);
		}
		else {
			return null;
		}
    	
    	
	}
	
}
