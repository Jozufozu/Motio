package com.jozufozu.motio.common.items;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.jozufozu.motio.common.ModBlocks;
import com.jozufozu.motio.common.lib.LibItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemLooneyTunesGravity extends ItemBase implements IBauble
{
    public ItemLooneyTunesGravity()
    {
        super(LibItems.LOONEY_TUNES);
    }
    
    @Override
    public BaubleType getBaubleType(ItemStack itemstack)
    {
        return BaubleType.CHARM;
    }
    
    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase player)
    {
        if (!player.onGround && player.isOnLadder() || player.isElytraFlying() || (player instanceof EntityPlayer && ((EntityPlayer) player).capabilities.isFlying))
            return;
        
        World world = player.world;
        
        BlockPos playerPos = new BlockPos(player);
        
        BlockPos.MutableBlockPos testPos = new BlockPos.MutableBlockPos(playerPos);

        int i = 0;
        while (!world.getBlockState(testPos).getMaterial().blocksMovement() && i < 3)
        {
            testPos.move(EnumFacing.DOWN);
            i++;
        }

        if (!shouldFall(player) && i == 3)
        {
            world.setBlockState(playerPos.down(), ModBlocks.LOONY_TUNES.getDefaultState());
        }
    }
    
    public static boolean shouldFall(EntityLivingBase player)
    {
        return player.getLookVec().y < -0.7 || player.fallDistance > 0;
    }
    
    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase player)
    {
        player.setNoGravity(false);
    }
}
