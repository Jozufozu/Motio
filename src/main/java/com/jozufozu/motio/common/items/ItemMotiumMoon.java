package com.jozufozu.motio.common.items;

import baubles.api.BaubleType;
import com.jozufozu.motio.api.MotioAPI;
import com.jozufozu.motio.api.cap.IMotio;
import com.jozufozu.motio.common.lib.LibItems;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class ItemMotiumMoon extends ItemActive
{
    public ItemMotiumMoon()
    {
        super(LibItems.REVERSE_GRAVITY, BaubleType.AMULET);
        this.setMaxStackSize(1);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack stack = playerIn.getHeldItem(handIn);
        
        stack.setItemDamage((stack.getItemDamage() + 1) % 2); // flips damage between 1 and 0

        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }
    
    @Override
    public boolean hasEffect(ItemStack stack)
    {
        return stack.getItemDamage() == 1;
    }
    
    @Override
    void update(ItemStack stack, EntityPlayer user)
    {
        if (stack.getItemDamage() == 1 && !user.capabilities.isFlying)
        {
            IMotio motio = MotioAPI.getMotioStorage(user);
            
            if (motio.available() < 3)
            {
                stack.setItemDamage(0);
                return;
            }
            
            /*
            We want to make it like the player is on the moon,
            so we find the ratio between the moon's gravity and earth's gravity
            and multiply it by the number minecraft uses for gravity
            then we subtract that from minecraft's number to get what we have to add to make an equivalent force
             */
            user.motionY += 0.06672;
            user.fallDistance *= 0.1;
            
            motio.tap(3, false);
        }
    }
    
    @Override
    public void registerModels()
    {
        super.registerModels();
        ModelLoader.setCustomModelResourceLocation(this, 1, new ModelResourceLocation(this.getRegistryName(), "inventory"));
    }
}
