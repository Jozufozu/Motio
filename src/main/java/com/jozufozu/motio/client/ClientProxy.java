package com.jozufozu.motio.client;

import com.jozufozu.motio.Motio;
import com.jozufozu.motio.common.CommonProxy;
import com.jozufozu.motio.common.ModBlocks;
import com.jozufozu.motio.common.ModItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Motio.MODID)
public class ClientProxy extends CommonProxy
{
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void loadModels(ModelRegistryEvent event)
    {
        for (Item item : ModItems.modItems)
        {
            if (item instanceof IModelRegister)
            {
                ((IModelRegister) item).registerModels();
            }
        }
    
        for (Block block : ModBlocks.modBlocks)
        {
            if (block instanceof IModelRegister)
            {
                ((IModelRegister) block).registerModels();
            }
        }
    }
}
