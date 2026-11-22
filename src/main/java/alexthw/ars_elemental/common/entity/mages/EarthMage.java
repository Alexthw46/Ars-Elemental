package alexthw.ars_elemental.common.entity.mages;

import alexthw.ars_elemental.common.glyphs.EffectConjureTerrain;
import alexthw.ars_elemental.registry.ModEntities;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectLeap;
import com.hollingsworth.arsnouveau.common.spell.method.MethodPantomime;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class EarthMage extends EntityMageBase {

    /**
     * ProjSpells
     * <p>
     * 1: Envenom
     * 2: Burst Spike
     * 3: Crush
     * 4: Snare Spike
     * 5: Shoot Terrain
     */
    public EarthMage(EntityType<? extends EntityMageBase> p_21368_, Level level) {
        super(p_21368_, level, SpellSchools.ELEMENTAL_EARTH);
        pSpells.add(Spell.fromBinaryBase64("H4sIAAAAAAAA/91WXW+bMBT9L35mE46atM3rFmkv1aqmb9OEHLik7oxt2SYpi/jv8wUyMEnXNFWlbbwE3+9zzwGyI1aVMiPzXR0RAynXQObfCDM2karcACvna1Hph0Qb9Qip4wJI1LhBQAHSMdEFgNyAVMUzXq24VTKxWhmwXUzYgBVa8Lwi3yOimXE8FXDPCxBc+ol2QcJgFu/JBeNmkefegsdCOa4k3vkwfcN0i81ViCwo4wzjgtR9v68aU+0+F7wV7Li51QC4L1LdcLlsD/HHOPJn9jQ8P/0cBXjDMKIOt7AfImkn3fVb6Cfvt9rs2q8ThPDbtOXq9tmBUyWUGRb8tDfwbLSR1KN3TDpf0gdMptOIrPE3Iisyp1c4suPSfeaWrQTCcKaEuh4hyUBa7irs8PuWevyGZby0iJ3G7UUvrmNKZ3TiIWi2lR3U5e2Xxd2C1CFtx8FjUMqsW3Yy7vXcCNoqsYGjvob9P8smQFWoDAQasWhp0rGarOY/oOfiSIWziaANDTS+bHk4YCFnwiIN/4vM97EStslaqO27KbxbLH1psafo++0CP4CNfiXvGhFzuT7nHbcqjXXvRD6dXgfXVSiGDz5jdiCH2Rv0sGVGQ9Z+RP6Jt94kEMX0NSIIsLZCWGLyOSKQyn9DX6uBv+pBPZ2UEx6pI/7BPwqMkKzACN87PX/A+hfb/dhyXAkAAA=="));//(MethodProjectile.INSTANCE, EffectEnvenom.INSTANCE, EffectSpores.INSTANCE, AugmentAmplify.INSTANCE));
        pSpells.add(Spell.fromBinaryBase64("H4sIAAAAAAAA/+1Vz2+bMBT+X3xmE6ZJ1ea6ReolatX0Nk2RA4/UnbEt2yRlEf/7nk1oIIVVTdWdxgX8fr/vfTzviVWlzMhsX0fEQMo1kNkPwoxdSVVugZWzjaj040ob9QSp4wJINKBel8a6QY0Fabnj29YPBBQgHROtXvNfwzFZoQXPK/IzIpoZx1MBD7wAwSXWuO85dKpDTS4YN/M8R4k/FspxJf0XmukF0023rvK99sJIhaHrY7pb7T1t6wooBfsq98F41QTcH4sdSAC71UaoHfZry/XdaMxUCWW6sb61Ap6dhEyxQMekBx8Nkuk0Ihv/jsiazOhV7Dvl0n3nlq0FoLczJdT4DCPQFuj1KbNueaDHkSeBKFaJLQzqnGFc/B38XsZCZSC80ActTXpaUcuPA14DEc4Gi15dBLDoJA5oXdNXYOVM2HG0Qq/vJozVAAG2asHlsjnEX7GEasGeu+fn3ycGKOha1NHZRGT28R9wsCEhvsdI2G8gC6ui8ilePmmMfRuW8dL6njGi1WwnDy0t727m93MyMh7fpFcpeR8Iy+XmnK3Q7LZPmvK0P+YvL6Ke0Qcm/ekrhyaXYdyTJEx7koz+RG9Pe9qdNY4+PHRyHVN6SZP3zL63yBTCi47/r4QPXwkD+s7d6y0kK7wF5k7PL7D+AyOePE6YCAAA"));//MethodProjectile.INSTANCE, EffectBurst.INSTANCE, AugmentSensitive.INSTANCE, EffectSpike.INSTANCE, AugmentAmplify.INSTANCE));
        pSpells.add(Spell.fromBinaryBase64("H4sIAAAAAAAA/+1Vz2+bMBT+X3xmFUSj27iukXapVjW7TRN6mAfxZGzLNk1ZxP8+20kKNKRS0uU2Lrbf7+97D3tLjGxFSbJtHxGNlCkk2U8C2uRCtk8IbVbzTq1zpeVvpJZxJNGMmurWrGc10CjOqu5N3a+IKNCWUY4/WIOcCVfFduIwyu80FQeml1XlJP7YSMuk8Dtnpu5B7fDYzqOZhBHShe6HdN+V9zQHV3RSNEe598b5LuB2KHYmAW7ymsuNw2va4uFkTCq51ONYXw8CVr4KSV2BFoR1IZ3BIk0jUvs1IgXJks+xR8qEvWMGCo7O2+oWe/fNM3Ao0OspGLvaD8AwCWEUjORPeKQbNkdl1sAgr6BhvAsMM0vXJItvbtNQiQbG3+7YJFojS+Re6CtpNT3AQI4NCgs8k7oYSJ6JcDHDyeJTYDisRViPKK6Am9McB7Bnj5lRiIHa7p6J1e4Q38SRO8Pz+Pz855WBE4wt+uji8YXwF19ncs/hdYqgRGGY7XyOl23qcGsoWWs85oUrWsFG7CGtHr4tH5fkRHs8SK+S4jGMORP1JXdJ0Wpjr9XldNrmDy+iidE7On31i+oft3vc7STefcnHL3GS3CZndX9yAUpHsHP8/5S8+ymZ0Y/ebG8hoPEWLje9vMD+L02WTBqyCAAA"));//(MethodProjectile.INSTANCE, EffectCrush.INSTANCE, AugmentAmplify.INSTANCE, AugmentAmplify.INSTANCE));
        pSpells.add(Spell.fromBinaryBase64("H4sIAAAAAAAA/+1VS2/bMAz+Lzp7hV3MRZfrFmCXokXT2zAYtMyk2mRJ0KNJFvi/j7Ljxk6dAks3YAOmi8XXR34kYe2Y00FVbLZrEmaRC4Ns9oWBdYXS4QkhzFZyax4LY/U35F5IZMmE2Smw0xbceFRV4UXd21FijcqD7GON+D4dCxrZ14QZsF5wiQ+EIYWiCncj50FtZFlKEHa+XJImirX2Qqt4IzdzA6bj6reR6QhGaYJuDuluTYx0fSiSFt2L3HvnogPcHYqdSIDrYiX1mri6UN6dxORaajvE+tgrRHUEyalAD8oTJDlc5nnCVvGbsJLNsus0MhXKfxIOSokU7W3Ahs50B/oCo52D84v9chy2pF0Tp+UTTtq8BSFfb/4oY60rlFEZQYPlxxXxUOKhXRMAZ/fqOmtblXetyvMXnVqCdKdb1RL95W1xBrHt2fZGqEUnpBdpQjJshvLmx5EDKYYeTXL2FoJ7/AsWcFx/hcoJv40Znq9ZSrQtVCK4SJnE9mTvP6RZdpVdEgcDa7VnuLj7PL+fsxPTipyjSav7dnmFWp3zhyiDdf5PDT0fT/3ds2rk9IbBC491wXW5xvKfWID8t41/yLxbg0WM/f9IvPmRmLAPXuPooaCOHpSbn19g8xPYex+UqAgAAA=="));//(MethodProjectile.INSTANCE, EffectSnare.INSTANCE, AugmentExtendTime.INSTANCE, EffectSpike.INSTANCE));

        pSpells.add(new Spell(MethodPantomime.INSTANCE, EffectConjureTerrain.INSTANCE, AugmentAmplify.INSTANCE, AugmentAmplify.INSTANCE, EffectLeap.INSTANCE));
    }

    @Override
    protected void populateDefaultEquipmentSlots(@NotNull RandomSource randomSource, @NotNull DifficultyInstance pDifficulty) {
        super.populateDefaultEquipmentSlots(randomSource, pDifficulty);
        for (var book : this.getHandSlots()) book.set(DataComponents.BASE_COLOR, DyeColor.GREEN);
    }

    public EarthMage(Level level) {
        this(ModEntities.EARTH_MAGE.get(), level);
    }
}
