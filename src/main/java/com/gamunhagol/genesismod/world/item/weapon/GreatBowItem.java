package com.gamunhagol.genesismod.world.item.weapon;

import com.gamunhagol.genesismod.world.entity.projectile.LargeArrowEntity;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;

import java.util.function.Predicate;

public class GreatBowItem extends BowItem {
    public static final int MAX_CHARGE_TIME = 38;

    private final GreatBowTier tier;

    public GreatBowItem(Properties pProperties, GreatBowTier tier) {
        super(pProperties.durability(tier.getDurability()));
        this.tier = tier;
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        boolean hasAmmo = !pPlayer.getProjectile(itemstack).isEmpty();

        if (pPlayer.getAbilities().instabuild || hasAmmo) {
            pPlayer.startUsingItem(pHand);
            return InteractionResultHolder.consume(itemstack);
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
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

                if (i >= 100) {
                    if (abstractarrow instanceof LargeArrowEntity largeArrow) {
                        largeArrow.setEmpowered(true);
                    }

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

                int flameLevel = net.minecraft.world.item.enchantment.EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, pStack);
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

    public static float getPowerForTime(int pCharge) {
        float f = (float) pCharge / (float) MAX_CHARGE_TIME;
        f = (f * f + f * 2.0F) / 3.0F;
        return Math.min(f, 1.0F);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int count) {
        if (!level.isClientSide) {
            int chargeTicks = this.getUseDuration(stack) - count;

            if (chargeTicks == 100) {
                boolean isPlayer = entity instanceof Player;
                SoundSource source = isPlayer ? SoundSource.PLAYERS : SoundSource.HOSTILE;

                if (isPlayer) {
                    Player player = (Player) entity;
                    EpicFightCapabilities.getPlayerPatchAsOptional(player).ifPresent(patch -> {
                        float staminaCost = 20.0F;
                        float currentStamina = patch.getStamina();

                        if (currentStamina >= staminaCost) {
                            patch.setStamina(currentStamina - staminaCost);
                            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                                    SoundEvents.ANVIL_PLACE, source, 1.0F, 0.5F);
                        } else {
                            player.stopUsingItem();
                            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                                    SoundEvents.FIRE_EXTINGUISH, source, 1.0F, 1.0F);
                        }
                    });
                } else {
                    level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                            SoundEvents.ANVIL_PLACE, source, 1.0F, 0.5F);
                }
            }
        }
    }

    public GreatBowTier getTier() {
        return this.tier;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == Enchantments.INFINITY_ARROWS) {
            return false;
        }
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return stack -> stack.getItem() instanceof LargeArrowItem;
    }

    private LargeArrowItem getCustomDefaultArrow() {
        return (LargeArrowItem) GenesisItems.LARGE_ARROW.get();
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }
}