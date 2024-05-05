package pl.edu.icm.trurl.world2d.action.movement;

import net.snowyhollows.bento.annotation.WithFactory;
import pl.edu.icm.trurl.ecs.EngineBuilder;
import pl.edu.icm.trurl.world2d.model.space.Velocity;
import pl.edu.icm.trurl.world2d.model.space.VelocityDao;

public class DefaultFilter implements CollisionFilter {
    private VelocityDao velocityDao;

    @WithFactory
    public DefaultFilter(EngineBuilder engineBuilder) {
        engineBuilder.addListener(e -> {
            velocityDao = (VelocityDao) e.getDaoManager().classToDao(Velocity.class);
        });
    }

    @Override
    public CollisionType testPerTarget(int movingId, int targetId) {
        return CollisionType.HARD;
    }

    @Override
    public boolean test(int movingId) {
        return true;
    }
}
