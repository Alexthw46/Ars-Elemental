package alexthw.ars_elemental.client;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.ConfigHandler;
import alexthw.ars_elemental.common.items.ISchoolItem;
import alexthw.ars_elemental.util.ParticleUtil.ParticleBuilder;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

import java.util.List;

public class SpellFocusRenderer implements ICurioRenderer {

    ParticleColor fireColor = new ParticleColor(250, 15, 15);
    ParticleColor waterColor = new ParticleColor(50, 50, 250);
    ParticleColor earthColor = new ParticleColor(50, 250, 55);
    ParticleColor airColor = new ParticleColor(255,255,26);
    ParticleColor soulColor = new ParticleColor(250,250,250);


    public ParticleColor schoolToColor(SpellSchool school){
        return switch (school.getId()){

            case "fire" -> fireColor;

            case "water" -> waterColor;

            case "earth" -> earthColor;

            case "air" -> airColor;

            case "necromancy" -> soulColor;

            default -> ParticleUtil.defaultParticleColor();
        };
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

        if (ConfigHandler.Client.EnableSFRendering.get() && stack.getItem() instanceof ISchoolItem focus && slotContext.entity() instanceof Player player){

            if (player.getUUID().equals(ArsElemental.Dev)) {
                specialRender( stack, player,  matrixStack, renderLayerParent, buffers, light, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
            }else {
                new ParticleBuilder(schoolToColor(focus.getSchool()))
                        .scale(0.25F)
                        .alpha(0.4F)
                        .spawn(player.getLevel(),
                                getRelativeAngleX(player, 0.5, 0),
                                player.getEyeY() + 0.2 + 0.05 * Math.sin(ageInTicks/10),
                                getRelativeAngleZ(player, 0.5, 0));
            }
            //TODO render a model

        }

    }

    final List<ParticleColor> colors = List.of(fireColor,airColor,waterColor,earthColor);

    public <T extends LivingEntity, M extends EntityModel<T>> void specialRender(ItemStack stack, Player player, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

        int i = 0;
        for (ParticleColor color : colors){

            double bob = 0.05 * Math.sin(ageInTicks/10);

            new ParticleBuilder(color).scale(1/4F)
                    .alpha(0.5F)
                    .spawn(player.level,
                            getRelativeAngleX(player, i * 0.5, ageInTicks),
                            player.getEyeY() + 0.2 + bob,
                            getRelativeAngleZ(player, i * 0.5, ageInTicks));
            new ParticleBuilder(colors.get( i + 1 - 2*(i % 2))).scale(1/6F)
                    .alpha(0.2F)
                    .setLifetime(10)
                    .spawn(player.level,
                            getRelativeAngleX(player, i * 0.5, ageInTicks),
                            player.getEyeY() + 0.2 + bob,
                            getRelativeAngleZ(player, i * 0.5, ageInTicks));
            i++;
        }
    }

    public double getRelativeAngleX(Player player, double rad, float spin){
        return player.getX() + Math.sin(Math.toRadians(player.yBodyRot + rad * 180 + spin))/1.5;
    }

    public double getRelativeAngleZ(Player player, double rad, float spin){
        return player.getZ() - Math.cos(Math.toRadians(player.yBodyRot + rad * 180 + spin))/1.5;
    }


}
