/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.thingml.java;

import org.thingml.java.ext.Event;
import org.thingml.java.ext.EventType;

import java.util.*;

/**
 *
 * @author bmori
 */
public class HandlerHelper {

    private IHandler[][] handlers;

    //private final Map<IState, Map<EventType, List<IHandler>>> map;

    public HandlerHelper() {
        //map = new HashMap<IState, Map<EventType, List<IHandler>>>();
    }
    
    public HandlerHelper init(final List<IState> states, final List<Handler> transitions) {
        int index = 0;
        int indexT = 0;
        handlers = new IHandler[states.size()][transitions.size()]; //TODO: we should optimize the size of the handlers arrays (currently safe, but likely too big)
        for (IState is : states) {
            AtomicState s = (AtomicState)is;
            s.ID = index;
            index++;
            //Map<EventType, List<IHandler>> tmap = new HashMap<EventType, List<IHandler>>();
            //map.put(s, tmap);
            indexT = 0;
            for (Handler h : transitions) {
                if(h.getSource().equals(s)) {
                    handlers[s.ID][indexT] = h;
                    indexT++;
                    /*List <IHandler> handlers = tmap.get(h.getEvent());
                    if (handlers == null) handlers = new ArrayList<IHandler>();
                    handlers.add(h);
                    tmap.put(h.getEvent(), handlers);*/
                }
            }
            /*transitions.stream().filter(h -> h.getSource().equals(s)).forEach(h -> {
                List <IHandler> handlers = tmap.getOrDefault(h.getEvent(), new ArrayList<>());
                handlers.add(h);
                tmap.put(h.getEvent(), handlers);
            });*///Wonderful Java 8 implementation...
            /*if (s instanceof CompositeState) {
                CompositeState c = (CompositeState) s;
                //c.regions.forEach(r -> init(r.states, r.transitions));
                for(Region r : c.regions) {
                    init(r.states, r.transitions);
                }
            }*/
        }
        return this;
    }

    public IHandler getActiveHandler(final IState current, final Event e, final Port port) {
        for(IHandler h : handlers[((AtomicState)current).ID]) {
            if (h!=null && h.check(e, port)) {
                return h;
            }
        }

        /*final Map <EventType, List<IHandler>> events = map.get(current);
        if (events != null) {
            final List<IHandler> handlers = events.get(e.getType());
            if (handlers != null) {
                for (IHandler h : handlers) {
                    if (h.check(e, port)) {
                        return h;
                    }
                }
            }
        }*/
        return new NullHandler(e.getType(), current);

        /*return map.getOrDefault(current, Collections.emptyMap()).getOrDefault(e.getType(), Collections.emptyList()).stream()//get the list (potentially empty but not null) of potential handlers
                .filter(p -> p.check(e, port)).findFirst()//find the handler that actually could be triggered
                .orElse(new NullHandler(e.getType(), current));//or return a NullHandler if none can be triggered*///Wonderful Java 8 implementation...
    }
}
