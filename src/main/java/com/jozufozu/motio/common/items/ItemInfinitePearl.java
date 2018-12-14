package com.jozufozu.motio.common.items;

import com.jozufozu.motio.api.MotioAPI;
import com.jozufozu.motio.api.cap.IMotio;
import com.jozufozu.motio.common.lib.LibItems;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemInfinitePearl extends ItemBase
{
    public ItemInfinitePearl()
    {
        super(LibItems.INFINITE_PEARL);
        this.setMaxStackSize(1);
    }
    
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
    
        IMotio motio = MotioAPI.getMotioStorage(playerIn);

        return MotioAPI.runIfAvailable(motio, 400, new ActionResult<>(EnumActionResult.FAIL, itemstack), () -> {
            worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_ENDERPEARL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
            playerIn.getCooldownTracker().setCooldown(this, 20);

            if (!worldIn.isRemote)
            {
                EntityEnderPearl entityenderpearl = new EntityEnderPearl(worldIn, playerIn);
                entityenderpearl.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 0);
                worldIn.spawnEntity(entityenderpearl);
            }

            playerIn.addStat(StatList.getObjectUseStats(this));

            return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
        });
    }
}
