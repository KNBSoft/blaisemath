/*
 * VPrimitiveMappingPlottable.java
 * Created on Nov 4, 2009
 */

package org.bm.blaise.specto.plottable;

import org.bm.utils.Spacing;
import scio.coordinate.utils.SampleSetGenerator;
import org.bm.blaise.specto.primitive.PrimitiveStyle;
import org.bm.blaise.specto.visometry.*;

/**
 * <p>
 *   This plottable uses a <code>SampleSetGenerator</code> and a <code>PrimitiveStyle</code>
 *   to plot a grid of primitives. It is intended to display repeated graphical elements
 *   that differ slightly from one point to the next, for example vector fields.
 * </p>
 *
 * @param <C> the coordinate type
 * @param <P> the primitive type
 *
 * @author Elisha Peterson
 */
public abstract class VPrimitiveMappingPlottable<C, P> extends AbstractPlottable<C> implements PrimitiveMapper<C, P>, VisometryChangeListener {

    //
    //
    // PROPERTIES
    //
    //

    /** The styling class */
    protected PrimitiveStyle style;

    /** The coordinate grid generator. */
    protected SampleSetGenerator<C> ssg;

    /** The spacing directive. */
    protected Spacing spacing = Spacing.REGULAR;

    //
    //
    // CONSTRUCTORS
    //
    //

    public VPrimitiveMappingPlottable(PrimitiveStyle style, SampleSetGenerator<C> ssg) {
        this.style = style;
        this.ssg = ssg;
    }

    //
    //
    // GET/SET PATTERNS
    //
    //

    public Spacing getSpacing() {
        return spacing;
    }

    public void setSpacing(Spacing spacing) {
        this.spacing = spacing;
    }

    public SampleSetGenerator<C> getSampleSetGenerator() {
        return ssg;
    }

    public void setSampleSetGenerator(SampleSetGenerator<C> ssg) {
        this.ssg = ssg;
    }
    
    //
    //
    // DRAW METHODS
    //
    //

    /** Stores graphic primitive objects displayed. */
    transient protected P[] primitives;

    /** 
     * Scales the size of graphics primitives appropriately. Extended classes
     * should use this method to adjust the size of the primitives, if necessary.
     */
    public void scalePrimitives() {
        
    }

    public void visometryChanged(Visometry vis, VisometryGraphics canvas) {
        primitives = primitivesAt(ssg.getSamples(), vis, canvas);
        scalePrimitives();
    }

    @Override
    public void paintComponent(VisometryGraphics<C> vg) {
        if (primitives != null) {
            vg.setPrimitiveStyle(style);
            vg.draw(primitives);
        }
    }

}