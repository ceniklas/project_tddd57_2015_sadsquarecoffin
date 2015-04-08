package mygame;

import com.jme3.math.FastMath;

public class Cal {
    public static float clamp(float x, float minVal, float maxVal) {
        return Math.max(minVal, Math.min(maxVal, x));
    }
    
    public static float inpo(float val, float tar, float step) {
        return val + (tar - val)*step;
    }
    
    public static float pathFun(float positionX) {
        
        float xLimit = 50;
        float positionY = FastMath.abs((positionX* positionX*positionX)*2.5f);
        if(positionX > xLimit || positionX < -xLimit){
            positionY = (xLimit*xLimit*xLimit)/50000;
        }
        return positionY;
    }
}
