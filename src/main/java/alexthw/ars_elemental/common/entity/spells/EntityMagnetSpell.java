package alexthw.ars_elemental.common.entity.spells;

import alexthw.ars_elemental.registry.ModEntities;
import alexthw.ars_elemental.util.GlyphEffectUtil;
import com.hollingsworth.arsnouveau.api.spell.IFilter;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.common.entity.EntityLingeringSpell;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.entity.familiar.FamiliarEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class EntityMagnetSpell extends EntityLingeringSpell {

    List<Predicate<Entity>> ignored;
    public EntityMagnetSpell(EntityType<? extends EntityProjectileSpell> type, Level worldIn) {
        super(type, worldIn);
    }

    public EntityMagnetSpell(Level worldIn) {
        super(ModEntities.LINGER_MAGNET.get(), worldIn);
    }

    @Override
    public EntityType<?> getType() {
        return ModEntities.LINGER_MAGNET.get();
    }

    static public void createMagnet(Level world, LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, Vec3 location) {
        EntityMagnetSpell magnet = new EntityMagnetSpell(world);
        magnet.ignored = makeIgnores(shooter, spellContext.getSpell(), spellContext.getCurrentIndex() + 1);
        magnet.setPos(location);
        magnet.setAoe((float) spellStats.getAoeMultiplier());
        magnet.setOwner(shooter);
        magnet.extendedTime = spellStats.getDurationMultiplier();
        magnet.setColor(spellContext.getColors());
        world.addFreshEntity(magnet);
    }

    @Override
    public float getAoe() {
        return super.getAoe() / 2;
    }

    @Override
    public void tick() {

        age++;

        if (this.age > getExpirationTime()) {
            this.remove(RemovalReason.DISCARDED);
            return;
        }
        if (level().isClientSide() && this.age > getParticleDelay()) {
            playParticles();
        }
        // Magnetize entities
        if (!level().isClientSide() && this.age % 5 == 0) {
            for (Entity entity : level().getEntities(this, new AABB(this.blockPosition()).inflate(getAoe()))) {
                if (testFilters(entity)) continue;
                Vec3 vec3d = new Vec3(this.getX() - entity.getX(), this.getY() - entity.getY(), this.getZ() - entity.getZ());
                if (vec3d.length() < 1) continue;
                entity.setDeltaMovement(entity.getDeltaMovement().add(vec3d.normalize()).scale(0.5F));
                entity.hurtMarked = true;
            }
        }

    }

    public boolean testFilters(Entity entity) {
        return ignored.stream().anyMatch(filter -> filter.test(entity));
    }

    public static List<Predicate<Entity>> makeIgnores(LivingEntity shooter, Spell spell, int index) {
        List<Predicate<Entity>> ignore = new ArrayList<>();
        // prevent magnet from pulling itself and other lingering spells and familiars and entities that are ignored by filters
        ignore.add((entity -> entity instanceof EntityLingeringSpell));
        ignore.add((entity -> entity == shooter));
        ignore.add(entity -> entity instanceof FamiliarEntity);
        ignore.add(shooter::isAlliedTo);
        Set<IFilter> filters = GlyphEffectUtil.getFilters(spell.unsafeList(), index);
        if (!filters.isEmpty()) {
            ignore.add(entity -> GlyphEffectUtil.checkIgnoreFilters(entity, filters));
        }
        return ignore;
    }

}
