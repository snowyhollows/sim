package pl.edu.icm.trurl.gdx.action;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.snowyhollows.bento.annotation.WithFactory;
import pl.edu.icm.trurl.ecs.EngineBuilder;
import pl.edu.icm.trurl.ecs.Entity;
import pl.edu.icm.trurl.ecs.Session;
import pl.edu.icm.trurl.ecs.util.Action;
import pl.edu.icm.trurl.gdx.GdxTileTextureLoader;
import pl.edu.icm.trurl.gdx.managed.ManagedOrthographicCamera;
import pl.edu.icm.trurl.gdx.managed.ManagedSpriteBatch;
import pl.edu.icm.trurl.world2d.model.*;
import pl.edu.icm.trurl.world2d.service.AnimationResolver;

public class RepresentationAction<T> implements Action<T> {
    private final EngineBuilder engineBuilder;

    @WithFactory
    public RepresentationAction(AnimationResolver animationResolver,
                                 ManagedOrthographicCamera orthographicCamera,
                                 ManagedSpriteBatch spriteBatch,
                                 GdxTileTextureLoader textureLoader,
                                 EngineBuilder engineBuilder) {
        this.animationResolver = animationResolver;
        this.orthographicCamera = orthographicCamera;
        this.spriteBatch = spriteBatch;
        this.textureLoader = textureLoader;

        engineBuilder.addComponentWithDao(BoundingBox.class, DaoOfBoundingBoxFactory.IT);
        engineBuilder.addComponentWithDao(Displayable.class, DaoOfDisplayableFactory.IT);
        this.engineBuilder = engineBuilder;
    }

    private final AnimationResolver animationResolver;
    private final OrthographicCamera orthographicCamera;
    private final SpriteBatch spriteBatch;
    private final GdxTileTextureLoader textureLoader;

    BoundingBoxDao boundingBoxDao;
    DisplayableDao hasRepresentationDao;
    GraphicsTransformDao graphicsTransformDao;


    @Override
    public void init() {
        hasRepresentationDao = (DisplayableDao) engineBuilder.getEngine().getDaoManager().classToDao(Displayable.class);
        boundingBoxDao = (BoundingBoxDao) engineBuilder.getEngine().getDaoManager().classToDao(BoundingBox.class);
        graphicsTransformDao = (GraphicsTransformDao) hasRepresentationDao.getGraphicsTransformDao();
    }

    @Override
    public void perform(T t, Session session, int idx) {
        if (!hasRepresentationDao.isPresent(idx) || !boundingBoxDao.isPresent(idx)) {
            return;
        }

        float centerX = boundingBoxDao.getCenterX(idx);
        float centerY = boundingBoxDao.getCenterY(idx);
        float width = boundingBoxDao.getWidth(idx);
        float height = boundingBoxDao.getHeight(idx);

        if (!orthographicCamera.frustum.boundsInFrustum(centerX, centerY, 0, width / 2, height / 2, 0)) {
            return;
        }

        boolean horizontalFlip = false;
        float rotation = 0;
        float alpha = 1;
        float scale = 1;

        if (graphicsTransformDao.isPresent(idx)) {
            horizontalFlip = graphicsTransformDao.isHorizontalFlip(idx);
            rotation = graphicsTransformDao.getRotation(idx);
            alpha = graphicsTransformDao.getAlpha(idx);
            scale = graphicsTransformDao.getScale(idx);
        }

        int representationId = hasRepresentationDao.getRepresentation(idx);
        Entity baseRepresentation = session.getEntity(representationId);

        Entity representationResolved = animationResolver.resolveRepresentationToAnimationFrame(baseRepresentation);

        TextureRegion textureRegion = textureLoader.getRegion(representationResolved);
        if (horizontalFlip) {
            if (!textureRegion.isFlipX()) {
                textureRegion.flip(true, false);
            }
        } else {
            if (textureRegion.isFlipX()) {
                textureRegion.flip(true, false);
            }
        }
        Color color = spriteBatch.getColor();
        spriteBatch.setColor(color.r, color.g, color.b, alpha);

        float x = centerX - textureRegion.getRegionWidth() / 2;
        float y = centerY - textureRegion.getRegionHeight() / 2;
        x = Math.round(x);
        y = Math.round(y);

        spriteBatch.draw(textureRegion,
                x,
                y,
                textureRegion.getRegionWidth() / 2,
                textureRegion.getRegionHeight() / 2,
                textureRegion.getRegionWidth(),
                textureRegion.getRegionHeight(),
                scale,
                scale,
                rotation + 90,
                true);
    }
}
