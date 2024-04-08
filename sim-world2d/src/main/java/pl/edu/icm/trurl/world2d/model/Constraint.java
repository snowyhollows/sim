package pl.edu.icm.trurl.world2d.model;

import pl.edu.icm.trurl.ecs.Entity;
import pl.edu.icm.trurl.ecs.dao.annotation.WithDao;


@WithDao
public class Constraint {
    private Entity constraint;

    public Entity getConstraint() {
        return constraint;
    }

    public void setConstraint(Entity constraint) {
        this.constraint = constraint;
    }
}
