package mygame;

import com.jme3.math.Vector3f;
import com.leapmotion.leap.Controller;

public abstract class PlayerControls {

    public Controller controller = new Controller();

    public Vector3f getSpeedPosTilt(float tpf) {
        return getTargetSpeedPosTilt(tpf);
    }
    abstract Vector3f getTargetSpeedPosTilt(float tpf);
}
