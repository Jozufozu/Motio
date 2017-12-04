package com.jozufozu.motio.common.items;

import com.jozufozu.motio.Motio;
import com.jozufozu.motio.client.IModelRegister;
import com.jozufozu.motio.common.ModItems;
import com.jozufozu.motio.common.MotioTabs;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBase extends Item implements IModelRegister
{
    public ItemBase(ResourceLocation name)
    {
        this.setRegistryName(name);
        this.setUnlocalizedName(Motio.MODID + "." + name.getResourcePath());
        this.setCreativeTab(MotioTabs.ITEMS);
    
        ModItems.modItems.add(this);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels()
    {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
    }
}
