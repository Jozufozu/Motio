package com.jozufozu.motio.common.blocks;

import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public interface IItemBlockRegister
{
    void registerItemBlock(IForgeRegistry<Item> registry);
}
