package alexthw.ars_elemental.common.entity.mages;

import alexthw.ars_elemental.common.entity.ai.SelfCastGoal;
import alexthw.ars_elemental.common.glyphs.EffectBubbleShield;
import alexthw.ars_elemental.registry.ModEntities;
import alexthw.ars_elemental.registry.ModPotions;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import com.hollingsworth.arsnouveau.common.spell.method.MethodSelf;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class WaterMage extends EntityMageBase {

    public static final Spell MANA_SHIELD_SPELL = new Spell(MethodSelf.INSTANCE, EffectBubbleShield.INSTANCE);

    /**
     * ProjSpells
     * 1: WaterGrave
     * 2: Cold Snapper
     * 3: Burst Spike
     * <p>
     * SelfSpells
     * 1: Bubble Shield
     */
    public WaterMage(EntityType<? extends EntityMageBase> p_21368_, Level level) {
        super(p_21368_, level, SpellSchools.ELEMENTAL_WATER);
        pSpells.add(Spell.fromBinaryBase64("H4sIAAAAAAAA/+VWwY7aMBD9F5+zqyTd0A3XLVIvq66WvVVVZJIhuHJsy3aALMq/dxwCJAEqFUpbqRzAHr+Z8bw3Y7EhRpYiI+NN7RENKVNAxl8J1SYBDgUIS/k455VaJAtZMJEnSsvvkFrGgXgNTshyCbRsUQaEYZYtd6fDKDlUBvRJ17kGeD/nt6IWdJXkmi5PJ4a1BZEllhVAvnlEUW1ZyuEN95wJrGrTc+qUgSdzTpmezOdocdtCWiaFWyFMPVO15cdWjp1eGKsp46Q+5PuinKvZ+QJawQyTZw1JlTPvl4HvowI0YyXC/XvcNp/gIfaDYBSEHjGKrsTb9g7Tl8+T14lL3I1rFIDTklTPTEy3mzv/HqPE/sPH0f479hBB1y0CAR5Zvx9cdoYuYpBpV26y5WRz4PuYIwGrJOdyhbKZcvZylpVUcqm7sZ52BpYNQqZIsaXCYkgERHjdfPszI+MwipxWTNhPzNAZdwXMKTdQ4+ekiPsLuvOUGjttZ+IwHM10GMmXcPKsaYOf908vYyEz4M7ogpY6Hd4ICoU9seer7cArNDDihvwj440AYdQIEDz6RwJYXZ7nf4ETur7FEH1oZyiIQz8OH+PRFUMU/tGZyTRTyj23zbv315UbMDQcgcPiKJ+SGA2yxBVkrBTQrNqy6vpsTwwIcCgpXpsRROslT/Ws1Mb+li6LbvJQNx3Ub7Go32N3e1MP9N88zRKJQGYvEV9g5/2y9v8Uk9c+sj0mT5x3/hA5hKCFQ2Du9PIL1j8AQLdWX18KAAA="));//(MethodHomingProjectile.INSTANCE, AugmentSensitive.INSTANCE, EffectBubble.INSTANCE, EffectWaterGrave.INSTANCE, AugmentExtendTime.INSTANCE, AugmentDurationDown.INSTANCE));
        pSpells.add(Spell.fromBinaryBase64("H4sIAAAAAAAA/+1WTY+bMBD9Lz7TCtKy2uTajdRL1NWmt6pCxgxZt8a2/LEJG+W/d0zIAln6sUQr7aFcwDPjNzPPb4A9scrLgiz2h4gYYFwDWXwj1NhMKv8A1C82otb3mTbqBzDHBZBoxF0agMdxF1OiyKyketRLKy14WZPvEdHUOM4EfOUVCC6xkP1gQ68E9JSCcrMsS7SEZaUcVzI8YZheYbamJVeHhgYwUiH0oUv3RYed9rQV0Ar2We42ODsC7rtiRxLANtsItcV+rc9vf4uJxCjTx/p0MvDiDJJhgY5Kh5AYMEvTiGzCPSI5WSTXceiUS3fDLc0F4G5nPBzwGmfgVGDwM2rdutVAJ4ZGDVaJBxj1OUO5+DP5g4yVKkAEYwD1hp1XxHwOHV0jAJO5SuOWqiNXeH/GVUmFhYarqSdu5Rs+7Xscpt2LBW81QHPs9YrL9XERv0cy6xXd9de7x7MANPQjLqD11QdpsjgKkJa7OqR4ekxiRDO04N6Gxj/EzZUk81k8n13Pr7AJTbey7XN9+3l5tyT/MqFK3jWjyOVmyvsu98a61zr/dCiAd0+mQdCFo4Wv+p/wBubr7xpI+wpI4lYCH+cog6tk9hIFdH0fJbAOO/9/7i7+3I34e/8VIULSKkRgbja9wMMv1Zdhc1kJAAA="));//(MethodProjectile.INSTANCE, EffectFreeze.INSTANCE, EffectColdSnap.INSTANCE, AugmentAmplify.INSTANCE));
        pSpells.add(Spell.fromBinaryBase64("H4sIAAAAAAAA/+1W3U/bMBD/X/ycoQQtaOvrVmkvFYjubZoqN7mUA8e2bKclq/K/75wPmrQpiAISEuQl8X35fr87n7NlVhUyZZNtFTADCWpgkz+MG7uQqlgDLyYrUeqbhTbqFhKHAlgwol4WxrpRjQVp0eG68wMBOUjHRafXeDcek+daYFayvwHT3DhMBPzGHARKynE7cOhlR5pMcDTTLCOJX+bKoZL+i8z0jOsGrSs91kEYqSh0tdvuUntP27kCScEe7N0aL5qA212yIxvAZrESakN4bbG8OhozUUKZfqwfnQDTvZAJJei49OSTwXkcB2zl3wFbskn0LfRIUbqfaPlSAHk7U0BFzzgDXYJen3Dr5m177PqkbhSrxBpGdc5wFI+TP9gxVykIL/RBC5PsZ9T1R8vXSISTyYrDlquGLHofkJVxYY+zVWN9dsNYDVDTVs5QzptFeEa5lDN+31/f/9szIEHfogpObkQr30UTDgGk9ago/Q4Pn1FIuA1PsbAeMy3rJ/r6PYyii+icMGi+kS3E+dWv6fWUHSlXDdrrlLyuOxjl6pQx0Qy7Nyp7PKz7lwfRwOgFpX/zGfSMY/V0/eNXq/5gtCnilxw/L4kXXxIj+t5tvFfh5uRQhp8n8KOfwEf/IY5WnJSS515JSJPT+736DzM0BmL5CgAA"));//(MethodProjectile.INSTANCE, EffectBurst.INSTANCE, AugmentSensitive.INSTANCE, EffectSpike.INSTANCE, AugmentAmplify.INSTANCE));

        pSpells.add(Spell.fromBinaryBase64("H4sIAAAAAAAA/+1W32+bMBD+X/zMKkhHteVxXaS9RKua7mmakIEL9WpsZJukLMr/vjOQAo2ztWSRWnW8AL7z/fi+u7M3RMtSpGS62XpEQcIKINPvhCodCVmugJbTjFfFbVQo+RMSwzgQzyGOyzg+IEqB08otKRU1TIoolWvRagCHHIShvNVJ6IoZaoD88EhBlWEJhxuWA2cCI90MrPZiRMmSU6ZmyyWu2N9cWlf2C9WKOS2anE1lMx6YERJNbzt3Xwu7U++2Aq6C3vPdKkeNwU0XrMMBrKOMyzWmrMv46qDNRHKp+rYudwssfWQywQANFQZNosIkDD2S2bdHYjINPvg2UybMZ6Yp8oS7jSphi48bgV2AVp5QbRZtkXTVUpeLlnwFTplRlPE/gz/wmEssE7tojZYq2UXUlYNUcYeXw8JosEK/xaoBC997YC0p11CDNZZyLV4w3TVZz654XQDUvFdzJhbNj3+GYFZzet//v//1SAEX+hpHwHryTgoaYCfnTW1Mzp9YGykIzUxlPTx8XmDqiqas1DZtS5Yu6Fq0aV1++zQjT+lHKa7rxmMiGzPd4lJpcyqywyHb7x6WBkpH9hEO9jt4Ac30d8bDPuGB3zzB+49+EFwEkyH/i6svs+uDFdDl3ZTAwu78f7gdfbg55L1bxB7F9iaDEUpxw5K7MfBzlt2erPuC1z1rRx/ErtabDIZt8JxeGxSIeruz9nUR/u9mrZt/9zV3f3czJKxQ0NwKMddk/Gjb/gbobiMzog0AAA=="));//(MethodProjectile.INSTANCE, EffectBubble.INSTANCE, EffectDelay.INSTANCE, EffectCavitate.INSTANCE));
        pSpells.add(Spell.fromBinaryBase64("H4sIAAAAAAAA/+1W32/TMBD+X/wcUFIaBH2FSrxMTOveEIrc5Jp5OLZlO+tClf8dX5q2cZLBFqhEEXmJfT98d999PnlHjCxFRha7OiAaUqaALL4Qqk0iZPkAtFzkvFJ3idLyHlLLOJCgUQOHAoSlvDXYUgs6uQfb6n33DDitJnkaxZklXwOiqLYs5XDLCuBMuDx3nnknQ6fZcMr0crNxEtwW0jIpcOXM1BVV+4pthfV6x1hNGSf1Kd5nha7m4AtOCqYf3CgARJFUV0ys9pvwdRi4PX3s7h+/9wycoGtR+xgckkj2me5OKAwzzzRTiol8D6gD05Tr66czbvtOhguW9U5WkgkLWYIRjJUCmlUbp657OaeSS93N9cNBMDg4dcBaKrDzzmAWxwHJ8R+QNVlE7xAN60J/ZIauOSJkdQmDgBkIw2yFEY7LuYNW04yVBmF9E/rf3IGj6Fa0KK6uPy1vlqQeZ0QPV7RKqbGrPm51c4WM5A8wqmuY9XNKenEL6W4NCvHQUqf9vKBQtjq1uSX1E0VcLq0FbJOcy+2vCD2ZdnHYsC5qaedYOKDdhnLzPN5FoUe8qCVcNH8fRtHbaPYS4h0rR70UNw23HBGnjLV1qY09V/9jnwCvjiLP6G/mwJ8cPfG5CLBCxynNF25kv7j3F9qfZyA5ou88HgYdxpeLy1CKW5Z+mwI/Z/nd2e5e9E8MX/z99uyd+aN38k3T/wftRfT7TIP22P7xV9zIA7EZEagUtEClKzWdPtjqH6IwpxKVDQAA"));//(MethodProjectile.INSTANCE, EffectWaterJet.INSTANCE, EffectDelay.INSTANCE, EffectWaterJet.INSTANCE));
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new SelfCastGoal<>(this, () -> MANA_SHIELD_SPELL, () -> !this.hasEffect(ModPotions.MANA_BUBBLE) && this.getHealth() < this.getMaxHealth() * 0.8, 1, 10));
    }

    @Override
    protected void populateDefaultEquipmentSlots(@NotNull RandomSource randomSource, @NotNull DifficultyInstance pDifficulty) {
        super.populateDefaultEquipmentSlots(randomSource, pDifficulty);
        for (var book : this.getHandSlots()) book.set(DataComponents.BASE_COLOR, DyeColor.CYAN);
    }

    public WaterMage(Level level) {
        this(ModEntities.WATER_MAGE.get(), level);
    }

}
