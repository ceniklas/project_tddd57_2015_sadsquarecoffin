package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import com.jme3.bounding.BoundingSphere;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import java.util.ArrayList;


public class MooseGame extends SimpleApplication {
    
    private Player player;
    private InfinitePath pickUps;
    private InfinitePath terrain;
    private Node skyNode;
    
    private AudioNode music;
    private float hudScore = 0;
    private ArrayList<AudioNode> scoreSounds;
    private String speedThing;
    private float meterThingy;
    
    private Spatial leapBall1;
    private Spatial leapBall2;
    
    private Controller controller = new Controller();

    public MooseGame() {
        AppSettings cfg = new AppSettings(true);
        
        cfg.setFullscreen(true);

        cfg.setTitle("Sad Square Coffin");
        
        setDisplayFps(false);

        setDisplayStatView(false);
        
        setShowSettings(true);

        setSettings(cfg);
    }
   
    @Override
    public void simpleInitApp() {
        player = new Player(assetManager);
        rootNode.attachChild(player);
        
        flyCam.setMoveSpeed(20);  
        setupCamera(player,new Vector3f(0, 3, -10));
        
        pickUps = new InfinitePath(-10,400,new BasicRewardMaker(assetManager));
        rootNode.attachChild(pickUps.getNode());

        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(0,-1,0).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);
                
        terrain = new InfinitePath(-50,300, new BasicWallMaker(assetManager));
        rootNode.attachChild(terrain.getNode());
        
        skyNode = new Node();      
        skyNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/skybox.dds", false));
        skyNode.rotate(0,3.14f,0);
        rootNode.attachChild(skyNode);
        
        //music
        music = new AudioNode(assetManager, "Sounds/Cinematic.wav");
        music.setPositional(false);
        music.setLooping(true);
        rootNode.attachChild(music);
        music.play();
        
        //init sound effects
        initSoundEffects();
        
        //shadows
        pickUps.getNode().setShadowMode(ShadowMode.Cast);
        terrain.getNode().setShadowMode(ShadowMode.Receive);
        player.setShadowMode(ShadowMode.Cast);
        
