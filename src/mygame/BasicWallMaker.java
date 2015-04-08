package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

class BasicWallMaker implements SectionMaker {
    
    private AssetManager assetManager;
    private int length = 42;
    
    public BasicWallMaker(AssetManager assetManager) {
        this.assetManager = assetManager;
    }
    
    @Override
    public int getSectionLength() {
        return length;
    }

    @Override
    public Spatial getSection(int n) {
        Node section = new Node();
        
        ColorRGBA ceilingColor = ColorRGBA.White;
        if(n%2 == 0) {
            ceilingColor = ColorRGBA.Gray;
        }
        //setup head
         Material grassMaterial = new Material(assetManager, 
        "Common/MatDefs/Light/Lighting.j3md");
        grassMaterial.setTexture("DiffuseMap", 
        assetManager.loadTexture("Textures/Terrain/grass.jpg"));
        //grassMaterial.setBoolean("UseMaterialColors",true);    
        grassMaterial.setColor("Diffuse",ColorRGBA.White);  // minimum material color
        grassMaterial.setColor("Specular",ColorRGBA.White); // for shininess
        grassMaterial.setFloat("Shininess", 0f); // [1,128] for shininess
        //Material grassMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture grassTexture = assetManager.loadTexture("Textures/Terrain/grass.jpg");
        grassTexture.setWrap(Texture.WrapMode.Repeat);
        //grassMaterial.setTexture("ColorMap", grassTexture);
        Spatial floor = assetManager.loadModel("Models/grass.obj");
        floor.setMaterial(grassMaterial);
        floor.setLocalTranslation(0,0,0);
        section.attachChild(floor);
        
        return section;
    }
    
    private Spatial makeBox(float width, float height, float length, ColorRGBA color) {
        Geometry box = new Geometry("Box", new Box(width/2.0f, height/2.0f, length/2.0f));
        Material mat = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        
        mat.setColor("Color", color);

        box.setMaterial(mat);
        return box;
    }

}
