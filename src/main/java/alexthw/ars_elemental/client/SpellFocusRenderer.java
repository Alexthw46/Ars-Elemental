package alexthw.ars_elemental.client;

import alexthw.ars_elemental.common.items.ISchoolItem;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.client.particle.GlowParticleData;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class SpellFocusRenderer implements ICurioRenderer {

    ParticleColor schoolToColor(SpellSchool school){
        return switch ( school.getId() ){

            case "fire" -> new ParticleColor(250, 15, 15);

            case "water" -> new ParticleColor(50, 50, 250);

            case "earth" -> new ParticleColor(50, 250, 55);

            case "air" -> new ParticleColor(255,255,26);

            default -> new ParticleColor(250,250,250);
        };
    }


    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

        if (stack.getItem() instanceof ISchoolItem focus && slotContext.entity() instanceof Player player){

            ParticleColor color = schoolToColor(focus.getSchool());
            player.getLevel().addParticle(GlowParticleData.createData(color,false,0.25f, 0.40f, 20),
                    player.getX() + 0.5 * Math.sin(Math.toRadians(player.yBodyRot + 90)), player.getEyeY() + 0.1 + 0.07 * Math.sin(Math.toRadians(player.level.getDayTime()*5)), player.getZ() - 0.5 * Math.cos(Math.toRadians(player.yBodyRot + 90)),
                    0,0,0);

            //TODO render a model

        }

    }

}
