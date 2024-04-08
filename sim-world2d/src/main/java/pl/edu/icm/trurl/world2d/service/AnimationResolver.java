package pl.edu.icm.trurl.world2d.service;

import net.snowyhollows.bento.annotation.WithFactory;
import pl.edu.icm.trurl.ecs.EngineBuilder;
import pl.edu.icm.trurl.ecs.Entity;
import pl.edu.icm.trurl.world2d.model.AnimationComponent;
import pl.edu.icm.trurl.world2d.model.AnimationFrame;
import pl.edu.icm.trurl.world2d.model.DaoOfAnimationComponentFactory;

public class AnimationResolver {
    private final GlobalTimer globalTimer;

    @WithFactory
    public AnimationResolver(GlobalTimer globalTimer, EngineBuilder engineBuilder) {
        this.globalTimer = globalTimer;
        engineBuilder.addComponentWithDao(AnimationComponent.class, DaoOfAnimationComponentFactory.IT);
    }

    public Entity resolveRepresentationToAnimationFrame(Entity representation) {
        AnimationComponent animationComponent = representation.get(AnimationComponent.class);

        if (animationComponent == null || animationComponent.getFrames().isEmpty()) {
            return representation;
        }

        float totalDuration = 0;
        for (AnimationFrame frame : animationComponent.getFrames()) {
            totalDuration += frame.getDuration();
        }

        float time = (float) (globalTimer.getTotalTimePassed() % totalDuration);

        for (AnimationFrame frame : animationComponent.getFrames()) {
            time -= frame.getDuration();
            if (time < 0) {
                return frame.getFrame();
            }
        }

        // shouldn't happen
        return animationComponent.getFrames().get(animationComponent.getFrames().size() - 1).getFrame();
    }
}
