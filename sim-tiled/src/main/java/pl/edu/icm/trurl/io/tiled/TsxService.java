package pl.edu.icm.trurl.io.tiled;

import net.snowyhollows.bento.annotation.WithFactory;
import pl.edu.icm.trurl.ecs.EngineBuilder;
import pl.edu.icm.trurl.ecs.Entity;
import pl.edu.icm.trurl.ecs.Session;
import pl.edu.icm.trurl.io.tiled.model.AnimationFrameMetadata;
import pl.edu.icm.trurl.io.tiled.model.TileMetadata;
import pl.edu.icm.trurl.io.tiled.model.TilesetMetadata;
import pl.edu.icm.trurl.io.tiled.parser.TsxParser;
import pl.edu.icm.trurl.world2d.model.AnimationComponent;
import pl.edu.icm.trurl.world2d.model.AnimationFrame;
import pl.edu.icm.trurl.world2d.model.Named;
import pl.edu.icm.trurl.world2d.model.Typed;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class TsxService {
    private final XmlLoader xmlLoader;
    private final RepresentationEntitiesCreator gdxEntitiesCreator;
    private final EngineBuilder engineBuilder;

    private final Map<String, TilesetMetadata> tilesetMetadataMap = new HashMap<>();

    @WithFactory
    public TsxService(XmlLoader xmlLoader, RepresentationEntitiesCreator gdxEntitiesCreator, EngineBuilder engineBuilder) {
        this.xmlLoader = xmlLoader;
        this.gdxEntitiesCreator = gdxEntitiesCreator;
        this.engineBuilder = engineBuilder;
    }

    public TilesetMetadata getTileset(String path) {
        return tilesetMetadataMap.computeIfAbsent(path, this::load);
    }

    public TilesetMetadata load(String path) {
        try {
            TilesetMetadata loaded = new TsxParser(xmlLoader.load(path), path).load();

            engineBuilder.getEngine().execute(sessionFactory -> {
                Session session = sessionFactory.createOrGet();
                for (int i = 0; i < loaded.getTileCount(); i++) {
                    Entity created = gdxEntitiesCreator.createOrGetRepresentation(session, loaded.getTileByLocalId(i));
                    assignTileNameAndType(loaded, i, created);
                }
                for (int i = 0; i < loaded.getTileCount(); i++) {
                    TileMetadata tile = loaded.getTileByLocalId(i);
                    if (tile.getAnimationFrames().size() > 0) {
                        Entity representation = gdxEntitiesCreator.createOrGetRepresentation(session, tile);
                        assignTileNameAndType(loaded, i, representation);
                        AnimationComponent animationComponent = representation.getOrCreate(AnimationComponent.class);

                        for (AnimationFrameMetadata animationFrame : tile.getAnimationFrames()) {
                            Entity target = gdxEntitiesCreator.createOrGetRepresentation(session, loaded.getTileByLocalId(animationFrame.getLocalId()));
                            animationComponent.getFrames().add(AnimationFrame.of(target, animationFrame.getDuration()));
                        }
                    }
                }
                session.close();
            });
            return loaded;
        } catch (XMLStreamException | FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static void assignTileNameAndType(TilesetMetadata loaded, int i, Entity created) {
        String tileName = loaded.getTileByLocalId(i).getProperties().get("tile.name");
        if (tileName != null) {
            created.getOrCreate(Named.class).setName(tileName);
        }
        String tileType = loaded.getTileByLocalId(i).getProperties().get("tile.type");
        if (tileType != null) {
            created.getOrCreate(Typed.class).setType(tileType);
        }
    }

}
