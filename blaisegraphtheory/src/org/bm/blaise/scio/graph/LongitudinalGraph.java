/*
 * LongitudinalGraph.java
 * Created Jul 5, 2010
 */

package org.bm.blaise.scio.graph;

import java.util.List;

/**
 * <p>
 * The interface contains methods describing a graph that evolves over time.
 * The primary method allows the user to extract a graph for a specified time
 * (called a "slice"),provided that the graph exists at that time.
 * The time format is assumed to be a double.
 * Implementations may choose how to store the underlying data.
 * </p>
 *
 * @param <V> the type of the nodes
 *
 * @author Elisha Peterson
 */
public interface LongitudinalGraph<V> {

    /**
     * Returns a view of the graph at the specified time. Depending upon the
     * implementation, a graph may or may not exist at a given time. In some cases,
     * the implementation's nodes and arcs will occur at <i>intervals</i>, so the
     * slice will pick out any node or arc that occurs within the interval. In other
     * cases, the implementation will have graphs that occur at specific times, and
     * the method will pick out the (discrete) graph instance for the specified time.
     *
     * @param time time of interest
     * @return a copy of the graph at the specified time, or null if no graph
     *   exists at that time
     */
    public Graph<V> slice(double time);

    /**
     * Returns the minimum time encoded by elements in this graph.
     * @return minimum time
     */
    public double getMinimumTime();

    /**
     * Returns the maximum time encoded by elements in this graph
     * @return maximum time
     */
    public double getMaximumTime();

    /**
     * Returns a list of times encoded by elements in this graph.
     * An empty list might be interpreted as a continuous spectrum.
     * @return a list of all times in this graph
     */
    public List<Double> getTimes();

}
