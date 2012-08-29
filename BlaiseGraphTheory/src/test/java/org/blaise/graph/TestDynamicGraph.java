/*
 * TestPlaneVisometry.java
 *
 * Created on Jul 30, 2009, 3:15:03 PM
 */
package org.blaise.graph;

import org.blaise.firestarter.PropertySheet;
import org.blaise.firestarter.editor.EditorRegistration;
import org.blaise.graph.layout.SpringLayout;
import org.blaise.graph.layout.StaticGraphLayout;
import org.blaise.graph.view.GraphComponent;
import org.blaise.graph.view.PlaneGraphAdapter;
import org.blaise.util.gui.RollupPanel;
import org.blaise.visometry.Plottable;


/**
 *
 * @author ae3263
 */
public class TestDynamicGraph extends javax.swing.JFrame {

    PlaneGraphAdapter pga;
    /** Flag for when el needs points updated */
    boolean updateEL = true;
    SpringLayout energyLayout;

    MyTestGraph graph = new MyTestGraph();


    /** Creates new form TestPlaneVisometry */
    public TestDynamicGraph() {
        EditorRegistration.registerEditors();
        initComponents();

        plot.setGraph(graph);

        // PANELS

        rollupPanel1.add("Visometry", new PropertySheet(plot.getVisometry()));
        rollupPanel1.add("Energy Layout", new PropertySheet(energyLayout = new SpringLayout(
                plot.getGraphManager().getLocationMap()
                )));
        for (Plottable p : plot.getPlottableArray()) {
            rollupPanel1.add(p.toString(), new PropertySheet(p));
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        randomLB = new javax.swing.JButton();
        circleLB = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jLabel1 = new javax.swing.JLabel();
        energyIB = new javax.swing.JButton();
        energyAB = new javax.swing.JButton();
        energySB = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jLabel2 = new javax.swing.JLabel();
        addVerticesB = new javax.swing.JButton();
        addEdgesB = new javax.swing.JButton();
        rewireB = new javax.swing.JButton();
        addThreadedB = new javax.swing.JButton();
        threadStopB = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        rollupPanel1 = new RollupPanel();
        plot = new GraphComponent();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));

        jToolBar1.setRollover(true);

