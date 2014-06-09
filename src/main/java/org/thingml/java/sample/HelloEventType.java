/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.thingml.java.sample;

import org.thingml.java.Port;
import org.thingml.java.ext.Event;
import org.thingml.java.ext.EventType;

/**
 *
 * @author bmori
 */
public class HelloEventType extends EventType {

    public HelloEventType() {
        name = "hello";
    }
    
    public Event instantiate(Port port, String who) {
        return new HelloEvent(this, port, who);
    }

}
