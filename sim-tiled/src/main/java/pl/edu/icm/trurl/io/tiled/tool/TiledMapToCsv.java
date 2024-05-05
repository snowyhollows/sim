package pl.edu.icm.trurl.io.tiled.tool;

import net.snowyhollows.bento.Bento;
import pl.edu.icm.trurl.ecs.EngineBuilder;
import pl.edu.icm.trurl.ecs.EngineBuilderFactory;
import pl.edu.icm.trurl.io.store.StoreIO;
import pl.edu.icm.trurl.io.store.StoreIOFactory;
import pl.edu.icm.trurl.io.tiled.TmxService;
import pl.edu.icm.trurl.io.tiled.TmxServiceFactory;
import pl.edu.icm.trurl.world2d.model.*;

import java.io.IOException;

public class TiledMapToCsv {

    public static void process(String inTmxFile, String outCsvFile, Class... componentClasses) {
        process(".", inTmxFile, outCsvFile, componentClasses);
    }

    public static void process(String tmxDir, String inTmxFile, String outCsvFile, Class... componentClasses) {
        Bento root = Bento.createRoot();

        Bento justLoader = root.create();
        justLoader.register("tmxDir", tmxDir);
        TmxService tmxService = justLoader.get(TmxServiceFactory.IT);

        StoreIO storeIO = justLoader.get(StoreIOFactory.IT);
        EngineBuilder engineBuilder = justLoader.get(EngineBuilderFactory.IT);
        engineBuilder.addComponentClasses(componentClasses);
        tmxService.loadTmx(inTmxFile);
        try {
            storeIO.writeStoreToFiles(outCsvFile + ".properties", outCsvFile, engineBuilder.getEngine().getRootStore(), "csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
