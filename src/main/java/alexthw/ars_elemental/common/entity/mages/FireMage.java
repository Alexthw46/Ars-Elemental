package alexthw.ars_elemental.common.entity.mages;

import alexthw.ars_elemental.common.entity.ai.SelfCastGoal;
import alexthw.ars_elemental.common.glyphs.EffectCauterize;
import alexthw.ars_elemental.registry.ModEntities;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectHeal;
import com.hollingsworth.arsnouveau.common.spell.method.MethodSelf;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.EffectCures;
import org.jetbrains.annotations.NotNull;

public class FireMage extends EntityMageBase {

    /**
     * ProjSpells
     * 1: Flare bolt
     * 2: Bouncing Firework
     * 3: Homing Explosion
     */
    public FireMage(EntityType<? extends EntityMageBase> type, Level level) {
        super(type, level, SpellSchools.ELEMENTAL_FIRE);
        pSpells.add(Spell.fromBinaryBase64("H4sIAAAAAAAA/+1WTW/iMBD9Lz5nK5s2Fcl1i7SXaquyt2qFTDKkbh3bsp0Ci/jvHYdQEggVoqpUreoLyXy8eZ55dlgRpyuVk3S1joiFTBgg6QPh1k2Url6AV2khl+ZxYqx+gswLCSTqcYtCCd/vmklugfyNiOHWi0zCH1GCFAoLrTrhrRLowTRhR7MZWsJrqb3QKjxhmLnlZkPZLwPhDozSCL3elfttQqbbpgJawR3UboInG8DVjmxPAZhPCqnnuFtXTe+OYmZaatvG+rk1iHwPMkOCniuPkBgwiOOIFOE3IlOSsiENOxXK3wjHpxIw29sK1rj6O7AlGPwZd37czHg37HraTssX6PV5y4V8v/lnt8+V+hm+QO+6Wi11DjLgh7ZUNttnDaXxyx3rRoBHBvCI+l58qgbxdJRfr4k5KCewTVjh7ZFRipg8FxXyoxeXtF6MJQOaDIbJNe7B8Llq9ji++zW6H5E9XGcAao0ub4Uab15+0At6RTuLRRjAF00A+i+TzhpGZPGvg8BqSyuFHZnopt/BqdV9fXCEKs65naaVdf7/uJ5iWqsjPhTHjEt3mjritjbYdo5XCQrkmg3O1QaOfl8K8cHsG1Mn6JQLVSMGkvr+NH3409Tjb/0HCBEqnLqUYO3sfILrVyrP91/lCAAA"));//(new Spell(MethodProjectile.INSTANCE, EffectIgnite.INSTANCE, EffectFlare.INSTANCE));
        pSpells.add(Spell.fromBinaryBase64("H4sIAAAAAAAA/91WTW+jMBD9Lz6zFZBSAddtpF6qrZq9rVbIkAnx1tiWbZpkI/77jgkkkI9VFalSVF8C8/FmPO8xypYYWYs5SbeNRzQUTAFJfxGqTSZk/Q60Tku+UctMafkHCss4EO+Mm5WC2d4FHCoQlvLOWUix4LTUFCN+e0RRbVnB4SergDOBBbcjxEEp9GAm09PFAi3utZKWSeGeMEw9U7Vr3W5c4yMYIRG6OZT7oVym6VMBrWBOanfB2Q5we2j2TAFYZSWXK7y1qfOXi5iF5FIPsb73BjY/gsRJGUuFRUgMCKPII6X79UhO0iD23U2ZsI/M0JwDZltdQ4Pn/AT6Bp2/oMbOOq73pBPFbLEkqX8XNq0AjOTvcBLW4mvK+P95uHqSppJvcANjHCu7knPgDt+NpdbFcddQKbs5dN1p8QIXS5T6+lPliB9KdXtDnIMwDMeEFfaPge8jJp2z2jjpTfz2BEES+kkYJw94B0VXorvj7OVp+jolR7hGAbQa3TwzMdu9fPPv/Ht/dAIPA+i6C0D/JBmd2CPrvyOEoLUMUoILjO7m7ZxSvLYfDhPlNYsqr7WxX2NTRX6rjuhUHAvKzcfUEQ21EfQ83icokIcgvFYbSP2xFKIT7jvTKOgju1UiBjZ1m+TnrMzK2thPJH/Skh/HLflx/OXJ34/0gn/wD8ZFCLcoUoLTKq7fts0/kZ+VLasJAAA="));//(new Spell(MethodProjectile.INSTANCE, EffectIgnite.INSTANCE, EffectConflagrate.INSTANCE));
        pSpells.add(Spell.fromBinaryBase64("H4sIAAAAAAAA/+1VTY/TMBD9Lz6HVdKlq22vUGkvFavt3hCKXGdShnVsy3aahqr/nXGaNgmksOwKcYBcEs88v/l4Y2fPnC5Vxub7Q8QsCDTA5h8Zty4FCQUoz+V8I2vzOeVWpMbqLyA8SmBRA1K63AIvW4gzEv1vergQFMlyP05pEKwYd+VoodL2aZy2oIh5PeqDnQeVpR4LYJ8iZrj1KCQ80lqiogbsB5t6RZMnlxztIs/JEpaF9qhV+CKYWXJzbKWvQyMHNEoT9aEL98GEne60FcgK7ofYLTg9Eu67ZEcCQJVupK6oZleu7y9yCi217XO9Oxkw+45SUIKeq6AcASbTacQ24R2xNZsnt3GoFJV/j46vJdBub0s40DPegVOCwS+486t29rohbKbQabmFUZ+3HOXPmz+IWOgMZDAG0jIM0jAjKIyvu3618l1Iv+LbPytgb6D/toDDY5OBckiNogjnzySOiZNnWFJ+8dV13DxJMpvEs8nt7IZqMLxSbZmr+7vFw4Jd6Oy58ODX6qGZAFSblxyzdWmd/1fO2a9lmvZFSuJWpbczUuommYyLNGR1BqA5iPUS1eq4iK9I/HrJd92aAu2+dog3Z9MA9JybQRMHJfX/jn31HTvi7/3MAkLxIiAotnh5godvTOXqUkYIAAA="));//(new Spell(MethodArcProjectile.INSTANCE, AugmentSplit.INSTANCE, AugmentSplit.INSTANCE, AugmentAccelerate.INSTANCE, AugmentPierce.INSTANCE, EffectFirework.INSTANCE, AugmentAmplify.INSTANCE, AugmentExtendTime.INSTANCE));
        pSpells.add(Spell.fromBinaryBase64("H4sIAAAAAAAA/9VWwY6bMBD9F5/ZFU7KdptrG6mXVVeb3qoKGTNk3TW2ZZskFPHvtYGEkLBVSptW5WKYeX4znnkDVMjIQqRoUdUB0kCZArT4gog2MXDIQVjCF2tequf4WeZMrGOl5TeglnFAQYMTstgAKTqUAWGYZZtxL+wUl4ZJMeolueIsK3/ZlzonCPQ1QIpoyyiHzywHzoQ7SzXAHyXvPBknTC+zzFn8Yy6tz8zdOZh6IKqtii19TQY0VhPGUd3H+6T8VrPfC84K5ix4B45bxqrP9jwCdUfKmIaYSlPGJpcvvqCmSB5fpaeSS31M+35vYOkpu8vVEmEdpQPMoihAa78GKEELfB/6UzNhPzBDEg5ut9UF1HU9LH7atLr0EQ63OHSUJGWFSy+8nUfh4Jq7IyiyFd2BV48fl09LdEJrFPiQFSofmFi1DzfhbXgXOAvZdRZnCNDuew/ZG44R9Xj7xorroZQYu+rGoZ+LZjCM5BsY9TVa+LmIBsFzmQL3Rk9aaLpPrh83qZO+1yMMkxuN7+dNo3Hb57MuZ4Qb3+Z/JfpEA3nx7xjKNOXXF3wUNoWIzvW+r8Qlgh8oHndKx2/ehRjf4dlUxTdy/kN6P62rh0nx1Ijamae8AZNCG3tVMQjYxmsut1dTAZ69bafBr0mz/oYOor+lgmgog5uDaQB6RQmHorYSWPmkprRfSPdt/a+7f/lH74JKjviP/jQ8QpDcI1xsOj3B+gc9e17PrgkAAA=="));//(new Spell(MethodHomingProjectile.INSTANCE, AugmentSensitive.INSTANCE, EffectExplosion.INSTANCE, AugmentAmplify.INSTANCE, AugmentAmplify.INSTANCE, AugmentDampen.INSTANCE));
    }

    public FireMage(Level level) {
        this(ModEntities.FIRE_MAGE.get(), level);
    }

    public static final Spell PURIFY_SPELL = new Spell(MethodSelf.INSTANCE, EffectCauterize.INSTANCE, EffectHeal.INSTANCE);

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new SelfCastGoal<>(this, () -> PURIFY_SPELL, () -> this.getHealth() < this.getMaxHealth() * 0.8 && this.getActiveEffects().stream().anyMatch(e -> e.getEffect().value().getCategory() == MobEffectCategory.HARMFUL && e.getCures().contains(EffectCures.MILK)), 1, 10));
    }

    @Override
    protected void populateDefaultEquipmentSlots(@NotNull RandomSource randomSource, @NotNull DifficultyInstance pDifficulty) {
        super.populateDefaultEquipmentSlots(randomSource, pDifficulty);
        for (var book : this.getHandSlots()) book.set(DataComponents.BASE_COLOR, DyeColor.RED);
    }

}
