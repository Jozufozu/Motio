package com.jozufozu.motio.client.handlers;

import com.jozufozu.motio.Motio;
import com.jozufozu.motio.client.particles.ParticleTeleport;
import com.jozufozu.motio.common.ModItems;
import com.jozufozu.motio.common.TeleportResult;
import com.jozufozu.motio.common.items.ItemBlinkStaff;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Motio.MODID)
public class BlinkRenderHandler
{
    private static Random rand;
    
    @SubscribeEvent
    public static void spawnParticles(TickEvent.WorldTickEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        
        if (rand == null)
        {
            rand = new Random();
        }

        EntityPlayer player = mc.player;
        if (player == null || player.getHeldItemMainhand().getItem() != ModItems.BLINK)
            return;
    
        TeleportResult teleportPos = ItemBlinkStaff.getTeleportPos(player, 1);
        
        if (!teleportPos.canTeleport)
            return;
    
        double steps = 3;
        for (double i = 0; i < steps; i++)
        {
            mc.effectRenderer.addEffect(new ParticleTeleport(mc.world, teleportPos, Math.PI * 2 * i / steps + event.world.getTotalWorldTime() * 0.1));
        }
    }
    
    @SubscribeEvent
    public static void renderBlink(DrawBlockHighlightEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        
        EntityPlayer player = mc.player;
        if (player.getHeldItemMainhand().getItem() != ModItems.BLINK)
            return;
    
        float partialTicks = event.getPartialTicks();
    
        double xPlayer = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)partialTicks;
        double yPlayer = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)partialTicks;
        double zPlayer = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)partialTicks;
        TeleportResult teleportResult = ItemBlinkStaff.getTeleportPos(player, partialTicks);
        
        Vec3d look = teleportResult.lookVec.subtract(xPlayer, yPlayer, zPlayer);
        Vec3d target = teleportResult.teleportPos.subtract(xPlayer, yPlayer, zPlayer);
    
        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
    
        GlStateManager.translate(target.x, target.y, target.z);
        //GlStateManager.rotate(-player.rotationYaw, 0, 1, 0);
        
        float r = 0.9f;
        float g = 0.3f;
        float b = 0.1f;
        
        if (teleportResult.canTeleport)
        {
            r = 0.1f;
            g = 0.6f;
            b = 0.9f;
        }
        
        double radius = player.width * 0.25;
        Vec3d from = new Vec3d(-radius, 0, -radius);
        Vec3d to = new Vec3d(radius, Math.max(look.y - target.y, player.height), radius);
        RenderGlobal.renderFilledBox(new AxisAlignedBB(from, to), r, g, b, 0.3f);
        
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }
}
