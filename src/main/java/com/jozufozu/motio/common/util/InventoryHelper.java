package com.jozufozu.motio.common.util;

import baubles.api.BaublesApi;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;

import java.util.function.Predicate;

public class InventoryHelper
{
    public static boolean playerHasItem(EntityPlayer player, boolean main, boolean baubles, Predicate<ItemStack> test)
    {
        IItemHandler inventory = getPlayerInventory(player, main, baubles);
    
        for (int i = 0; i < inventory.getSlots(); i++)
        {
            if (test.test(inventory.getStackInSlot(i)))
                return true;
        }
        
        return false;
    }
    
    public static IItemHandler getPlayerInventory(EntityPlayer player, boolean main, boolean baubles)
    {
        if (baubles && main)
            return new CombinedInvWrapper(new PlayerInvWrapper(player.inventory), BaublesApi.getBaublesHandler(player));
        if (main)
            return new PlayerInvWrapper(player.inventory);
        if (baubles)
            return BaublesApi.getBaublesHandler(player);
        return null;
    }
}
