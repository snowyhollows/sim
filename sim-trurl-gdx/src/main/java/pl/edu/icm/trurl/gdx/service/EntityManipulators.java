package pl.edu.icm.trurl.gdx.service;

import net.snowyhollows.bento.annotation.WithFactory;
import pl.edu.icm.trurl.ecs.Entity;
import pl.edu.icm.trurl.ecs.Session;
import pl.edu.icm.trurl.world2d.model.BoundingBox;
import pl.edu.icm.trurl.world2d.model.Displayable;
import pl.edu.icm.trurl.world2d.model.GraphicsTransform;
import pl.edu.icm.trurl.world2d.model.Named;
import pl.edu.icm.trurl.world2d.service.NamesAndTypesService;

public class EntityManipulators {
    private final NamesAndTypesService namesAndTypesService;

    @WithFactory
    public EntityManipulators(NamesAndTypesService namesAndTypesService) {
        this.namesAndTypesService = namesAndTypesService;
    }

    public EntityManipulator entity(Session session) {
        return new EntityManipulator(session.createEntity());
    }

    public EntityManipulator entity(Entity entity) {
        return new EntityManipulator(entity);
    }

    public final class EntityManipulator {
        private final Entity entity;

        private EntityManipulator(Entity entity) {
            this.entity = entity;
        }

        public EntityManipulator named(String name) {
            entity.getOrCreate(Named.class).setName(name);
            return this;
        }

        public EntityManipulator ofType(String name) {
            entity.getOrCreate(Named.class).setName(name);
            return this;
        }

        public EntityManipulator moveTo(float x, float y) {
            BoundingBox bb = entity.getOrCreate(BoundingBox.class);
            bb.setCenterX(x);
            bb.setCenterY(y);
            return this;
        }

        public EntityManipulator moveBy(float dx, float dy) {
            BoundingBox bb = entity.getOrCreate(BoundingBox.class);
            bb.setCenterX(bb.getCenterY() + dx);
            bb.setCenterY(bb.getCenterY() + dy);
            return this;
        }

        public EntityManipulator withSize(float width, float height) {
            BoundingBox bb = entity.getOrCreate(BoundingBox.class);
            bb.setWidth(width);
            bb.setHeight(height);
            return this;
        }

        public EntityManipulator displayedAs(String representationName) {
            Entity representation = entity.getSession().getEntity(namesAndTypesService.getId(representationName));
            return displayedAs(representation);
        }

        public EntityManipulator displayedAs(Entity representation){
            Displayable displayable = entity.getOrCreate(Displayable.class);
            displayable.setRepresentation(representation);
            return this;
        }

        public EntityManipulator copyPositionFrom(String name) {
            Entity entity = this.entity.getSession().getEntity(namesAndTypesService.getId(name));
            return copyPositionFrom(entity);
        }

        public EntityManipulator copyPositionFrom(Entity entity) {
            BoundingBox bb = entity.get(BoundingBox.class);
            if (bb != null) {
                BoundingBox bb2 = this.entity.getOrCreate(BoundingBox.class);
                bb2.setCenterX(bb.getCenterX());
                bb2.setCenterY(bb.getCenterY());
                bb2.setWidth(bb.getWidth());
                bb2.setHeight(bb.getHeight());
            }
            return this;
        }

        public EntityManipulator copyRepresentationFrom(String name) {
            Entity entity = this.entity.getSession().getEntity(namesAndTypesService.getId(name));
            return copyRepresentationFrom(entity);
        }

        public EntityManipulator copyRepresentationFrom(Entity entity) {
            Displayable displayable = entity.get(Displayable.class);
            if (displayable != null) {
                Displayable displayable2 = this.entity.getOrCreate(Displayable.class);
                displayable2.setRepresentation(displayable.getRepresentation());
            }
            return this;
        }

        public EntityManipulator copyTransformFrom(String name) {
            Entity entity = this.entity.getSession().getEntity(namesAndTypesService.getId(name));
            return copyTransformFrom(entity);
        }

        public EntityManipulator copyTransformFrom(Entity entity) {
            GraphicsTransform graphicsTransform = entity.get(GraphicsTransform.class);
            if (graphicsTransform != null) {
                GraphicsTransform graphicsTransform2 = this.entity.getOrCreate(GraphicsTransform.class);
                graphicsTransform2.setAlpha(graphicsTransform.getAlpha());
                graphicsTransform2.setScale(graphicsTransform.getScale());
                graphicsTransform2.setRotation(graphicsTransform.getRotation());
                graphicsTransform2.setHorizontalFlip(graphicsTransform.isHorizontalFlip());
            }
            return this;
        }

        public EntityManipulator setRotation(float angle) {
            GraphicsTransform graphicsTransform = getOrCreateGraphicsTransform();
            graphicsTransform.setRotation(angle);
            return this;
        }

        public EntityManipulator rotateClockwise(float da) {
            Displayable displayable = entity.getOrCreate(Displayable.class);
            GraphicsTransform graphicsTransform = entity.getOrCreate(GraphicsTransform.class);
            graphicsTransform.setRotation(graphicsTransform.getRotation() + da);
            return this;
        }

        public Entity get() {
            return entity;
        }

        private GraphicsTransform getOrCreateGraphicsTransform() {
            Displayable displayable = entity.getOrCreate(Displayable.class);
            GraphicsTransform graphicsTransform = displayable.getGraphicsTransform();
            if (graphicsTransform == null) {
                graphicsTransform = new GraphicsTransform();
                displayable.setGraphicsTransform(graphicsTransform);
            }
            return graphicsTransform;
        }
    }

}
