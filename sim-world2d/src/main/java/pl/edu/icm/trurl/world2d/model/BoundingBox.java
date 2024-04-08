package pl.edu.icm.trurl.world2d.model;

import pl.edu.icm.trurl.ecs.dao.annotation.WithDao;

import static java.lang.Math.signum;

@WithDao
public class BoundingBox {
    private float centerX;
    private float centerY;
    private float width;
    private float height;

    public float getCenterX() {
        return centerX;
    }

    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    public float getMinX() {
        return centerX - width / 2;
    }

    public float getMaxX() {
        return centerX + width / 2;
    }

    public float getMinY() {
        return centerY - height / 2;
    }

    public float getMaxY() {
        return centerY + height / 2;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void resolveBySlide(BoundingBox other, float dx, float dy) {
        if (dx == 0 && dy == 0) {
            return; // cannot slide
        }
        boolean forceHorizontal = dx != 0 && dy == 0;
        boolean forceVertical = dx == 0 && dy != 0;
        float clearX = centerX - (signum(dx)) * ((width + other.width) / 2);
        float clearY = centerY - (signum(dy)) * ((height + other.height) / 2);

        if (forceHorizontal) {
            other.setCenterX(clearX);
        } else if (forceVertical) {
            other.setCenterY(clearY);
        } else {
            throw new IllegalArgumentException("xxx");
        }
    }

    public boolean overlaps(BoundingBox other) {
        return (other.getMaxX() > getMinX() && other.getMinX() < getMaxX() && other.getMaxY() > getMinY() && other.getMinY() < getMaxY());
    }

    public static boolean overlaps(float minX, float maxX, float minY, float maxY, float minX2, float maxX2, float minY2, float maxY2) {
        return (maxX2 > minX && minX2 < maxX && maxY2 > minY && minY2 < maxY);
    }

    public boolean overlaps(float minX, float maxX, float minY, float maxY) {
        return overlaps(minX, maxX, minY, maxY, getMinX(), getMaxX(), getMinY(), getMaxY());
    }

    public void moveBy(Speed speed) {
        centerX += speed.getDx();
        centerY += speed.getDy();
    }

    public void moveBy(Speed speed, float fraction) {
        centerX += speed.getDx() * fraction;
        centerY += speed.getDy() * fraction;
    }
}
