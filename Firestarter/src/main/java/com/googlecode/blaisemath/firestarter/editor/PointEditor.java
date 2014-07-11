/**
 * PointEditor.java
 * Created on Jun 18, 2009
 */
package com.googlecode.blaisemath.firestarter.editor;

/*
 * #%L
 * Firestarter
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

import java.awt.Point;
import javax.swing.SpinnerNumberModel;

/**
 * <p>
 *   The <code>PointEditor</code> displays two spinners that change the value
 *   of an underlying point.
 * </p>
 *
 * @author Elisha Peterson
 */
public class PointEditor extends MultiSpinnerSupport {

    public PointEditor() {
        super(2);
        setNewValue(0,0);
    }

    @Override
    public void initCustomizer() {
        super.initCustomizer();
        spinners[0].setToolTipText("x coordinate");
        spinners[1].setToolTipText("y coordinate");
    }

    //
    //
    // PROPERTYEDITOR METHODS
    //
    //
    @Override
    public String getJavaInitializationString() {
        Object value = getValue();
        return (value != null) ? ("new java.awt.Point(" + getAsText() + ")") : "null";
    }

    @Override
    public void setAsText(String... s) throws IllegalArgumentException {
        try {
            int x = Integer.decode(s[0]);
            int y = Integer.decode(s[1]);
            setValue(new Point(x, y));
        } catch (Exception ex) {
            throw new IllegalArgumentException(s.toString());
        }
    }

    @Override
    protected void initEditorValue() {
        if (panel != null) {
            spinners[0].setModel(new SpinnerNumberModel((Number) getNewValue(0), Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
            spinners[1].setModel(new SpinnerNumberModel((Number) getNewValue(1), Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
        }
    }

    //
    //
    // ADDITIONAL METHODS
    //
    //
    @Override
    public Object getValue(Object bean, int i) {
        switch (i) {
            case 0:
                return ((Point) bean).x;
            case 1:
                return ((Point) bean).y;
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    @Override
    void setNewValue(Object... values) {
        setNewValue(new Point((Integer) values[0], (Integer) values[1]));
    }
}
