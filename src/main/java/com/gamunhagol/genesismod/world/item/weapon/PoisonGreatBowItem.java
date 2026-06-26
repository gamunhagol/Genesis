package com.gamunhagol.genesismod.world.item.weapon;

import com.gamunhagol.genesismod.world.entity.projectile.LargeArrowEntity;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.particle.EpicFightParticles;

public class PoisonGreatBowItem extends GreatBowItem {

    public PoisonGreatBowItem(Properties pProperties, GreatBowTier tier) {
        super(pProperties, tier);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (!pTarget.level().isClientSide) {
            if (pTarget.level().random.nextFloat() < 0.3F) {
                pTarget.addEffect(new MobEffectInstance(MobEffects.POISON, 60, 2));
            }
        }
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
        boolean isPlayer = pEntityLiving instanceof Player;
        boolean hasInfiniteAmmo = false;
        ItemStack projectileStack = ItemStack.EMPTY;

        if (isPlayer) {
            Player player = (Player) pEntityLiving;
            hasInfiniteAmmo = player.getAbilities().instabuild;
            projectileStack = player.getProjectile(pStack);

            if (hasInfiniteAmmo && (projectileStack.isEmpty() || !(projectileStack.getItem() instanceof LargeArrowItem))) {
                projectileStack = new ItemStack(getCustomDefaultArrow());
            }
        } else {
            hasInfiniteAmmo = true;
            projectileStack = new ItemStack(getCustomDefaultArrow());
        }

        if (projectileStack.isEmpty() || !(projectileStack.getItem() instanceof LargeArrowItem)) {
            return;
        }

        int i = this.getUseDuration(pStack) - pTimeLeft;
        float f = getPowerForTime(i);

        if (!((double) f < 0.1D)) {
            if (!pLevel.isClientSide) {
                LargeArrowItem arrowitem = (LargeArrowItem) projectileStack.getItem();
                AbstractArrow abstractarrow = arrowitem.createArrow(pLevel, projectileStack, pEntityLiving);

                if (abstractarrow instanceof LargeArrowEntity largeArrow) {
                    largeArrow.setPoisonous(true);

                    if (i >= 100) {
                        largeArrow.setEmpowered(true);
                    }
                }

                if (i >= 100) {
                    ServerLevel serverLevel = (ServerLevel) pLevel;
                    Vec3 look = pEntityLiving.getLookAngle();
                    Vec3 horizontalForward = new Vec3(look.x, 0, look.z).normalize();

                    double px = pEntityLiving.getX() + horizontalForward.x * 0.8;
                    double py = pEntityLiving.getY() + 1.2;
                    double pz = pEntityLiving.getZ() + horizontalForward.z * 0.8;

                    serverLevel.sendParticles((ParticleOptions) EpicFightParticles.AIR_BURST.get(),
                            px, py, pz,
                            0,
                            -90.0F, pEntityLiving.getYRot(), -1.0F, 1.0);

                    SoundSource source = isPlayer ? SoundSource.PLAYERS : SoundSource.HOSTILE;
                    serverLevel.playSound(null, pEntityLiving.getX(), pEntityLiving.getY(), pEntityLiving.getZ(),
                            (SoundEvent) EpicFightSounds.NEUTRALIZE_MOBS.get(), source, 1.0F, 1.0F);
                }

                abstractarrow.shootFromRotation(pEntityLiving, pEntityLiving.getXRot(), pEntityLiving.getYRot(), 0.0F, f * 5.6F, 1.0F);

                int flameLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, pStack);
                if (flameLevel > 0) {
                    abstractarrow.setSecondsOnFire(100);
                }

                if (f == 1.0F) abstractarrow.setCritArrow(true);

                if (hasInfiniteAmmo) {
                    abstractarrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                }

                pStack.hurtAndBreak(1, pEntityLiving, (entity) -> entity.broadcastBreakEvent(pEntityLiving.getUsedItemHand()));

                SoundSource source = isPlayer ? SoundSource.PLAYERS : SoundSource.HOSTILE;
                pLevel.playSound(null, pEntityLiving.getX(), pEntityLiving.getY(), pEntityLiving.getZ(),
                        SoundEvents.ARROW_SHOOT, source, 1.0F, 1.0F / (pLevel.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                if (isPlayer) {
                    Player player = (Player) pEntityLiving;
                    if (!hasInfiniteAmmo) {
                        projectileStack.shrink(1);
                        if (projectileStack.isEmpty()) {
                            player.getInventory().removeItem(projectileStack);
                        }
                    }
                    player.awardStat(Stats.ITEM_USED.get(this));
                }

                pLevel.addFreshEntity(abstractarrow);
            }
        }
    }

    private LargeArrowItem getCustomDefaultArrow() {
        return (LargeArrowItem) GenesisItems.LARGE_ARROW.get();
    }
}