package com.jozufozu.motio.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * This is used to determine when to show the bar
 * If the player has an item in their inventory or equipped in their baubles slots that extends this, the bar will be shown
 */
public interface IMotioItem
{
    default boolean usesMotio(EntityPlayer player, ItemStack stack)
    {
        return true;
    }
}
