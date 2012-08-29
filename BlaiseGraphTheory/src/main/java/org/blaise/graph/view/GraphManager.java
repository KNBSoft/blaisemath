/*
 * GraphManager.java
 * Created Jan 29, 2011
 */

package org.blaise.graph.view;

import java.awt.geom.Point2D;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.blaise.graph.Graph;
import org.blaise.graph.GraphBuilders;
import org.blaise.graph.layout.IterativeGraphLayout;
import org.blaise.graph.layout.StaticGraphLayout;
import org.blaise.util.PointManager;

/**
 * <p>
 *   Links up a {@link IterativeGraphLayout} with a {@link PointManager} to maintain
 *   consistent positions of nodes in a graph.
 *   Also provides timers/threads for executing iterative layouts.
 * </p>
 * <p>
 *  {@link PropertyChangeListener}s will be notified of three kinds of changes:
 *  <ul>
 *      <li>{@code layoutAlgorithm}: the layout algorithm has changed</li>
 *      <li>{@code layoutAnimating}: the timer controlling layout iteration has started or stopped</li>
 *      <li>{@code locationArray}: node locations have changed, or graph data has changed</li>
 *  </ul>
 * </p>
 * <p>
 * External changes to this class are heavily managed to ensure multiple threads
 * are properly synchronized.. The following methods may be accessed to make changes to the graph:
 *  <ul>
 *      <li>{@link #setGraph(org.bm.blaise.scio.graph.Graph)}: changes the underlying graph completely; an active layout algorithm will keep running</li>
 *      <li>{@link #updateGraph()}: notifies that the graph's nodes and edges have changed; an active layout algorithm will keep running</li>
 *      <li>{@link #requestPositionMap(java.util.Map)}, {@link #requestPositionArray(java.util.List, java.awt.geom.Point2D.Double[])}:
 *          two methods for setting the locations of nodes in the graph externally</li>
 *      <li>{@link #applyLayout(org.bm.blaise.scio.graph.layout.StaticGraphLayout, double[])}: another way to set positions of nodes in the graph; this uses the specified
 *          layout algorithm to compute the locations</li>
 * </ul>
 * </p>
 *
 * @param <C> type of node in graph
 * @author elisha
 */
public final class GraphManager<C> implements PropertyChangeListener {

    //<editor-fold defaultstate="collapsed" desc="CONSTANTS">
    /** Default time between layout iterations. */
    private static final int DEFAULT_DELAY = 10;
    /** Default # iterations per layout step */
    private static final int DEFAULT_ITER = 2;

    /** The initial layout scheme */
    private static final StaticGraphLayout INITIAL_LAYOUT = StaticGraphLayout.CIRCLE;
    /** The layout scheme for adding vertices */
    private static final StaticGraphLayout ADDING_LAYOUT = StaticGraphLayout.ORIGIN;
    /** The initial layout parameters */
    private static final double[] LAYOUT_PARAMETERS = new double[] { 3 };
    //</editor-fold>

    /** Graph */
    private Graph<C> graph;
    /** Maintains locations of nodes in the graph (in local coordinates) */
    private final PointManager<C, Point2D.Double> locator = new PointManager<C, Point2D.Double>();
    /** Used for iterative graph layouts */
    private transient IterativeGraphLayout iLayout;

    /** Timer that performs iterative layout */
    private transient java.util.Timer layoutTimer;
    /** Timer task */
    private transient java.util.TimerTask layoutTask;


    /** Initializes with an empty graph */
    public GraphManager() {
        this(GraphBuilders.EMPTY_GRAPH);
    }

    /**
     * Constructs manager for the specified graph.
     * @param graph the graph
     */
    public GraphManager(Graph<C> graph) {
        setGraph(graph);
        locator.addPropertyChangeListener(this);
    }


    // <editor-fold defaultstate="collapsed" desc="PROPERTIES & UPDATE METHODS">
    //
    // PROPERTIES & UPDATE METHODS
    //

    /**
     * Return the graph
     * @return the adapter's graph
     */
    public Graph<C> getGraph() {
        return graph;
    }

    /**
     * Changes the graph.
     * @param g the graph
     */
    public synchronized void setGraph(Graph<C> g) {
        if (g == null) {
            throw new IllegalArgumentException("Graph cannot be null.");
        }
        if (this.graph == null) {
            this.graph = g;

            locator.replace(INITIAL_LAYOUT.layout(g, LAYOUT_PARAMETERS));
            if (iLayout != null) {
                iLayout.requestPositions(locator.getLocationMap(), true);
            }
            fireGraphChanged();
        } else {
            this.graph = g;
            updateGraph();
        }
    }

    /**
     * Call this method to notify that the set of vertices AND edges has changed.
     */
    public synchronized void updateGraph() {
        if (iLayout != null)
            iLayout.requestPositions(locator.getLocationMap(), true);
        fireGraphChanged();
    }

    /**
     * Returns a defensive view of the locations of objects in the graph.
     * @return locations (cannot be modified)
     */
    public Map<C, Point2D.Double> getLocationMap() {
        return Collections.unmodifiableMap(locator.getLocationMap());
    }

