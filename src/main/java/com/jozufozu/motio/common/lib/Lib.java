package com.jozufozu.motio.common.lib;

import com.jozufozu.motio.Motio;
import net.minecraft.util.ResourceLocation;

public class Lib
{
    public static final String MOTIUM_MATERIAL = "motium";
    
    protected static ResourceLocation res(String resourceName)
    {
        return new ResourceLocation(Motio.MODID, resourceName);
    }
}
