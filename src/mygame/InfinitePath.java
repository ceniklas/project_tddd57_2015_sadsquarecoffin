package mygame;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class InfinitePath {
    private Node root;
    private double prevPos;
    private int startPos;
    private int endPos;
    private int pathLength;
    private int sectionLength;
    private int sectionIndex;
    private SectionMaker sectionMaker;
    
    public InfinitePath(int startPos, int endPos, SectionMaker sectionMaker) {
        if(startPos > endPos) {
            System.out.println("WARNING: startPos > endPos!");
        }
        
        sectionIndex = 0;
        
        root = new Node();
        prevPos = 0.0f;
        this.startPos = startPos;
        this.endPos = endPos;
        this.sectionMaker = sectionMaker;
        //build path
        sectionLength = sectionMaker.getSectionLength();
        //make sure path length is divisble by sectionLength
        pathLength = (endPos - startPos)/sectionLength * sectionLength;
        endPos = startPos + pathLength;
        
        for(int p = startPos; p < pathLength; p += sectionLength) {
            Spatial section = sectionMaker.getSection(p/sectionLength);
            section.setLocalTranslation(0.0f,0.0f,p);
            root.attachChild(section);
        }
    }
    
    public Node getNode() {
        return root;
    }
    

    public void update(double playerPos) {
        int posChange = (int)(playerPos - prevPos);
        int moveDist = Math.abs(posChange);
        if( moveDist >= sectionLength ) {
            int length = root.getChildren().size();
            for(int i=0; i<length; i++) {
                float pos = root.getChild(i).getLocalTranslation().getZ();
                if(pos - playerPos < startPos) {
                    int newPos = (int)(pos + pathLength);
                    Spatial section = sectionMaker.getSection(newPos/sectionLength);
                    section.setLocalTranslation(0.0f,0.0f,newPos);
                    root.detachChildAt(i);
                    root.attachChildAt(section, i);
                }
            }
            prevPos = playerPos;
        }
    }

}
