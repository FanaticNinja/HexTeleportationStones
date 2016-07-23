package org.hexcraft.hexstones;

import com.google.gson.annotations.SerializedName;

public class ConfigStone {
	
	@SerializedName("Creator") public String Creator;
	@SerializedName("World") public String World;
	
	@SerializedName("x") public Integer x;
	@SerializedName("y") public Integer y;
	@SerializedName("z") public Integer z;
	
	@SerializedName("yaw") public Float yaw;
	@SerializedName("pitch") public Float pitch;
	
	@SerializedName("numUses") public Integer numUses;

}
