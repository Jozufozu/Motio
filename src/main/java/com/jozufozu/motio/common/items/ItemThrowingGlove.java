package com.jozufozu.motio.common.items;

import com.jozufozu.motio.api.MotioAPI;
import com.jozufozu.motio.api.cap.IMotio;
import com.jozufozu.motio.common.lib.LibItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemThrowingGlove extends ItemBase
{
    public ItemThrowingGlove()
    {
        super(LibItems.THROWING_GLOVE);
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        IMotio motio = MotioAPI.getMotioStorage(playerIn);
        
        if (motio.available() == 0)
            return new ActionResult<>(EnumActionResult.PASS, itemstack);
        
        playerIn.setActiveHand(handIn);
        return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
    }
    
    @SubscribeEvent
    public void onLivingEntityUseItem(LivingEntityUseItemEvent.Tick event)
    {
    }
    
    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
    {
        if (!(entityLiving instanceof EntityPlayer))
            return;
    
        EntityPlayer player = (EntityPlayer) entityLiving;
        IMotio motio = MotioAPI.getMotioStorage(player);
        
        int maxChargeTime = 30;
        int ticksUsed = Math.min(72000 - timeLeft, maxChargeTime);
        ticksUsed = Math.min(ticksUsed, MathHelper.floor(MathHelper.sqrt(2 * motio.available())));
        
        double charge = (double) ticksUsed / (double) maxChargeTime + 0.1;
        
        Vec3d scale = entityLiving.getLookVec().scale(charge * 2.8);
        
        player.getCooldownTracker().setCooldown(this, MathHelper.floor(20 * charge));
        
        motio.tap(MathHelper.ceil(ticksUsed * ticksUsed / 2), false);
        
        worldIn.playSound(null, entityLiving.posX, entityLiving.posY + entityLiving.getEyeHeight(), entityLiving.posZ, SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.PLAYERS, 1.0f, 1.4f + worldIn.rand.nextFloat() * 0.3f - 0.15f);
        entityLiving.addVelocity(scale.x, scale.y, scale.z);
    }
    
    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BOW;
    }
    
    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;
    }
}
