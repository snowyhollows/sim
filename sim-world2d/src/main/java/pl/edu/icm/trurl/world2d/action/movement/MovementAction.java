package pl.edu.icm.trurl.world2d.action.movement;

import net.snowyhollows.bento.annotation.WithFactory;
import pl.edu.icm.trurl.ecs.EngineBuilder;
import pl.edu.icm.trurl.ecs.Entity;
import pl.edu.icm.trurl.ecs.Session;
import pl.edu.icm.trurl.ecs.Steps;
import pl.edu.icm.trurl.world2d.model.*;
import pl.edu.icm.trurl.world2d.service.CollisionService;

public class MovementAction implements Steps.TwoComponentStep<Speed, BoundingBox> {
    private final CollisionFilter collisionFilter;
    private final CollisionService collisionService;

    @WithFactory
    public MovementAction(CollisionFilter collisionFilter, CollisionService collisionService, EngineBuilder engineBuilder) {
        engineBuilder.addComponentWithDao(BoundingBox.class, DaoOfBoundingBoxFactory.IT);
        engineBuilder.addComponentWithDao(Speed.class, DaoOfSpeedFactory.IT);
        engineBuilder.addComponentWithDao(Collider.class, DaoOfColliderFactory.IT);
        engineBuilder.addComponentWithDao(GraphicsTransform.class, DaoOfGraphicsTransformFactory.IT);
        this.collisionFilter = collisionFilter;
        this.collisionService = collisionService;
    }

    @Override
    public void execute(Entity e, Speed speed, BoundingBox box) {
        Session session = e.getSession();

        if (!collisionFilter.collisionPossible(e)) {
            box.setCenterY(box.getCenterY() + speed.getDy());
            box.setCenterX(box.getCenterX() + speed.getDx());
            return;
        };

        Collider collider = e.getOrCreate(Collider.class);

        if (speed.getDx() == 0 && speed.getDy() == 0) {
            return;
        }
        collider.getCollisionsV().clear();
        collider.getCollisionsH().clear();
        box.setCenterX(box.getCenterX() + speed.getDx());
        float initialX = box.getCenterX();
        collisionService.find(box, (idx, targetId) -> {
            if (targetId == e.getId()) return;
            Entity entity = session.getEntity(targetId);
            if (collisionFilter.test(e, entity)) {
                BoundingBox targetBox = entity.get(BoundingBox.class);
                targetBox.resolveBySlide(box, speed.getDx(), 0);
            }
            collider.getCollisionsH().add(entity);
        });
        if (initialX != box.getCenterX()) {
            speed.setDx(0);
        }

        box.setCenterY(box.getCenterY() + speed.getDy());
        float initialY = box.getCenterY();
        collisionService.find(box, (idx, targetId) -> {
            if (targetId == e.getId()) return;
            Entity entity = session.getEntity(targetId);
            if (collisionFilter.test(e, entity)) {
                BoundingBox targetBox = entity.get(BoundingBox.class);
                targetBox.resolveBySlide(box, 0, speed.getDy());
            }
            collider.getCollisionsV().add(entity);
        });
        if (initialY != box.getCenterY()) {
            speed.setDy(0);
        }

        GraphicsTransform transform = e.get(GraphicsTransform.class);

        if (transform != null && speed.getDx() != 0) {
            transform.setHorizontalFlip(speed.getDx() < 0);
        }
    }
}
