package pl.edu.icm.trurl.gdx.action;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.snowyhollows.bento.annotation.WithFactory;
import pl.edu.icm.trurl.ecs.Engine;
import pl.edu.icm.trurl.ecs.EngineBuilder;
import pl.edu.icm.trurl.ecs.Entity;
import pl.edu.icm.trurl.ecs.Session;
import pl.edu.icm.trurl.ecs.index.ChunkInfo;
import pl.edu.icm.trurl.ecs.util.Action;
import pl.edu.icm.trurl.gdx.GdxTileTextureLoader;
import pl.edu.icm.trurl.gdx.managed.ManagedOrthographicCamera;
import pl.edu.icm.trurl.gdx.managed.ManagedSpriteBatch;
import pl.edu.icm.trurl.world2d.model.display.*;
import pl.edu.icm.trurl.world2d.model.space.BoundingBox;
import pl.edu.icm.trurl.world2d.model.space.BoundingBoxDao;
import pl.edu.icm.trurl.world2d.model.space.DaoOfBoundingBoxFactory;
import pl.edu.icm.trurl.world2d.service.AnimationResolver;

public class RepresentationAction implements Action {
    private final EngineBuilder engineBuilder;
    private final AnimationResolver animationResolver;
    private final OrthographicCamera orthographicCamera;
    private final SpriteBatch spriteBatch;
    private final GdxTileTextureLoader textureLoader;
    private final DisplayListener displayListener;

    BoundingBoxDao boundingBoxDao;
    DisplayableDao displayableDao;
    GraphicsTransformDao graphicsTransformDao;

    @Override
    public SpriteBatch initPrivateContext(Session session, ChunkInfo chunkInfo) {
        spriteBatch.setProjectionMatrix(orthographicCamera.combined);
        spriteBatch.begin();
        return spriteBatch;
    }

    @Override
    public void closePrivateContext(Object context) {
        spriteBatch.end();
    }

    @WithFactory
    public RepresentationAction(AnimationResolver animationResolver,
                                ManagedOrthographicCamera orthographicCamera,

                                ManagedSpriteBatch spriteBatch,
                                GdxTileTextureLoader textureLoader,
                                EngineBuilder engineBuilder, DisplayListener displayListener) {
        this.animationResolver = animationResolver;
        this.orthographicCamera = orthographicCamera;
        this.spriteBatch = spriteBatch;
        this.textureLoader = textureLoader;
        this.displayListener = displayListener;

        engineBuilder.addComponentWithDao(BoundingBox.class, DaoOfBoundingBoxFactory.IT);
        engineBuilder.addComponentWithDao(Displayable.class, DaoOfDisplayableFactory.IT);
        this.engineBuilder = engineBuilder;
        this.engineBuilder.addListener(this::initEngine);
    }

    private void initEngine(Engine engine) {
        displayableDao = (DisplayableDao) engine.getDaoManager().classToDao(Displayable.class);
        boundingBoxDao = (BoundingBoxDao) engine.getDaoManager().classToDao(BoundingBox.class);
        graphicsTransformDao = (GraphicsTransformDao) displayableDao.getGraphicsTransformDao();
    }

    @Override
    public void perform(Void t, Session session, int idx) {
        if (!displayableDao.isPresent(idx) || Float.isNaN(boundingBoxDao.getCenterX(idx))) {
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
        float offset = 0;

        TextureRegion textureRegion = null;
        if (graphicsTransformDao.isPresent(idx)) {
            horizontalFlip = GraphicsTransform.isHorizontalFlip(graphicsTransformDao.getTransformMask(idx));
            rotation = graphicsTransformDao.getRotation(idx);
            alpha = graphicsTransformDao.getAlpha(idx);
            scale = graphicsTransformDao.getScale(idx);
            offset = graphicsTransformDao.getAnimationOffset(idx);
        }

        int representationId = displayableDao.getRepresentation(idx);
        Entity baseRepresentation = session.getEntity(representationId);

        Entity representationResolved = animationResolver.resolveRepresentationToAnimationFrame(baseRepresentation, offset);

        textureRegion = textureLoader.getRegion(representationResolved);
        float drawWidth = textureRegion.getRegionWidth();
        float drawHeight = textureRegion.getRegionHeight();
        if (horizontalFlip) {
            if (!textureRegion.isFlipX()) {
                textureRegion.flip(true, false);
            }
        } else {
            if (textureRegion.isFlipX()) {
                textureRegion.flip(true, false);
            }
        }

        if (scale < 0) {
            drawWidth = width * -scale;
            drawHeight = height * -scale;
            scale = -scale;
        }

        Color color = spriteBatch.getColor();
        spriteBatch.enableBlending();
        spriteBatch.setColor(color.r, color.g, color.b, alpha);

        float x = centerX - drawHeight / 2;
        float y = centerY - drawWidth / 2;
        x = Math.round(x);
        y = Math.round(y);

        displayListener.onRepresentationDrawn(idx, centerX, centerY, drawWidth, drawHeight, rotation, scale, alpha);
        if (textureRegion != null) {
            spriteBatch.draw(textureRegion, x, y, drawHeight / 2, drawWidth / 2, drawHeight, drawWidth, scale, scale, 90 + rotation, true);
        }
    }
}
