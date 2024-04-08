package pl.edu.icm.trurl.world2d.action.movement;

import net.snowyhollows.bento.annotation.ImplementationSwitch;
import pl.edu.icm.trurl.ecs.Entity;

@ImplementationSwitch(configKey = "trurl.world2d.movement.collisionFilter", cases = {
        @ImplementationSwitch.When(name = "everything", implementation = EverythingObstructsFilter.class, useByDefault = true)
})
public interface CollisionFilter {
    boolean test(Entity a, Entity b);

    boolean collisionPossible(Entity a);
}
