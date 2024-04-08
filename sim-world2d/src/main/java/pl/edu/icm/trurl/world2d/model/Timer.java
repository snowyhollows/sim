package pl.edu.icm.trurl.world2d.model;

import pl.edu.icm.trurl.ecs.dao.annotation.WithDao;

@WithDao
public class Timer {
    private float alarm;
    
    public Timer(float alarm) {
        this.alarm = alarm;
    }

    public Timer() {
    }

    public float getAlarm() {
        return alarm;
    }

    public void setAlarm(float alarm) {
        this.alarm = alarm;
    }
}
