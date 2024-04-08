package pl.edu.icm.trurl.world2d.model;

import pl.edu.icm.trurl.ecs.dao.annotation.WithDao;

@WithDao
public class Color {
    private int packedColor;

    public int getPackedColor() {
        return packedColor;
    }

    public void setPackedColor(int packedColor) {
        this.packedColor = packedColor;
    }
}
