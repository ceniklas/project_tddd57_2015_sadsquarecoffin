package mygame;

import com.jme3.math.Vector3f;
import com.leapmotion.leap.Hand;

public class AdvTwoHandControls extends PlayerControls {
    
    float currSpeed = 0;
    float currPosX = 0;
    float currTilt = 0;
    
    
    
    @Override
    public Vector3f getSpeedPosTilt(float tpf) {
        Vector3f spt = getTargetSpeedPosTilt(tpf);
        
        currSpeed = Cal.inpo(currSpeed, spt.getX(), tpf*3);
        currPosX = Cal.inpo(currPosX, spt.getY(), tpf*3);
        currTilt = Cal.inpo(currTilt, spt.getZ(), tpf*9);
        
        
        return new Vector3f(currSpeed,currPosX,currTilt);
    }
    
    public Vector3f getTargetSpeedPosTilt(float tpf) {
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
        
        float positionX = avgPosX/20;
        positionX = positionX < -1.0f ? -1.0f
              : positionX > 1.0f ? 1.0f
              : positionX;
        
        float tilt = -diffPosY/50;
        tilt = tilt < -1.0f ? -1.0f
             : tilt > 1.0f ? 1.0f
             : tilt;
        
        return new Vector3f(speed, positionX, tilt);
    }
    
}
