package com.jozufozu.motio.common;

import com.jozufozu.motio.Motio;
import com.jozufozu.motio.api.cap.CapabilityMotio;
import com.jozufozu.motio.api.cap.IMotio;
import com.jozufozu.motio.api.cap.MotioBank;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod.EventBusSubscriber(modid = Motio.MODID)
public class CommonProxy
{
    public void preInit(FMLPreInitializationEvent event)
    {
        CapabilityManager.INSTANCE.register(IMotio.class, new CapabilityMotio(), MotioBank.class);
    }
    
    public void init(FMLInitializationEvent event)
    {
    
    }
    
    public void postInit(FMLPostInitializationEvent event)
    {
    
    }
}
