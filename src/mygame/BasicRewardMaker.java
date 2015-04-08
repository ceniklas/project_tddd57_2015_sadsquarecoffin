/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingSphere;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import java.util.Random;

/**
 *
 * @author Aron
 */
public class BasicRewardMaker implements SectionMaker {
    
    private AssetManager assetManager;
    private int length = 50;
    
    BasicRewardMaker(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public Spatial getSection(int n) {
        Node section = new Node();
        if(n%2 != 0 || n == 0) {
            return section;
        }
        Random r = new Random(n-45645645);
        r.nextFloat();
        float x = r.nextFloat()*2 - 1;
        float y = r.nextFloat()*2 - 1;
        float z = r.nextFloat()*2 - 1;
        float a = r.nextFloat()*2 - 1;
        
        //x = 0;
        y = Cal.pathFun(x);
        
        Vector3f bc = new Vector3f(x*5,y-1.95f,z);
        float radius = 1.5f;
        float ballCount = 3.0f;
        
        Node bonuses = new Node();
        
        for(float i = -ballCount/2; i < ballCount/2; i++){
            Spatial bonus = makeBall(radius, ColorRGBA.Green);
            bonus.setLocalTranslation(new Vector3f((radius)*i,1.02f,0));
            bonuses.attachChild(bonus);
        }
        bonuses.setLocalTranslation(bc);
        
        Quaternion rot = new Quaternion();
        rot.fromAngleAxis(a, new Vector3f(0,0,1));
        bonuses.setLocalRotation(rot);
        
        
        section.attachChild(bonuses);
        
        return section;
    }

    public int getSectionLength() {
        return length;
    }
    
    private Spatial makeBall(float r, ColorRGBA color) {
        Geometry ball = new Geometry("Sphere", new Sphere(25,25,r,true,false));
        Material mat = new Material(assetManager, 
        "Common/MatDefs/Light/Lighting.j3md");
        
        mat.setBoolean("UseMaterialColors",true);    
        mat.setColor("Diffuse",color);  // minimum material color
        mat.setColor("Specular",color); // for shininess
        mat.setFloat("Shininess", 64f); // [1,128] for shininess

        ball.setMaterial(mat);
        BoundingSphere bound = new BoundingSphere(r, Vector3f.ZERO);
        ball.setModelBound(bound);
        return ball;
    }
    
}
