/*
 * Copyright 2014 Elisha.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.googlecode.blaisemath.style;

/*
 * #%L
 * BlaiseGraphics
 * --
 * Copyright (C) 2009 - 2015 Elisha Peterson
 * --
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
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
import java.awt.Shape;
import java.util.List;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 *
 * @author Elisha
 */
public class MarkersTest {
    
    public MarkersTest() {
    }

    @Test
    public void testGetAvailableMarkers() {
        System.out.println("getAvailableMarkers");
        List<Marker> result = Markers.getAvailableMarkers();
        for (Marker m : result) {
            Shape s = m.create(new Point(), .5, 1f);
            assertNotNull(s);
        }
    }
    
}
