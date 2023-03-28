package alexthw.ars_elemental.datagen;

import alexthw.ars_elemental.common.glyphs.MethodCurvedProjectile;
import alexthw.ars_elemental.util.ParticleUtil;
import com.hollingsworth.arsnouveau.api.sound.ConfiguredSpellSound;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.datagen.CasterTomeProvider;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAOE;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDampen;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSensitive;
import com.hollingsworth.arsnouveau.common.spell.effect.*;
import com.hollingsworth.arsnouveau.common.spell.method.MethodSelf;
import com.hollingsworth.arsnouveau.common.spell.method.MethodTouch;
import com.hollingsworth.arsnouveau.common.tomes.CasterTomeData;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import java.io.IOException;
import java.nio.file.Path;

import static alexthw.ars_elemental.ArsElemental.prefix;
import static alexthw.ars_elemental.registry.ModItems.*;

public class AECasterTomeProvider extends CasterTomeProvider {
    public AECasterTomeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    public void run(CachedOutput cache) throws IOException {

        tomes.add(buildTome(WATER_CTOME, "dolphin", "Poseidon's Steed", new Spell()
                        .add(MethodSelf.INSTANCE)
                        .add(EffectSummonSteed.INSTANCE)
                        .add(AugmentAOE.INSTANCE)
                        .add(AugmentExtendTime.INSTANCE)
                        .add(AugmentExtendTime.INSTANCE)
                , "Summon two rideable Dolphins for a short time.",
                ParticleUtil.waterColor));

        tomes.add(buildTome(FIRE_CTOME, "strider", "It's Striding Time", new Spell()
                        .add(MethodSelf.INSTANCE)
                        .add(EffectSummonSteed.INSTANCE)
                        .add(AugmentExtendTime.INSTANCE)
                        .add(AugmentExtendTime.INSTANCE)
                , "Summon a rideable Strider for a short time.",
                ParticleUtil.fireColor));


        tomes.add(buildTome(EARTH_CTOME, "magnet", "Gravity Well", new Spell()
                        .add(MethodCurvedProjectile.INSTANCE)
                        .add(EffectGravity.INSTANCE)
                        .add(AugmentSensitive.INSTANCE)
                        .add(AugmentAOE.INSTANCE)
                        .add(AugmentExtendTime.INSTANCE)
                        .add(EffectLinger.INSTANCE)
                        .add(AugmentSensitive.INSTANCE)
                        .add(EffectLaunch.INSTANCE)
                        .add(AugmentDampen.INSTANCE),
                "Creates a gravity center that also attract nearby blocks.",
                ParticleUtil.earthColor));

        tomes.add(buildTome(FIRE_CTOME, "hellflare", "Phoenix Fire", new Spell()
                        .add(MethodTouch.INSTANCE)
                        .add(EffectIgnite.INSTANCE)
                        .add(EffectFlare.INSTANCE)
                        .add(EffectHeal.INSTANCE)
                        .add(EffectFlare.INSTANCE)
                        .add(EffectHeal.INSTANCE)
                        .add(EffectFlare.INSTANCE)
                , "The hellfire flames burn even the heal magic.",
                ParticleUtil.fireColor));

        tomes.add(buildTome(NECRO_CTOME, "skelehorse", "Undead Steed", new Spell()
                        .add(MethodSelf.INSTANCE)
                        .add(EffectSummonSteed.INSTANCE)
                        .add(AugmentExtendTime.INSTANCE)
                        .add(AugmentExtendTime.INSTANCE)
                , "Summon a Skeletal Horse for a short time.",
                ParticleUtil.soulColor));

        Path output = this.generator.getOutputFolder();
        for (CasterTomeData g : tomes) {
            Path path = getRecipePath(output, g.getId().getPath());
            DataProvider.saveStable(cache, g.toJson(), path);
        }
    }

    public CasterTomeData buildTome(RegistryObject<Item> item, String id, String name, Spell spell, String flavorText, ParticleColor particleColor) {
        return new CasterTomeData(prefix(id + "_tome"),
                name,
                spell.serializeRecipe(),
                item.getId(),
                flavorText,
                ParticleColor.defaultParticleColor().getColor(), ConfiguredSpellSound.DEFAULT);
    }

    @Override
    public String getName() {
        return "Ars Elemental Caster Tomes Datagen";
    }

}
