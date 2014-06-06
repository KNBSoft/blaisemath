/**
 * BasicPointGraphic.java
 * Created Jan 22, 2011
 */
package org.blaise.graphics;

/*
 * #%L
 * BlaiseGraphics
 * --
 * Copyright (C) 2009 - 2014 Elisha Peterson
 * --
 * Licensed under the Apache License, Version 2.0.
 * You may not use this file except in compliance with the License.
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

import com.google.common.base.Objects;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import javax.annotation.Nonnull;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import org.blaise.style.PointStyle;
import org.blaise.util.PointBean;
import org.blaise.util.PointFormatters;

/**
 * A point with position, orientation, and an associated style.
 * Implements {@code PointBean}, allowing the point to be dragged around.
 * {@link ChangeEvent}s are generated when the graphic's point changes.
 * 
 * @see PointStyle
 * 
 * @author Elisha Peterson
 */
public abstract class AbstractPointGraphic extends GraphicSupport implements PointBean<Point2D> {

    /** The object that will be drawn. */
    protected Point2D point;
    
    protected final ChangeEvent changeEvent = new ChangeEvent(this);
    protected final EventListenerList listenerList = new EventListenerList();

    /**
     * Construct with default point
     */
    protected AbstractPointGraphic() {
        this(new Point2D.Double());
    }

    /** 
     * Construct with given primitive and style.
     * @param p initial point
     */
    protected AbstractPointGraphic(Point2D p) {
        setPoint(p);
        PointBeanDragger dragger = new PointBeanDragger(this);
        addMouseListener(dragger);
        addMouseMotionListener(dragger);
    }

    @Override
    public String toString() {
        return "PointGraphic:"+PointFormatters.formatPoint(point, 2);
    }
    
    //<editor-fold defaultstate="collapsed" desc="PROPERTY PATTERNS">
    //
    // PROPERTY PATTERNS
    //

    public Point2D getPoint() { 
        return point;
    }
    public final void setPoint(Point2D p) {
        if (!Objects.equal(point, p)) {
            point = new Point2D.Double(p.getX(), p.getY());
            fireGraphicChanged();
            fireStateChanged();
        }
    }
    
    //</editor-fold>
    

    @Override
    public boolean contains(Point2D p) {
        PointStyle style = drawStyle();
        return p.distance(point) <= style.getMarkerRadius();
    }

    @Override
    public boolean intersects(Rectangle2D box) {
        PointStyle style = drawStyle();
        double r = (double) style.getMarkerRadius();
        return new Ellipse2D.Double(point.getX()-r,point.getY()-r,2*r,2*r).intersects(box);
    }
    
    /**
     * Return draw style for this object.
     * @return draw style
     */
    @Nonnull 
    public abstract PointStyle drawStyle();
    
    
    //<editor-fold defaultstate="collapsed" desc="EVENT HANDLING">
    //
    // EVENT HANDLING
    //

    public final void addChangeListener(ChangeListener l) { 
        listenerList.add(ChangeListener.class, l);
    }
    
    public final void removeChangeListener(ChangeListener l) { 
        listenerList.remove(ChangeListener.class, l); 
    }

    /** Notify interested listeners of an (unspecified) change in the plottable. */
    public final void fireStateChanged() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (changeEvent == null) {
                    return;
                }
                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }
    
    //</editor-fold>
    
}
