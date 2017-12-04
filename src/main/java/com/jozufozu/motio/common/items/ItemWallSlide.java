package com.jozufozu.motio.common.items;

import baubles.api.BaubleType;
import com.jozufozu.motio.api.MotioAPI;
import com.jozufozu.motio.api.cap.IMotio;
import com.jozufozu.motio.common.lib.LibItems;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleBlockDust;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemWallSlide extends ItemBase
{
    public ItemWallSlide()
    {
        super(LibItems.WALL_SLIDE);
        this.setMaxStackSize(1);
    }
    
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (!isSelected || !(entityIn instanceof EntityPlayer))
            return;
    
        EntityPlayer player = (EntityPlayer) entityIn;
    
        IMotio motio = MotioAPI.getMotioStorage(player);
    
        if (player.isSneaking() && player.fallDistance > 0 && !player.capabilities.isFlying && !player.isOnLadder() && !player.isElytraFlying() && motio.available() > 3)
        {
            boolean canSlide = false;
            EnumFacing wallFace = EnumFacing.DOWN;
            BlockPos pos;
            IBlockState state = Blocks.STONE.getDefaultState();
            
            Vec3d playerPos = player.getPositionVector();
            for (EnumFacing facing : EnumFacing.HORIZONTALS)
            {
                wallFace = facing.getOpposite();
                pos = new BlockPos(playerPos.addVector((double) facing.getFrontOffsetX() * 0.5, 0.0, (double) facing.getFrontOffsetZ() * 0.5));
        
                state = worldIn.getBlockState(pos);
        
                if (state.isSideSolid(worldIn, pos, facing.getOpposite()))
                {
                    canSlide = true;
                    break;
                }
            }
            
            if (canSlide)
            {
                motio.tap(3, false);
                player.motionY = -0.1;
                player.fallDistance = 1;
                
                double xSpeed = worldIn.rand.nextDouble() * 0.08 - 0.04;
                double zSpeed = worldIn.rand.nextDouble() * 0.08 - 0.04;
                xSpeed += wallFace.getFrontOffsetX() * (worldIn.rand.nextDouble() * 0.1);
                zSpeed += wallFace.getFrontOffsetZ() * (worldIn.rand.nextDouble() * 0.1);
                double xCoord = player.posX + (player.width / 2) * wallFace.getOpposite().getFrontOffsetX();
                double zCoord = player.posZ + (player.width / 2) * wallFace.getOpposite().getFrontOffsetZ();

                xCoord += wallFace.rotateY().getFrontOffsetX() * (worldIn.rand.nextDouble() * player.width - player.width / 2);
                zCoord += wallFace.rotateY().getFrontOffsetZ() * (worldIn.rand.nextDouble() * player.width - player.width / 2);

                worldIn.spawnParticle(EnumParticleTypes.BLOCK_CRACK, xCoord, player.posY + 0.7, zCoord, xSpeed, worldIn.rand.nextDouble() * 0.1, zSpeed, Block.getStateId(state));
            }
        }
    }
    
    public static boolean canSlide(World worldIn, EntityPlayer player)
    {
        Vec3d playerPos = player.getPositionVector();
        for (EnumFacing facing : EnumFacing.HORIZONTALS)
        {
            BlockPos test = new BlockPos(playerPos.addVector((double) facing.getFrontOffsetX() * 0.5, 0.0, (double) facing.getFrontOffsetZ() * 0.5));
        
            IBlockState state = worldIn.getBlockState(test);
            
            if (state.isSideSolid(worldIn, test, facing.getOpposite()))
                return true;
        }
        
        return false;
    }
}
