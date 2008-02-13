/*
 * Settings.java
 * Created on Sep 6, 2007, 9:51:00 AM
 */

package sequor.component;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import sequor.model.*;

/**
 * This class is intended as a superclass to add property change functionality to a collection
 * of settings. Mainly consists of some event handling routines! Extending classes should do the following
 * for each property which they implement:
 * <ul>
 *   <li>Implement as a model which generates checks the values and change events
 *   <li>Implement normal get/set methods for the property
 *   <li>Add the property with string, editor, and type code in the initializer using "addProperty"
 * </ul>
 * Property change events are automatically generated using the string from the property, and fired
 * to any classes which listen for property changes from this one.
 * If a GUI is required, a panel can be automatically generated using the underlying models
 * and a standard set of editors. The panel uses a SpringLayout to create two columns... one
 * with text labels and the other with the editing objects.
 * <br><br>
 * @author Elisha Peterson
 */
public abstract class Settings implements ChangeListener,PropertyChangeListener {
    
    // PROPERTY HANDLING SUPPORT
    
    /** List of property names. */
    protected Vector<String> names=new Vector<String>();
    /** List of property object models (contains the underlying data). */
    protected Vector<FiresChangeEvents> models=new Vector<FiresChangeEvents>();
    /** List of editor types to use. */
    protected Vector<Integer> editors=new Vector<Integer>();
    
    /** Adds a listening property which cannot be edited */
    public static final int NO_EDIT=0;
    /** Adds a spinner with double editing */
    public static final int EDIT_DOUBLE=1;
    /** Adds a spinner with integer editing */
    public static final int EDIT_INTEGER=2;
    /** Adds a combo box with several strings */
    public static final int EDIT_COMBO=3;
    /** Adds a text field with string editing */
    public static final int EDIT_STRING=4;
    /** Adds a button for color editing */
    public static final int EDIT_COLOR=5;
    /** Adds a checkbox for boolean editing */
    public static final int EDIT_BOOLEAN=6;
    /** Adds field for function editing */
    public static final int EDIT_FUNCTION=9;
    /** Adds two fields for parametric function editing */
    public static final int EDIT_PARAMETRIC=10;
    /** Adds a separator */
    public static final int EDIT_SEPARATOR=99;
    
    /** Adds property to the editor */
    public void addProperty(String s,FiresChangeEvents model,int type){names.add(s);models.add(model);editors.add(type);}
    /** Adds a separator */
    public void addPropertySeparator(){names.add(null);models.add(null);editors.add(EDIT_SEPARATOR);}
    
    /** Changes edit status of a property, by name */
    public void setPropertyEditor(String s,int newType){
        for(int i=0;i<names.size();i++){
            if(names.get(i)==null){continue;}
            if(names.get(i)==s){editors.set(i,newType);}
        }
    }
    
    // EVENT HANDLING SUPPORT
    
    /** Sets up event listening. Should be called by the constructor!! */
    protected void initEventListening(){
        for(FiresChangeEvents m:models){
            if(m==null){continue;}
            m.addChangeListener(this);
        }
    }
    /**This should pass state changes to pcs. */
    public void stateChanged(ChangeEvent e){
        for(int i=0;i<models.size();i++){
            if(models.get(i)==null){continue;}
            if(e.getSource()==models.get(i)){
                propertyChange(models.get(i).getChangeEvent(names.get(i)));
            }
        }
    }
    /** Utility class for handling bean property changes. */
    protected PropertyChangeSupport pcs=new PropertyChangeSupport(this);
    /**Add a property change listener for a specific property. */
    public void addPropertyChangeListener(PropertyChangeListener l){pcs.addPropertyChangeListener(l);}
    /**Remove a property change listener for a specific property. */
    public void removePropertyChangeListener(PropertyChangeListener l){pcs.removePropertyChangeListener(l);}
    /** Handles property change events fired from a few properties */
    public void propertyChange(PropertyChangeEvent e) {pcs.firePropertyChange(e);}
    
    
    // GUI HANDLING SUPPORT
    
    /** Generates a combo box given a string list. */
    public JComboBox getComboBox(ComboBoxRangeModel cbrm){
        JComboBox result=new JComboBox(new ComboBoxEditor(cbrm));
        result.setMinimumSize(new Dimension(50,20));
        result.setPreferredSize(new Dimension(50,25));
        result.setMaximumSize(new Dimension(200,25));
        return result;
    }
    /** Generates a spinner given a range model and a step size. */
    public JSpinner getSpinner(DoubleRangeModel drm){
        JSpinner result=new JSpinner(new SpinnerDoubleEditor(drm));
        result.setMinimumSize(new Dimension(20,20));
        result.setPreferredSize(new Dimension(50,25));
        result.setMaximumSize(new Dimension(50,25));
        return result;
    }
    public JSpinner getSpinner(IntegerRangeModel irm){
        JSpinner result=new JSpinner(new SpinnerIntegerEditor(irm));
        result.setMinimumSize(new Dimension(20,20));
        result.setPreferredSize(new Dimension(50,25));
        result.setMaximumSize(new Dimension(50,25));
        return result;
    }
    
    
    // GUI METHODS
    
    /** Generates a JPanel with the settings contained herein */
    public JPanel getPanel(){
        JPanel result=new JPanel(new SpringLayout());
        int numComponents=names.size();
        for(int i=0;i<names.size();i++){
            if(editors.get(i)==NO_EDIT){numComponents--;
            }else{
                if(editors.get(i)==EDIT_SEPARATOR){
                    result.add(new JSeparator());
                    result.add(new JSeparator());
                }else{
                    result.add(new JLabel(names.get(i)));
                    switch(editors.get(i)){
                    case EDIT_DOUBLE    : result.add(getSpinner((DoubleRangeModel)models.get(i)));break;
                    case EDIT_INTEGER   : result.add(getSpinner((IntegerRangeModel)models.get(i)));break;
                    case EDIT_COMBO     : result.add(getComboBox((ComboBoxRangeModel)models.get(i)));break;
                    case EDIT_STRING    : result.add(new JTextField());break;
                    case EDIT_COLOR     : result.add(new ColorEditor((ColorModel)models.get(i)).getButton());break;
                    case EDIT_PARAMETRIC: result.add(new BParametricFunctionPanel((ParametricModel)models.get(i)));break;
                    }
                }
            }
        }
        SpringUtilities.makeCompactGrid(result,numComponents,2,5,5,5,5);
        return result;
    }
}