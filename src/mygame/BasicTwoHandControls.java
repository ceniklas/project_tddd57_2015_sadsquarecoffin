package mygame;

import com.jme3.math.Vector3f;
import com.leapmotion.leap.Hand;

public class BasicTwoHandControls extends PlayerControls {

    @Override
    Vector3f getTargetSpeedPosTilt(float tpf) {
        //get leap data
        Hand leftHand = controller.frame().hands().leftmost();
        Hand rightHand = controller.frame().hands().rightmost();
        
        float avgPosX = (leftHand.palmPosition().getX() + rightHand.palmPosition().getX())/2; 
        float diffPosY = leftHand.palmPosition().getY() - rightHand.palmPosition().getY();
        float avgPitch = (leftHand.direction().pitch() + rightHand.direction().pitch())/2;
        
        float speed = avgPitch;
        speed = speed < 0.0f ? 0.0f
              : speed > 1.0f ? 1.0f
              : speed;
        speed = 1.0f - speed;
        
        float positionX = avgPosX;
        positionX /= 50;
        positionX = positionX < -1.0f ? -1.0f
              : positionX > 1.0f ? 1.0f
              : positionX;
        
        float tilt = -diffPosY/100;
        tilt = tilt < -1.0f ? -1.0f
             : tilt > 1.0f ? 1.0f
             : tilt;
        
        return new Vector3f(speed,positionX,tilt);
    }
    
}
