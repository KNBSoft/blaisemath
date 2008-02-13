/*
 * ParametricModel.java
 *
 * Created on Sep 10, 2007, 3:49:14 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package sequor.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import scio.function.Function;
import scribo.parser.FunctionSyntaxException;
import scribo.tree.FunctionTreeFactory;
import scribo.tree.FunctionTreeRoot;
import scio.coordinate.R2;

/**
 * This class
 * <br><br>
 * @author Elisha Peterson
 */
public class ParametricModel extends FiresChangeEvents implements ActionListener{
    FunctionTreeRoot fx,fy;
    private String sx="cos(t)";
    private String sy="sin(t)";
    public ParametricModel(){
        try {
            fx = FunctionTreeFactory.getFunction(sx);
            fy = FunctionTreeFactory.getFunction(sy);
        } catch (FunctionSyntaxException ex) {            
        }
    }
    public void setValue(String s){throw new UnsupportedOperationException("Not supported yet.");}
    public void setXString(String s){
        try {
            fx=FunctionTreeFactory.getFunction(s);
            sx=s;
        } catch (FunctionSyntaxException ex){}
    }
    public void setYString(String s){
        try {
            fy=FunctionTreeFactory.getFunction(s);
            sy=s;
        } catch (FunctionSyntaxException ex){}
    }
    public FunctionTreeRoot getTreeX(){return fx;}
    public FunctionTreeRoot getTreeY(){return fy;}
    public String getStringX(){return sx;}
    public String getStringY(){return sy;}
    public R2 getValue(double t){return new R2(fx.getValue(t),fy.getValue(t));}
    public Function<Double,R2> getFunction(){
        return new Function<Double,R2>(){
            public R2 getValue(Double x){return getValue(x);}
            public R2 minValue(){return null;}
            public R2 maxValue(){return null;}            
        };
    }
    public String toLongString(){throw new UnsupportedOperationException("Not supported yet.");}
    public PropertyChangeEvent getChangeEvent(String s){return new PropertyChangeEvent(this,s,null,null);}
    public String toString(){return "("+sx+","+sy+")";}
    public void actionPerformed(ActionEvent e){fireStateChanged();}
}