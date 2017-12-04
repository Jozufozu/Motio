package com.jozufozu.motio.common;

import com.jozufozu.motio.Motio;
import com.jozufozu.motio.common.blocks.BlockLoonyTunes;
import com.jozufozu.motio.common.blocks.BlockMotium;
import com.jozufozu.motio.common.blocks.IItemBlockRegister;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = Motio.MODID)
public class ModBlocks
{
    public static final ArrayList<Block> modBlocks = new ArrayList<>();
    
    public static Block LOONY_TUNES;
    public static Block MOTIUM;
    
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        LOONY_TUNES = new BlockLoonyTunes();
        MOTIUM = new BlockMotium();
        
        for (Block block : modBlocks)
        {
            event.getRegistry().register(block);
        }
    }
    
    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> event)
    {
        for (Block block : modBlocks)
        {
            if (block instanceof IItemBlockRegister)
                ((IItemBlockRegister) block).registerItemBlock(event.getRegistry());
        }
    }
}
