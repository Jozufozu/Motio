package com.jozufozu.motio.common.items;

import com.jozufozu.motio.Motio;
import com.jozufozu.motio.client.IModelRegister;
import com.jozufozu.motio.common.ModItems;
import com.jozufozu.motio.common.ModMaterials;
import com.jozufozu.motio.common.MotioTabs;
import com.jozufozu.motio.common.lib.LibItems;
import javafx.scene.paint.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.model.ModelLoader;

public class ItemMotiumSword extends ItemSword implements IModelRegister
{
    public ItemMotiumSword()
    {
        super(ModMaterials.MOTIUM);
        this.setRegistryName(LibItems.MOTIUM_SWORD);
        this.setUnlocalizedName(Motio.MODID + "." + LibItems.MOTIUM_SWORD.getResourcePath());
        this.setCreativeTab(MotioTabs.ITEMS);
    
        ModItems.modItems.add(this);
    }
    
    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
        //Forge doesn't let me change modify attack damage!
    
        if (entity.canBeAttackedWithItem())
        {
            if (!entity.hitByEntity(player))
            {
                float attackDamage = (float)player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
                float enchantmentModifier;
            
                if (entity instanceof EntityLivingBase)
                {
                    enchantmentModifier = EnchantmentHelper.getModifierForCreature(player.getHeldItemMainhand(), ((EntityLivingBase)entity).getCreatureAttribute());
                }
                else
                {
                    enchantmentModifier = EnchantmentHelper.getModifierForCreature(player.getHeldItemMainhand(), EnumCreatureAttribute.UNDEFINED);
                }
            
                float f2 = player.getCooledAttackStrength(0.5F);
                attackDamage = attackDamage * (0.2F + f2 * f2 * 0.8F);
                enchantmentModifier = enchantmentModifier * f2;
                player.resetCooldown();
            
                if (attackDamage > 0.0F || enchantmentModifier > 0.0F)
                {
                    boolean flag = f2 > 0.9F;
                    boolean flag1 = false;
                    int i = 0;
                    i = i + EnchantmentHelper.getKnockbackModifier(player);
                
                    if (player.isSprinting() && flag)
                    {
                        player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, player.getSoundCategory(), 1.0F, 1.0F);
                        ++i;
                        flag1 = true;
                    }
                
                    boolean flag2 = flag && player.fallDistance > 0.0F && !player.onGround && !player.isOnLadder() && !player.isInWater() && !player.isPotionActive(MobEffects.BLINDNESS) && !player.isRiding() && entity instanceof EntityLivingBase;
                    flag2 = flag2 && !player.isSprinting();
                
                    net.minecraftforge.event.entity.player.CriticalHitEvent hitResult = net.minecraftforge.common.ForgeHooks.getCriticalHit(player, entity, flag2, flag2 ? 1.5F : 1.0F);
                    flag2 = hitResult != null;
                    if (flag2)
                    {
                        attackDamage *= hitResult.getDamageModifier();
                    }
                
                    attackDamage = attackDamage + enchantmentModifier;
                    
                    //The only stuff we add
                    Vec3d relativeMotion = new Vec3d(player.motionX - entity.motionX, player.motionY - entity.motionY, player.motionZ - entity.motionZ);
                    
                    attackDamage += 30 * relativeMotion.lengthVector() / Math.log(relativeMotion.lengthVector() * 20 + 2);
                    
                    boolean flag3 = false;
                    double d0 = (double)(player.distanceWalkedModified - player.prevDistanceWalkedModified);
                
                    if (flag && !flag2 && !flag1 && player.onGround && d0 < (double)player.getAIMoveSpeed())
                    {
                        ItemStack itemstack = player.getHeldItem(EnumHand.MAIN_HAND);
                    
                        if (itemstack.getItem() instanceof ItemSword)
                        {
                            flag3 = true;
                        }
                    }
                
                    float f4 = 0.0F;
                    boolean flag4 = false;
                    int j = EnchantmentHelper.getFireAspectModifier(player);
                
                    if (entity instanceof EntityLivingBase)
                    {
                        f4 = ((EntityLivingBase)entity).getHealth();
                    
                        if (j > 0 && !entity.isBurning())
                        {
                            flag4 = true;
                            entity.setFire(1);
                        }
                    }
                
                    double d1 = entity.motionX;
                    double d2 = entity.motionY;
                    double d3 = entity.motionZ;
                
                    if (entity.attackEntityFrom(DamageSource.causePlayerDamage(player), attackDamage))
                    {
                        player.sendStatusMessage(new TextComponentString("Attack: " + attackDamage), true);
                        if (i > 0)
                        {
                            if (entity instanceof EntityLivingBase)
                            {
                                ((EntityLivingBase)entity).knockBack(player, (float)i * 0.5F, (double)MathHelper.sin(player.rotationYaw * 0.017453292F), (double)(-MathHelper.cos(player.rotationYaw * 0.017453292F)));
                            }
                            else
                            {
                                entity.addVelocity((double)(-MathHelper.sin(player.rotationYaw * 0.017453292F) * (float)i * 0.5F), 0.1D, (double)(MathHelper.cos(player.rotationYaw * 0.017453292F) * (float)i * 0.5F));
                            }
                        
                            player.motionX *= 0.6D;
                            player.motionZ *= 0.6D;
                            player.setSprinting(false);
                        }
                    
                        if (flag3)
                        {
                            float f3 = 1.0F + EnchantmentHelper.getSweepingDamageRatio(player) * attackDamage;
                        
                            for (EntityLivingBase entitylivingbase : player.world.getEntitiesWithinAABB(EntityLivingBase.class, entity.getEntityBoundingBox().grow(1.0D, 0.25D, 1.0D)))
                            {
                                if (entitylivingbase != player && entitylivingbase != entity && !player.isOnSameTeam(entitylivingbase) && player.getDistanceSq(entitylivingbase) < 9.0D)
                                {
                                    entitylivingbase.knockBack(player, 0.4F, (double)MathHelper.sin(player.rotationYaw * 0.017453292F), (double)(-MathHelper.cos(player.rotationYaw * 0.017453292F)));
                                    entitylivingbase.attackEntityFrom(DamageSource.causePlayerDamage(player), f3);
                                }
                            }
                        
                            player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.getSoundCategory(), 1.0F, 1.0F);
                            player.spawnSweepParticles();
                        }
                    
                        if (entity instanceof EntityPlayerMP && entity.velocityChanged)
                        {
                            ((EntityPlayerMP)entity).connection.sendPacket(new SPacketEntityVelocity(entity));
                            entity.velocityChanged = false;
                            entity.motionX = d1;
                            entity.motionY = d2;
                            entity.motionZ = d3;
                        }
                    
                        if (flag2)
                        {
                            player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, player.getSoundCategory(), 1.0F, 1.0F);
                            player.onCriticalHit(entity);
                        }
                    
                        if (!flag2 && !flag3)
                        {
                            if (flag)
                            {
                                player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, player.getSoundCategory(), 1.0F, 1.0F);
                            }
                            else
                            {
                                player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_WEAK, player.getSoundCategory(), 1.0F, 1.0F);
                            }
                        }
                    
                        if (enchantmentModifier > 0.0F)
                        {
                            player.onEnchantmentCritical(entity);
                        }
                    
                        player.setLastAttackedEntity(entity);
                    
                        if (entity instanceof EntityLivingBase)
                        {
                            EnchantmentHelper.applyThornEnchantments((EntityLivingBase)entity, player);
                        }
                    
                        EnchantmentHelper.applyArthropodEnchantments(player, entity);
                        ItemStack itemstack1 = player.getHeldItemMainhand();
                    
                        if (entity instanceof MultiPartEntityPart)
                        {
                            IEntityMultiPart ientitymultipart = ((MultiPartEntityPart)entity).parent;
                        
                            if (ientitymultipart instanceof EntityLivingBase)
                            {
                                entity = (EntityLivingBase)ientitymultipart;
                            }
                        }
                    
                        if (!itemstack1.isEmpty() && entity instanceof EntityLivingBase)
                        {
                            ItemStack beforeHitCopy = itemstack1.copy();
                            itemstack1.hitEntity((EntityLivingBase)entity, player);
                        
                            if (itemstack1.isEmpty())
                            {
                                net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, beforeHitCopy, EnumHand.MAIN_HAND);
                                player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
                            }
                        }
                    
                        if (entity instanceof EntityLivingBase)
                        {
                            float f5 = f4 - ((EntityLivingBase)entity).getHealth();
                            player.addStat(StatList.DAMAGE_DEALT, Math.round(f5 * 10.0F));
                        
                            if (j > 0)
                            {
                                entity.setFire(j * 4);
                            }
                        
                            if (player.world instanceof WorldServer && f5 > 2.0F)
                            {
                                int k = (int)((double)f5 * 0.5D);
                                ((WorldServer)player.world).spawnParticle(EnumParticleTypes.DAMAGE_INDICATOR, entity.posX, entity.posY + (double)(entity.height * 0.5F), entity.posZ, k, 0.1D, 0.0D, 0.1D, 0.2D);
                            }
                        }
                    
                        player.addExhaustion(0.1F);
                    }
                    else
                    {
                        player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, player.getSoundCategory(), 1.0F, 1.0F);
                    
                        if (flag4)
                        {
                            entity.extinguish();
                        }
                    }
                }
            }
        }
    
        return false;
    }
    
    @Override
    public void registerModels()
    {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(LibItems.MOTIUM_SWORD, "inventory"));
    }
}
