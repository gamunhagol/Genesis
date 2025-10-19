package com.gamunhagol.genesismod.data.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Supplier;

/**
 * 완전히 타입 안전한 버전
 * - 여러 테이블별 확률 지정 가능
 * - 기존 전리품 유지
 * - Codec 타입 추론 문제 해결
 */
public class AddSusSandItemModifier extends LootModifier {
    public static final Supplier<Codec<AddSusSandItemModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.create((RecordCodecBuilder.Instance<AddSusSandItemModifier> inst) ->
                    codecStart(inst)
                            .and(Codec.unboundedMap(
                                    ResourceLocation.CODEC,
                                    RecordCodecBuilder.create((RecordCodecBuilder.Instance<Pair> entry) ->
                                            entry.group(
                                                    ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(Pair::getItem),
                                                    Codec.FLOAT.fieldOf("chance").forGetter(Pair::getChance)
                                            ).apply(entry, Pair::new)
                                    )
                            ).fieldOf("entries").forGetter(m -> m.entries))
                            .apply(inst, AddSusSandItemModifier::new)
            )
    );

    private final Map<ResourceLocation, Pair> entries;

    public AddSusSandItemModifier(LootItemCondition[] conditions, Map<ResourceLocation, Pair> entries) {
        super(conditions);
        this.entries = entries;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(@NotNull ObjectArrayList<ItemStack> generatedLoot,
                                                          @NotNull LootContext context) {
        ResourceLocation tableId = context.getQueriedLootTableId();
        if (tableId == null) return generatedLoot;

        Pair entry = entries.get(tableId);
        if (entry != null && context.getRandom().nextFloat() < entry.chance) {
            generatedLoot.add(new ItemStack(entry.item));
        }
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }

    /** Codec 타입 안전성을 위한 정적 내부 클래스 */
    public static class Pair {
        private final Item item;
        private final float chance;

        public Pair(Item item, float chance) {
            this.item = item;
            this.chance = chance;
        }

        public Item getItem() {
            return item;
        }

        public float getChance() {
            return chance;
        }
    }
}
