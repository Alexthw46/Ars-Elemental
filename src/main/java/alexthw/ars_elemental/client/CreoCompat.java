package alexthw.ars_elemental.client;

import alexthw.ars_elemental.registry.ModEntities;
import com.hollingsworth.ars_creo.client.render.TrainHatMap;
import org.joml.Vector3f;

public class CreoCompat {

    public static void registerHats() {
        TrainHatMap.put(ModEntities.FIRENANDO_ENTITY.get(), "head", new Vector3f(1.2f), new Vector3f(0, 0.45f, 0));
        TrainHatMap.put(ModEntities.SIREN_ENTITY.get(), "head", new Vector3f(0.78f, 0.65f, 0.65f), new Vector3f(0f, 0.335f, 0));
        TrainHatMap.put(ModEntities.FLASHJACK_ENTITY.get(), "head", new Vector3f(0.55f), new Vector3f(0f, 0.4f, 0.05f));
    }

}
