package mygame;

import com.jme3.math.Vector3f;
import com.leapmotion.leap.Hand;

public class BasicPlayerControls extends PlayerControls {

    @Override
    Vector3f getTargetSpeedPosTilt(float tpf) {
        //get leap data
        Hand hand = controller.frame().hands().get(0);
        float handPositionX = hand.palmPosition().getX();
        float handRoll = hand.palmNormal().roll();
        float handPitch = hand.direction().pitch();
        //set target value
        float positionX = handPositionX;
        //Set point where movement will start to decay
        float xSlowPoint = 30;
        if(positionX >= xSlowPoint){
            positionX = (2 * xSlowPoint/3) + positionX/3;
        }
        else if(positionX <= -xSlowPoint){
            positionX = - (2 * xSlowPoint/3) + positionX/3;
        }
        //Calculate y based on how far you travelled in x
        //set point where movement will be stopped
        positionX /= 50;
        float xLimit = 1;
        if(positionX > xLimit || positionX < -xLimit){
            if (positionX < 0)
                positionX = -xLimit;
            else
                positionX = xLimit;
        }

        float speed = hand.grabStrength();
        float tilt = handRoll;
        tilt = tilt > 1.0f ? 1.0f
             : tilt < -1.0f ? -1.0f
             : tilt;
        
        
        return new Vector3f(speed,positionX,tilt);
    }
    
}
