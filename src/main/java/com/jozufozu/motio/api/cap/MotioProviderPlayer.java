package com.jozufozu.motio.api.cap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MotioProviderPlayer implements ICapabilityProvider
{
    private IMotioPlayer motio;
    
    public MotioProviderPlayer(EntityPlayer player)
    {
        this.motio = new MotioBankPlayer();
        this.motio.setPlayer(player);
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
        return capability == CapabilityMotio.MOTIO_CAPABILITY ? (T) motio : null;
    }
}
