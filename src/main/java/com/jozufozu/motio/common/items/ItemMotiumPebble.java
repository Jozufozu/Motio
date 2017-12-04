package com.jozufozu.motio.common.items;

import baubles.api.BaubleType;
import com.jozufozu.motio.api.MotioAPI;
import com.jozufozu.motio.common.lib.LibItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class ItemMotiumPebble extends ItemActive
{
    public ItemMotiumPebble()
    {
        super(LibItems.MOTIUM_PEBBLE, BaubleType.TRINKET);
    }
    
    @Override
    void update(ItemStack stack, EntityPlayer user)
    {
        Vec3d velocity = new Vec3d(user.motionX, 0, user.motionZ);
        
        int toAdd = MathHelper.floor(8.0 * velocity.lengthVector());
    
        MotioAPI.getMotioStorage(user).fill(toAdd, false);
    }
}
