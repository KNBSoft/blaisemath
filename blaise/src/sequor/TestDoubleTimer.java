/*
 * TestDoubleTimer.java
 *
 * Created on March 3, 2008, 10:22 AM
 */

package sequor;

import java.awt.event.ActionEvent;
import sequor.component.*;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import scio.coordinate.R2;
import specto.dynamicplottable.Point2D;

/**
 *
 * @author  ae3263
 */
public class TestDoubleTimer extends javax.swing.JFrame {
    
    Point2D point;
    
    /** Creates new form TestDoubleTimer */
    public TestDoubleTimer() {
        initComponents();
        point=new Point2D();
        plot2D1.add(point);
        doubleRangeModel1.setMaximum(10.0);
        doubleRangeModel1.setMinimum(-10.0);
        RangeTimer drt=new RangeTimer(doubleRangeModel1);
        JPopupMenu contextMenu=new JPopupMenu();
        for(JMenuItem jmi:drt.getMenuItems()){contextMenu.add(jmi);}
        plot2D1.setComponentPopupMenu(contextMenu);
        drt.setLooping(true);
        drt.actionPerformed(new ActionEvent(this,0,"play"));
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        doubleRangeModel1 = new sequor.model.DoubleRangeModel();
        plot2D1 = new specto.plotpanel.Plot2D();
        jToolBar1 = new javax.swing.JToolBar();

        doubleRangeModel1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                doubleRangeModel1StateChanged(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jToolBar1.setRollover(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addComponent(plot2D1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(plot2D1, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void doubleRangeModel1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_doubleRangeModel1StateChanged
        point.setPoint(new R2(doubleRangeModel1.getValue(),doubleRangeModel1.getValue()));
    }//GEN-LAST:event_doubleRangeModel1StateChanged
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TestDoubleTimer().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private sequor.model.DoubleRangeModel doubleRangeModel1;
    private javax.swing.JToolBar jToolBar1;
    private specto.plotpanel.Plot2D plot2D1;
    // End of variables declaration//GEN-END:variables
    
}