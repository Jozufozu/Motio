package com.jozufozu.motio.api;

import com.jozufozu.motio.api.cap.CapabilityMotio;
import com.jozufozu.motio.api.cap.IMotio;
import com.jozufozu.motio.api.cap.IMotioPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class MotioAPI
{
    public static IMotio getMotioStorage(EntityPlayer player)
    {
        IMotioPlayer handler = (IMotioPlayer) player.getCapability(CapabilityMotio.MOTIO_CAPABILITY, null);
        handler.setPlayer(player);
        return handler;
    }
    
    @Nullable
    public static IMotio getMotioStorage(ItemStack stack)
    {
        return stack.getCapability(CapabilityMotio.MOTIO_CAPABILITY, null);
    }

    @FunctionalInterface
    public static interface Consumer<R> {
        R run();
    }

    @Nullable
    public static <R> R runIfAvailable(IMotio motio, long toConsume, Consumer<R> function)
    {
        if (motio.available() >= toConsume)
        {
            R result = function.run();
            motio.tap(toConsume, false);
            return result;
        }

        return null;
    }

    public static <R> R runIfAvailable(IMotio motio, long toConsume, R fallback, Consumer<R> function)
    {
        if (motio.available() >= toConsume)
        {
            R result = function.run();
            motio.tap(toConsume, false);
            return result;
        }

        return fallback;
    }

    public static boolean runIfAvailable(IMotio motio, long toConsume, Runnable function)
    {
        if (motio.available() >= toConsume)
        {
            function.run();
            motio.tap(toConsume, false);
            return true;
        }

        return false;
    }
}
