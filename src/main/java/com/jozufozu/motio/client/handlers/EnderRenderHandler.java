package com.jozufozu.motio.client.handlers;

import com.jozufozu.motio.Motio;
import com.jozufozu.motio.client.particles.ParticleTeleport;
import com.jozufozu.motio.common.ModItems;
import com.jozufozu.motio.common.TeleportResult;
import com.jozufozu.motio.common.items.ItemBlinkStaff;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.Random;

@SideOnly(Side.CLIENT)
//@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Motio.MODID)
public class EnderRenderHandler
{
    //This was an experiment
    public static void renderBlink(DrawBlockHighlightEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        
        EntityPlayer player = mc.player;
        if (player.getHeldItemMainhand().getItem() != ModItems.INFINITE_PEARL)
            return;
    
        float partialTicks = event.getPartialTicks();
    
        double xPlayer = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)partialTicks;
        double yPlayer = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)partialTicks;
        double zPlayer = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)partialTicks;
        
        Vec3d look = player.getLook(partialTicks);

        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
    
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUAD_STRIP, DefaultVertexFormats.POSITION_COLOR);
    
        AxisAlignedBB bb = new AxisAlignedBB(-0.125, -0.125, -0.125, 0.125, 0.125, 0.125);
    
        Vec3d pos = Vec3d.ZERO.addVector(0, player.eyeHeight, 0);
        Vec3d posLast = pos;
        Vec3d velocity = look.scale(1.5);
        bb = bb.offset(pos);
        bb = bb.offset(xPlayer, yPlayer, zPlayer);
    
        int i = 0;
        while (player.world.getCollisionBoxes(player, bb).isEmpty() && i < 1000)
        {
            posLast = pos;

            Vec3d offset = new Vec3d(-velocity.z, 0, velocity.x).scale(0.05);
            buffer.pos(pos.x - offset.x, pos.y + 0.1, pos.z - offset.z).color(0.1f, 0.6f, 0.9f, 0.3f).endVertex();
            buffer.pos(pos.x + offset.x, pos.y + 0.1, pos.z + offset.z).color(0.1f, 0.6f, 0.9f, 0.3f).endVertex();
            
            velocity = velocity.scale(0.99);
            velocity = velocity.subtract(0, 0.03, 0);
            pos = pos.add(velocity);
            bb = bb.offset(velocity);
            
            i++;
        }
    
        RayTraceResult rayTraceResult = player.world.rayTraceBlocks(posLast.addVector(xPlayer, yPlayer, zPlayer), pos.addVector(xPlayer, yPlayer, zPlayer), true, true, true);
    
        tessellator.draw();
    
        if (rayTraceResult != null)
        {
            Vec3d hitVec = rayTraceResult.hitVec;
            
            RenderGlobal.renderFilledBox(new AxisAlignedBB(-0.125, -0.125, -0.125, 0.125, 0.125, 0.125).offset(hitVec).offset(-xPlayer, -yPlayer, -zPlayer), 0.1f, 0.6f, 0.9f, 0.3f);
        }

        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }
}
