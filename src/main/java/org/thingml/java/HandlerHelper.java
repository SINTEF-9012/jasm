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

    public HandlerHelper(final List<IState> states, final List<Handler> transitions) {
        map = new HashMap<>(states.size() * 4 / 3 + 1);//perfect size to avoid re-hashing during init
        init(states, transitions);
    }
    
    private void init(final List<IState> states, final List<Handler> transitions) {
        for (IState s : states) {
            Map<EventType, List<IHandler>> tmap = new HashMap<>();
            map.put(s, tmap);
            for (Handler t : transitions) {//TODO: maybe use some Java8 features here to make the code nicer?
                if (t.getSource() == s) {
                    List<IHandler> handlers = tmap.get(t.getEvent());
                    if (handlers == null) {
                        handlers = new ArrayList<>();
                        tmap.put(t.getEvent(), handlers);
                    }
                    handlers.add(t);
                }
            }
            if (s instanceof CompositeState) {
                CompositeState c = (CompositeState) s;
                c.getRegions().forEach(r -> init(r.states, r.transitions));
            }
        }
    }

    public IHandler getActiveHandler(final IState current, final Event e) {
        List<IHandler> handlers = (map.get(current) != null) ? map.get(current).get(e.getType()) : null;
        IHandler result;
        if (handlers != null) {
            Optional<IHandler> status = handlers.stream().filter(p -> p.check(e)).findFirst();
            if (status.isPresent()) {
                result = status.get();
            } else {
                result = new NullHandler(e.getType(), current);
            }
        } else {
            result = new NullHandler(e.getType(), current);
        }
        return result;
    }
}
