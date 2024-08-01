package alexthw.ars_elemental.common.items.caster_tools;

import alexthw.ars_elemental.api.item.ISchoolFocus;
import alexthw.ars_elemental.common.components.SchoolCasterTomeData;
import alexthw.ars_elemental.registry.ModRegistry;
import com.hollingsworth.arsnouveau.api.mana.IManaCap;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import com.hollingsworth.arsnouveau.common.items.CasterTome;
import com.hollingsworth.arsnouveau.common.network.Networking;
import com.hollingsworth.arsnouveau.common.network.NotEnoughManaPacket;
import com.hollingsworth.arsnouveau.common.util.PortUtil;
import com.hollingsworth.arsnouveau.setup.registry.CapabilityRegistry;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class ElementalCasterTome extends CasterTome implements ISchoolFocus {
    private final SpellSchool school;

    public ElementalCasterTome(Properties properties, SpellSchool school) {
        super(properties.component(ModRegistry.E_TOME_CASTER, new SchoolCasterTomeData()));
        this.school = school;
    }

    @Override
    public SpellSchool getSchool() {
        return school;
    }


    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip2, TooltipFlag flagIn) {
        tooltip2.add(Component.translatable("tooltip.ars_elemental.caster_tome"));
        super.appendHoverText(stack, context, tooltip2, flagIn);
    }

    @Override
    public double getDiscount() {
        return 0;
    }

    public static class ETomeResolver extends SpellResolver {

        public SpellSchool getSchool() {
            return school;
        }

        private final SpellSchool school;

        public ETomeResolver(SpellContext context, SpellSchool school) {
            super(context);
            this.school = school;
        }

        @Override
        public boolean hasFocus(ItemStack stack) {
            if (stack.getItem() instanceof ISchoolFocus focus) {
                return getSchool() == focus.getSchool();
            } else if (stack.getItem() == ItemsRegistry.SHAPERS_FOCUS.get()) {
                return getSchool() == SpellSchools.MANIPULATION;
            }
            return super.hasFocus(stack);
        }

        @Override
        protected boolean enoughMana(LivingEntity entity) {
            int totalCost = getResolveCost();
            IManaCap manaCap = CapabilityRegistry.getMana(entity);
            if (manaCap == null)
                return false;
            boolean canCast = totalCost <= manaCap.getCurrentMana() || manaCap.getCurrentMana() == manaCap.getMaxMana() || (entity instanceof Player player && player.isCreative());
            if (!canCast && !entity.getCommandSenderWorld().isClientSide && !silent) {
                PortUtil.sendMessageNoSpam(entity, Component.translatable("ars_nouveau.spell.no_mana"));
                if (entity instanceof ServerPlayer serverPlayer)
                    Networking.sendToPlayerClient(new NotEnoughManaPacket(totalCost), serverPlayer);
            }
            return canCast;
        }

        @Override
        public SpellResolver getNewResolver(SpellContext context) {
            return new ETomeResolver(context, getSchool());
        }
    }
}
