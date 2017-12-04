package com.jozufozu.motio.common;

import com.jozufozu.motio.Motio;
import com.jozufozu.motio.common.items.*;
import com.jozufozu.motio.common.lib.LibItems;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = Motio.MODID)
public class ModItems
{
    public static final ArrayList<Item> modItems = new ArrayList<>();
    
    public static Item MOTIUM;
    
    public static Item MOTIO_BAND;
    public static Item CREATIVE_BAND;
    public static Item BIG_BAND;
    
    public static Item SPINNER;
    public static Item PEBBLE;
    
    public static Item STEP_ASSIST;
    public static Item BLINK;
    public static Item LOONY_TUNES;
    public static Item INFINITE_PEARL;
    public static Item WALL_SLIDE;
    public static Item REVERSE_GRAVITY;
    public static Item THROWING_GLOVE;
    
    public static Item MOTIUM_SWORD;
    
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        MOTIUM = new ItemBase(LibItems.MOTIUM_INGOT);
        MOTIO_BAND = new ItemMotioStorage(LibItems.BASIC_BAND, 100);
        BIG_BAND = new ItemMotioStorage(LibItems.BIG_BAND, Integer.MAX_VALUE / 2);
        CREATIVE_BAND = new ItemCreativeRing();
        BLINK = new ItemBlinkStaff();
        LOONY_TUNES = new ItemLooneyTunesGravity();
        SPINNER = new ItemSpinner();
        PEBBLE = new ItemMotiumPebble();
        INFINITE_PEARL = new ItemInfinitePearl();
        WALL_SLIDE = new ItemWallSlide();
        REVERSE_GRAVITY = new ItemMotiumMoon();
        MOTIUM_SWORD = new ItemMotiumSword();
        THROWING_GLOVE = new ItemThrowingGlove();
        STEP_ASSIST = new ItemStepAssist();
        
        for (Item item : modItems)
        {
            event.getRegistry().register(item);
        }
    }
}
