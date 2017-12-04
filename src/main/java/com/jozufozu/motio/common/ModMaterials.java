package com.jozufozu.motio.common;

import com.jozufozu.motio.common.lib.Lib;
import com.jozufozu.motio.common.lib.LibItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModMaterials
{
    @GameRegistry.ItemStackHolder("motio:motium")
    public static ItemStack MOTIUM_REPAIR;
    public static Item.ToolMaterial MOTIUM = EnumHelper.addToolMaterial(Lib.MOTIUM_MATERIAL,4, 1024, 8.0F, 3.0F, 13).setRepairItem(MOTIUM_REPAIR);
}
