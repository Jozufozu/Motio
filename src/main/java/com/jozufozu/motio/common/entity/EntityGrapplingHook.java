package com.jozufozu.motio.common.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.ArrayList;

public class EntityGrapplingHook extends EntityThrowable
{
    public static double segmentSpacing = 0.2;
    public ArrayList<AxisAlignedBB> rope;
    private double maxLength;
    
    public EntityGrapplingHook(World worldIn)
    {
        super(worldIn);
    }
    
    public EntityGrapplingHook(World world, double posX, double posY, double posZ, double maxLength)
    {
        super(world);
        this.setPosition(posX, posY, posZ);
        this.maxLength = maxLength;
    }
    
    public EntityGrapplingHook(World worldIn, EntityLivingBase throwerIn, double maxLength)
    {
        super(worldIn, throwerIn);
        this.maxLength = maxLength;
    }
    
    @Override
    protected void entityInit()
    {
    
    }
    
    @Override
    protected void onImpact(RayTraceResult result)
    {
    
    }
    
    @Override
    public void onEntityUpdate()
    {
        super.onEntityUpdate();
        
        if (rope == null)
        {
            int segments = MathHelper.ceil(this.maxLength / segmentSpacing);
    
            for (int i = 0; i < segments; i++)
            {
            
            }
        }
    }
    
    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy)
    {
    
    }
}
