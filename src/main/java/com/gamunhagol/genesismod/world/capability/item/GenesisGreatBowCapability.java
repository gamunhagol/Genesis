package com.gamunhagol.genesismod.world.capability.item;

import com.gamunhagol.genesismod.world.item.GreatBowItem;
import net.minecraft.world.InteractionHand;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.item.BowCapability;
import yesman.epicfight.world.capabilities.item.CapabilityItem;


public class GenesisGreatBowCapability extends BowCapability {
    public GenesisGreatBowCapability(CapabilityItem.Builder builder) {
        super(builder);
        this.zoomInType = CapabilityItem.ZoomInType.USE_TICK;
    }

    @Override
    public LivingMotion getLivingMotion(LivingEntityPatch<?> entitypatch, InteractionHand hand) {
        // getTicksUsingItem이 아니라 isUsingItem만으로 체크해서
        // 아주 미세한 딜레이라도 있으면 즉시 AIM 상태로 밀어넣어야 합니다.
        if (entitypatch.getOriginal().isUsingItem() &&
                entitypatch.getOriginal().getUseItem().getItem() instanceof GreatBowItem) {
            return LivingMotions.AIM;
        }
        return super.getLivingMotion(entitypatch, hand);
    }
}