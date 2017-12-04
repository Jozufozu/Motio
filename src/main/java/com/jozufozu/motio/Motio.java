package com.jozufozu.motio;

import com.jozufozu.motio.api.cap.*;
import com.jozufozu.motio.common.CommonProxy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = Motio.MODID, name = Motio.NAME, version = Motio.VERSION)
public class Motio
{
    public static final String MODID = "motio";
    public static final String NAME = "Motio";
    public static final String VERSION = "1.0";
    
    @SidedProxy(serverSide = "com.jozufozu.motio.common.CommonProxy", clientSide = "com.jozufozu.motio.client.ClientProxy")
    public static CommonProxy proxy;
    
    public Motio()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @Mod.Instance("motio")
    public static Motio instance;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.preInit(event);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit(event);
    }
    
    @SubscribeEvent
    public void attachMotio(AttachCapabilitiesEvent<Entity> event)
    {
        Entity entity = event.getObject();
        
        if (entity instanceof EntityPlayer)
        {
            event.addCapability(new ResourceLocation(MODID, "motio"), new MotioProviderPlayer(((EntityPlayer) entity)));
        }
    }
}
