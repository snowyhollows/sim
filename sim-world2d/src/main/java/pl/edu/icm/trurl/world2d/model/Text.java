package pl.edu.icm.trurl.world2d.model;

import pl.edu.icm.trurl.ecs.dao.annotation.WithDao;

@WithDao
public class Text {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
