package com.jozufozu.motio.api.cap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MotioBank implements IMotio, ICapabilitySerializable<NBTTagCompound>
{
    private long capacity;
    private long available;
    
    public MotioBank()
    {
        this(100);
    }
    
    public MotioBank(long capacity)
    {
        this.capacity = capacity;
    }
    
    @Override
    public long capacity()
    {
        return capacity;
    }
    
    @Override
    public long available()
    {
        return available;
    }
    
    @Override
    public long unusedCapacity()
    {
        return capacity - available;
    }
    
    @Override
    public long fill(long amount, boolean simulate)
    {
        long fill = Math.min(unusedCapacity(), amount);
        
        if (!simulate)
            available += fill;
        
        return fill;
    }
    
    @Override
    public long tap(long amount, boolean simulate)
    {
        if (amount > available)
            return 0;
        
        if (!simulate)
            available -= amount;
        
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
    
    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound out = new NBTTagCompound();
        
        out.setLong("capacity", this.capacity);
        out.setLong("available", this.available);
        
        return out;
    }
    
    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        this.capacity = nbt.getLong("capacity");
        this.available = nbt.getLong("available");
    }
}
