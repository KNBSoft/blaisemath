/*
 * DESolution2D.java
 * Created on Oct 17, 2007, 3:01:18 PM
 */

package specto.euclidean2;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import scio.coordinate.R3;
import scio.function.FunctionValueException;
import java.awt.Graphics2D;
import java.util.Vector;
import scio.function.Function;
import scio.coordinate.R2;
import scio.function.BoundedFunction;
import sequor.component.RangeTimer;
import sequor.model.BooleanModel;
import sequor.model.DoubleRangeModel;
import sequor.model.PointRangeModel;
import sequor.style.VisualStyle;
import specto.Decoration;
import specto.style.LineStyle;

/**
 * Represents a solution curve to a differential equation. Visually consists of an initial point,
 * a forwards solution curve, and a backwards solution curve.
 * <br><br>
 * @author ae3263
 */
public class DESolution2D extends InitialPointSet2D implements Decoration<Euclidean2,VectorField2D> {

    
    /** The underlying vector field. */
    VectorField2D parent;
    /** The backwards solution. */
    PointSet2D reversePath;
    /** Whether to show the "reverse path" */
    boolean showReverse=true;
    
    // CONSTRUCTOR
    
    public DESolution2D(Point2D point,VectorField2D parent){
        super(point);
        this.parent=parent;
        setColor(new Color(.5f,0,.5f));
    }
    public DESolution2D(VectorField2D parent){
        super();
        this.parent=parent;
        setColor(new Color(.5f,0,.5f));
    }
    DESolution2D(PointRangeModel initialPoint, VectorField2D parent) {
        super(initialPoint);
        this.parent=parent;
        setColor(new Color(.5f,0,.5f));
    }
    
    // DECORATION METHODS

    public void setParent(VectorField2D parent) {this.parent=parent;}
    public VectorField2D getParent() {return parent;}

    public DESolution2D() {
    }
    
    
    // COMPUTING AND VISUALIZING
    
    /** Determines whether to use a box visualization of the solution. */
    BooleanModel useBox = new BooleanModel(true);
    /** Separation for the box solution. */
    DoubleRangeModel boxSep = new DoubleRangeModel(.2,.001,5);
    /** Solution curves for the box visualization of the solution. */
    Vector<Vector<R2>> boxSolutions;
    
    
    /** Initializes solution curve models. */
    void initSolutionCurves(){
        if(path==null){
            path=new PointSet2D(parent.getColor());
        }
        if(reversePath==null){
            reversePath=new PointSet2D(parent.getColor());
            reversePath.style.setValue(PointSet2D.DOTTED);
        }
        path.getPath().clear();
        reversePath.getPath().clear();    
    }

    @Override
    public void recompute(Euclidean2 v) {
        R2 pt = getPoint();
        BoundedFunction<R2,R2> fn = parent.getFunction();
        try {
            initSolutionCurves();
            switch(algorithm){
                case ALGORITHM_RUNGE_KUTTA:
                    path.setPath(calcRungeKutta4(fn,pt,500,.04));
                    reversePath.setPath(calcRungeKutta4(fn,pt,500,-.04));
                    break;
                case ALGORITHM_NEWTON:
                default:
                    path.setPath(calcNewton(fn,pt,500,.04));
                    reversePath.setPath(calcNewton(fn,pt,500,-.04));
                    break;
            }
            if (useBox.isTrue()) {
                boxSolutions = new Vector<Vector<R2>>();
                double sep = boxSep.getValue();
                R2[] corner = {pt.plus(sep,sep), pt.plus(-sep,sep), pt.plus(-sep,-sep), pt.plus(sep,-sep)};
                switch(algorithm){
                    case ALGORITHM_RUNGE_KUTTA:
                        for(int i=0;i<4;i++){
                            boxSolutions.add(calcRungeKutta4(fn,corner[i],500,.04));
                        }
                        break;
                    case ALGORITHM_NEWTON:
                    default:
                        for(int i=0;i<4;i++){
                            boxSolutions.add(calcNewton(fn,corner[i],500,.04));
                        }
                        break;
                }
            } 
        } catch (FunctionValueException ex) {}
    }
    
    @Override
    public void paintComponent(Graphics2D g,Euclidean2 v) {
        if(path!=null){path.paintComponent(g,v);}
        if(showReverse&&reversePath!=null){
            g.setComposite(VisualStyle.COMPOSITE2);
            reversePath.paintComponent(g,v);
            g.setComposite(AlphaComposite.SrcOver);
        }
    }

