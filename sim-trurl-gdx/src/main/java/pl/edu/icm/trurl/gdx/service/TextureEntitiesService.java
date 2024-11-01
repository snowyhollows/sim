package pl.edu.icm.trurl.gdx.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import net.snowyhollows.bento.annotation.WithFactory;
import pl.edu.icm.trurl.ecs.EngineBuilder;
import pl.edu.icm.trurl.ecs.Entity;
import pl.edu.icm.trurl.ecs.Session;
import pl.edu.icm.trurl.world2d.model.DaoOfNamedFactory;
import pl.edu.icm.trurl.world2d.model.Named;
import pl.edu.icm.trurl.world2d.model.display.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class TextureEntitiesService {

    private final EngineBuilder engineBuilder;

    @WithFactory
    public TextureEntitiesService(EngineBuilder engineBuilder) {
        engineBuilder.addComponentWithDao(TextureComponent.class, DaoOfTextureComponentFactory.IT);
        engineBuilder.addComponentWithDao(Named.class, DaoOfNamedFactory.IT);
        engineBuilder.addComponentWithDao(AnimationComponent.class, DaoOfAnimationComponentFactory.IT);
        engineBuilder.addComponentWithDao(TextureRegionComponent.class, DaoOfTextureRegionComponentFactory.IT);
        this.engineBuilder = engineBuilder;
    }

    private Session session() {
        AtomicReference<Session> sessionRef = new AtomicReference<>();
        engineBuilder.getEngine().execute(sf -> sessionRef.set(sf.createOrGet()));
        return sessionRef.get();
    }

    public Entity createTextureEntity(String path) {
        return createTextureEntity(path, path);
    }

    public Entity createTextureEntity(String path, String name) {
        Entity entity = session().createEntity();
        entity.getOrCreate(TextureComponent.class).setTexturePath(path);
        entity.getOrCreate(Named.class).setName(path);
        return entity;
    }

    public void createTexturesFromAtlas(String atlasPath) {
        FileHandle file = Gdx.files.internal(atlasPath);

        TextureAtlas.TextureAtlasData data = new TextureAtlas.TextureAtlasData(file, file.parent(), false);
        Map<String, Entity> textures = new HashMap<>();
        Map<String, List<TextureAtlas.TextureAtlasData.Region>> frames = new HashMap<>();

        data.getPages().forEach(page -> {
            String path = page.textureFile.path();
            Entity textureEntity = createTextureEntity(path);
            textures.put(path, textureEntity);
        });

        data.getRegions().forEach(region -> {
            frames.computeIfAbsent(region.name, k -> new ArrayList<>()).add(region);
        });

        frames.forEach((name, regions) -> {
            if (regions.size() == 1) {
                TextureAtlas.TextureAtlasData.Region region = regions.get(0);
                createTextureRegionEntity(name, textures, region);
            } else {
                regions.sort(Comparator.comparing(r -> r.index));
                Entity animationEntity = session().createEntity();
                AnimationComponent animationComponent = animationEntity.getOrCreate(AnimationComponent.class);
                animationEntity.getOrCreate(Named.class).setName(name);

                regions.forEach(region -> {
                    Entity entity = createTextureRegionEntity(name + "_" + region.index, textures, region);
                    animationComponent.getFrames().add(AnimationFrame.of(entity, 0.2f));
                });
            }
        });
        session().flush(session().getEngine().getDaoManager().classToToken(Named.class));
    }

    private Entity createTextureRegionEntity(String name, Map<String, Entity> textures, TextureAtlas.TextureAtlasData.Region region) {
        Entity entity = session().createEntity();
        TextureRegionComponent textureRegionComponent = entity.getOrCreate(TextureRegionComponent.class);
        textureRegionComponent.setTexture(textures.get(region.page.textureFile.path()));
        textureRegionComponent.setX((short) region.left);
        textureRegionComponent.setY((short) region.top);
        textureRegionComponent.setWidth((short) region.width);
        textureRegionComponent.setHeight((short) region.height);
        entity.getOrCreate(Named.class).setName(name);
        return entity;
    }

    // TODO:
    //  - create textureRegions
    //  - create entities from atlases
    //  - create animations (frames) from atlases (or from slices as well...?)
    //  - create TextureRegion entities from slices (prefixes)
}
