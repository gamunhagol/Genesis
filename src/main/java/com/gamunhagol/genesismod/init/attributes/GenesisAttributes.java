package com.gamunhagol.genesismod.init.attributes;

import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GenesisAttributes {

    // 1. 속성(Attribute)을 담을 레지스터 생성
    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(ForgeRegistries.ATTRIBUTES, GenesisMod.MODID);

    // 2. 마법 방어력(Magic Defense) 속성 실제 등록
    // "magic_defense": 내부 ID (genesis:magic_defense)
    // RangedAttribute: 범위가 있는 속성 (이름, 기본값, 최솟값, 최댓값)
    public static final RegistryObject<Attribute> MAGIC_DEFENSE = ATTRIBUTES.register("magic_defense",
            () -> new RangedAttribute("attribute.name.genesis.magic_defense", 0.0D, 0.0D, 1024.0D)
                    .setSyncable(true)); // true로 해야 클라이언트(GUI 등)에서도 수치가 보입니다.

    public static final RegistryObject<Attribute> HOLY_DEFENSE = ATTRIBUTES.register("holy_defense",
            () -> new RangedAttribute("attribute.name.genesis.holy_defense", 0.0D, 0.0D, 1000.0D).setSyncable(true));

    public static final RegistryObject<Attribute> HOLY_DAMAGE = ATTRIBUTES.register("holy_damage",
            () -> new RangedAttribute("attribute.name.genesis.holy_damage", 0.0D, 0.0D, 1024.0D).setSyncable(true));

    public static final RegistryObject<Attribute> DESTRUCTION_DAMAGE = ATTRIBUTES.register("destruction_damage",
            () -> new RangedAttribute("attribute.name.genesis.destruction_damage", 0.0D, 0.0D, 1024.0D).setSyncable(true));


    // 3. 메인 클래스에서 이 레지스터를 실행시키기 위한 메서드
    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }
}
