package ru.endlessrise.common.world;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.Mod;
import ru.endlessrise.EndlessRiseMain;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = EndlessRiseMain.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ImmersiveEngineeringWorldTweaks {
    private static final Set<String> DISABLED_IE_ORES = Collections.unmodifiableSet(new LinkedHashSet<>(Arrays.asList(
            "copper",
            "bauxite",
            "lead",
            "silver",
            "nickel",
            "uranium"
    )));

    private static final Map<ResourceLocation, ResourceLocation> ENGINEER_HOUSES = createEngineerHouses();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBiomeLoad(BiomeLoadingEvent event) {
        List<Supplier<ConfiguredFeature<?, ?>>> undergroundOres = event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES);
        undergroundOres.removeIf(featureSupplier -> isDisabledImmersiveEngineeringOre(featureSupplier.get()));
    }

    public static void onLoadComplete(FMLLoadCompleteEvent event) {
        event.enqueueWork(ImmersiveEngineeringWorldTweaks::removeEngineerHousesFromVillages);
    }

    private static boolean isDisabledImmersiveEngineeringOre(ConfiguredFeature<?, ?> feature) {
        ResourceLocation key = WorldGenRegistries.CONFIGURED_FEATURE.getKey(feature);
        return key != null
                && "immersiveengineering".equals(key.getNamespace())
                && DISABLED_IE_ORES.contains(key.getPath());
    }

    private static void removeEngineerHousesFromVillages() {
        for (Map.Entry<ResourceLocation, ResourceLocation> entry : ENGINEER_HOUSES.entrySet()) {
            removeFromPool(entry.getKey(), entry.getValue());
        }
    }

    private static Map<ResourceLocation, ResourceLocation> createEngineerHouses() {
        Map<ResourceLocation, ResourceLocation> engineerHouses = new LinkedHashMap<>();
        engineerHouses.put(new ResourceLocation("village/plains/houses"), new ResourceLocation("immersiveengineering", "village/houses/plains_engineer"));
        engineerHouses.put(new ResourceLocation("village/snowy/houses"), new ResourceLocation("immersiveengineering", "village/houses/snowy_engineer"));
        engineerHouses.put(new ResourceLocation("village/savanna/houses"), new ResourceLocation("immersiveengineering", "village/houses/savanna_engineer"));
        engineerHouses.put(new ResourceLocation("village/desert/houses"), new ResourceLocation("immersiveengineering", "village/houses/desert_engineer"));
        engineerHouses.put(new ResourceLocation("village/taiga/houses"), new ResourceLocation("immersiveengineering", "village/houses/taiga_engineer"));
        return Collections.unmodifiableMap(engineerHouses);
    }

    private static void removeFromPool(ResourceLocation poolId, ResourceLocation pieceId) {
        JigsawPattern pool = WorldGenRegistries.TEMPLATE_POOL.get(poolId);
        if (pool == null) {
            return;
        }

        List<JigsawPiece> shuffledPieces = pool.getShuffledTemplates(new Random(0L));
        Object2IntLinkedOpenHashMap<JigsawPiece> retainedPieces = new Object2IntLinkedOpenHashMap<>();
        for (JigsawPiece piece : shuffledPieces) {
            if (piece.toString().contains(pieceId.toString())) {
                continue;
            }
            retainedPieces.computeInt(piece, (ignored, count) -> (count == null ? 0 : count) + 1);
        }

        List<Pair<JigsawPiece, Integer>> weightedPieces = retainedPieces.object2IntEntrySet().stream()
                .map(entry -> Pair.of(entry.getKey(), entry.getIntValue()))
                .collect(Collectors.toList());

        Registry.register(WorldGenRegistries.TEMPLATE_POOL, poolId, new JigsawPattern(poolId, pool.getName(), weightedPieces));
    }
}
