/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.scene.Spatial;

/**
 *
 * @author Aron
 */
public interface SectionMaker {

    Spatial getSection(int n);

    int getSectionLength();
    
}
