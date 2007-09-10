package Blaise;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * <b>BFunctionEditField.java</b><br>
 * Author: <i>Elisha Peterson</i><br>
 * Created on <i>June 20, 2007, 10:58 AM</i><br><br>
 *
 * A text field with an underlying parser which determines when to
 * fire change events, etc. Works with a BPlotFunction2D.
 */
public class BFunctionEditField extends JTextField implements ActionListener {
    
// constants
    
    public static String DEFAULT_LABEL_STRING="Enter function: f(x)=";
    
// Fields    
    
    private String label=DEFAULT_LABEL_STRING;
    /** Parser corresponding to the function */
    private BParser parser;
    
    /** Constructor: creates a new instance of BFunctionEditField */
    public BFunctionEditField(){this(DEFAULT_LABEL_STRING,"x");}
    public BFunctionEditField(String label,String var){
        super();
        this.label=label;
        parser=new BParser(var);
        parser.addActionListener(this);
        getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
            public void insertUpdate(javax.swing.event.DocumentEvent e){parser.setExpressionString(getText());}
            public void removeUpdate(javax.swing.event.DocumentEvent e){parser.setExpressionString(getText());}
            public void changedUpdate(javax.swing.event.DocumentEvent e){}
        });    
        setText(parser.DEFAULT_STRING);  
        setPreferredSize(new Dimension(50,20));
    }
    
// Bean patterns
    
    public String getLabel(){return label;}
    public void setLabel(String s){label=s;}
    public void setParser(BParser p){parser=p;}
    public BParser getParser(){return parser;}
    public BPlotFunction2D getPlotFunction(BPlot2D owner){return new BPlotFunction2D(owner,parser);}
    
// Handles actions created by the parser

    public void actionPerformed(ActionEvent e){
        if(e.getSource()==parser){
            if(e.getActionCommand()=="error"){setForeground(Color.RED);}
            else if(e.getActionCommand()=="change"){setForeground(Color.BLACK);}
        }
    }
}