        randomLB.setText("Random Layout");
        randomLB.setFocusable(false);
        randomLB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        randomLB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        randomLB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                randomLBActionPerformed(evt);
            }
        });
        jToolBar1.add(randomLB);

        circleLB.setText("Circle Layout");
        circleLB.setFocusable(false);
        circleLB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        circleLB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        circleLB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                circleLBActionPerformed(evt);
            }
        });
        jToolBar1.add(circleLB);
        jToolBar1.add(jSeparator1);

        jLabel1.setText("ENERGY:");
        jToolBar1.add(jLabel1);

        energyIB.setText("iterate");
        energyIB.setFocusable(false);
        energyIB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        energyIB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        energyIB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                energyIBActionPerformed(evt);
            }
        });
        jToolBar1.add(energyIB);

        energyAB.setText("animate");
        energyAB.setFocusable(false);
        energyAB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        energyAB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        energyAB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                energyABActionPerformed(evt);
            }
        });
        jToolBar1.add(energyAB);

        energySB.setText("stop");
        energySB.setFocusable(false);
        energySB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        energySB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        energySB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                energySBActionPerformed(evt);
            }
        });
        jToolBar1.add(energySB);
        jToolBar1.add(jSeparator2);

        jLabel2.setText("ADD:");
        jToolBar1.add(jLabel2);

        addVerticesB.setText("vertices");
        addVerticesB.setFocusable(false);
        addVerticesB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addVerticesB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        addVerticesB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addVerticesBActionPerformed(evt);
            }
        });
        jToolBar1.add(addVerticesB);

        addEdgesB.setText("edges");
        addEdgesB.setFocusable(false);
        addEdgesB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addEdgesB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        addEdgesB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addEdgesBActionPerformed(evt);
            }
        });
        jToolBar1.add(addEdgesB);

        rewireB.setText("rewire");
        rewireB.setFocusable(false);
        rewireB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rewireB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        rewireB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rewireBActionPerformed(evt);
            }
        });
        jToolBar1.add(rewireB);

        addThreadedB.setText("threaded");
        addThreadedB.setFocusable(false);
        addThreadedB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addThreadedB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        addThreadedB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addThreadedBActionPerformed(evt);
            }
        });
        jToolBar1.add(addThreadedB);

        threadStopB.setText("stop");
        threadStopB.setFocusable(false);
        threadStopB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        threadStopB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        threadStopB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                threadStopBActionPerformed(evt);
            }
        });
        jToolBar1.add(threadStopB);

        getContentPane().add(jToolBar1, java.awt.BorderLayout.PAGE_START);

        jScrollPane1.setViewportView(rollupPanel1);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.EAST);
        getContentPane().add(plot, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void randomLBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_randomLBActionPerformed
        updateEL = true;
        plot.getGraphManager().applyLayout(StaticGraphLayout.RANDOM, 5.0);
    }//GEN-LAST:event_randomLBActionPerformed

    private void circleLBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_circleLBActionPerformed
        updateEL = true;
        plot.getGraphManager().applyLayout(StaticGraphLayout.CIRCLE, 5.0);
    }//GEN-LAST:event_circleLBActionPerformed

    private void energyIBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_energyIBActionPerformed
        if (energyLayout == null)
            energyLayout = new SpringLayout(plot.getGraphManager().getLocationMap());
        plot.getGraphManager().setLayoutAlgorithm(energyLayout);
        plot.getGraphManager().iterateLayout();
        updateEL = false;
    }//GEN-LAST:event_energyIBActionPerformed

    private void energyABActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_energyABActionPerformed
        if (energyLayout == null)
            energyLayout = new SpringLayout(plot.getGraphManager().getLocationMap());
        plot.getGraphManager().setLayoutAlgorithm(energyLayout);
        plot.getGraphManager().startLayoutTask(50, 2);
    }//GEN-LAST:event_energyABActionPerformed

    private void energySBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_energySBActionPerformed
        plot.getGraphManager().stopLayoutTask();
    }//GEN-LAST:event_energySBActionPerformed

    private void addVerticesBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addVerticesBActionPerformed
        graph.addVertices(5);
        plot.getGraphManager().updateGraph();
    }//GEN-LAST:event_addVerticesBActionPerformed

    private void addEdgesBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addEdgesBActionPerformed
        graph.addEdges(5);
        plot.getGraphManager().updateGraph();
    }//GEN-LAST:event_addEdgesBActionPerformed

    private void rewireBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rewireBActionPerformed
        graph.rewire(10, 5);
        plot.getGraphManager().updateGraph();
    }//GEN-LAST:event_rewireBActionPerformed

    java.util.Timer t = new java.util.Timer();
    java.util.TimerTask tt;

    private void addThreadedBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addThreadedBActionPerformed
        if (tt != null) tt.cancel();
        tt = new java.util.TimerTask(){
            @Override public void run() {
                synchronized(plot.getGraphManager()) {
                    graph.removeVertices(1);
                    graph.removeEdges(10);
                    graph.addVertices(1);
                    graph.addEdges(2);
                }
                plot.getGraphManager().updateGraph();
            }
        };
        t.schedule(tt, 100, 500);
    }//GEN-LAST:event_addThreadedBActionPerformed

    private void threadStopBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_threadStopBActionPerformed
        if (tt != null) tt.cancel();
    }//GEN-LAST:event_threadStopBActionPerformed


    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new TestDynamicGraph().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addEdgesB;
    private javax.swing.JButton addThreadedB;
    private javax.swing.JButton addVerticesB;
    private javax.swing.JButton circleLB;
    private javax.swing.JButton energyAB;
    private javax.swing.JButton energyIB;
    private javax.swing.JButton energySB;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar jToolBar1;
    private GraphComponent plot;
    private javax.swing.JButton randomLB;
    private javax.swing.JButton rewireB;
    private RollupPanel rollupPanel1;
    private javax.swing.JButton threadStopB;
    // End of variables declaration//GEN-END:variables
}