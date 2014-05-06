/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.thingml.java.sample;

import org.thingml.java.ext.Event;
import org.thingml.java.ext.EventType;


/**
 *
 * @author bmori
 */
public class HelloEvent extends Event {

    public final String who;
    
    protected HelloEvent(EventType type, String who) {
        super(type);
        this.who = who;
    }
    
}
