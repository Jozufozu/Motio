package com.jozufozu.motio.common.items;

import baubles.api.BaubleType;
import com.jozufozu.motio.api.MotioAPI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemMotioPassiveGen extends ItemActive
{
    private int generation;
    
    public ItemMotioPassiveGen(ResourceLocation name, BaubleType type, int gen)
    {
        super(name, type);
        this.setMaxStackSize(1);
        this.generation = gen;
    }
    
    @Override
    void update(ItemStack stack, EntityPlayer user)
    {
        MotioAPI.getMotioStorage(user).fill(generation, false);
    }
}
