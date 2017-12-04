package com.jozufozu.motio.api.cap;

import baubles.api.BaublesApi;
import com.jozufozu.motio.api.MotioAPI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;

import java.util.ArrayList;

public class MotioBankPlayer implements IMotioPlayer
{
    private EntityPlayer player;
    private ArrayList<IMotio> heldBanks = new ArrayList<>();
    
    public MotioBankPlayer()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @Override
    public long capacity()
    {
        if (heldBanks.isEmpty())
            return 0;
    
        long outLast, out = 0;
    
        synchronized (this)
        {
            for (IMotio motio : heldBanks)
            {
                outLast = out;
                if (motio == null)
                    continue;

                out += motio.capacity();
                
                if (out < outLast) //Test for overflows
                    return Long.MAX_VALUE;
            }
        }
        
        return out;
    }
    
    @Override
    public long available()
    {
        if (heldBanks.isEmpty())
            return 0;
    
        long outLast, out = 0;
    
        synchronized (this)
        {
            for (IMotio motio : heldBanks)
            {
                outLast = out;
                if (motio == null) continue;
    
                out += motio.available();
                
                if (out < outLast) //Test for overflows
                    return Long.MAX_VALUE;
            }
        }
        
        return out;
    }
    
    @Override
    public long unusedCapacity()
    {
        long out = 0;
        
        synchronized (this)
        {
            for (IMotio motio : heldBanks)
            {
                if (motio == null) continue;
        
                out += motio.unusedCapacity();
            }
        }
        
        return out;
    }
    
    @Override
    public long fill(long amount, boolean simulate)
    {
        if (heldBanks.isEmpty())
            return 0;
        
        amount = Math.min(unusedCapacity(), amount);
        
        long toAdd = amount;
        
        synchronized (this)
        {
            while (toAdd != 0)
            {
                //We want to add to the least full banks first
                long mostSpace = -1;
                IMotio toFill = this.heldBanks.get(0);
        
                for (IMotio motio : this.heldBanks)
                {
                    if (motio == null) continue;
            
                    long availableSpace = motio.unusedCapacity();
                    if (availableSpace <= mostSpace) continue;
            
                    mostSpace = availableSpace;
                    toFill = motio;
                }
    
                long fill = toFill.fill(amount, simulate);
        
                //The bank with the most available space couldn't hold any more. We're done
                if (fill == 0) break;
        
                toAdd -= fill;
            }
        }
        
        return amount - toAdd;
    }
    
    @Override
    public long tap(long amount, boolean simulate)
    {
        if (heldBanks.isEmpty())
            return 0;
        
        if (amount > available())
            return 0;
    
        long toRemove = amount;
        
        synchronized (this)
        {
            while (toRemove > 0)
            {
                //We want to tap from the most full banks first
                long mostAvailable = -1;
                IMotio toTap = this.heldBanks.get(0);
        
                for (IMotio motio : this.heldBanks)
                {
                    if (motio == null) continue;
    
                    long available = motio.available();
                    if (available <= mostAvailable) continue;
            
                    mostAvailable = available;
                    toTap = motio;
                }
        
                //We want to only take as much as we can. If we try to take more than what's available it won't let us
                long remove = Math.min(mostAvailable, amount);
        
                //We still might have to remove some
                toRemove -= toTap.tap(remove, simulate);
            }
        }
    
        return amount;
    }
    
    @Override
    public void setPlayer(EntityPlayer player)
    {
        this.player = player;
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerUpdate(TickEvent.PlayerTickEvent event)
    {
        if (event.phase != TickEvent.Phase.START || this.player == null)
            return;
        
        //This function only handles updates for our player
        if (!event.player.getUniqueID().equals(this.player.getUniqueID()))
            return;
        
        synchronized (this)
        {
            updateHeldBanks();
        }
    }
    
    private void updateHeldBanks()
    {
        this.heldBanks.clear();
    
        CombinedInvWrapper inventories = new CombinedInvWrapper(new PlayerInvWrapper(player.inventory), BaublesApi.getBaublesHandler(player));
    
        for (int i = 0; i < inventories.getSlots(); i++)
        {
            IMotio bank = MotioAPI.getMotioStorage(inventories.getStackInSlot(i));
        
            if (bank == null)
                continue;
        
            if (!this.heldBanks.contains(bank))
            {
                this.heldBanks.add(bank);
            }
        }
    }
}
