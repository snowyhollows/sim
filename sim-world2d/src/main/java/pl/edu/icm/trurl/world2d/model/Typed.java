package pl.edu.icm.trurl.world2d.model;

import pl.edu.icm.trurl.ecs.dao.annotation.WithDao;

@WithDao
public class Typed {
    private String type;
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
