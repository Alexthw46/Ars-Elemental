package alexthw.ars_elemental.client;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.api.item.ISchoolFocus;
import alexthw.ars_elemental.util.ParticleUtil;
import alexthw.ars_elemental.util.ParticleUtil.ParticleBuilder;
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

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

        if (stack.getItem() instanceof ISchoolFocus focus && slotContext.entity() instanceof Player player) {

            if (player.getUUID().equals(ArsElemental.Dev)) {
                specialRender(player, ageInTicks);
            } else {
                double XRot = getRelativeAngleX(player, 0.5, 0);
                double ZRot = getRelativeAngleZ(player, 0.5, 0);
                new ParticleBuilder(ParticleUtil.schoolToColor(focus.getSchool().getId()))
                        .scale(1 / 6F)
                        .alpha(0.4F)
                        .spawn(player.getLevel(),
                                XRot,
                                player.getEyeY() + 0.2 + 0.05 * Math.sin(ageInTicks / 10),
                                ZRot);
                new ParticleBuilder(ParticleUtil.schoolToColor2(focus.getSchool().getId()))
                        .scale(1 / 8F)
                        .alpha(0.2F)
                        .setLifetime(10)
                        .spawn(player.getLevel(),
                                XRot,
                                player.getEyeY() + 0.2 + 0.05 * Math.sin(ageInTicks / 10),
                                ZRot);
            }
            //TODO render a model

            /*
            matrixStack.pushPose();

            matrixStack.scale(0.5F,0.5F,0.5F);
            matrixStack.translate(0.45, -player.getEyeHeight() + 1, 0);
            matrixStack.translate(-Math.sin(Math.toRadians(270)),-0.05 * Math.sin(ageInTicks / 10), Math.cos(Math.toRadians(270)));
            //matrixStack.scale(0.75F,0.75F,0.75F);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(ageInTicks * 2 + partialTicks));
            Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.HEAD, light, OverlayTexture.NO_OVERLAY, matrixStack, buffers, 0);

            matrixStack.popPose();
            */

        }

    }

    final List<String> schools = List.of("fire","air","water","earth");

    public void specialRender(Player player, float ageInTicks) {

        int i = 0;
        for (String school : schools){

            double bob = 0.05 * Math.sin(ageInTicks/10);

            new ParticleBuilder(ParticleUtil.schoolToColor(school)).scale(1 / 6F)
                    .alpha(0.6F)
                    .spawn(player.level,
                            getRelativeAngleX(player, i * 0.5, ageInTicks),
                            player.getEyeY() + 0.2 + bob,
                            getRelativeAngleZ(player, i * 0.5, ageInTicks));
            new ParticleBuilder(ParticleUtil.schoolToColor2(school)).scale(1 / 8F)
                    .alpha(0.4F)
                    .setLifetime(10)
                    .spawn(player.level,
                            getRelativeAngleX(player, i * 0.5, ageInTicks),
                            player.getEyeY() + 0.2 + bob,
                            getRelativeAngleZ(player, i * 0.5, ageInTicks));
            i++;
        }
    }

    public double getRelativeAngleX(Player player, double rad, float spin) {
        return player.getX() + Math.sin(getRadians(player, rad, spin)) / 1.5;
    }

    public double getRelativeAngleZ(Player player, double rad, float spin) {
        return player.getZ() - Math.cos(getRadians(player, rad, spin)) / 1.5;
    }

    private double getRadians(Player player, double rad, float spin) {
        return Math.toRadians(player.yBodyRot + rad * 180 + spin);
    }


}
