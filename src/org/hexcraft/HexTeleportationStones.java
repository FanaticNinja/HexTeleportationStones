package org.hexcraft;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.hexcraft.HexCore;
import org.hexcraft.hexstones.Config;
import org.hexcraft.hexstones.Warmup;
import org.hexcraft.listeners.*;
import org.hexcraft.util.DiscUtil;

public final class HexTeleportationStones extends HexCore {
	
	public String dataLayerFolderPath = "plugins" + File.separator + "HexTeleportationStones";
    public String configFilePath = dataLayerFolderPath + File.separator + "config.json";
    
    //public String dataLayerFolderPath = "plugins" + File.separator + "HexTeleportationStones";
    //public String configFilePath = dataLayerFolderPath + File.separator + "config.yml";

    public Config config;
    public Warmup wManager;
    
    @Override
	public void onEnable()
	{
		if ( ! preEnable()) return;
		
		//getConfig().options().copyDefaults(true);
		//saveConfig();
		
		// -- load stuff
		
		//FileConfiguration config = YamlConfiguration.loadConfiguration(new File(this.configFilePath));
		
		// -- load from json config
		String content = DiscUtil.readCatch(new File(configFilePath));
		
		
		// -- check for defaults
		if(content == null) {
			
			config = new Config();
			config.configVersion = this.getDescription().getVersion();
			config.uniqueID = 0;
			config.warmupTimeInSeconds = 5;
			config.cooldownTimeInSeconds = 8;
			
			config.bCancelOnMove = true;
			
			config.numUses = -1;
			config.Name = "Teleportation Stone";
			config.Material = Material.MAGMA_CREAM;
			
			List<String> ShapedRecipe = new ArrayList<String>();
			ShapedRecipe.add("gxg");
			ShapedRecipe.add("zaz");
			ShapedRecipe.add("gxg");
			
			config.ShapedRecipe = ShapedRecipe;
			
			Map<String, Material> Recipe = new HashMap<String, Material>();
			
			Recipe.put("g", Material.EMERALD);
			Recipe.put("a", Material.GHAST_TEAR);
			Recipe.put("z", Material.DIAMOND);
			Recipe.put("x", Material.BLAZE_POWDER);
			
			config.RecipeMaterials = Recipe;
			
			// -- Save
			String save = gson.toJson(config);
			DiscUtil.writeCatch(new File(configFilePath), save);
		}
		else {
			// -- create config from json
			config = gson.fromJson(content, Config.class);
			
			// -- check config version
			if (config.configVersion.equals("0.1.1")) {
				
				config.configVersion = this.getDescription().getVersion();
				config.numUses = -1;
				config.Name = "Teleportation Stone";
				config.Material = Material.MAGMA_CREAM;
				
				List<String> ShapedRecipe = new ArrayList<String>();
				ShapedRecipe.add("gxg");
				ShapedRecipe.add("zaz");
				ShapedRecipe.add("gxg");
				
				config.ShapedRecipe = ShapedRecipe;
				
				Map<String, Material> Recipe = new HashMap<String, Material>();
				
				Recipe.put("g", Material.EMERALD);
				Recipe.put("a", Material.GHAST_TEAR);
				Recipe.put("z", Material.DIAMOND);
				Recipe.put("x", Material.BLAZE_POWDER);
				
				config.RecipeMaterials = Recipe;
				
				this.log("Updating config to version: " + config.configVersion);
				
				// -- Save
				String save = gson.toJson(config);
				DiscUtil.writeCatch(new File(configFilePath), save);
				
			}
		}
		
		List<String> lore = new ArrayList<String>();
    	lore.add("Unidentified Stone");
		
		ItemStack TeleportationStoneItem = CreateItem(config.Material, 1, (short)0, ChatColor.DARK_AQUA + config.Name, lore);

		/*
		ShapedRecipe TeleportationStoneRecipe = new ShapedRecipe(TeleportationStoneItem)
			.shape(new String[] { "gxg", "zaz", "gxg" })
			.setIngredient('g', Material.EMERALD)
			.setIngredient('a', Material.GHAST_TEAR)
			.setIngredient('z', Material.DIAMOND)
			.setIngredient('x', Material.BLAZE_POWDER);
		
		getServer().addRecipe(TeleportationStoneRecipe);
		*/
		
		// -- build shaped recipe
		ShapedRecipe sr = new ShapedRecipe(TeleportationStoneItem);
	    sr.shape(new String[] { config.ShapedRecipe.get(0), config.ShapedRecipe.get(1), config.ShapedRecipe.get(2) });
	    
	    for(Entry<String, Material> entry : config.RecipeMaterials.entrySet()) {
	    	
	        String key = entry.getKey();
	        char c = key.charAt(0);
	        Material value = entry.getValue();

	        sr.setIngredient(c, value);
	    }
	    
	    // -- add recipe to server
	    getServer().addRecipe(sr);
		
		// -- Listeners
		new Stone(this);
		
		// -- Warmup Support
		//new StoneDamageListener();
		//new StoneMoveListener();
		
		wManager = new Warmup(this);
		
		postEnable();
	}
	
	public ItemStack CreateItem(Material mat,
			int ammount,
			short damage,
			String name,
			List<String> lore)
    {
		
		// create new item stack
		ItemStack customItem = new ItemStack(mat, ammount, damage);
		
		// -- create meta
		ItemMeta meta = customItem.getItemMeta();

		// -- set lore
		meta.setLore(lore);
		meta.setDisplayName(name);
		
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		// -- apply new meta
		customItem.setItemMeta(meta);
		
		return customItem;
		
    }
	
	public void AddLogEntry(String entry)
    {
      log.info("HexTeleportationStones: " + entry);
    }
	
}
