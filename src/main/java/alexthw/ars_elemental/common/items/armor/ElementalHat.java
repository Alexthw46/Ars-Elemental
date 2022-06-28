package alexthw.ars_elemental.common.items.armor;

import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;

public class ElementalHat extends GeoArmorItem implements IElementalArmor, IAnimatable {
    final SpellSchool element;

    public ElementalHat(SpellSchool element, Properties builder) {
        super(IElementalArmor.schoolToMaterial(element), EquipmentSlot.HEAD, builder);
        this.element = element;
    }

    public SpellSchool getSchool() {
        return element;
    }

    @Override
    public void registerControllers(AnimationData animationData) {

    }

    public AnimationFactory factory = new AnimationFactory(this);

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public int getMaxManaBoost(ItemStack i) {
        return 100;
    }

    @Override
    public int getManaRegenBonus(ItemStack i) {
        return 9;
    }

}