    /**
     * Update the locations of the specified nodes with the specified values
     * @param nodePositions new locations for objects
     */
    public void requestPositionMap(Map<C, Point2D.Double> nodePositions) {
        if (nodePositions != null)
            if (iLayout != null)
                iLayout.requestPositions(nodePositions, false);
            else {
                locator.update(nodePositions);
                fireLocationsChanged();
            }
    }

    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="Layout Code">
    //
    // LAYOUT METHODS
    //

    /**
     * Applies a particular layout algorithm and updates node positions.
     * @param layout the layout algorithm
     * @param parameters parameters for the algorithm
     */
    public void applyLayout(StaticGraphLayout layout, double... parameters) {
        Map<Object,Point2D.Double> pos = layout.layout(graph, parameters);
        requestPositionMap(pos);
        if (iLayout != null)
            iLayout.requestPositions(pos, false);
        fireGraphChanged();
    }

    /** @return current iterative layout algorithm */
    public IterativeGraphLayout getLayoutAlgorithm() {
        return iLayout;
    }

    /**
     * Sets up with an iterative graph layout. Cancels any ongoing layout timer.
     * Does not start a new layout.
     * @param layout the layout algorithm
     */
    public void setLayoutAlgorithm(IterativeGraphLayout layout) {
        if (layout != iLayout) {
            IterativeGraphLayout old = iLayout;
            stopLayoutTask();
            iLayout = layout;
            iLayout.requestPositions(locator.getLocationMap(), true);
            fireAlgorithmChanged(old, layout);
        }
    }

    /**
     * Whether layout is animating
     * @return true if an iterative layout is active
     */
    public boolean isLayoutAnimating() {
        return layoutTask != null;
    }

    /**
     * Sets layout to animate
     * @param value true to animate, false to stop animating
     */
    public void setLayoutAnimating(boolean value) {
        boolean old = layoutTask != null;
        if (value != old) {
            if (value)
                startLayoutTask(DEFAULT_DELAY, DEFAULT_ITER);
            else
                stopLayoutTask();
            fireAnimatingChanged(old, value);
        }
    }

    private static int _i = 0;
    private static int _p = 0;

    /** Iterates layout, if an iterative layout has been provided. */
    public synchronized void iterateLayout() {
//        System.out.println("Starting layout");
        if (iLayout != null) {
            long t0 = System.currentTimeMillis();
            try {
                iLayout.iterate(graph);
            } catch (ConcurrentModificationException ex) {
                Logger.getLogger(GraphManager.class.getName()).log(Level.WARNING, "Failed Layout Iteration: ConcurrentModificationException");
            }
            long t1 = System.currentTimeMillis();
            locator.setLocationMap(iLayout.getPositions());
            fireGraphChanged();
            long t2 = System.currentTimeMillis();
//            if ((t1-t0) > 200)
//                Logger.getLogger(GraphManager.class.getName()).log(Level.WARNING, "Long Iterative Layout Step {0}: {1}ms", new Object[]{++_i, t1-t0});
//            if ((t2-t1) > 200)
//                Logger.getLogger(GraphManager.class.getName()).log(Level.WARNING, "Long Position Update {0}: {1}ms", new Object[]{++_p, t2-t1});
        }
//        System.out.println("... layout complete");
    }

    /**
     * Activates the layout timer, if an iterative layout has been provided.
     * @param delay delay in ms between layout calls
     * @param iter number of iterations per update
     */
    public void startLayoutTask(int delay, final int iter) {
        if (iLayout == null) return;
        if (layoutTimer == null)
            layoutTimer = new java.util.Timer();
        stopLayoutTask();
        layoutTask = new TimerTask() {
            @Override public void run() {
                for(int i=0;i<iter;i++)
                    iterateLayout();
            }
        };
        layoutTimer.schedule(layoutTask, delay, delay);
    }

    /** Stops the layout timer */
    public void stopLayoutTask() {
        if (layoutTask != null) {
            layoutTask.cancel();
            layoutTask = null;
        }
    }// </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="Event Handling">
    //
    // EVENT HANDLING
    //

    /** The entire graph has changed */
    private synchronized void fireGraphChanged() {
        Object[] loc = locator.getLocationArray();
        Point2D.Double[] pLoc = new Point2D.Double[loc.length];
        System.arraycopy(loc, 0, pLoc, 0, loc.length);
        pcs.firePropertyChange("graph", null, pLoc);
    }

    /** Only the positions have changed */
    private synchronized void fireLocationsChanged() {
        Object[] loc = locator.getLocationArray();
        Point2D.Double[] pLoc = new Point2D.Double[loc.length];
        System.arraycopy(loc, 0, pLoc, 0, loc.length);
        pcs.firePropertyChange("locationArray", null, pLoc);
    }

    /** The layout algorithm has changed */
    private synchronized void fireAlgorithmChanged(IterativeGraphLayout old, IterativeGraphLayout nue) {
        pcs.firePropertyChange("layoutAlgorithm", old, nue);
    }

    /** The layout algorithm has started/stopped its timer */
    private synchronized void fireAnimatingChanged(boolean old, boolean nue) {
        pcs.firePropertyChange("layoutAnimating", old, nue);
    }

    /** Handles property change events */
    PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public synchronized void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) { pcs.removePropertyChangeListener(propertyName, listener); }
    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) { pcs.removePropertyChangeListener(listener); }
    public synchronized void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) { pcs.addPropertyChangeListener(propertyName, listener); }
    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) { pcs.addPropertyChangeListener(listener); }

    // </editor-fold>


}
