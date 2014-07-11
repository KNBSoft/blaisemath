/**
 * CoordinateChangeEvent.java
 * Created Aug 31, 2012
 */

package com.googlecode.blaisemath.util;

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

import java.util.EventObject;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

/**
 * <p>
 *  Event describing a change to a collection of coordinates (specifically to
 *  a {@link CoordinateManager} instance).
 * </p>
 * @author elisha
 */
public final class CoordinateChangeEvent extends EventObject {

    /** Added coords */
    private Map added = null;
    /** Removed coords */
    private Set removed = null;

    /** 
     * Initialize with given source object
     * @param src source of event
     */
    public CoordinateChangeEvent(Object src) {
        super(src);
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="FACTORY METHODS">
    //
    // FACTORY METHODS
    //

    /** 
     * Creates add event 
     * @param src source of event
     * @param added map of added objects, keys are objects/values are coordinates
     */
    public static CoordinateChangeEvent createAddEvent(Object src, Map added) {
        CoordinateChangeEvent evt = new CoordinateChangeEvent(src);
        evt.added = added;
        return evt;
    }

    /** 
     * Creates remove event 
     * @param src source of event
     * @param removed set of removed objects
     */
    public static CoordinateChangeEvent createRemoveEvent(Object src, Set removed) {
        CoordinateChangeEvent evt = new CoordinateChangeEvent(src);
        evt.removed = removed;
        return evt;
    }

    /** 
     * Creates add/remove event
     * @param src source of event
     * @param added map of added objects, keys are objects/values are coordinates
     * @param removed set of removed objects
     */
    public static CoordinateChangeEvent createAddRemoveEvent(Object src, Map added, Set removed) {
        CoordinateChangeEvent evt = new CoordinateChangeEvent(src);
        evt.added = added;
        evt.removed = removed;
        return evt;
    }
    
    //</editor-fold>
    

    @Override
    public String toString() {
        return String.format("CoordinateChangeEvent[%d added,%d removed,source=%s]",
                added==null?0:added.size(), removed==null?0:removed.size(), source);
    }

    /**
     * Whether event indicates added coords 
     * @return true if coordinates were added
     */
    public boolean isAddEvent() {
        return added != null;
    }

    /** 
     * Whether event indicates removed coords 
     * @return true if coordinates were removed
     */
    public boolean isRemoveEvent() {
        return removed != null;
    }

    /**
     * Get the collection of coordinate that were added
     * @return map whose keys are the objects and values are their coordinates
     */
    @Nullable 
    public Map getAdded() {
        return added;
    }

    /**
     * Get the collection of objects whose coordinates were removed
     * @return set of objects removed
     */
    @Nullable
    public Set getRemoved() {
        return removed;
    }

}
