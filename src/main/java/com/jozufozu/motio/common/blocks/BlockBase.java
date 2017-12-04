package com.jozufozu.motio.common.blocks;

import com.jozufozu.motio.Motio;
import com.jozufozu.motio.common.ModBlocks;
import com.jozufozu.motio.common.MotioTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

public class BlockBase extends Block implements IItemBlockRegister
{
    private final ResourceLocation name;
    public boolean registerItemBlock = true;
    
    public BlockBase(ResourceLocation name)
    {
        this(name, Material.GROUND);
    }
    
    public BlockBase(ResourceLocation name, Material materialIn)
    {
        this(name, materialIn, materialIn.getMaterialMapColor());
    }
    
    public BlockBase(ResourceLocation name, Material blockMaterialIn, MapColor blockMapColorIn)
    {
        super(blockMaterialIn, blockMapColorIn);
        this.name = name;
        this.setRegistryName(name);
        this.setUnlocalizedName(Motio.MODID + "." + name.getResourcePath());
        this.setCreativeTab(MotioTabs.ITEMS);
    
        ModBlocks.modBlocks.add(this);
    }
    
    public BlockBase setRegisterItemBlock(boolean registerItemBlock)
    {
        this.registerItemBlock = registerItemBlock;
        return this;
    }
    
    @Override
    public void registerItemBlock(IForgeRegistry<Item> registry)
    {
        if (registerItemBlock)
        {
            registry.register(new ItemBlock(this).setRegistryName(this.name));
        }
    }
}
