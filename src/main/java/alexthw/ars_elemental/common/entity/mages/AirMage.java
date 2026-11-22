package alexthw.ars_elemental.common.entity.mages;

import alexthw.ars_elemental.registry.ModEntities;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class AirMage extends EntityMageBase {


    /**
     * SelfSpells
     * 0: Leap towards the enemy
     * 1: To use after leap, launch and shear enemies
     * <p>
     * ProjSpells
     * 1: Bouncing Lightning
     * 2: Launch + Shear
     * 3: Spark-Discharge
     */
    public AirMage(EntityType<? extends EntityMageBase> p_21368_, Level level) {
        super(p_21368_, level, SpellSchools.ELEMENTAL_AIR);
        //sSpells.add(new Spell(MethodSelf.INSTANCE, EffectLeap.INSTANCE, AugmentAmplify.INSTANCE, EffectBounce.INSTANCE));
        //sSpells.add(new Spell(MethodSelf.INSTANCE, EffectOrbit.INSTANCE, EffectLaunch.INSTANCE, EffectDelay.INSTANCE, EffectWindshear.INSTANCE, EffectGravity.INSTANCE, AugmentExtendTime.INSTANCE));
        pSpells.add(Spell.fromBinaryBase64("H4sIAAAAAAAA/+1XTW/bMAz9Lzp7hR00w+brFmCXYEXT2zAYik07WmVJkOSmXpD/PspO4vgj7Zo2XVbUF0skRZGPT5S9IkYWIiHhau0RDTFTQMIfhGoTCVncAS3CjJdqESktf0FsGQfiDag5LUS8eKoqAU7LQc2SiSQyC6Ca/PSIotqymMMNy4EzgSGuWmv2gkNNyinTkzRFiZvm0jIp3AjN1JSqOllbulRbboRE1+tmu+/KrTTbpYBSML29N8ZR7XDVBDuwASyjjMslpmyK+dVBn7HkUu/7+rIVsKTjMsYALRUWXaLBaDz2SObeHpmTMPjku0yZsF+ZoXMOuNrqAtb4DCOwDdDpY2rsbMOOhiYVT4zkd9DTNYNemBZyBcbqMkppznhZwcws8iL0Ly6raDRl/OGqtTzmEtnjhC6aQsfbVIBDDsJSHmaFsQ3SAy5eAOYaZ3w/Decq2ZNSTUmZvgLNHs2/fbgTEIbZ0m2xGwY++qQJK4zjQuDXT3D52Q+Cj8EIc1B0KTYpzq6+Ta4npOPWKICKd+WUiVk98S/Qbzml9/vz+98dAxTsWxwoVwWm00lxXXGfieyYBjMvNFLyFGVvWI9o6dsIEeH8DNrM4+Ufv1b1x+3yf9iJWkYdBgzjWlNh5iJ7v2eefc8MHbjmQu9RyH0yYIRS3LD49hj4OcsWpzmF/wD/TQF68KeUm787gKN2/33BExcc3XBb/NDn2XL/y2Kfd7d9uPzP+948sFXdTzo5KsRe5vizUTeaM73zz7Dbv2EC9n93Dt1dO/I4A0EdiwgCGR+P9voPz0ZzVacPAAA="));
        // new Spell(MethodProjectile.INSTANCE, EffectLaunch.INSTANCE, AugmentExtendTime.INSTANCE, EffectDelay.INSTANCE, EffectWindshear.INSTANCE);
        pSpells.add(Spell.fromBinaryBase64("H4sIAAAAAAAA/+1X3W/aMBD/X/zMqgSVaeN1Q+oLWlX6Nk2RMRe44diW7ZRmiP9954SvQKgK7VqKlpfE9vl3X787O3PmdK5GrDtftJgFgQZY9yfj1iUgIQPlueyOZWEmCbciMVb/BuFRAmuVQkrnD8DzpYhBsOLoJYnjiVeoxuxXixluPQoJ95iBREXWzGtbtiyglVRytL00pZkwzLRHrcIXiZk+N5Vfvghe1WCUJujFRt0PE3a61VagWXB7upfCSQU43xjboABmyVjqGXns8uHtQUyhpbbbWN9WEzjagRRkoOfKEyQJtDudFhuHd4sNWTf+EgVPUfnv6PhQAu32NocFPc0RWBkY1gV3frAkwpoRm489UzxkBpy3RZLyDGVRhhK9mJAhV9eLkktOywd4JdCoAvWWo3w63UcnbENzR6vTxBmQ8gyyVq+UTI9ABvwQ1zxUUl2LMziFjdUNsXg3opVJ+ye19l6pqwJB7+flbgTKoS+CivVnHEUEykeYu8BuGpZPfP01iuPPcZucMHymls4Obm96dz22g0vuQllJRR/VoBpEV4Rb9Pnj9vjxz44ATWxL7CSuOaxBSKu7sqypV5/SdIe5df6imHBcETcRofNWNOjUefBpPVUTOoIKg2DZ/7P3xWdvw/rWJWePQpKXBNLqHsX0lPCX161Lif8yAXvhT7l0zyvAdr0Rv2LFxUd33kZ+2PNsuR8y2efdbZ9O/8uu0gdUVf1kx0dDsdcZ/YBVjeZMz/wz7PYXTMD9P7lDZ9eaPEFA8cAiRoEUp0d78Rev4K0fphAAAA=="));//(new Spell(MethodArcProjectile.INSTANCE, AugmentPierce.INSTANCE, AugmentPierce.INSTANCE, EffectLightning.INSTANCE));
        pSpells.add(Spell.fromBinaryBase64("H4sIAAAAAAAA/+1Vy27bMBD8F57VQExjo/W1MdBL0CDurSgMmlop21AkQVJxVMP/3qUk15It14HbU1BdLM3Ozj40FjfMm0pnbLbZJsyBRAts9o0J55faVM8gqlmhavu4tM78ABlQAUuaMCgoQQehOoK3wj11sWGqKK3CvD6Rl6GXj8IVwL4njDQCSgVfsQSFmnrZDAR7XVAkVwLdPM8JiY+lCWh0vCOavRO2nSrUcaaBjDYkvd2X+2Jjpt+lAqHgj2p35GUruNk3O1IA1stCmTXN7KvV/UlNaZRxfa1POwCzA0lJDQahA0kS4XoySVgRfxO2YjP+IY2Tog636MVKAWUHV8GWrvEN7BqMcSl8WHQ22PuhMYQ36hlGY8EJVH9e/qBiaTJQEYyilZOHHXmLT7Df14jCP1hWt62jXeVC+dPLakZ9K345t4LhPzgD7THUscLvW56mpCkyrKi/9Op9OrxuaAYr1robc3H/ef4wZwe63gI0lqrvUC/ah3fpVTpNCBEvHdICLz8HHN5CPRKfvsblRj80dkZdXPLNWFXOh7dignMfjfMmmPQtwLtXz28+ppxP+fWlFqAXfmiAyZEBOmhAep0BFrGp/wfGXx8YI/HeyRwZWpSRQbXl5Q1ufwHLwOwFnggAAA=="));//(new Spell(MethodProjectile.INSTANCE, EffectSpark.INSTANCE, AugmentAmplify.INSTANCE, EffectDischarge.INSTANCE));
    }

    public AirMage(Level level) {
        this(ModEntities.AIR_MAGE.get(), level);
    }

    @Override
    protected void populateDefaultEquipmentSlots(@NotNull RandomSource randomSource, @NotNull DifficultyInstance pDifficulty) {
        super.populateDefaultEquipmentSlots(randomSource, pDifficulty);
        for (var book : this.getHandSlots()) book.set(DataComponents.BASE_COLOR, DyeColor.YELLOW);
    }

}
