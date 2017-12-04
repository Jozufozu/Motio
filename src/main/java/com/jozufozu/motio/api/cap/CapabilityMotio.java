package com.jozufozu.motio.api.cap;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class CapabilityMotio implements Capability.IStorage<IMotio>
{
    @CapabilityInject(IMotio.class)
    public static final Capability<IMotio> MOTIO_CAPABILITY = null;

    public CapabilityMotio()
    {
    }
    
    public NBTBase writeNBT(Capability<IMotio> capability, IMotio instance, EnumFacing side)
    {
        if (instance instanceof MotioBank)
        {
            return ((MotioBank) instance).serializeNBT();
        }
        return null;
    }
    
    public void readNBT(Capability<IMotio> capability, IMotio instance, EnumFacing side, NBTBase nbt)
    {
        if (instance instanceof MotioBank && nbt instanceof NBTTagCompound)
        {
            ((MotioBank) instance).deserializeNBT(((NBTTagCompound) nbt));
        }
    }
}
