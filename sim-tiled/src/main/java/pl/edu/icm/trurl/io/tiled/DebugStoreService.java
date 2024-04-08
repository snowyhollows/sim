package pl.edu.icm.trurl.io.tiled;

import net.snowyhollows.bento.annotation.WithFactory;
import pl.edu.icm.trurl.ecs.EngineBuilder;
import pl.edu.icm.trurl.store.Store;
import pl.edu.icm.trurl.store.attribute.Attribute;

import java.util.List;

public class DebugStoreService {

    private final EngineBuilder engineBuilder;

    @WithFactory
    public DebugStoreService(EngineBuilder engineBuilder) {
        this.engineBuilder = engineBuilder;
    }

    public void systemOutPrintStore() {
        Store componentStore = engineBuilder.getEngine().getRootStore();
        List<Attribute> attributes = componentStore.getAllAttributes();
        for (Attribute attribute : attributes) {



            System.out.print(attribute.name() + ",");
        }
        System.out.println();
        for (int i = 0; i < componentStore.getCounter().getCount(); i++) {
            for (Attribute attribute : attributes) {
                System.out.print((!attribute.isEmpty(i) ? attribute.getString(i) : '-') + ",");
            }
            System.out.println();
        }
    }

}
