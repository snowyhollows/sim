package pl.edu.icm.trurl.world2d.service;


import net.snowyhollows.bento.annotation.ImplementationSwitch;
import pl.edu.icm.trurl.world2d.model.Timer;

@ImplementationSwitch(
        configKey = "trurl.world2d.globalTimer",
        cases = {
                @ImplementationSwitch.When(name = "default", implementation = DefaultGlobalTimer.class, useByDefault = true)
        }
)
public interface GlobalTimer {
    double getTotalTimePassed();

    default Timer now() {
        return new Timer((float) getTotalTimePassed());
    }

    default Timer nowPlus(float time) {
        Timer timer = now();
        timer.setAlarm(timer.getAlarm() + time);
        return timer;
    }

}
