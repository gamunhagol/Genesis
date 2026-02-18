package com.gamunhagol.genesismod.world.weapon;

import com.gamunhagol.genesismod.api.StatType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import java.util.HashMap;
import java.util.Map;

public class WeaponDataManager extends SimpleJsonResourceReloadListener {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Map<Item, WeaponStatData> DATA_MAP = new HashMap<>();

    public WeaponDataManager() {
        super(GSON, "weapon_stats");
    }

    private float getFloat(JsonObject json, String key) {
        return json.has(key) ? json.get(key).getAsFloat() : 0.0f;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objects, ResourceManager resourceManager, ProfilerFiller profiler) {
        DATA_MAP.clear();

        objects.forEach((location, json) -> {
            try {
                JsonObject jsonObject = json.getAsJsonObject();
                String itemIdStr = jsonObject.get("item").getAsString();
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemIdStr));

                if (item == null || item == net.minecraft.world.item.Items.AIR) {
                    LOGGER.error("Genesis Mod: Item not found or is air: {}", itemIdStr);
                    return;
                }

                // 1. 기본 스탯 파싱 (모든 속성 추가)
                JsonObject baseStats = jsonObject.has("base_stats") ? jsonObject.getAsJsonObject("base_stats") : new JsonObject();
                float physical = getFloat(baseStats, "physical");
                float magic = getFloat(baseStats, "magic");
                float fire = getFloat(baseStats, "fire");
                float lightning = getFloat(baseStats, "lightning");
                float frost = getFloat(baseStats, "frost");
                float holy = getFloat(baseStats, "holy");
                float destruction = getFloat(baseStats, "destruction");

                // 2. 요구치 파싱
                Map<StatType, Integer> requirements = new HashMap<>();
                if (jsonObject.has("requirements")) {
                    JsonObject reqObj = jsonObject.getAsJsonObject("requirements");
                    for (String key : reqObj.keySet()) {
                        StatType type = StatType.byName(key);
                        if (type != null) requirements.put(type, reqObj.get(key).getAsInt());
                    }
                }

                // 3. 보정치 파싱
                Map<StatType, Float> scaling = new HashMap<>();
                if (jsonObject.has("scaling")) {
                    JsonObject scaleObj = jsonObject.getAsJsonObject("scaling");
                    for (String key : scaleObj.keySet()) {
                        StatType type = StatType.byName(key);
                        if (type != null) {
                            String grade = scaleObj.get(key).getAsString().toUpperCase();
                            scaling.put(type, convertGradeToValue(grade));
                        }
                    }
                }

                boolean isSpecial = jsonObject.has("is_special") && jsonObject.get("is_special").getAsBoolean();
                float growth = jsonObject.has("damage_growth") ? jsonObject.get("damage_growth").getAsFloat() : 0.05f;

                // 4. 보정치 오버라이드 파싱
                Map<Integer, Map<StatType, Float>> overrides = new HashMap<>();
                if (jsonObject.has("scaling_overrides")) {
                    JsonObject overrideObj = jsonObject.getAsJsonObject("scaling_overrides");
                    for (String levelKey : overrideObj.keySet()) {
                        try {
                            int level = Integer.parseInt(levelKey);
                            Map<StatType, Float> levelScalings = new HashMap<>();
                            JsonObject statObj = overrideObj.getAsJsonObject(levelKey);
                            for (String statKey : statObj.keySet()) {
                                StatType type = StatType.byName(statKey);
                                if (type != null) {
                                    levelScalings.put(type, convertGradeToValue(statObj.get(statKey).getAsString().toUpperCase()));
                                }
                            }
                            overrides.put(level, levelScalings);
                        } catch (NumberFormatException ignored) {}
                    }
                }

                WeaponStatData data = new WeaponStatData(
                        physical, magic, fire, lightning, frost, holy, destruction,
                        requirements, scaling, isSpecial, growth, overrides
                );
                DATA_MAP.put(item, data);

            } catch (Exception e) {
                LOGGER.error("Genesis Mod: Failed to load weapon stat for {}", location, e);
            }
        });
        LOGGER.info("Genesis Mod: Loaded {} weapon stats.", DATA_MAP.size());
    }

    public static WeaponStatData get(Item item) {
        return DATA_MAP.getOrDefault(item, WeaponStatData.EMPTY);
    }

    public static boolean hasData(Item item) {
        return DATA_MAP.containsKey(item);
    }

    private float convertGradeToValue(String grade) {
        return switch (grade) {
            case "S" -> 1.50f; case "A" -> 1.25f; case "B" -> 1.00f;
            case "C" -> 0.75f; case "D" -> 0.50f; case "E" -> 0.25f;
            default -> 0.0f;
        };
    }
}