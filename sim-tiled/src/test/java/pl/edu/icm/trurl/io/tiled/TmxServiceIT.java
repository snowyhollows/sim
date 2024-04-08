package pl.edu.icm.trurl.io.tiled;

import net.snowyhollows.bento.Bento;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.icm.trurl.ecs.EngineBuilderFactory;
import pl.edu.icm.trurl.world2d.model.BoundingBox;
import pl.edu.icm.trurl.world2d.model.Named;
import pl.edu.icm.trurl.world2d.model.Typed;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.io.FileReader;

@ExtendWith(MockitoExtension.class)
class TmxServiceIT {
    Bento bento = Bento.createRoot();

    @Test
    void test() throws XMLStreamException, FileNotFoundException {
        bento.get(EngineBuilderFactory.IT).addComponentClasses(BoundingBox.class, Named.class, Typed.class);
        TmxService tmxService = bento.get(TmxServiceFactory.IT);
        tmxService.loadTmx("examples/basictiles.tmx");
        bento.get(DebugStoreServiceFactory.IT).systemOutPrintStore();
    }
}
