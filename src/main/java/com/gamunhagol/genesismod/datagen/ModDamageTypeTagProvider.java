package com.gamunhagol.genesismod.datagen;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.damagesource.GenesisDamageTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModDamageTypeTagProvider extends DamageTypeTagsProvider {
    public ModDamageTypeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, GenesisMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // 신성 대미지가 방어력을 무시하도록 설정
        this.tag(DamageTypeTags.BYPASSES_ARMOR).add(GenesisDamageTypes.HOLY);

        this.tag(DamageTypeTags.BYPASSES_ARMOR).add(GenesisDamageTypes.DESTRUCTION);
        this.tag(DamageTypeTags.BYPASSES_RESISTANCE).add(GenesisDamageTypes.DESTRUCTION);
        this.tag(DamageTypeTags.BYPASSES_ENCHANTMENTS).add(GenesisDamageTypes.DESTRUCTION);
    }
}
