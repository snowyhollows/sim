package pl.edu.icm.trurl.world2d.action.movement;

import net.snowyhollows.bento.annotation.WithFactory;
import pl.edu.icm.trurl.ecs.*;
import pl.edu.icm.trurl.ecs.index.ChunkInfo;
import pl.edu.icm.trurl.ecs.util.Action;
import pl.edu.icm.trurl.world2d.model.collider.Collider;
import pl.edu.icm.trurl.world2d.model.collider.DaoOfColliderFactory;
import pl.edu.icm.trurl.world2d.model.space.*;
import pl.edu.icm.trurl.world2d.service.CollisionService;

public class MovementAction implements Action {
    private final CollisionFilter collisionFilter;
    private final CollisionService collisionService;
    private VelocityDao velocityDao;
    private BoundingBoxDao boundingBoxDao;
    private ComponentToken<Velocity> velocityToken;

    @WithFactory
    public MovementAction(CollisionFilter collisionFilter, CollisionService collisionService, EngineBuilder engineBuilder, EngineBuilder engineBuilder1) {
        this.collisionFilter = collisionFilter;
        this.collisionService = collisionService;

        engineBuilder.addComponentWithDao(BoundingBox.class, DaoOfBoundingBoxFactory.IT);
        engineBuilder.addComponentWithDao(Velocity.class, DaoOfVelocityFactory.IT);
        engineBuilder.addComponentWithDao(Collider.class, DaoOfColliderFactory.IT);
        engineBuilder.addListener(this::initEngine);
    }

    private void initEngine(Engine engine) {
        velocityDao = (VelocityDao) engine.getDaoManager().classToDao(Velocity.class);
        boundingBoxDao = (BoundingBoxDao) engine.getDaoManager().classToDao(BoundingBox.class);
        velocityToken = engine.getDaoManager().classToToken(Velocity.class);
    }

    @Override
    public void init() {
    }

    @Override
    public BoundingBox initPrivateContext(Session session, ChunkInfo chunkInfo) {
        return new BoundingBox();
    }

    @Override
    public void perform(Void unused, Session session, int idx, Object tmpObject) {

        if (!velocityDao.isPresent(idx)) {
            return;
        }

        Velocity ifAvailable = session.getIfAvailable(idx, velocityToken);
        float dx = ifAvailable == null ? velocityDao.getDx(idx) : ifAvailable.getDx();
        float dy = ifAvailable == null ? velocityDao.getDy(idx) : ifAvailable.getDy();

        if (dx == 0 && dy == 0) {
            return;
        }

        Entity movingEntity = session.getEntity(idx);
        BoundingBox box = movingEntity.get(BoundingBox.class);

        if (!collisionFilter.test(session, idx)) {
            box.setCenterY(box.getCenterY() + dx);
            box.setCenterX(box.getCenterX() + dy);
            return;
        }


        Collider collider = movingEntity.get(Collider.class);

        if (collider != null) {
            collider.setDx(dx);
            collider.setDy(dy);
            collider.reset();
        }

        BoundingBox tmp = (BoundingBox) tmpObject;

        if (dx != 0) {
            box.moveX(dx);

            collisionService.find(box, (ix, targetId) -> {
                if (targetId == movingEntity.getId()) return;

                CollisionFilter.CollisionResponse collisionResponse = collisionFilter.testPerTarget(session, idx, targetId);

                if (collider != null) {
                    collider.setDx(dx);
                    collider.setDy(dy);
                    if (collisionResponse.remembers) {
                        collider.horizontalCollisionWith(session.getEntity(targetId));
                    }
                }

                if (collisionResponse.stops) {
                    movingEntity.get(velocityToken).setDx(0);
                    boundingBoxDao.load(session, tmp, targetId);
                    float overlap = box.overlapX(tmp);
                    box.moveX(overlap * -Math.signum(dx));
                }
            });
        }

        if (dy != 0) {
            box.moveY(dy);

            collisionService.find(box, (ix, targetId) -> {

                if (targetId == movingEntity.getId()) return;
                CollisionFilter.CollisionResponse collisionResponse = collisionFilter.testPerTarget(session, idx, targetId);

                if (collider != null) {
                    collider.setDx(dx);
                    collider.setDy(dy);
                    if (collisionResponse.remembers) {
                        collider.verticalCollisionWith(session.getEntity(targetId));
                    }
                }

                if (collisionResponse.stops) {
                    movingEntity.get(velocityToken).setDy(0);
                    boundingBoxDao.load(session, tmp, targetId);
                    float overlap = box.overlapY(tmp);
                    box.moveY(overlap * -Math.signum(dy));
                }
            });
        }
    }

    @Override
    public void perform(Void unused, Session session, int idx) {
        throw new IllegalStateException("xxx");
    }
}
