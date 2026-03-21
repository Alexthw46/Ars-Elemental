package alexthw.ars_elemental.common.entity.ai;

import alexthw.ars_elemental.common.entity.mages.EntityMageBase;
import com.hollingsworth.arsnouveau.api.spell.EntitySpellResolver;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.network.Networking;
import com.hollingsworth.arsnouveau.common.network.PacketAnimEntity;
import net.minecraft.world.item.ItemStack;

import java.util.EnumSet;
import java.util.function.Supplier;

import static com.alexthw.sauce.util.ParticleUtil.schoolToColor;

/**
 * Flexible self-cast goal that casts a specific spell (or uses a supplier) when the provided condition allows it.
 * Use this when you want fine-grained situational spells (healing, buffing, escape, etc.).
 */
public class SelfCastGoal<T extends EntityMageBase> extends CastGoal<T> {

    private final Supplier<Spell> spellSupplier;
    private Spell spell;

    public SelfCastGoal(T entity, Supplier<Spell> spellSupplier, Supplier<Boolean> canUse, int animId, int delayTicks) {
        super(entity, 1.0, 100, canUse, animId, delayTicks);
        this.spellSupplier = spellSupplier;
        this.setFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        // self-cast goals can work without a target
        return this.canUse.get();
    }

    @Override
    public void tick() {
        super.tick();
        // Only cast when the external condition allows and the entity cooldown permits
        if (mob.selfCastCooldown <= 0) {
            ParticleColor color = schoolToColor(mob.school.getId());
            EntitySpellResolver resolver = new EntityMageBase.MageResolver(new SpellContext(mob.level, this.spell, this.mob, new LivingCaster(this.mob)).withColors(color), mob.getSchool());
            resolver.onCast(ItemStack.EMPTY, mob.level);
            mob.selfCastCooldown = 60;
            stop();
        }
    }

    @Override
    public void start() {
        super.start();
        if (spell == null) spell = spellSupplier.get();
        Networking.sendToNearbyClient(mob.level, mob, new PacketAnimEntity(mob.getId(), animId));
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse.get();
    }

}