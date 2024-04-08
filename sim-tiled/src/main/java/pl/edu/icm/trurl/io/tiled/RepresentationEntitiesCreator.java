package pl.edu.icm.trurl.io.tiled;

import net.snowyhollows.bento.annotation.WithFactory;
import pl.edu.icm.trurl.ecs.EngineBuilder;
import pl.edu.icm.trurl.ecs.Entity;
import pl.edu.icm.trurl.ecs.Session;
import pl.edu.icm.trurl.io.tiled.model.TileMetadata;
import pl.edu.icm.trurl.world2d.model.AnimationComponent;
import pl.edu.icm.trurl.world2d.model.Displayable;
import pl.edu.icm.trurl.world2d.model.TextureComponent;
import pl.edu.icm.trurl.world2d.model.TextureFragmentComponent;

import java.util.HashMap;
import java.util.Map;

public class RepresentationEntitiesCreator {

    private final Map<TextureComponent, Entity> textures = new HashMap<>();
    private final Map<TextureFragmentComponent, Entity> fragments = new HashMap<>();

    @WithFactory
    public RepresentationEntitiesCreator(EngineBuilder engineBuilder) {
        engineBuilder.addComponentClasses(TextureComponent.class, TextureFragmentComponent.class, AnimationComponent.class, Displayable.class);
    }

    public Entity createOrGetRepresentation(Session session, TileMetadata tileMetadata) {
        if (tileMetadata.getRepresentation() != null) {
            return tileMetadata.getRepresentation();
        }

        TileMetadata tileByLocalId = tileMetadata.getTilesetMetadata().getTileByLocalId(tileMetadata.getLocalId());
        TextureComponent textureComponent = new TextureComponent();
        textureComponent.setTexturePath(tileByLocalId.getImagePath());

        Entity texture = textures.computeIfAbsent(textureComponent, session::createEntity);
        int cols = tileMetadata.getTilesetMetadata().getColumns();

        TextureFragmentComponent textureFragmentComponent = new TextureFragmentComponent();
        textureFragmentComponent.setTexture(texture);
        textureFragmentComponent.setWidth((short) tileMetadata.getTilesetMetadata().getTileWidth());
        textureFragmentComponent.setHeight((short) tileMetadata.getTilesetMetadata().getTileHeight());
        textureFragmentComponent.setU((short) (tileMetadata.getLocalId() % cols * tileMetadata.getTilesetMetadata().getTileWidth()));
        textureFragmentComponent.setV((short) (tileMetadata.getLocalId() / cols * tileMetadata.getTilesetMetadata().getTileHeight()));

        Entity representation = fragments.computeIfAbsent(textureFragmentComponent, session::createEntity);
        tileMetadata.setRepresentation(representation);
        return representation;
    }
}
