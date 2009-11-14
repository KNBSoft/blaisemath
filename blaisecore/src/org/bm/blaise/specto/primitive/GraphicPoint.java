/**
 * GraphicPoint.java
 * Created on Nov 4, 2009
 */

package org.bm.blaise.specto.primitive;

import java.awt.geom.Point2D;

/**
 * <p>
 *   <code>GraphicPoint</code> is a graphics primitive that represents a circle with a
 *   given radius about a point.
 * </p>
 *
 * @author Elisha Peterson
 */
public class GraphicPoint extends Point2D.Double {

    public double radius;

    public GraphicPoint(Point2D position) {
        setLocation(position);
    }

    /** Construct arrow with provided coords.
     */
    public GraphicPoint(Point2D position, double radius) {
        setLocation(position);
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    /**
     * Scales an array of points, ensuring that the maximum value of the radius is less than
     * maxX in the x direction and maxY in the y direction.
     *
     * @param maxRad the maximum permissible radius
     * @param exponent the exponent to scale the radius by (1 is linear, 0.5 makes fewer small circles, 2 makes fewer large circles)
     */
    public static void scalePoints(GraphicPoint[] points, double maxRad, double exponent) {
        double mr = 0;
        for (int i = 0; i < points.length; i++) {
            mr = Math.max(mr, Math.abs(points[i].radius));
        }
        for (int i = 0; i < points.length; i++) {
            points[i].radius = maxRad * Math.signum(points[i].radius) * Math.pow(Math.abs(points[i].radius) / mr, exponent);
        }
    }
}