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

    private final Map<IState, Map<EventType, List<IHandler>>> map;

    public HandlerHelper() {
        map = new HashMap<>();
    }
    
    public HandlerHelper init(final List<IState> states, final List<Handler> transitions) {
        for (IState s : states) {
            Map<EventType, List<IHandler>> tmap = new HashMap<>();
            map.put(s, tmap);
            transitions.stream().filter(h -> h.getSource().equals(s)).forEach(h -> {
                List <IHandler> handlers = tmap.getOrDefault(h.getEvent(), new ArrayList<>());
                handlers.add(h);
                tmap.put(h.getEvent(), handlers);
            });
            if (s instanceof CompositeState) {
                CompositeState c = (CompositeState) s;
                c.regions.forEach(r -> init(r.states, r.transitions));
            }
        }
        return this;
    }

    public IHandler getActiveHandler(final IState current, final Event e, final Port port) {
        return map.getOrDefault(current, Collections.emptyMap()).getOrDefault(e.getType(), Collections.emptyList()).stream()//get the list (potentially empty but not null) of potential handlers
                .filter(p -> p.check(e, port)).findFirst()//find the handler that actually could be triggered
                .orElse(new NullHandler(e.getType(), current));//or return a NullHandler if none can be triggered
    }
}
