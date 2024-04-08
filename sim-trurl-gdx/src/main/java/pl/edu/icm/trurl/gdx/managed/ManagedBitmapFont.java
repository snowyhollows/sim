package pl.edu.icm.trurl.gdx.managed;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import net.snowyhollows.bento.annotation.ByName;
import net.snowyhollows.bento.annotation.WithFactory;

public class ManagedBitmapFont extends BitmapFont {
    public ManagedBitmapFont() {
    }

    public ManagedBitmapFont(boolean flip) {
        super(flip);
    }

    public ManagedBitmapFont(FileHandle fontFile, TextureRegion region) {
        super(fontFile, region);
    }

    public ManagedBitmapFont(FileHandle fontFile, TextureRegion region, boolean flip) {
        super(fontFile, region, flip);
    }

    public ManagedBitmapFont(FileHandle fontFile) {
        super(fontFile);
    }

    public ManagedBitmapFont(FileHandle fontFile, boolean flip) {
        super(fontFile, flip);
    }

    public ManagedBitmapFont(FileHandle fontFile, FileHandle imageFile, boolean flip) {
        super(fontFile, imageFile, flip);
    }

    public ManagedBitmapFont(FileHandle fontFile, FileHandle imageFile, boolean flip, boolean integer) {
        super(fontFile, imageFile, flip, integer);
    }

    @WithFactory
    public ManagedBitmapFont(@ByName(value="gdx.bitmapFont.fontFile", fallbackValue = "font.file") String fontFile,
                             @ByName(value="gdx.bitmapFont.fontImage", fallbackValue = "font.png") String imageFile,
                             @ByName(value="gdx.bitmapFont.flip", fallbackValue = "true") boolean flip,
                             @ByName(value="gdx.bitmapFont.integer", fallbackValue = "true") boolean integer) {
        super(Gdx.files.internal(fontFile), Gdx.files.internal(imageFile), flip, integer);
    }

    public ManagedBitmapFont(BitmapFontData data, TextureRegion region, boolean integer) {
        super(data, region, integer);
    }

    public ManagedBitmapFont(BitmapFontData data, Array<TextureRegion> pageRegions, boolean integer) {
        super(data, pageRegions, integer);
    }
}
