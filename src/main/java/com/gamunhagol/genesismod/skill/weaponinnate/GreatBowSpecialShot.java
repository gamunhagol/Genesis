package com.gamunhagol.genesismod.skill.weaponinnate;

import net.minecraft.network.FriendlyByteBuf;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

public class GreatBowSpecialShot extends WeaponInnateSkill {
    public GreatBowSpecialShot(SkillBuilder<? extends WeaponInnateSkill> builder) {
        super(builder);
    }

    @Override
    public void executeOnServer(SkillContainer container, FriendlyByteBuf args) {
        // 1. 부모 클래스의 로직 실행 (스테미나 소모, 기본 애니메이션 처리 등)
        super.executeOnServer(container, args);

        // 2. 컨테이너에서 실행 주체(플레이어 패치)를 꺼냅니다.
        ServerPlayerPatch executor = container.getServerExecutor();

        // 3. 여기서부터 대궁 전용 특수 로직을 작성합니다.
        // 예: 특정 애니메이션 재생
        // executor.playAnimationSynchronized(ModAnimations.GREAT_BOW_POWER_SHOT, 0.0F);

        // 참고: args를 통해 클라이언트에서 보낸 추가 데이터(차징 시간 등)를 읽을 수도 있습니다.
    }
}