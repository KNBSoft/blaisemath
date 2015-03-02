/**
 * GestureOrchestrator.java
 * Created Oct 11, 2014
 */
package com.googlecode.blaisemath.gesture;

import com.google.common.collect.Maps;
import com.googlecode.blaisemath.util.Configurer;
import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Map;

/*
 * #%L
 * BlaiseSketch
 * --
 * Copyright (C) 2015 Elisha Peterson
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
 * Manages an active {@link MouseGesture}, and associated state changes.
 * 
 * @author elisha
 */
public class GestureOrchestrator {

    private final Component component;
    private MouseGesture activeGesture = null;
    private final Map<Class,Configurer> configs = Maps.newHashMap();

    /** Handles property listening */
    protected final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public GestureOrchestrator(Component component) {
        this.component = component;
    }
    
    //<editor-fold defaultstate="collapsed" desc="PROPERTIES">

    /**
     * Get the component related to the gestures
     * @return component
     */
    public Component getComponent() {
        return component;
    }
    
    /**
     * Get the active gesture.
     * @return active gesture
     */
    public MouseGesture getActiveGesture() {
        return activeGesture;
    }
    
    /**
     * Set the active gesture. This will finish any currently active gesture
     * before moving on.
     * @param g the new active gesture
     */
    public void setActiveGesture(MouseGesture g) {
        if (this.activeGesture != g) {
            if (this.activeGesture != null) {
                this.activeGesture.cancel();
            }
            this.activeGesture = g;
            if (this.activeGesture != null) {
                this.activeGesture.initiate();
            }
        }
    }
    
    //</editor-fold>

    public <T> void addConfigurer(Class<? super T> cls, Configurer<T> cfg) {
        configs.put(cls, cfg);
    }

    public <T> Configurer<T> getConfigurer(Class<? super T> cls) {
        return configs.get(cls);
    }
    
    /**
     * Called by gestures to yield control.
     * @param g the gesture yielding control
     */
    public void finishGesture(MouseGesture g) {
        activeGesture.finish();
        setActiveGesture(null);
    }

    /**
     * Call to finish the active gesture.
     */
    public void finishActiveGesture() {
        if (activeGesture != null) {
            finishGesture(activeGesture);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="PROPERTY CHANGE LISTENING">
    //
    // PROPERTY CHANGE LISTENING
    //
    public void addPropertyChangeListener(PropertyChangeListener pl) {
        pcs.addPropertyChangeListener(pl);
    }
    
    public void addPropertyChangeListener(String string, PropertyChangeListener pl) {
        pcs.addPropertyChangeListener(string, pl);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener pl) {
        pcs.removePropertyChangeListener(pl);
    }
    
    public void removePropertyChangeListener(String string, PropertyChangeListener pl) {
        pcs.removePropertyChangeListener(string, pl);
    }

    //</editor-fold>
    
}