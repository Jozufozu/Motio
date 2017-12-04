package com.jozufozu.motio.client.handlers;

import baubles.api.BaublesApi;
import com.jozufozu.motio.Motio;
import com.jozufozu.motio.api.IMotioItem;
import com.jozufozu.motio.api.MotioAPI;
import com.jozufozu.motio.api.cap.IMotio;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Motio.MODID)
public class GUIHandler
{
    public static final ResourceLocation HUD = new ResourceLocation(Motio.MODID, "textures/gui/motio_hud.png");
    private static long available;
    
    private static int lastTicksWithoutMotioItems;
    private static int ticksWithoutMotioItems;
    private static int animationTime = 15;
    
    private static long rotationsLast;
    private static long rotations;
    private static int angleLast;
    private static int angle;
    
    private static int frame;
    
    @SubscribeEvent
    public static void update(TickEvent.ClientTickEvent event)
    {
        EntityPlayer player = Minecraft.getMinecraft().player;
        
        if (player == null || event.phase == TickEvent.Phase.START)
            return;
        
        frame++;
        
        IMotio motio = MotioAPI.getMotioStorage(player);

        available = motio.available();
        
        rotationsLast = rotations;
        angleLast = angle;
        
        angle += Math.sqrt(2.0 * available);
        rotations += Math.floorDiv(angle, 360);
        angle = angle % 360;
        
        lastTicksWithoutMotioItems = ticksWithoutMotioItems;
        
        if (hasItems(player))
            ticksWithoutMotioItems = 0;
        else if (ticksWithoutMotioItems < animationTime)
            ticksWithoutMotioItems++;
    }
    
    @SubscribeEvent
    public static void render(RenderGameOverlayEvent.Post event)
    {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL)
            return;
        
        drawMotioMeter(event.getResolution(), event.getPartialTicks());
    }
    
    public static boolean hasItems(EntityPlayer player)
    {
        CombinedInvWrapper inventories = new CombinedInvWrapper(new PlayerInvWrapper(player.inventory), BaublesApi.getBaublesHandler(player));
    
        for (int i = 0; i < inventories.getSlots(); i++)
        {
            ItemStack stack = inventories.getStackInSlot(i);
            
            if (stack.isEmpty() || !(stack.getItem() instanceof IMotioItem))
                continue;
            
            if (((IMotioItem) stack.getItem()).usesMotio(player, stack))
                return true;
        }
        
        return false;
    }
    
    public static void drawMotioMeter(ScaledResolution res, float partialTicks)
    {
        Minecraft mc = Minecraft.getMinecraft();
    
        FontRenderer fontRenderer = mc.fontRenderer;
    
        mc.renderEngine.bindTexture(HUD);
    
        String avail = String.valueOf(available);
        
        int width = fontRenderer.getStringWidth(avail);
        
        int u = 32;
        int v = 0;
        
        int fillWidth = 2;
        int height = 11;
        int borderWidth = 5;
        int borderHeight = 4;
        
        double uSize = 1.0 / 128;
        double vSize = 1.0 / 128;
    
        int xPos = res.getScaledWidth() - 24 - width / 2;
        int yPos = 24;
    
        GlStateManager.pushMatrix();
        GlStateManager.translate(xPos, yPos,0);
        
        width += fillWidth;
        
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL11.GL_QUAD_STRIP, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(-width / 2 - borderWidth, -height / 2 - borderHeight,   0).tex(u * uSize,                                 v * vSize                               ).endVertex();
        bufferbuilder.pos(-width / 2 - borderWidth, height / 2 + borderHeight,    0).tex(u * uSize,                                 (v + height + 2 * borderHeight) * vSize ).endVertex();
        bufferbuilder.pos(-width / 2,               -height / 2 - borderHeight,   0).tex((u + borderWidth) * uSize,                 v * vSize                               ).endVertex();
        bufferbuilder.pos(-width / 2,               height / 2 + borderHeight,    0).tex((u + borderWidth) * uSize,                 (v + height + 2 * borderHeight) * vSize ).endVertex();
    
        int x = 0;
        
        for (int i = 1; i < MathHelper.ceil((float) width / (float) fillWidth); i++)
        {
            x = -width / 2 + i * fillWidth;
            
            bufferbuilder.pos(x, -height / 2 - borderHeight,    0).tex((u + borderWidth + fillWidth * (i % 2)) * uSize, v * vSize).endVertex();
            bufferbuilder.pos(x, height / 2 + borderHeight,     0).tex((u + borderWidth + fillWidth * (i % 2)) * uSize, (v + height + 2 * borderHeight) * vSize).endVertex();
        }
    
        bufferbuilder.pos(x,                -height / 2 - borderHeight,   0).tex((u + borderWidth + fillWidth) * uSize,     v * vSize                               ).endVertex();
        bufferbuilder.pos(x,                height / 2 + borderHeight,    0).tex((u + borderWidth + fillWidth) * uSize,     (v + height + 2 * borderHeight) * vSize ).endVertex();
        bufferbuilder.pos(x + borderWidth,  -height / 2 - borderHeight,   0).tex((u + borderWidth * 2 + fillWidth) * uSize, v * vSize                               ).endVertex();
        bufferbuilder.pos(x + borderWidth,  height / 2 + borderHeight,    0).tex((u + borderWidth * 2 + fillWidth) * uSize, (v + height + 2 * borderHeight) * vSize ).endVertex();
        tessellator.draw();
        
        fontRenderer.drawString(avail, -width / 2 + fillWidth /2, -fontRenderer.FONT_HEIGHT / 2, 0xFFFFFFFF);
        GlStateManager.popMatrix();
    }
    
    private static float lerp(float start, float end, float pct)
    {
        return (1.0f - pct) * start + pct * end;
    }
}
