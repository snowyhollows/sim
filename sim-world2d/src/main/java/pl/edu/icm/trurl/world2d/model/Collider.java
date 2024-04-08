package pl.edu.icm.trurl.world2d.model;

import pl.edu.icm.trurl.ecs.Entity;
import pl.edu.icm.trurl.ecs.dao.annotation.WithDao;

import java.util.ArrayList;
import java.util.List;

@WithDao
public class Collider {
    private List<Entity> collisionsV = new ArrayList<>();
    private List<Entity> collisionsH = new ArrayList<>();

    public List<Entity> getCollisionsV() {
        return collisionsV;
    }

    public List<Entity> getCollisionsH() {
        return collisionsH;
    }
}
