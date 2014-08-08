/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.thingml.java;

import org.thingml.java.ext.Event;

import java.util.List;

/**
 * @author bmori
 */
public class HandlerHelper {

    private IHandler[][] handlers;

    public HandlerHelper() {

    }

    public HandlerHelper init(final List<IState> states, final List<Handler> transitions) {
        int index = 0;
        handlers = new IHandler[states.size()][];
        for (IState is : states) {
            AtomicState s = (AtomicState) is;
            s.ID = index;
            index++;
            int indexT = 0;
            for (Handler h : transitions) {//this loop initializes the array with the proper size
                if (h.getSource().equals(s)) {
                    indexT++;
                }
            }
            handlers[s.ID] = new IHandler[indexT];
            indexT = 0;
            for (Handler h : transitions) {
                if (h.getSource().equals(s)) {
                    handlers[s.ID][indexT] = h;
                    indexT++;
                }
            }
        }
        return this;
    }

    public IHandler getActiveHandler(final IState current, final Event e, final Port port) {
        for (IHandler h : handlers[((AtomicState) current).ID]) {
            if (h != null && h.check(e, port)) {
                return h;
            }
        }
        return new NullHandler(e.getType(), current);
    }
}
