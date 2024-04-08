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

    private final Map<TextureComponent, Integer> textures = new HashMap<>();
    private final Map<TextureFragmentComponent, Integer> fragments = new HashMap<>();

    @WithFactory
    public RepresentationEntitiesCreator(EngineBuilder engineBuilder) {
        engineBuilder.addComponentClasses(TextureComponent.class, TextureFragmentComponent.class, AnimationComponent.class, Displayable.class);
    }

    public Entity createOrGetRepresentation(Session session, TileMetadata tileMetadata) {
        if (tileMetadata.getRepresentation() != null) {
            return session.getEntity(tileMetadata.getRepresentation());
        }

        TileMetadata tileByLocalId = tileMetadata.getTilesetMetadata().getTileByLocalId(tileMetadata.getLocalId());
        TextureComponent textureComponent = new TextureComponent();
        textureComponent.setTexturePath(tileByLocalId.getImagePath());

        Entity texture = session.getEntity(textures.computeIfAbsent(textureComponent, textureComponent1 -> session.createEntity(textureComponent1).getId()));
        int cols = tileMetadata.getTilesetMetadata().getColumns();

        TextureFragmentComponent textureFragmentComponent = new TextureFragmentComponent();
        textureFragmentComponent.setTexture(texture);
        textureFragmentComponent.setWidth((short) tileMetadata.getTilesetMetadata().getTileWidth());
        textureFragmentComponent.setHeight((short) tileMetadata.getTilesetMetadata().getTileHeight());
        textureFragmentComponent.setU((short) (tileMetadata.getLocalId() % cols * tileMetadata.getTilesetMetadata().getTileWidth()));
        textureFragmentComponent.setV((short) (tileMetadata.getLocalId() / cols * tileMetadata.getTilesetMetadata().getTileHeight()));

        Integer representation = fragments.computeIfAbsent(textureFragmentComponent, tfc -> session.createEntity(tfc).getId());
        tileMetadata.setRepresentation(representation);
        return session.getEntity(representation);
    }
}
