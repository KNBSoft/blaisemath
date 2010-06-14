/**
 * PlanePlotComponent.java
 * Created on Jul 30, 2009
 */

package visometry.plane;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import scio.coordinate.RealInterval;
import visometry.PlotComponent;

/**
 * <p>
 *   <code>PlanePlotComponent</code> is a <code>PlotComponent</code> for a
 *   two-dimensional Euclidean plane.
 * </p>
 *
 * @author Elisha Peterson
 */
public class PlanePlotComponent extends PlotComponent<Point2D.Double> {

    public PlanePlotComponent() {
        super(new PlaneVisometry(), new PlaneProcessor());
        defaultMouseListener = new PlanePlotMouseHandler((PlaneVisometry) visometry, this);
        defaultMouseWheelListener = (MouseWheelListener) defaultMouseListener;
        
        // set up the default domains for the plot
        PlaneVisometry pv = (PlaneVisometry) visometry;
        pGroup.registerDomain("x", pv.getHorizontalDomain(), Double.class);
        pGroup.registerDomain("y", pv.getVerticalDomain(), Double.class);
        pGroup.registerDomain("xy", pv.getPlaneDomain(), Point2D.Double.class);
        pGroup.registerDomain("time", new RealInterval(0, 100), Double.class);

    }

    @Override
    protected void paintComponent(Graphics g) {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintComponent(g);
    }


    //
    // DELEGATE METHODS
    //

    /**
     * Set desired range of values to display.
     * Recomputes transformation after setting.
     * @param minX
     * @param minY
     * @param maxX
     * @param maxY
     */
    public void setDesiredRange(double min1, double min2, double max1, double max2) {
        ((PlaneVisometry) visometry).setDesiredRange(min2, min2, max2, max2);
    }

    /**
     * Sets aspect ratio of plot
     * Recomputes transformation after setting
     * @param ratio new aspect ratio
     */
    public void setAspectRatio(double ratio) {
        ((PlaneVisometry) visometry).setAspectRatio(ratio);
    }
}
