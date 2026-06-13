package com.gamunhagol.genesismod.world.item;

import com.gamunhagol.genesismod.init.attributes.GenesisAttributes;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;

import java.util.UUID;

public class GenesisArmorItem extends ArmorItem {
    protected static final UUID[] MAGIC_DEFENSE_UUIDS = new UUID[]{
            UUID.fromString("DE1E515-6000-0000-0000-00000004000"), // FEET
            UUID.fromString("DE1E515-6000-0000-0000-00000003000"), // LEGS
            UUID.fromString("DE1E515-6000-0000-0000-00000002000"), // CHEST
            UUID.fromString("DE1E515-6000-0000-0000-00000001000")  // HEAD
    };

    protected static final UUID[] HOLY_DEFENSE_UUIDS = new UUID[]{
            UUID.fromString("FE1E515-7000-0000-0000-00000004000"),
            UUID.fromString("FE1E515-7000-0000-0000-00000003000"),
            UUID.fromString("FE1E515-7000-0000-0000-00000002000"),
            UUID.fromString("FE1E515-7000-0000-0000-00000001000")
    };

    protected static final UUID[] FIRE_DEFENSE_UUIDS = new UUID[]{
            UUID.fromString("AE1E515-8000-0000-0000-00000004000"),
            UUID.fromString("AE1E515-8000-0000-0000-00000003000"),
            UUID.fromString("AE1E515-8000-0000-0000-00000002000"),
            UUID.fromString("AE1E515-8000-0000-0000-00000001000")
    };

    protected static final UUID[] FROST_DEFENSE_UUIDS = new UUID[]{
            UUID.fromString("BE1E515-9000-0000-0000-00000004000"),
            UUID.fromString("BE1E515-9000-0000-0000-00000003000"),
            UUID.fromString("BE1E515-9000-0000-0000-00000002000"),
            UUID.fromString("BE1E515-9000-0000-0000-00000001000")
    };

    protected static final UUID[] LIGHTNING_DEFENSE_UUIDS = new UUID[]{
            UUID.fromString("CE1E515-0100-0000-0000-00000004000"),
            UUID.fromString("CE1E515-0100-0000-0000-00000003000"),
            UUID.fromString("CE1E515-0100-0000-0000-00000002000"),
            UUID.fromString("CE1E515-0100-0000-0000-00000001000")
    };

    public GenesisArmorItem(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(super.getDefaultAttributeModifiers(slot));

        if (slot == this.type.getSlot()) {
            if (this.getMaterial() instanceof GenesisArmorMaterials genesisMaterial) {
                float magicDef = genesisMaterial.getMagicDefense();
                if (magicDef != 0) {
                    builder.put(GenesisAttributes.MAGIC_DEFENSE.get(),
                            new AttributeModifier(MAGIC_DEFENSE_UUIDS[slot.getIndex()], "Magic defense", magicDef, AttributeModifier.Operation.ADDITION));
                }

                float holyDef = genesisMaterial.getHolyDefense();
                if (holyDef != 0) {
                    builder.put(GenesisAttributes.HOLY_DEFENSE.get(),
                            new AttributeModifier(HOLY_DEFENSE_UUIDS[slot.getIndex()], "Holy defense", holyDef, AttributeModifier.Operation.ADDITION));
                }

                float fireDef = genesisMaterial.getFireDefense();
                if (fireDef != 0) {
                    builder.put(GenesisAttributes.FIRE_DEFENSE.get(),
                            new AttributeModifier(FIRE_DEFENSE_UUIDS[slot.getIndex()], "Fire defense", fireDef, AttributeModifier.Operation.ADDITION));
                }

                float frostDef = genesisMaterial.getFrostDefense();
                if (frostDef != 0) {
                    builder.put(GenesisAttributes.FROST_DEFENSE.get(),
                            new AttributeModifier(FROST_DEFENSE_UUIDS[slot.getIndex()], "Frost defense", frostDef, AttributeModifier.Operation.ADDITION));
                }

                float lightningDef = genesisMaterial.getLightningDefense();
                if (lightningDef != 0) {
                    builder.put(GenesisAttributes.LIGHTNING_DEFENSE.get(),
                            new AttributeModifier(LIGHTNING_DEFENSE_UUIDS[slot.getIndex()], "Lightning defense", lightningDef, AttributeModifier.Operation.ADDITION));
                }
            }
        }
        return builder.build();
    }
}
