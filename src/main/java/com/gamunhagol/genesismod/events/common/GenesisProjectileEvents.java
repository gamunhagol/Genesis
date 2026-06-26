package com.gamunhagol.genesismod.events.common;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.stats.WeaponRequirementHelper;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.item.weapon.GreatBowItem;
import com.gamunhagol.genesismod.world.weapon.WeaponDataManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Method;

@Mod.EventBusSubscriber(modid = GenesisMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GenesisProjectileEvents {

    private static final Method GET_PICKUP_ITEM = ObfuscationReflectionHelper.findMethod(ThrownTrident.class, "m_7941_");

    @SubscribeEvent
    public static void attachProjectileCaps(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Projectile) {
            event.addCapability(new ResourceLocation(GenesisMod.MODID, "projectile_stats"), new ProjectileStatsProvider());
        }
    }

    @SubscribeEvent
    public static void onProjectileJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide) return;

        if (event.getEntity() instanceof Projectile projectile && projectile.getOwner() instanceof Player player) {
            ItemStack weaponStack = ItemStack.EMPTY;
            float enchantBonus = 0.0f;

            if (projectile instanceof ThrownTrident trident) {
                try {
                    weaponStack = (ItemStack) GET_PICKUP_ITEM.invoke(trident);
                } catch (Exception e) {
                    if (player.getMainHandItem().getItem() instanceof TridentItem) weaponStack = player.getMainHandItem();
                    else if (player.getOffhandItem().getItem() instanceof TridentItem) weaponStack = player.getOffhandItem();
                }

                int impalingLevel = EnchantmentHelper.getItemEnchantmentLevel(net.minecraft.world.item.enchantment.Enchantments.IMPALING, weaponStack);
                if (impalingLevel > 0) {
                    enchantBonus = impalingLevel * 2.5f;
                }
            }
            else if (isBowOrCrossbow(player.getUseItem())) {
                weaponStack = player.getUseItem();
            }
            else if (isBowOrCrossbow(player.getMainHandItem())) {
                weaponStack = player.getMainHandItem();
            }
            else if (isBowOrCrossbow(player.getOffhandItem())) {
                weaponStack = player.getOffhandItem();
            }
            if (!weaponStack.isEmpty() && WeaponDataManager.hasData(weaponStack.getItem())) {

                if (weaponStack.getItem() instanceof BowItem) {
                    int powerLevel = EnchantmentHelper.getItemEnchantmentLevel(net.minecraft.world.item.enchantment.Enchantments.POWER_ARROWS, weaponStack);
                    if (powerLevel > 0) enchantBonus = 0.5f * powerLevel + 0.5f;
                }
                DamageSnapshot rawSnapshot = WeaponRequirementHelper.calculateTotalDamage(player, weaponStack, enchantBonus);

                double velocity = projectile.getDeltaMovement().length();
                float chargeMultiplier = (float) (velocity / 3.0);

                float maxMultiplier = (weaponStack.getItem() instanceof GreatBowItem) ? 2.5f : 1.2f;
                chargeMultiplier = Math.min(Math.max(chargeMultiplier, 0.1f), maxMultiplier);

                final float finalMul = chargeMultiplier;
                DamageSnapshot scaledSnapshot = new DamageSnapshot(
                        rawSnapshot.physical() * finalMul,
                        rawSnapshot.magic() * finalMul,
                        rawSnapshot.fire() * finalMul,
                        rawSnapshot.lightning() * finalMul,
                        rawSnapshot.frost() * finalMul,
                        rawSnapshot.holy() * finalMul,
                        rawSnapshot.destruction() * finalMul
                );

                projectile.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> {
                    cap.setSnapshot(scaledSnapshot);
                });
            }
        }
    }

    private static boolean isBowOrCrossbow(ItemStack stack) {
        return !stack.isEmpty() && (stack.getItem() instanceof BowItem || stack.getItem() instanceof CrossbowItem);
    }
}