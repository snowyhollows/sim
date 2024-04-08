package pl.edu.icm.trurl.world2d.model;

import pl.edu.icm.trurl.ecs.Entity;
import pl.edu.icm.trurl.ecs.dao.annotation.WithDao;

import java.util.Objects;

@WithDao(namespace = "texture")
public class TextureFragmentComponent {
    private Entity texture;
    private short u;
    private short v;
    private short width;
    private short height;

    public Entity getTexture() {
        return texture;
    }

    public void setTexture(Entity texture) {
        this.texture = texture;
    }

    public short getU() {
        return u;
    }

    public void setU(short u) {
        this.u = u;
    }

    public short getV() {
        return v;
    }

    public void setV(short v) {
        this.v = v;
    }

    public short getWidth() {
        return width;
    }

    public void setWidth(short width) {
        this.width = width;
    }

    public short getHeight() {
        return height;
    }

    public void setHeight(short height) {
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextureFragmentComponent that = (TextureFragmentComponent) o;
        return u == that.u && v == that.v && width == that.width && height == that.height && Objects.equals(texture, that.texture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(texture, u, v, width, height);
    }
}
