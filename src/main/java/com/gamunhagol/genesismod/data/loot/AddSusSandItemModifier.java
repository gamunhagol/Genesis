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

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class AddSusSandItemModifier extends LootModifier {
    // 한 위치에 여러 아이템이 들어갈 수 있도록 .listOf() 코덱을 사용합니다.
    public static final Codec<Pair> PAIR_CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(Pair::getItem),
                    Codec.FLOAT.fieldOf("chance").forGetter(Pair::getChance)
            ).apply(inst, Pair::new)
    );

    public static final Supplier<Codec<AddSusSandItemModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.create(inst -> codecStart(inst)
                    .and(Codec.unboundedMap(ResourceLocation.CODEC, PAIR_CODEC.listOf())
                            .fieldOf("entries").forGetter(m -> m.entries))
                    .apply(inst, AddSusSandItemModifier::new)
            )
    );

    private final Map<ResourceLocation, List<Pair>> entries;

    public AddSusSandItemModifier(LootItemCondition[] conditions, Map<ResourceLocation, List<Pair>> entries) {
        super(conditions);
        this.entries = entries;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(@NotNull ObjectArrayList<ItemStack> generatedLoot,
                                                          @NotNull LootContext context) {
        ResourceLocation tableId = context.getQueriedLootTableId();
        if (tableId == null) return generatedLoot;

        List<Pair> lootList = entries.get(tableId);
        if (lootList != null) {
            // 해당 장소에 등록된 모든 아이템 후보를 검사합니다.
            for (Pair entry : lootList) {
                if (context.getRandom().nextFloat() < entry.chance) {
                    ItemStack stack = new ItemStack(entry.item);

                    // 성배 아이템 특수 처리
                    if (stack.getItem() instanceof com.gamunhagol.genesismod.world.item.DivineGrailItem grail) {
                        grail.setUses(stack, 0);
                    }

                    generatedLoot.add(stack);
                }
            }
        }
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }

    public static class Pair {
        private final Item item;
        private final float chance;

        public Pair(Item item, float chance) {
            this.item = item;
            this.chance = chance;
        }

        public Item getItem() { return item; }
        public float getChance() { return chance; }
    }
}