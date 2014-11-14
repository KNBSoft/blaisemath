/*
 * CoordinateListener.java
 * Created on Aug 30, 2012
 */

package com.googlecode.blaisemath.util.coordinate;

/*
 * #%L
 * BlaiseGraphics
 * --
 * Copyright (C) 2009 - 2014 Elisha Peterson
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

/**
 * Receives updates regarding the locations of a collection of objects. Handlers
 * should be aware that the update may be invoked from any thread.
 * 
 * @param <S> type of object being located
 * @param <C> type of coordinate
 * 
 * @author petereb1
 */
public interface CoordinateListener<S,C> {

    /**
     * Called when coordinates/points are added. This may occur on any thread.
     * @param evt description of what coordinates were added/removed/changed
     */
    void coordinatesChanged(CoordinateChangeEvent<S,C> evt);

}
