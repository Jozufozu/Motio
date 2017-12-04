package com.jozufozu.motio.common.items;

import com.jozufozu.motio.api.MotioAPI;
import com.jozufozu.motio.api.cap.IMotio;
import com.jozufozu.motio.common.TeleportResult;
import com.jozufozu.motio.common.lib.LibItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.server.CommandTeleport;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

public class ItemBlinkStaff extends ItemBase
{
    public static final double reach = 24;
    
    public ItemBlinkStaff()
    {
        super(LibItems.ADVANCED_BLINK);
        this.setMaxStackSize(1);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer playerIn, EnumHand handIn)
    {
        TeleportResult teleportPos = getTeleportPos(playerIn, 1.0f);
    
        IMotio motio = MotioAPI.getMotioStorage(playerIn);
        
        int cost = MathHelper.ceil(playerIn.getPositionVector().subtract(teleportPos.teleportPos).lengthSquared());
        
        if (teleportPos.canTeleport && motio.available() >= cost)
        {
            motio.tap(cost, false);
            playerIn.setPosition(teleportPos.teleportPos.x, teleportPos.teleportPos.y, teleportPos.teleportPos.z);
            playerIn.fallDistance = 0;
            world.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 0.4f, 1.1f + world.rand.nextFloat() * 0.2f - 0.1f);
        }

