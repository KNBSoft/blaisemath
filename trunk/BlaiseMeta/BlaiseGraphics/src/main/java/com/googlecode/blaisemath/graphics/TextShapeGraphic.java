/*
 * LabeledShapeGraphic.java
 * Created on Oct 15, 2012
 */

package com.googlecode.blaisemath.graphics;

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


import com.googlecode.blaisemath.style.ObjectStyler;
import com.googlecode.blaisemath.style.ShapeStyle;
import com.googlecode.blaisemath.style.TextStyleBasic;
import com.googlecode.blaisemath.style.TextStyleMultiline;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;

/**
 * Customizable graphic that represents a labeled item.
 * Uses an {@link ObjectStyler} to customize appearance.
 *
 * @param <E> type of item
 *
 * @author cornidc1
 */
public class TextShapeGraphic<E> extends DelegatingShapeGraphic<E> {

    /** Draws label */
    private final BasicTextGraphic labelGraphic = new BasicTextGraphic(new Point2D.Double(), "");
    /** Special style for Text */
    private TextStyleMultiline labelStyle;

    /**
     * Initialize with source object.
     * @param src source object
     * @param rect bounding rectangle for graphic
     * @param styler styler for source object
     */
    public TextShapeGraphic(E src, RectangularShape rect, ObjectStyler<E,ShapeStyle> styler) {
        super(src, rect, styler);
    }

    @Override
    protected void sourceGraphicUpdated() {
        if (labelGraphic != null) {
            labelGraphic.setString(source == null ? "" 
                    : getStyler() == null || getStyler().getLabelDelegate() == null ? ""+source
                    : getStyler().getLabelDelegate().apply(source));
            TextStyleBasic ss = (TextStyleBasic) (getStyler() == null || getStyler().getLabelStyleDelegate() == null ? null
                                        : getStyler().getLabelStyleDelegate().apply(source));
            labelStyle = (TextStyleMultiline) (ss == null ? null
                    : new TextStyleMultiline()
                        .textAnchor(ss.getTextAnchor())
                        .fill(ss.getFill())
                        .font(ss.getFont())
                        .fontSize(ss.getFontSize())
                        .offset(ss.getOffset()));
            labelGraphic.setStyle(labelStyle);
        }
    }

    @Override
    public ShapeStyle drawStyle() {
        final ShapeStyle parentStyle = super.drawStyle();
        return new ShapeStyle() {
            @Override
            public void draw(Shape primitive, Graphics2D canvas) {
                Shape drawPrim = primitive;
                if (primitive instanceof RectangularShape) {
                    RectangularShape rs = (RectangularShape) primitive;
                    int inset = 3;
                    RectangularShape rs2 = (RectangularShape) rs.clone();
                    rs2.setFrameFromCenter(rs.getCenterX(), rs.getCenterY(), rs.getMaxX()-inset, rs.getMaxY()-inset);
                    drawPrim = rs2;
                }
                parentStyle.draw(drawPrim, canvas);
                if (labelStyle != null) {
                    labelGraphic.setPoint(new Point2D.Double(
                            drawPrim.getBounds2D().getMinX()+2, 
                            drawPrim.getBounds2D().getMaxY()-2));
                    labelGraphic.draw(canvas);
                }
            }
        };
    }
}