    @Override
    public void paintComponent(Graphics2D g,Euclidean2 v,RangeTimer t){
        if(path!=null){path.paintComponent(g,v,t);}
        if(useBox.isTrue()) {
            int pos=t.getCurrentIntValue();
            int posB=pos<0?0:(pos>=boxSolutions.get(0).size()?boxSolutions.get(0).size()-1:pos);
            Vector<R2> rect = new Vector<R2>();
            for (int i = 0; i < 4; i++) {
                rect.add(boxSolutions.get(i).get(posB));
            }
            g.setComposite(VisualStyle.COMPOSITE5);
            g.setColor(Color.YELLOW);
            g.fill(v.closedPath(rect));
            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(1.0f));
//            g.draw(v.lineSegment(boxSolutions.get(0).get(posB),boxSolutions.get(2).get(posB)));
//            g.draw(v.lineSegment(boxSolutions.get(1).get(posB),boxSolutions.get(3).get(posB)));
            g.draw(v.closedPath(rect));
            g.setComposite(AlphaComposite.SrcOver);
        } else {
            if(showReverse&&reversePath!=null){
                g.setComposite(VisualStyle.COMPOSITE2);
                reversePath.paintComponent(g,v,t);
                g.setComposite(AlphaComposite.SrcOver);
            }
        }
    }

    // STYLE PARAMETERS

    int algorithm=ALGORITHM_RUNGE_KUTTA;
    public static final int ALGORITHM_NEWTON=0;
    public static final int ALGORITHM_RUNGE_KUTTA=1;
    
    @Override
    public String toString(){
        return "DE Solution Curve "+(algorithm==1?"(RK)":"(Newton)");
    }
    
    
    
    // STATIC METHODS FOR DE SOLVING    
    
    /** Re-calculates the solution curves, using Newton's Method.
     * @param steps     The number of iterations.
     * @param stepSize  The size of path added at each step.
     */
    public static Vector<R2> calcNewton(Function<R2,R2> field, R2 start, int steps, double stepSize) throws FunctionValueException{
        Vector<R2> result=new Vector<R2>();
        result.add(start);
        R2 last;
        for(int i=0;i<steps;i++){
            last=result.lastElement();
            result.add(last.plus(getScaledVector(field,last,stepSize)));
        }
        return result;
    }    
    
    /** Recalculates solution curves using Newton's Method (time-dependent function. */
    static Vector<R2> calcNewton(BoundedFunction<R3, R2> field, R2 start, int steps, double stepSize) throws FunctionValueException {
        Vector<R2> result=new Vector<R2>();
        result.add(start);
        R2 last;
        for(int i=0;i<steps;i++){
            last=result.lastElement();
            result.add(last.plus(getScaledVector(field,i*stepSize,last,stepSize)));
        }
        return result;
    }
    
    /** Re-calculates the solution curves, using Newton's Method. Instead of using a starting
     * point, uses a starting vector; removes "steps" number of points from the beginning, and
     * adds the same number onto the end of the vector.
     * @param steps     The number of iterations.
     * @param stepSize  The size of path added at each step.
     */
    public static Vector<R2> calcNewton(Function<R2,R2> field,Vector<R2> flow,int steps,double stepSize) throws FunctionValueException{
        R2 last;
        for(int i=0;i<steps;i++){
            last=flow.lastElement();
            flow.add(last.plus(getScaledVector(field,last,stepSize)));
            flow.remove(0);
        }
        return flow;
    }
    
    /** Recalculates solution curves using Newton's Method (time-dependent function. */
    static Vector<R2> calcNewton(BoundedFunction<R3, R2> field, Vector<R2> flow, int steps, double stepSize) throws FunctionValueException {
        R2 last;
        for(int i=0;i<steps;i++){
            last=flow.lastElement();
            flow.add(last.plus(getScaledVector(field,i*stepSize,last,stepSize)));
            flow.remove(0);
        }
        return flow;
    }
    
    /** Re-calculates solution curves using Runge-Kutta 4th order.
     * @param steps the number of iteration
     * @param stepSize the change in t for each iteration
     */
    public static Vector<R2> calcRungeKutta4(Function<R2,R2> field,R2 start,int steps,double stepSize) throws FunctionValueException{
        Vector<R2> result=new Vector<R2>();
        result.add(start);
        R2 k1,k2,k3,k4;
        R2 last;
        for(int i=0;i<steps;i++){
            last=result.lastElement();
            k1=getScaledVector(field,last,stepSize);
            k2=getScaledVector(field,last.plus(k1.multipliedBy(0.5)),stepSize);
            k3=getScaledVector(field,last.plus(k2.multipliedBy(0.5)),stepSize);
            k4=getScaledVector(field,last.plus(k3),stepSize);
            result.add(new R2(last.x+(k1.x+2*k2.x+2*k3.x+k4.x)/6,last.y+(k1.y+2*k2.y+2*k3.y+k4.y)/6));
            
        }
        return result;
    }

    /** Returns vector pointing in the direction of the field. */
    public static R2 getScaledVector(Function<R2,R2> field,R2 point,double size) throws FunctionValueException{
        return field.getValue(point).scaledToLength(size);
    }
    /** Returns vector pointing in the direction of the field. */
    public static R2 getScaledVector(Function<R3,R2> field,double time,R2 point,double size) throws FunctionValueException{
        return field.getValue(new R3(point.x,point.y,time)).scaledToLength(size);
    }
    public static R2 getMultipliedVector(BoundedFunction<R2,R2> field,R2 point,double size) throws FunctionValueException{
        return field.getValue(point).multipliedBy(size/(field.maxValue().x+field.maxValue().y));
    }
    public static R2 getMultipliedVector(BoundedFunction<R3, R2> field, double time, R2 point, double size) throws FunctionValueException {
        return field.getValue(new R3(point.x,point.y,time)).multipliedBy(size/(field.maxValue().x+field.maxValue().y));
    }
}
