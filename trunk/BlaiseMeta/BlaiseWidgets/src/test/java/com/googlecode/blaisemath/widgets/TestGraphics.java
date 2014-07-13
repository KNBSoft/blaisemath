/*
 * TestGraphics.java
 * Created on Apr 21, 2012, 8:58:19 AM
 */
package com.googlecode.blaisemath.widgets;

/*
 * #%L
 * BlaiseWidgets
 * --
 * Copyright (C) 2009 - 2014 Elisha Peterson
 * --
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.googlecode.blaisemath.widgets.BlaiseSlider;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.googlecode.blaisemath.graphics.BasicShapeGraphic;
import com.googlecode.blaisemath.graphics.GraphicComponent;
import com.googlecode.blaisemath.style.Styles;

/**
 *
 * @author Elisha
 */
public class TestGraphics extends javax.swing.JFrame {

    BasicShapeGraphic gr = new BasicShapeGraphic(new Rectangle2D.Double(10,10,100,100), Styles.fillStroke(Color.black, Color.red));
    
    /** Creates new form TestGraphics */
    public TestGraphics() {
        initComponents();
        graphicComponent1.addGraphic(gr);
        graphicComponent1.setSelectionEnabled(true);
        blaiseSlider2.setModel(new DefaultBoundedRangeModel(200,0,50,300));
        blaiseSlider2.getModel().addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e) {
                updateShape();
            }
        });
        updateShape();
    }
    
    private void updateShape() {
        int val = blaiseSlider2.getModel().getValue();
        gr.setPrimitive(new Rectangle2D.Double(10,10,val,val));
        gr.setDefaultTooltip(val + "x" + val);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        graphicComponent1 = new GraphicComponent();
        blaiseSlider2 = new BlaiseSlider();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        org.jdesktop.layout.GroupLayout graphicComponent1Layout = new org.jdesktop.layout.GroupLayout(graphicComponent1);
        graphicComponent1.setLayout(graphicComponent1Layout);
        graphicComponent1Layout.setHorizontalGroup(
            graphicComponent1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 623, Short.MAX_VALUE)
        );
        graphicComponent1Layout.setVerticalGroup(
            graphicComponent1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 321, Short.MAX_VALUE)
        );

        getContentPane().add(graphicComponent1, java.awt.BorderLayout.CENTER);

        org.jdesktop.layout.GroupLayout blaiseSlider2Layout = new org.jdesktop.layout.GroupLayout(blaiseSlider2);
        blaiseSlider2.setLayout(blaiseSlider2Layout);
        blaiseSlider2Layout.setHorizontalGroup(
            blaiseSlider2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 623, Short.MAX_VALUE)
        );
        blaiseSlider2Layout.setVerticalGroup(
            blaiseSlider2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 40, Short.MAX_VALUE)
        );

        getContentPane().add(blaiseSlider2, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TestGraphics.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TestGraphics.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TestGraphics.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TestGraphics.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new TestGraphics().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private BlaiseSlider blaiseSlider2;
    private GraphicComponent graphicComponent1;
    // End of variables declaration//GEN-END:variables
}
