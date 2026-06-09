package com.gamunhagol.genesismod.data.loot;

import com.gamunhagol.genesismod.world.item.tool.DivineGrailItem;
import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class AddChestItemModifier extends LootModifier {
    public static final Supplier<Codec<AddChestItemModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.create(inst -> codecStart(inst)
                    .and(ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(m -> m.item))
                    .and(Codec.FLOAT.fieldOf("chance").forGetter(m -> m.chance))
                    .and(Codec.INT.optionalFieldOf("min_count", 1).forGetter(m -> m.minCount))
                    .and(Codec.INT.optionalFieldOf("max_count", 1).forGetter(m -> m.maxCount))
                    .apply(inst, AddChestItemModifier::new)));

    private final Item item;
    private final float chance;
    private final int minCount;
    private final int maxCount;

    public AddChestItemModifier(LootItemCondition[] conditionsIn, Item item, float chance, int minCount, int maxCount) {
        super(conditionsIn);
        this.item = item;
        this.chance = chance;
        this.minCount = minCount;
        this.maxCount = maxCount;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(@NotNull ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        float luck = context.getLuck();

        float finalChance = this.chance * (1.0f + (luck * 0.05f));

        if (context.getRandom().nextFloat() < finalChance) {
            int count = context.getRandom().nextInt(maxCount - minCount + 1) + minCount;
            ItemStack stack = new ItemStack(item, count);

            if (stack.getItem() instanceof DivineGrailItem grail) {
                grail.setUses(stack, 0);
            }

            generatedLoot.add(stack);
        }
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}