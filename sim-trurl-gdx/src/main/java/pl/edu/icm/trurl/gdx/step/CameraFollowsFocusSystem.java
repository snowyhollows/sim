package pl.edu.icm.trurl.gdx.step;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.snowyhollows.bento.annotation.WithFactory;
import pl.edu.icm.trurl.ecs.EngineBuilder;
import pl.edu.icm.trurl.ecs.Step;
import pl.edu.icm.trurl.ecs.SessionFactory;
import pl.edu.icm.trurl.ecs.util.Indexes;
import pl.edu.icm.trurl.ecs.util.ActionService;
import pl.edu.icm.trurl.ecs.util.IteratingStepBuilder;
import pl.edu.icm.trurl.gdx.managed.ManagedOrthographicCamera;
import pl.edu.icm.trurl.gdx.managed.ManagedSpriteBatch;
import pl.edu.icm.trurl.world2d.model.*;

public class CameraFollowsFocusSystem implements Step {
    private final Indexes indices;
    private final ActionService visitorService;
    private final OrthographicCamera orthographicCamera;
    private final SpriteBatch spriteBatch;
    private final Step step;

    @WithFactory
    public CameraFollowsFocusSystem(EngineBuilder engineBuilder,
                                    Indexes indices,
                                    ActionService visitorService,
                                    ManagedOrthographicCamera orthographicCamera,
                                    ManagedSpriteBatch spriteBatch) {
        this.indices = indices;
        this.visitorService = visitorService;
        this.orthographicCamera = orthographicCamera;
        this.spriteBatch = spriteBatch;

        engineBuilder.addComponentWithDao(Focusable.class, DaoOfFocusableFactory.IT);
        engineBuilder.addComponentWithDao(BoundingBox.class, DaoOfBoundingBoxFactory.IT);
        engineBuilder.addComponentWithDao(Named.class, DaoOfNamedFactory.IT);

        step = IteratingStepBuilder.iteratingOver(indices.allWithComponents(Focusable.class))
                .persisting(Focusable.class)
                .withoutContext()
                .perform(visitorService.withComponents(Focusable.class, BoundingBox.class, Named.class, (e, focusable, boundingBox, named) -> {
                    if (focusable.getFocus().focused()) {
                        orthographicCamera.position.x = boundingBox.getCenterX();
                        orthographicCamera.position.y = boundingBox.getCenterY();
                        if (focusable.getFocus() == Focusable.Focus.ON) {
                            orthographicCamera.viewportWidth = boundingBox.getWidth();
                            orthographicCamera.viewportHeight = boundingBox.getHeight();
                        }
                        orthographicCamera.update();
                        spriteBatch.setProjectionMatrix(orthographicCamera.combined);
                    }
                    focusable.setFocus(Focusable.Focus.OFF);
                }))
                .build();
    }

    @Override
    public void execute(SessionFactory sessionFactory) {
        step.execute(sessionFactory);
    }
}
