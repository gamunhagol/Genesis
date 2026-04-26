package com.gamunhagol.genesismod.datagen.tags;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.util.GenesisTags;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class GenesisEntityTagProvider extends EntityTypeTagsProvider {
    public GenesisEntityTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, GenesisMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(GenesisTags.EntityTypes.FACTION_MOBS)
                .add(GenesisEntities.COLLECTOR_GUARD.get());
    }
}
