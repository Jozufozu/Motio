package com.jozufozu.motio.common.items;

import com.jozufozu.motio.api.MotioAPI;
import com.jozufozu.motio.common.lib.LibItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemSpinner extends ItemBase
{
    public ItemSpinner()
    {
        super(LibItems.SPINNER);
        this.setMaxStackSize(1);
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BOW;
    }
    
    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 1;
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
    }
    
    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count)
    {
        if (player instanceof EntityPlayer)
        {
            MotioAPI.getMotioStorage(((EntityPlayer) player)).fill(MathHelper.ceil(MathHelper.sqrt(count)), false);
        }
    }
    
    @SubscribeEvent
    public void infiniteUse(LivingEntityUseItemEvent.Tick event)
    {
        if (event.getItem().getItem() == this)
        {
            event.setDuration(event.getDuration() + 2);
        }
    }
}
