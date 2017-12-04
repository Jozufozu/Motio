package com.jozufozu.motio.common.items;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.jozufozu.motio.api.IMotioItem;
import com.jozufozu.motio.common.lib.LibItems;
import com.jozufozu.motio.common.util.InventoryHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemStepAssist extends ItemBase implements IBauble, IMotioItem
{
    public ItemStepAssist()
    {
        super(LibItems.STEP_ASSIST);
        
        this.maxStackSize = 1;
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event)
    {
        if (event.getEntityLiving() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            player.stepHeight = 0.5f;
            if (InventoryHelper.playerHasItem(player, false, true, stack -> stack.getItem() == this)) {
                player.stepHeight = 1.0f;
            }
        }
    }
    
    @Override
    public BaubleType getBaubleType(ItemStack itemstack)
    {
        return BaubleType.BELT;
    }
}
