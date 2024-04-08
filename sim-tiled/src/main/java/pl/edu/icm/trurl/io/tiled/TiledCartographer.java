package pl.edu.icm.trurl.io.tiled;

import net.snowyhollows.bento.annotation.WithFactory;
import pl.edu.icm.trurl.ecs.EngineBuilder;
import pl.edu.icm.trurl.io.tiled.model.TilesetMetadata;
import pl.edu.icm.trurl.store.Store;


public class TiledCartographer {

    private final TsxService tsxService;
    private final EngineBuilder engineBuilder;

    @WithFactory
    public TiledCartographer(TsxService tsxService, EngineBuilder engineBuilder) {
        this.tsxService = tsxService;
        this.engineBuilder = engineBuilder;
    }

    public TiledSingleMap createMapBuilder() {
        return new TiledSingleMap(this);
    }

    public TilesetMetadata get(String path) {
        return tsxService.getTileset(path);
    }

    public Store getStore() {
        return engineBuilder.getEngine().getRootStore();
    }
}
