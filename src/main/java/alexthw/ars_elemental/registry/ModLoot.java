package alexthw.ars_elemental.registry;

import alexthw.ars_elemental.common.glyphs.MethodCurvedProjectile;
import alexthw.ars_elemental.util.ParticleUtil;
import com.hollingsworth.arsnouveau.api.spell.ISpellCaster;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.util.CasterUtil;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAOE;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDampen;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSensitive;
import com.hollingsworth.arsnouveau.common.spell.effect.*;
import com.hollingsworth.arsnouveau.common.spell.method.MethodSelf;
import com.hollingsworth.arsnouveau.common.spell.method.MethodTouch;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

import static alexthw.ars_elemental.registry.ModItems.*;
import static com.hollingsworth.arsnouveau.api.loot.DungeonLootTables.RARE_LOOT;

public class ModLoot {

    //Hooking to Ars GLM
    static {

        RARE_LOOT.add(() -> makeTome(NECRO_CTOME, "Undead Steed", new Spell()
                        .add(MethodSelf.INSTANCE)
                        .add(EffectSummonSteed.INSTANCE)
                        .add(AugmentExtendTime.INSTANCE)
                        .add(AugmentExtendTime.INSTANCE)
                , "Summon a Skeletal Horse for a short time.",
                ParticleUtil.waterColor));

        RARE_LOOT.add(() -> makeTome(WATER_CTOME, "Poseidon's Steed", new Spell()
                        .add(MethodSelf.INSTANCE)
                        .add(EffectSummonSteed.INSTANCE)
                        .add(AugmentAOE.INSTANCE)
                        .add(AugmentExtendTime.INSTANCE)
                        .add(AugmentExtendTime.INSTANCE)
                , "Summon two rideable Dolphins for a short time.",
                ParticleUtil.waterColor));

        RARE_LOOT.add(() -> makeTome(FIRE_CTOME, "It's Striding Time", new Spell()
                        .add(MethodSelf.INSTANCE)
                        .add(EffectSummonSteed.INSTANCE)
                        .add(AugmentExtendTime.INSTANCE)
                        .add(AugmentExtendTime.INSTANCE)
                , "Summon a rideable Strider for a short time.",
                ParticleUtil.fireColor));

        RARE_LOOT.add(() -> makeTome(FIRE_CTOME, "Phoenix Fire", new Spell()
                        .add(MethodTouch.INSTANCE)
                        .add(EffectIgnite.INSTANCE)
                        .add(EffectFlare.INSTANCE)
                        .add(EffectHeal.INSTANCE)
                        .add(EffectFlare.INSTANCE)
                        .add(EffectHeal.INSTANCE)
                        .add(EffectFlare.INSTANCE)
                , "The hellfire flames burn even the heal magic.",
                ParticleUtil.fireColor));

        RARE_LOOT.add(() -> makeTome(EARTH_CTOME, "Gravity Well", new Spell()
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

    }

    public static ItemStack makeTome(RegistryObject<Item> item, String name, Spell spell, String flavorText, ParticleColor particleColor) {
        ItemStack stack = item.get().getDefaultInstance();
        ISpellCaster spellCaster = CasterUtil.getCaster(stack);
        spellCaster.setSpell(spell);
        stack.setHoverName(Component.literal(name).setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_PURPLE).withItalic(true)));
        spellCaster.setFlavorText(flavorText);
        spellCaster.setColor(particleColor);
        return stack;
    }

}
