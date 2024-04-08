package pl.edu.icm.trurl.world2d.service;

import net.snowyhollows.bento.annotation.WithFactory;
import pl.edu.icm.trurl.ecs.EngineBuilder;
import pl.edu.icm.trurl.ecs.Entity;
import pl.edu.icm.trurl.world2d.model.*;

import java.util.concurrent.ConcurrentHashMap;

public class NamesAndTypesService {
    private TypedDao typedDao;
    private NamedDao namedDao;
    private ConcurrentHashMap<String, Integer> nameds = new ConcurrentHashMap<>();

    private EngineBuilder engineBuilder;

    @WithFactory
    public NamesAndTypesService(EngineBuilder engineBuilder) {
        this.engineBuilder = engineBuilder;
        this.engineBuilder.addComponentWithDao(Named.class, DaoOfNamedFactory.IT);
        this.engineBuilder.addComponentWithDao(Typed.class, DaoOfTypedFactory.IT);
        this.engineBuilder.addListener(engine -> {
            namedDao = (NamedDao) engineBuilder.getEngine().getDaoManager().classToDao(Named.class);
            typedDao = (TypedDao) engineBuilder.getEngine().getDaoManager().classToDao(Typed.class);
        });
    }

    public boolean hasName(Entity entity, String name) {
        return hasName(entity.getId(), name);
    }

    private boolean hasName(int id, String name) {
        return name.equals(namedDao.getName(id));
    }

    public boolean hasType(Entity entity, String type) {
        return hasType(entity.getId(), type);
    }

    private boolean hasType(int id, String type) {
        return type.equals(typedDao.getType(id));
    }

    public int getId(String name) {
        if (!nameds.containsKey(name)) {
            for (int i = 0; i < engineBuilder.getEngine().getCount(); i++) {
                if (namedDao.isPresent(i)) {
                    String newName = namedDao.getName(i);
                    if (!nameds.containsKey(name)) {
                        nameds.put(newName, i);
                    }
                }
            }
        }

        Integer result = nameds.get(name);
        return result == null ? Integer.MIN_VALUE : result;
    }


}
