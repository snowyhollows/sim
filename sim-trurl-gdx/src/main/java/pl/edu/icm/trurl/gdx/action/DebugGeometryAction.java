package pl.edu.icm.trurl.gdx.action;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import net.snowyhollows.bento.annotation.WithFactory;
import pl.edu.icm.trurl.ecs.Engine;
import pl.edu.icm.trurl.ecs.EngineBuilder;
import pl.edu.icm.trurl.ecs.Session;
import pl.edu.icm.trurl.ecs.index.ChunkInfo;
import pl.edu.icm.trurl.ecs.util.Action;
import pl.edu.icm.trurl.gdx.managed.ManagedOrthographicCamera;
import pl.edu.icm.trurl.gdx.managed.ManagedShapeRenderer;
import pl.edu.icm.trurl.world2d.model.space.BoundingBox;
import pl.edu.icm.trurl.world2d.model.space.BoundingBoxDao;

public class DebugGeometryAction implements Action {

    private final ShapeRenderer shapeRenderer;
    private final Camera camera;
    private BoundingBoxDao boundingBoxDao;

    @WithFactory
    public DebugGeometryAction(EngineBuilder engineBuilder, ManagedShapeRenderer shapeRenderer, ManagedOrthographicCamera camera) {
        this.shapeRenderer = shapeRenderer;
        this.camera = camera;
        engineBuilder.addListener(this::initEngine);
    }

    private void initEngine(Engine engine) {
        boundingBoxDao = (BoundingBoxDao) engine.getDaoManager().classToDao(BoundingBox.class);
    }

    @Override
    public ShapeRenderer initPrivateContext(Session session, ChunkInfo chunkInfo) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setProjectionMatrix(camera.combined);
        return shapeRenderer;
    }

    @Override
    public void perform(Void unused, Session session, int idx) {
        if (!boundingBoxDao.isPresent(idx)) {
            return;
        }

        float x = boundingBoxDao.getCenterX(idx);
        float y = boundingBoxDao.getCenterY(idx);
        float w = boundingBoxDao.getWidth(idx);
        float h = boundingBoxDao.getHeight(idx);

        shapeRenderer.rect(x - w / 2, y - h / 2, w, h);
    }

    @Override
    public void closePrivateContext(Object context) {
        shapeRenderer.end();
    }
}