        return super.onItemRightClick(world, playerIn, handIn);
    }
    
    public static TeleportResult getTeleportPos(EntityPlayer player, float partialTicks)
    {
        RayTraceResult result = rayTrace(player, partialTicks);
        World world = player.world;
        
        if (result == null) //It's happened before, I know
            return new TeleportResult(false, player.getPositionVector(), player.getPositionEyes(partialTicks));
    
        Vec3d out = result.hitVec;
        
        AxisAlignedBB playerBB = new AxisAlignedBB(-player.width * 0.5, 0, -player.width * 0.5, player.width * 0.5, player.height, player.width * 0.5);
    
        AxisAlignedBB aabb = playerBB.offset(result.hitVec.x, Math.floor(result.hitVec.y), result.hitVec.z);
        
        if (result.typeOfHit == RayTraceResult.Type.BLOCK)
        {
            BlockPos up = result.getBlockPos().up();
            
            int offsetX = result.sideHit.getFrontOffsetX();
            int offsetZ = result.sideHit.getFrontOffsetZ();
            
            boolean flag = true;
            
            if (result.sideHit.getAxis() != EnumFacing.Axis.Y && canTeleportToBlock(world, up))
            {
                double height = 0;
                
                do
                {
                    height += 0.0625;
                    aabb = aabb.offset(0, 0.0625, 0);
                }
                while (!world.getCollisionBoxes(null, aabb).isEmpty() && height < 2.0);
                
                flag = height == 2.0;
                
                if (!flag)
                    out = new Vec3d(out.x, Math.floor(out.y) + height, out.z);
            }
            
            if (result.sideHit == EnumFacing.DOWN)
            {
                out = out.addVector(0, -player.height, 0);
            }
            
            if (flag)
            {
                out = out.addVector(offsetX * player.width * 0.5, 0, offsetZ * player.width * 0.5);
            }
        }
    
        if (player.isSneaking())
            return new TeleportResult(world.getCollisionBoxes(null, playerBB.offset(out)).isEmpty(), out, result.hitVec);
    
        double y = world.getCollisionBoxes(null, playerBB.offset(out)).isEmpty() ? out.y : result.sideHit == EnumFacing.UP ? Math.ceil(out.y) + 1 : Math.floor(out.y);
        
        aabb = playerBB.offset(out.x, y, out.z);
    
        double shift = -0.0625;
        
        boolean colliding = world.getCollisionBoxes(null, aabb.offset(0, shift, 0)).isEmpty();
        boolean lastColliding = false;
        boolean moved = false;
        
        double startY = aabb.minY;
        //I have no idea why this works, but it gives me what I want
        while ((colliding ^ lastColliding || colliding) && startY - aabb.minY < reach)
        {
            aabb = aabb.offset(0, shift, 0);
            moved = true;
    
            lastColliding = colliding;
            colliding = world.getCollisionBoxes(null, aabb.offset(0, shift, 0)).isEmpty();
        }
    
        Vec3d vec3d = new Vec3d(out.x, aabb.minY + 1E-3 + (moved ? -shift : 0), out.z);
        return new TeleportResult(world.getCollisionBoxes(null, playerBB.offset(vec3d)).isEmpty(), vec3d, result.hitVec);
    }
    
    public static boolean canTeleportToBlock(World world, BlockPos pos)
    {
        IBlockState blockState = world.getBlockState(pos);
        return pos.getY() > 0 && pos.getY() < world.getActualHeight() && (blockState.getMaterial() != Material.PORTAL || !blockState.getMaterial().blocksMovement());
    }
    
    public static RayTraceResult rayTrace(EntityPlayer player, float partialTicks)
    {
        World world = player.world;
        
        Vec3d test = player.getPositionEyes(partialTicks);
        Vec3d target = test.add(player.getLook(partialTicks).scale(reach));
    
        int toX = MathHelper.floor(target.x);
        int toY = MathHelper.floor(target.y);
        int toZ = MathHelper.floor(target.z);
        int xTest = MathHelper.floor(test.x);
        int yTest = MathHelper.floor(test.y);
        int zTest = MathHelper.floor(test.z);
        BlockPos testBlock = new BlockPos(xTest, yTest, zTest);
        IBlockState iblockstate = world.getBlockState(testBlock);
        Block block = iblockstate.getBlock();
    
        if ((iblockstate.getCollisionBoundingBox(world, testBlock) != Block.NULL_AABB) && block.canCollideCheck(iblockstate, true))
        {
            RayTraceResult rayTraceResult = iblockstate.collisionRayTrace(world, testBlock, test, target);
            
            if (rayTraceResult != null) //It can be null, shut up Intellij
                return rayTraceResult;
        }
    
        RayTraceResult out = new RayTraceResult(RayTraceResult.Type.MISS, target, EnumFacing.UP, new BlockPos(target));
        int steps = 200;
    
        while (steps-- >= 0)
        {
            if (Double.isNaN(test.x) || Double.isNaN(test.y) || Double.isNaN(test.z))
            {
                return out;
            }
        
            if (xTest == toX && yTest == toY && zTest == toZ)
            {
                return out;
            }
        
            boolean flag2 = true;
            boolean flag = true;
            boolean flag1 = true;
            double someX = 999.0;
            double someY = 999.0;
            double someZ = 999.0;
        
            if (toX > xTest)
            {
                someX = (double)xTest + 1.0;
            }
            else if (toX < xTest)
            {
                someX = (double)xTest + 0.0;
            }
            else
            {
                flag2 = false;
            }
        
            if (toY > yTest)
            {
                someY = (double)yTest + 1.0;
            }
            else if (toY < yTest)
            {
                someY = (double)yTest + 0.0;
            }
            else
            {
                flag = false;
            }
        
            if (toZ > zTest)
            {
                someZ = (double)zTest + 1.0;
            }
            else if (toZ < zTest)
            {
                someZ = (double)zTest + 0.0;
            }
            else
            {
                flag1 = false;
            }
        
            double d3 = 999.0;
            double d4 = 999.0;
            double d5 = 999.0;
            double diffX = target.x - test.x;
            double diffY = target.y - test.y;
            double diffZ = target.z - test.z;
        
            if (flag2)
            {
                d3 = (someX - test.x) / diffX;
            }
        
            if (flag)
            {
                d4 = (someY - test.y) / diffY;
            }
        
            if (flag1)
            {
                d5 = (someZ - test.z) / diffZ;
            }
        
            if (d3 == -0.0)
            {
                d3 = -1.0E-4;
            }
        
            if (d4 == -0.0)
            {
                d4 = -1.0E-4;
            }
        
            if (d5 == -0.0)
            {
                d5 = -1.0E-4;
            }
        
            EnumFacing enumfacing;
        
            if (d3 < d4 && d3 < d5)
            {
                enumfacing = toX > xTest ? EnumFacing.WEST : EnumFacing.EAST;
                test = new Vec3d(someX, test.y + diffY * d3, test.z + diffZ * d3);
            }
            else if (d4 < d5)
            {
                enumfacing = toY > yTest ? EnumFacing.DOWN : EnumFacing.UP;
                test = new Vec3d(test.x + diffX * d4, someY, test.z + diffZ * d4);
            }
            else
            {
                enumfacing = toZ > zTest ? EnumFacing.NORTH : EnumFacing.SOUTH;
                test = new Vec3d(test.x + diffX * d5, test.y + diffY * d5, someZ);
            }
        
            xTest = MathHelper.floor(test.x) - (enumfacing == EnumFacing.EAST ? 1 : 0);
            yTest = MathHelper.floor(test.y) - (enumfacing == EnumFacing.UP ? 1 : 0);
            zTest = MathHelper.floor(test.z) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
            testBlock = new BlockPos(xTest, yTest, zTest);
            IBlockState iblockstate1 = world.getBlockState(testBlock);
            Block block1 = iblockstate1.getBlock();
        
            if (iblockstate1.getMaterial() == Material.PORTAL || iblockstate1.getCollisionBoundingBox(world, testBlock) != Block.NULL_AABB)
            {
                if (block1.canCollideCheck(iblockstate1, true))
                {
                    RayTraceResult rayTraceResult = iblockstate1.collisionRayTrace(world, testBlock, test, target);
                    
                    if (rayTraceResult != null) //It can be null, shut up Intellij
                        return rayTraceResult;
                }
                else
                {
                    out = new RayTraceResult(RayTraceResult.Type.MISS, test, enumfacing, testBlock);
                }
            }
        }
    
        return out;
    }
}
