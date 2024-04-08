package pl.edu.icm.trurl.gdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.IntMap;
import net.snowyhollows.bento.annotation.WithFactory;
import pl.edu.icm.trurl.ecs.EngineBuilder;
import pl.edu.icm.trurl.ecs.Entity;
import pl.edu.icm.trurl.world2d.model.DaoOfTextureComponentFactory;
import pl.edu.icm.trurl.world2d.model.DaoOfTextureFragmentComponentFactory;
import pl.edu.icm.trurl.world2d.model.TextureComponent;
import pl.edu.icm.trurl.world2d.model.TextureFragmentComponent;

import java.util.HashMap;
import java.util.Map;

public class GdxTileTextureLoader  {
    private final IntMap<TextureRegion> textureRegions = new IntMap<>();
    private final Map<String, Texture> textures = new HashMap<>();


    @WithFactory
    public GdxTileTextureLoader(EngineBuilder engineBuilder) {
        engineBuilder.addComponentWithDao(TextureComponent.class, DaoOfTextureComponentFactory.IT);
        engineBuilder.addComponentWithDao(TextureFragmentComponent.class, DaoOfTextureFragmentComponentFactory.IT);

    }

    public TextureRegion getRegion(Entity representation) {
        int id = representation.getId();
        TextureRegion textureRegion = textureRegions.get(id);
        if (textureRegion != null) {
            return textureRegion;
        }

        TextureFragmentComponent textureFragmentComponent = representation.get(TextureFragmentComponent.class);
        String texturePath = textureFragmentComponent.getTexture().get(TextureComponent.class).getTexturePath();

        Texture texture = textures.computeIfAbsent(texturePath, this::loadTexture);
        textureRegion = new TextureRegion(
                texture,
                textureFragmentComponent.getU(),
                textureFragmentComponent.getV(),
                textureFragmentComponent.getWidth(),
                textureFragmentComponent.getHeight()
        );
        textureRegions.put(id, textureRegion);
        return textureRegion;
    }

    private Texture loadTexture(String s) {
        return new Texture(s);
    }
}
