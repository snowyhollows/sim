package pl.edu.icm.trurl.world2d.action.movement;

import net.snowyhollows.bento.annotation.ImplementationSwitch;

@ImplementationSwitch(configKey = "trurl.world2d.movement.collisionFilter", cases = {
        @ImplementationSwitch.When(name = "everything", implementation = DefaultFilter.class, useByDefault = true)
})
public interface CollisionFilter {
    CollisionType testPerTarget(int movingId, int targetId);
    boolean test(int movingId);

    enum CollisionType {
        HARD(true, true),
        SOFT(false, true),
        SIMPLE(true, false),
        NONE(false, false);

        public final boolean stops;
        public final boolean remembers;

        CollisionType(boolean stops, boolean remembers) {
            this.stops = stops;
            this.remembers = remembers;
        }
    }
}
