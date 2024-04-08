package pl.edu.icm.trurl.world2d.model;

import pl.edu.icm.trurl.ecs.dao.annotation.WithDao;

@WithDao
public class TouchSensitive {
    private Touch touch = Touch.NOT_TOUCHED;

    public Touch getTouch() {
        return touch;
    }

    public void setTouch(Touch touch) {
        this.touch = touch;
    }

    public enum Touch {
        NOT_TOUCHED, JUST_TOUCHED, TOUCHED, DRAGGED, JUST_RELEASED
    }
}
