package mygame;

import com.jme3.material.Material;
import com.jme3.scene.Node;
import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import java.util.List;

public class Player extends Node {
    private Node headNode;
    private Node bodyNode;
    private Node mooseNode;
    private Node rayNodes;
    
    private double positionZ = 0;
    private float time = 0;
    private float lastCollisionTime = 0;
    private int collisionCounter = 0;
    
    private PlayerControls playerControls;
    private float playerSpeed;
    
    public Player(AssetManager assetManager) {
        
        playerControls = new AdvTwoHandControls();
        
        mooseNode = new Node();
        
        //setup head
        Material mooseMaterial = new Material(assetManager, 
        "Common/MatDefs/Light/Lighting.j3md");
        
        mooseMaterial.setBoolean("UseMaterialColors",true);    
        mooseMaterial.setColor("Diffuse",ColorRGBA.Brown);  // minimum material color
        mooseMaterial.setColor("Specular",ColorRGBA.White); // for shininess
        mooseMaterial.setFloat("Shininess", 64f); // [1,128] for shininess
        
        Texture mooseTexture = assetManager.loadTexture("Textures/Terrain/moose.jpg");
        mooseTexture.setWrap(Texture.WrapMode.Repeat);
        //mooseMaterial.setTexture("ColorMap", mooseTexture);
        Spatial body = assetManager.loadModel("Models/mooseBody.obj");
        Spatial head = assetManager.loadModel("Models/mooseHead.obj");
        head.setMaterial(mooseMaterial);
        headNode = new Node();
        headNode.attachChild(head);
        mooseNode.attachChild(headNode);
        
        //setup body
        
        body.setMaterial(mooseMaterial);
        bodyNode = new Node();
        bodyNode.attachChild(body);
        mooseNode.attachChild(bodyNode);
        
        //set head pivot point
        head.setLocalTranslation(0,-8,0);
        //position head
        headNode.setLocalTranslation(0,6.5f,0);
        //set body pivot poitn
        body.setLocalTranslation(0,-7.5f,0);
        //position body
        bodyNode.setLocalTranslation(0,7.5f,0);
        //scale moose
        mooseNode.scale(0.3f);
        
        Node wrapper = new Node();
        wrapper.setLocalTranslation(0,-3,0);
        
        wrapper.attachChild(mooseNode);
        attachChild(wrapper);
        
        
        //Create start and end positions for rays.
        float rayWidth = 7.4f;
        float rayHeight = 3.4f;
        int numberOfRays = 5;
        
        float rayStart = -rayWidth/2.0f;
        float rayStep = rayWidth/(numberOfRays-1);
        rayNodes = new Node();
        for(int i = 0; i < numberOfRays; i++) {
            Node ray = new Node();
            ray.setLocalTranslation(rayStart+rayStep*i,rayHeight,0);
            rayNodes.attachChild(ray);
            
            //debug balls
            /*
            Geometry bolleboll = new Geometry("Sphere", new Sphere(10,10,0.4f,true,false));
            Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            bolleboll.setMaterial(mat);
            ray.attachChild(bolleboll);
            */
        }
        headNode.attachChild(rayNodes);
        
    }
    
    public double getPositionZ() {
        return positionZ;
    }
    
    public void update(float tpf){
        //basicOneHandControls(tpf);
        Vector3f speedPosTilt = playerControls.getSpeedPosTilt(tpf);
        playerSpeed = speedPosTilt.getX()*50;
        float positionX = speedPosTilt.getY();
        float tilt = speedPosTilt.getZ();
        
        Vector3f targetSpeedPosTilt = playerControls.getTargetSpeedPosTilt(tpf);
        float targetSpeed = targetSpeedPosTilt.getX()*50;
        float targetPositionX = targetSpeedPosTilt.getY()*50;
        float targetTilt = targetSpeedPosTilt.getZ();
        
        float positionY = Cal.pathFun(positionX);
        
        //animate based on current values
        mooseNode.setLocalTranslation(-positionX*5 + 0.1f*(float)Math.sin(2*time), positionY + 0.2f*(float)Math.sin(3*time), 0);
        this.setLocalTranslation(0,0, (float) positionZ);
        Quaternion headTiltQuat = new Quaternion();
        headTiltQuat.fromAngleAxis(-tilt, new Vector3f(0,0,1));
        headNode.setLocalRotation(headTiltQuat);
        
        //animate based on hand directly
        Quaternion bodyDragQuat = new Quaternion();
        bodyDragQuat.fromAngleAxis(0.02f*targetSpeed, new Vector3f(1,0,0));
        Quaternion bodyTiltQuat = new Quaternion();
        bodyTiltQuat.fromAngleAxis(-targetTilt, new Vector3f(0,1,0));
        bodyDragQuat = bodyDragQuat.mult(bodyTiltQuat);
        bodyNode.setLocalRotation(bodyDragQuat);
        
        //update position
        positionZ += playerSpeed*tpf;
        
        time += tpf;
    }
    
    public float getPlayerSpeed(){
        return playerSpeed;
    }

    int checkCollision(Node stuff) {
        List<Spatial> rays = rayNodes.getChildren();
        
        int counter = 0;
        for(int i=0; i<rays.size(); i++) {
            CollisionResults res = new CollisionResults();
            Ray ray = new Ray(rays.get(i).getWorldTranslation(), new Vector3f(0,0,-1));
            int n = stuff.collideWith(ray, res);
            CollisionResult thing = res.getClosestCollision();
            if(thing != null) {
                if(thing.getGeometry().getMaterial().getName() == null){
                    thing.getGeometry().getMaterial().setColor("Diffuse", ColorRGBA.Blue);
                    thing.getGeometry().getMaterial().setName("Collided");
                    counter++;
                }
                
                
            }
        }
              
        int val = 0;
        if(time - lastCollisionTime > 0.05f) {
            val = collisionCounter;
            collisionCounter = 0;
            if(val > 0)
                System.out.println(val);
        }
        
        collisionCounter += counter;
        
        if(counter > 0) {
                lastCollisionTime = time;
        }
        
        
        return val;
    }
}
