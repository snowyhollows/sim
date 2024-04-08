package pl.edu.icm.trurl.world2d.model;

import pl.edu.icm.trurl.ecs.dao.annotation.WithDao;

@WithDao
public class Speed {
    private float dx;
    private float dy;

    public float getDx() {
        return dx;
    }

    public void setDx(float dx) {
        this.dx = dx;
    }

    public float getDy() {
        return dy;
    }

    public void setDy(float dy) {
        this.dy = dy;
    }
}
