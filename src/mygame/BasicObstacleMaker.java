/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import java.util.Random;

/**
 *
 * @author Aron
 */
public class BasicObstacleMaker implements SectionMaker {
    
    private AssetManager assetManager;
    private int length = 10;
    
    BasicObstacleMaker(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public Spatial getSection(int n) {
        Node section = new Node();
        Random r = new Random(n);
        Spatial bonus = makeBall(0.2f, ColorRGBA.Red);
        r.nextFloat();
        float x = r.nextFloat()*2 - 1;
        float y = r.nextFloat()*2 - 1;
        float z = r.nextFloat()*2 - 1;

        bonus.setLocalTranslation(x,y,z);
        section.attachChild(bonus);
        return section;
    }

    public int getSectionLength() {
        return length;
    }
    
    private Spatial makeBall(float r, ColorRGBA color) {
        Geometry ball = new Geometry("Sphere", new Sphere(10,10,r,true,false));
        Material mat = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        
        mat.setColor("Color", color);

        ball.setMaterial(mat);
        return ball;
    }
    
}
