package com.jozufozu.motio.common.blocks;

import com.jozufozu.motio.common.lib.LibBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.block.SoundType;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BlockMotium extends BlockBase
{
    public BlockMotium()
    {
        super(LibBlocks.MOTIUM_BLOCK);
        this.setHardness(5.0F);
        this.setResistance(10.0F);
        this.setSoundType(SoundType.METAL);
    }
    
    @Override
    public Vec3d modifyAcceleration(World worldIn, BlockPos pos, Entity entityIn, Vec3d motion)
    {
        return super.modifyAcceleration(worldIn, pos, entityIn, motion);
    }
    
    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
    {
        entityIn.motionX *= 1.4;
        entityIn.motionZ *= 1.4;
    }
}
