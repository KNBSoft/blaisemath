/*
 * ColorModel.java
 * 
 * Created on Sep 7, 2007, 2:18:25 PM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package sequor.model;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import javax.swing.JButton;

/**
 * This class
 * <br><br>
 * @author Elisha Peterson
 */
public class ColorModel extends FiresChangeEvents {
    private Color c;
    public ColorModel(){}
    public ColorModel(Color c){setValue(c);}
    @Override
    public void setValue(String s){throw new UnsupportedOperationException("Not supported yet.");}
    public Color getValue(){return c;}
    public void setValue(Color newValue){if(newValue!=c){c=newValue;fireStateChanged();}}
    @Override
    public String toLongString(){throw new UnsupportedOperationException("Not supported yet.");}
    @Override
    public PropertyChangeEvent getChangeEvent(String s){return new PropertyChangeEvent(this,s,null,c);}
    @Override
    public FiresChangeEvents clone(){return new ColorModel(c);}
    @Override
    public void copyValuesFrom(FiresChangeEvents parent){c=((ColorModel)parent).c;}
    
    public JButton getButton(){
        return new ColorEditor(this).button;
    }
}
