package com.jozufozu.motio.common.items;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.jozufozu.motio.api.IMotioItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Basic wrapper to run the same update code whether the item is equipped in baubles or in the inventory
 */
public abstract class ItemActive extends ItemBase implements IBauble, IMotioItem
{
    protected BaubleType baubleType;
    
    protected ItemActive(ResourceLocation name, BaubleType type)
    {
        super(name);
        this.setMaxStackSize(1);
        this.baubleType = type;
    }
    
    @Override
    public BaubleType getBaubleType(ItemStack itemstack)
    {
        return baubleType;
    }
    
    abstract void update(ItemStack stack, EntityPlayer user);
    
    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase player)
    {
        if (player instanceof EntityPlayer)
            update(itemstack, ((EntityPlayer) player));
    }
    
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (entityIn instanceof EntityPlayer)
            update(stack, ((EntityPlayer) entityIn));
    }
}
