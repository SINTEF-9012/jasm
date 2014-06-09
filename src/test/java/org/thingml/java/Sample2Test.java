/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.thingml.java;

import org.thingml.java.*;
import org.thingml.java.ext.DebugHandlerAction;
import org.thingml.java.ext.NullEventType;
import org.thingml.java.ext.NullStateAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.*;
import org.thingml.java.sample.HelloEventType;

/**
 *
 * @author bmori
 */
public class Sample2Test extends TestCase {

    public static void test() {
        //Event types
        final NullEventType nullEventType = new NullEventType();
        final HelloEventType helloEventType = new HelloEventType();
        
        //Default region of composite
        IState s1 = new AtomicState("s1");
        IState s2 = new AtomicState("s2");
        IState s3 = new AtomicState("s3");
        Transition t1 = new Transition("t1", new DebugHandlerAction(), helloEventType, null, s1, s2);
        Transition t2 = new Transition("t2", new DebugHandlerAction(), helloEventType, null, s2, s3);
        

        List<IState> states = new ArrayList<IState>();
        states.add(s1);
        states.add(s2);
        states.add(s3);

        List<Handler> transitions = new ArrayList<Handler>();
        transitions.add(t1);    
        transitions.add(t2);

        //Composite
        CompositeState c = new CompositeState("c", states, s1, transitions, new NullStateAction(), Collections.EMPTY_LIST, false);
        
        //Root composite
        //Default region of root composite
        IState s4 = new AtomicState("s4");
        Transition t1_root = new Transition("t1_root", new DebugHandlerAction(), helloEventType, null, c, s4);

        List<IState> states_root = new ArrayList<IState>();
        states_root.add(c);
        states_root.add(s4);

        List<Handler> transitions_root = new ArrayList<Handler>();
        transitions_root.add(t1_root);
        
        CompositeState root = new CompositeState("root", states_root, c, transitions_root, new NullStateAction(), Collections.EMPTY_LIST, false);
        
        root.onEntry();//c.onEntry, s1.onEntry
        assertEquals(c, root.getRegions().get(0).getCurrent());
        assertEquals(s1, c.getRegions().get(0).getCurrent());
        root.dispatch(helloEventType.instantiate(null, "world"));//s1 --> s2 (not c-->s4)
        assertEquals(c, root.getRegions().get(0).getCurrent());
        assertEquals(s2, c.getRegions().get(0).getCurrent());
        root.dispatch(helloEventType.instantiate(null, "world"));//s2 --> s3 (not c-->s4)
        assertEquals(c, root.getRegions().get(0).getCurrent());
        assertEquals(s3, c.getRegions().get(0).getCurrent());
        root.dispatch(helloEventType.instantiate(null, "world"));//c --> s4
        assertEquals(s4, root.getRegions().get(0).getCurrent());
    }
}
