package pl.edu.icm.trurl.world2d.model;

import pl.edu.icm.trurl.ecs.dao.annotation.WithDao;

@WithDao
public class Focusable {
    private Focus focus = Focus.OFF;

    public Focus getFocus() {
        return focus;
    }

    public void setFocus(Focus focus) {
        this.focus = focus;
    }

    public enum Focus {
        ON, OFF, PAN;

        public boolean pan() {
            return this == ON || this == PAN;
        }

        public boolean zoom() {
            return this == ON;
        }

        public boolean focused() {
            return this != OFF;
        }
    }
}
