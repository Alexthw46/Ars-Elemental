package alexthw.ars_elemental.client.mages;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Mob;

public class MageModel<M extends Mob> extends HumanoidModel<M> {

    public MageModel(ModelPart pRoot) {
        super(pRoot, RenderType::entityTranslucent);
    }

}
