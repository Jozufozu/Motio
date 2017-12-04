package com.jozufozu.motio.common.items;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.jozufozu.motio.api.cap.CapabilityMotio;
import com.jozufozu.motio.api.cap.IMotio;
import com.jozufozu.motio.common.lib.LibItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemCreativeRing extends ItemBase implements IBauble
{
    public ItemCreativeRing()
    {
        super(LibItems.CREATIVE_BAND);
        this.setMaxStackSize(1);
    }
    
    @Override
    public BaubleType getBaubleType(ItemStack itemstack)
    {
        return BaubleType.RING;
    }
    
    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
    {
        return new InfiniteBank();
    }
    
    public static class InfiniteBank implements IMotio, ICapabilityProvider
    {
        @Override
        public long capacity()
        {
            return 0;
        }
    
        @Override
        public long available()
        {
            return Long.MAX_VALUE;
        }
    
        @Override
        public long unusedCapacity()
        {
            return 0;
        }
    
        @Override
        public long fill(long amount, boolean simulate)
        {
            return 0;
        }
    
        @Override
        public long tap(long amount, boolean simulate)
        {
            return amount;
        }
    
        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
        {
            return capability == CapabilityMotio.MOTIO_CAPABILITY;
        }
    
        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
        {
            if (capability == CapabilityMotio.MOTIO_CAPABILITY)
            {
                return CapabilityMotio.MOTIO_CAPABILITY.cast(this);
            }
            return null;
        }
    }
}
