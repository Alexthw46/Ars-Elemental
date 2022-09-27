package alexthw.ars_elemental.datagen;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.registry.ModAdvTriggers;
import alexthw.ars_elemental.registry.ModEntities;
import alexthw.ars_elemental.registry.ModItems;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.common.datagen.Advancements;
import com.hollingsworth.arsnouveau.common.datagen.advancement.ANAdvancementBuilder;
import com.hollingsworth.arsnouveau.common.datagen.advancement.ANAdvancements;
import com.hollingsworth.arsnouveau.setup.ItemsRegistry;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.advancements.critereon.SummonedEntityTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;

public class AEAdvancementsProvider extends Advancements {
    public AEAdvancementsProvider(DataGenerator generatorIn, ExistingFileHelper fileHelperIn) {
        super(generatorIn, fileHelperIn);
    }

    @Override
    public String getName() {
        return "Ars Elemental Advancement Datagen";
    }

    @Override
    protected void registerAdvancements(Consumer<Advancement> consumer, ExistingFileHelper fileHelper) {
        for (Consumer<Consumer<Advancement>> consumer1 : ImmutableList.of(new AEAdvancements())) {
            consumer1.accept(consumer);
        }
    }

    public static class AEAdvancements extends ANAdvancements {

        static Consumer<Advancement> advancementConsumer;

        static Advancement dummy(String name) {
            return new Advancement(new ResourceLocation(ArsNouveau.MODID, name), null, null, AdvancementRewards.EMPTY, ImmutableMap.of(), null);
        }

        @Override
        public void accept(Consumer<Advancement> con) {
            advancementConsumer = con;

            saveBasicItem(ModItems.SIREN_CHARM.get(), dummy("poof_mob"));
            saveBasicItem(ModItems.FIRENANDO_CHARM.get(), dummy("enchanting_apparatus"));
            saveBasicItem(ModItems.MARK_OF_MASTERY.get(), dummy("wilden_tribute"));
            saveBasicItem(ModItems.WATER_URN.get(), dummy("enchanting_apparatus"));

            builder("mirror_shield").display(ItemsRegistry.ENCHANTERS_SHIELD, FrameType.CHALLENGE, true).addCriterion(new PlayerTrigger.TriggerInstance(ModAdvTriggers.MIRROR.getId(), EntityPredicate.Composite.ANY)).parent(dummy("enchanting_apparatus")).save(con);

            Advancement curioBag = saveBasicItem(ModItems.CURIO_BAG.get(), dummy("magebloom_crop"));
            saveBasicItem(ModItems.CASTER_BAG.get(), curioBag);

            Advancement air = saveBasicItem(ModItems.LESSER_AIR_FOCUS.get(), curioBag);
            Advancement fire = saveBasicItem(ModItems.LESSER_FIRE_FOCUS.get(), curioBag);
            Advancement earth = saveBasicItem(ModItems.LESSER_EARTH_FOCUS.get(), curioBag);
            Advancement water = saveBasicItem(ModItems.LESSER_WATER_FOCUS.get(), curioBag);
            buildBasicItem(ModItems.AIR_FOCUS.get(),"air_focus", FrameType.CHALLENGE, air).save(con);
            buildBasicItem(ModItems.FIRE_FOCUS.get(), "fire_focus", FrameType.CHALLENGE, fire).save(con);
            buildBasicItem(ModItems.EARTH_FOCUS.get(), "earth_focus", FrameType.CHALLENGE, earth).save(con);
            buildBasicItem(ModItems.WATER_FOCUS.get(), "water_focus", FrameType.CHALLENGE, water).save(con);

            //anima summoning
            Advancement necro = saveBasicItem(ModItems.NECRO_FOCUS.get(), dummy("summon_focus"));
            builder("summon_skeleton_horse").display(ModItems.NECRO_CTOME.get(), FrameType.GOAL)
                    .addCriterion(SummonedEntityTrigger.TriggerInstance.summonedEntity(EntityPredicate.Builder.entity().of(ModEntities.SKELEHORSE_SUMMON.get()))).parent(necro).save(con);

            //path of Fire
            builder("summon_strider").display(ModItems.FIRE_CTOME.get(), FrameType.GOAL)
                    .addCriterion(SummonedEntityTrigger.TriggerInstance.summonedEntity(EntityPredicate.Builder.entity().of(ModEntities.STRIDER_SUMMON.get()))).parent(fire).save(con);

            //path of Water
            builder("summon_dolphin").display(ModItems.WATER_CTOME.get(), FrameType.GOAL)
                    .addCriterion(SummonedEntityTrigger.TriggerInstance.summonedEntity(EntityPredicate.Builder.entity().of(ModEntities.DOLPHIN_SUMMON.get()))).parent(water).save(con);

            //path of Air
            builder("levitation").display(ModItems.AIR_CTOME.get(), FrameType.GOAL).addCriterion(new PlayerTrigger.TriggerInstance(ModAdvTriggers.LEVITATE.getId(), EntityPredicate.Composite.ANY)).parent(air).save(con);

            //path of Earth
            builder("spore_blossom").display(ModItems.GROUND_BLOSSOM.get(), FrameType.GOAL).addCriterion(new PlayerTrigger.TriggerInstance(ModAdvTriggers.BLOSSOM.getId(), EntityPredicate.Composite.ANY)).parent(earth).save(con);

        }

        public Advancement saveBasicItem(ItemLike item, Advancement parent) {
            return buildBasicItem(item, ForgeRegistries.ITEMS.getKey(item.asItem()).getPath(), FrameType.TASK, parent).save(advancementConsumer);
        }

        public ANAdvancementBuilder buildBasicItem(ItemLike item, String id, FrameType frame,Advancement parent) {
            return builder(id).display(item, frame).requireItem(item).parent(parent);
        }

        public ANAdvancementBuilder builder(String key) {
            return ANAdvancementBuilder.builder(ArsElemental.MODID, key);
        }

    }

}
