package com.jozufozu.motio.common.items;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.jozufozu.motio.api.MotioAPI;
import com.jozufozu.motio.api.cap.IMotio;
import com.jozufozu.motio.api.cap.MotioBank;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.List;

public class ItemMotioStorage extends ItemBase implements IBauble
{
    private long capacity;
    private BaubleType type;
    
    public ItemMotioStorage(ResourceLocation name, long capacity)
    {
        this(name, BaubleType.RING, capacity);
    }
    
    public ItemMotioStorage(ResourceLocation name, BaubleType type, long capacity)
    {
        super(name);
        this.setMaxStackSize(1);
        this.type = type;
        this.capacity = capacity;
    }
    
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        IMotio motio = MotioAPI.getMotioStorage(stack);
        
        if (motio != null)
        {
            tooltip.add(I18n.format("motio.info.storage_tooltip", motio.available(), motio.capacity()));
        }
        
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
    
    @Override
    public boolean showDurabilityBar(ItemStack stack)
    {
        IMotio motio = MotioAPI.getMotioStorage(stack);
    
        if (motio != null)
        {
            return motio.available() != 0 && motio.available() != motio.capacity();
        }
        return super.showDurabilityBar(stack);
    }
    
    @Override
    public double getDurabilityForDisplay(ItemStack stack)
    {
        IMotio motio = MotioAPI.getMotioStorage(stack);
    
        if (motio != null)
        {
            return (double) (motio.unusedCapacity()) / (double) motio.capacity();
        }
        return super.getDurabilityForDisplay(stack);
    }
    
    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack)
    {
        float durability = (float) getDurabilityForDisplay(stack);
        return MathHelper.hsvToRGB(Math.max(0.0F, durability * 0.18f + 0.45f), 1.0F, (1.0f - durability) * 0.5f + 0.5f);
    }
    
    @Override
    public BaubleType getBaubleType(ItemStack itemstack)
    {
        return type;
    }
    
    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
    {
        MotioBank motioBank = new MotioBank(capacity);
        if (nbt != null)
        {
            if (nbt.hasKey("Parent", Constants.NBT.TAG_COMPOUND))
                nbt = nbt.getCompoundTag("Parent");
                
            motioBank.deserializeNBT(nbt);
        }
        return motioBank;
    }
}
