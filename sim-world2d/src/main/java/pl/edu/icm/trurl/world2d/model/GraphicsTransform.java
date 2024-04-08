package pl.edu.icm.trurl.world2d.model;

import pl.edu.icm.trurl.ecs.dao.annotation.WithDao;

@WithDao
public class GraphicsTransform {
    private boolean horizontalFlip = false;
    private float alpha = 1;
    private float rotation = 0;
    private float scale = 1;

    public boolean isHorizontalFlip() {
        return horizontalFlip;
    }

    public void setHorizontalFlip(boolean horizontalFlip) {
        this.horizontalFlip = horizontalFlip;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
