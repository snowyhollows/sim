package pl.edu.icm.trurl.world2d.action.movement;

import net.snowyhollows.bento.annotation.WithFactory;
import pl.edu.icm.trurl.ecs.Entity;

public class EverythingObstructsFilter implements CollisionFilter {

    @WithFactory
    public EverythingObstructsFilter() {
    }

    @Override
    public boolean test(Entity a, Entity b) {
        return true;
    }

    @Override
    public boolean collisionPossible(Entity a) {
        return true;
    }
}
