package org.hexcraft.hexstones;

import java.util.List;
import java.util.Map;

import org.bukkit.Material;

import com.google.gson.annotations.SerializedName;

public class Config {
	
	// -- General Config
	@SerializedName("configVersion") public String configVersion;
	@SerializedName("uniqueID") public Integer uniqueID;
	
	@SerializedName("warmupTimeInSeconds") public Integer warmupTimeInSeconds;
	@SerializedName("cooldownTimeInSeconds") public Integer cooldownTimeInSeconds;
	
	@SerializedName("numUses") public Integer numUses;
	
	@SerializedName("bCancelOnMove") public boolean bCancelOnMove;
	
	@SerializedName("Name") public String Name;
	@SerializedName("Material") public Material Material;
	@SerializedName("ShapedRecipe") public List<String> ShapedRecipe;
	@SerializedName("RecipeMaterials") public Map<String, Material> RecipeMaterials;
	
	// -- MultiWorld
	//@SerializedName("worldWhitelist") public List<String> worldWhitelist;

}
