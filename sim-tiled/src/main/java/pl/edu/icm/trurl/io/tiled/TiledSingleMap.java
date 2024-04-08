package pl.edu.icm.trurl.io.tiled;

import pl.edu.icm.trurl.io.tiled.model.TileMetadata;
import pl.edu.icm.trurl.io.tiled.model.TilesetMetadata;
import pl.edu.icm.trurl.store.Store;
import pl.edu.icm.trurl.store.attribute.Attribute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class TiledSingleMap {

    private final List<TilesetWithFirstGid> tilesets = new ArrayList<>();
    private final Map<String, String> objectsToEntities = new HashMap<>();
    private final List<Consumer<Map<String, String>>> entityReferenceWriters = new ArrayList<>();
    private final TiledCartographer tiledCartographer;

    public TiledSingleMap(TiledCartographer tiledCartographer) {
        this.tiledCartographer = tiledCartographer;
    }

    public void addTileset(int firstGid, String source) {
        tilesets.add(new TilesetWithFirstGid(tiledCartographer.get(source), firstGid));
    }

    public void addTile(int x, int y, int gid) {
        TileMetadata tile = getTile(gid);
        if (tile == null) {
            return;
        }
        Map<String, String> data = new HashMap<>();
        data.putAll(tile.getProperties());
        if (tile.getType() != null) {
            data.put("type", tile.getType());
        }
        data.put("x", Integer.toString(x));
        data.put("y", Integer.toString(y));
        data.put("width", Integer.toString(tile.getTileWidth()));
        data.put("height", Integer.toString(tile.getTileHeight()));
        data.put("centerX", Integer.toString(x + tile.getTileWidth() / 2));
        data.put("centerY", Integer.toString(y + tile.getTileHeight() / 2));
        data.put("representation", Integer.toString(tile.getRepresentation()));
//        data.put("representation", Integer.toString(tile.getRepresentation().getId(), 36));
        saveToStore(data, Collections.emptySet());
    }

    private int saveToStore(Map<String, String> data, Set<String> entityAttributes) {
        Store store = tiledCartographer.getStore();
        int entityId = store.getCounter().next();
        for (Map.Entry<String, String> stringStringEntry : data.entrySet()) {
            String key = stringStringEntry.getKey();
            String value = stringStringEntry.getValue();
            Attribute attribute = store.get(key);
            if (attribute != null) {
                if (!entityAttributes.contains(key)) {
                    attribute.setString(entityId, value);
                } else {
                    entityReferenceWriters.add(mappings -> {
                        attribute.setString(entityId, mappings.get(value));
                    });
                }
            }

        }
        return entityId;
    }

    public void addObject(int x, int y, String id, int gid, int width, int height, Shape shape, Map<String, String> properties, Set<String> objectProperties) {
        TileMetadata tile = getTile(gid);
        Map<String, String> data = new HashMap<>();
        if (tile != null) {
            data.putAll(tile.getProperties());
        }
        data.putAll(properties);
        if (tile != null) {
//            data.put("representation", Integer.toString(tile.getRepresentation().getId(), 36));
            data.put("representation", Integer.toString(tile.getRepresentation()));
            if (tile.getType() != null) {
                data.put("type", tile.getType());
            }
        }
        data.put("x", Integer.toString(x));
        data.put("y", Integer.toString(y));
        data.put("width", Integer.toString(width - 1));
        data.put("height", Integer.toString(height - 1));
        data.put("shape", shape.toString());
        data.put("centerX", Integer.toString(x + width / 2));
        data.put("centerY", Integer.toString(y + height / 2));
        int entityId = saveToStore(data, objectProperties);
        objectsToEntities.put(id, Integer.toString(entityId));
    }

    public void connectEntities() {
        for (Consumer<Map<String, String>> entityReferenceWriter : entityReferenceWriters) {
            entityReferenceWriter.accept(objectsToEntities);
        }
    }

    public enum Shape {
        TILE, RECTANGLE, ELLIPSE, POLYGON
    }

    private TileMetadata getTile(int gid) {
        for (TilesetWithFirstGid tileset : tilesets) {
            if (tileset.firstGid <= gid && gid < tileset.firstGid + tileset.tileset.getTileCount()) {
                return tileset.tileset.getTileByLocalId(gid - tileset.firstGid);
            }
        }
        return null;
    }

    private class TilesetWithFirstGid {
        public final TilesetMetadata tileset;
        public final int firstGid;

        public TilesetWithFirstGid(TilesetMetadata tileset, int firstGid) {
            this.tileset = tileset;
            this.firstGid = firstGid;
        }
    }
}
