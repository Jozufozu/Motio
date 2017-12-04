package com.jozufozu.motio.client.particles;

import com.jozufozu.motio.common.TeleportResult;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleDragonBreath;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class ParticleTeleport extends Particle
{
    private TeleportResult teleport;
    
    public ParticleTeleport(World worldIn, double posXIn, double posYIn, double posZIn)
    {
        super(worldIn, posXIn, posYIn, posZIn);
    }
    
    public ParticleTeleport(World worldIn, TeleportResult teleport, double angle)
    {
        super(worldIn,
                teleport.teleportPos.x + Math.cos(angle) * 0.3,
                teleport.lookVec.y - teleport.teleportPos.y < Minecraft.getMinecraft().player.height ? teleport.teleportPos.y + Minecraft.getMinecraft().player.height : teleport.lookVec.y,
                teleport.teleportPos.z + Math.sin(angle) * 0.3
        );

        this.teleport = teleport;
        this.canCollide = false;
    
        this.particleScale = 0.5f;
        this.setParticleTextureIndex((int)(Math.random() * 8.0));
        
        this.particleRed = 0.4f + rand.nextFloat() * 0.1f - 0.05f;
        this.particleGreen = 0.1f + rand.nextFloat() * 0.1f - 0.05f;
        this.particleBlue = 0.3f + rand.nextFloat() * 0.1f - 0.05f;
        this.particleAlpha = 0.8f;
        
        if (rand.nextFloat() > 0.8)
        {
            this.particleRed *= 0.2;
            this.particleGreen *= 0.2;
            this.particleBlue *= 0.2;
        }
        
        this.particleMaxAge = 30;
    }
    
    @Override
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setExpired();
        }
        
        this.setBoundingBox(this.getBoundingBox().offset(this.motionX, this.motionY, this.motionZ));

        this.resetPositionToBB();
        
        this.motionY = -0.1 * (this.posY - teleport.teleportPos.y);
        this.motionX = 0.04 * (teleport.teleportPos.x - this.posX + Math.sin(this.particleAge) * (this.particleMaxAge - this.particleAge) / this.particleMaxAge);
        this.motionZ = 0.04 * (teleport.teleportPos.z - this.posZ + Math.cos(this.particleAge) * (this.particleMaxAge - this.particleAge) / this.particleMaxAge);
        
        this.particleScale *= 0.98;
        
        //this.motionX /= 20;
        //this.motionZ /= 20;
    }
    
    @Override
    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
    }
}
