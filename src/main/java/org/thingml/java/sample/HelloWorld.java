/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.thingml.java.sample;

import org.thingml.java.*;
import org.thingml.java.ext.NullStateAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author bmori
 */
public class HelloWorld {

    public static void main(String args[]) {
        //Event types
        final HelloEventType helloEventType = new HelloEventType();
        
        //Default region of composite
        IState s1 = new AtomicState("start");
        IState s2 = new AtomicState("hello");
        Transition t1 = new Transition("sayHello", new HelloHandlerAction(), helloEventType, s1, s2);

        List<IState> states = new ArrayList<>();
        states.add(s1);
        states.add(s2);

        List<Handler> transitions = new ArrayList<>();
        transitions.add(t1);

        CompositeState c = new CompositeStateST("c", states, s1, transitions, new NullStateAction(), Collections.EMPTY_LIST, false);

        c.onEntry();
        c.dispatch(helloEventType.instantiate("world"));
    }
}