        final int SHADOWMAP_SIZE=1024;
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, SHADOWMAP_SIZE, 3);
        dlsr.setLight(sun);
        viewPort.addProcessor(dlsr);
 
        DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter(assetManager, SHADOWMAP_SIZE, 3);
        dlsf.setLight(sun);
        dlsf.setEnabled(true);
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        fpp.addFilter(dlsf);
        viewPort.addProcessor(fpp);
        
        //fog
        FilterPostProcessor processor = (FilterPostProcessor) assetManager.loadAsset("Effects/Fog.j3f");
        viewPort.addProcessor(processor);
        
        displayGuiText();
        
        initLeapPositionLine();
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        player.update(tpf);
        pickUps.update(player.getPositionZ());
        
        int score = player.checkCollision(pickUps.getNode());
        hudScore += score;
        updateTextGui(""+(int)(hudScore), tpf);
        
        if(score == 1)
            scoreSounds.get(0).playInstance();
        else if(score == 2)
            scoreSounds.get(1).playInstance();
        else if(score == 3)
            scoreSounds.get(2).playInstance();

        terrain.update(player.getPositionZ());
        
        drawLeapPositionLine();
    }
    
    public void setupCamera(Node target, Vector3f camPos) {
        flyCam.setEnabled(false);
        CameraNode camNode = new CameraNode("Camera Node", cam);
        target.attachChild(camNode);
        camNode.setLocalTranslation(camPos);
        camNode.lookAt(target.getLocalTranslation(), Vector3f.UNIT_Y);
    }
    
    private void displayGuiText() {
        speedThing = "";
        
        meterThingy = 0;
        
        BitmapText scoreText = new BitmapText(guiFont, false);   
        scoreText.setName("ScoreText");
        scoreText.setSize(guiFont.getCharSet().getRenderedSize()*1.3f);      // font size
        scoreText.setColor(ColorRGBA.Black);                             // font color
        scoreText.setText("You can write any string here");             // the text
        scoreText.setLocalTranslation(cam.getWidth()/2 - scoreText.getLineWidth()/2, scoreText.getLineHeight(), 0); // position
        
        BitmapText hudText = new BitmapText(guiFont, false);   
        hudText.setName("HUDText");
        hudText.setSize(guiFont.getCharSet().getRenderedSize()*1.3f);      // font size
        hudText.setColor(ColorRGBA.Black);                             // font color
        hudText.setText("You can write any string here");             // the text
        hudText.setLocalTranslation(cam.getWidth()/2 - hudText.getLineWidth()/2, cam.getHeight(), 0); // position

        guiNode.attachChild(scoreText);
        guiNode.attachChild(hudText);
    }

    public void updateTextGui(String text, float tpf) {
        BitmapText scoreText = ((BitmapText)guiNode.getChild("ScoreText"));
        scoreText.setText(text);
        scoreText.setLocalTranslation(cam.getWidth()/2 - scoreText.getLineWidth()/2, scoreText.getLineHeight(), 0); 
        
        //System.out.println("S="+meterThingy + " - P="+player.getPlayerSpeed() + " M=" + (player.getPlayerSpeed() - 25.0f)*tpf);
        
        if(meterThingy >= 0.0f && meterThingy <= 50.0f){
            float meterChange = (player.getPlayerSpeed() - 35.0f)*tpf*0.2f;
            if(meterChange < 0 ) 
                meterChange *= 2;
            meterThingy += meterChange;
            
            if(meterThingy < 0 || meterThingy > 50){
                meterThingy = meterThingy < 0 ? 0 : 50;
            }
        }
        System.out.println(player.getWorldTranslation().z);
        if(meterThingy < 0.2f && player.getWorldTranslation().z > 25){
            hudScore -= tpf*0.5;
        }
        
        speedThing = "";
        for (int i = 0; i < meterThingy; i++) {
            speedThing += "|";
        }
        
        BitmapText hudText = ((BitmapText)guiNode.getChild("HUDText"));
        hudText.setText(speedThing);
        hudText.setLocalTranslation(cam.getWidth()/2 - hudText.getLineWidth()/2, cam.getHeight(), 0);
        
        
    }

    private void initSoundEffects() {
        scoreSounds = new ArrayList(3);
        for(int i=0; i<3; i++) {
            AudioNode sound = new AudioNode(assetManager, "Sounds/score"+i+".wav", false);
            sound.setPositional(false);
            sound.setLooping(false);
            sound.setVolume(0.1f);
            scoreSounds.add(sound);
            rootNode.attachChild(sound);
        }
    }

    private void initLeapPositionLine() {
        leapBall1 = makeLeapBall(0.1f, new ColorRGBA(1.0f,1.0f,1.0f,0.5f));
        leapBall2 = makeLeapBall(0.1f, new ColorRGBA(1.0f,1.0f,1.0f,0.5f));
        player.attachChild(leapBall1);
        player.attachChild(leapBall2);
    }
    
    private void drawLeapPositionLine() {
        Frame frame = controller.frame();
        Hand leftHand = frame.hands().leftmost();
        Hand rightHand = frame.hands().rightmost();
        Vector3f leftPos = new Vector3f(-leftHand.palmPosition().getX()-50,
                                        leftHand.palmPosition().getY()-200,
                                        -leftHand.palmPosition().getZ());
        Vector3f rightPos = new Vector3f(-rightHand.palmPosition().getX()+50,
                                         rightHand.palmPosition().getY()-200,
                                         -rightHand.palmPosition().getZ());
        
        
        leftPos = leftPos.divide(20);
        rightPos = rightPos.divide(20);
                
        Vector3f leftTarget = new Vector3f(2.5f, 0, 0);
        Vector3f rightTarget = new Vector3f(-2.5f, 0, 0);
        
        float leftDist = leftTarget.distance(leftPos);
        float rightDist = rightTarget.distance(rightPos);
        
        ((Geometry)leapBall1).getMaterial().setColor("Diffuse",redGreen(leftDist));
        ((Geometry)leapBall2).getMaterial().setColor("Diffuse",redGreen(rightDist));

        
        leapBall1.setLocalTranslation(leftPos);
        leapBall2.setLocalTranslation(rightPos);
        
    }
    
    private Spatial makeLeapBall(float r, ColorRGBA color) {
        Geometry ball = new Geometry("Sphere", new Sphere(10,10,r,true,false));
        Material mat = new Material(assetManager, 
        "Common/MatDefs/Light/Lighting.j3md");
        
        mat.setBoolean("UseMaterialColors",true);    
        mat.setColor("Diffuse",color);  // minimum material color
        mat.setColor("Specular",color); // for shininess
        mat.setFloat("Shininess", 64f); // [1,128] for shininess
        mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        ball.setMaterial(mat);
        ball.setShadowMode(ShadowMode.Off);
        ball.setQueueBucket(Bucket.Translucent);
        BoundingSphere bound = new BoundingSphere(r, Vector3f.ZERO);
        ball.setModelBound(bound);
        return ball;
    }
    
    private ColorRGBA redGreen(float x) {
        float red = Cal.clamp(x, 1.5f, 2.5f) - 1.5f;
        float green = 1.0f - red;
        return new ColorRGBA(red,green,0,0.5f);
    }
}
