/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.thingml.java;

import org.thingml.java.*;
import org.thingml.java.ext.NullEventType;
import org.thingml.java.ext.NullHandlerAction;
import org.thingml.java.ext.NullStateAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.*;

/**
 *
 * @author bmori
 */
public class Sample1Test extends TestCase {

    public void test() {
        //Event types
        final NullEventType nullEventType = new NullEventType();
        
        
        //Default region of composite
        IState s1 = new AtomicState("s1");
        IState s2 = new AtomicState("s2");
        Transition t1 = new Transition("t1", new NullHandlerAction(), nullEventType, null, s1, s2);

        List<IState> states = new ArrayList<IState>();
        states.add(s1);
        states.add(s2);

        List<Handler> transitions = new ArrayList<Handler>();
        transitions.add(t1);

        
        //region 1
        IState s1_r = new AtomicState("s1_r");
        IState s2_r = new AtomicState("s2_r");
        Transition t1_r = new Transition("t1_r", new NullHandlerAction(), nullEventType, null, s1_r, s2_r);

        List<IState> states_r = new ArrayList<IState>();
        states_r.add(s1_r);
        states_r.add(s2_r);

        List<Handler> transitions_r = new ArrayList<Handler>();
        transitions_r.add(t1_r);
        Region r = new Region("r", states_r, s1_r, transitions_r, false);
        
        //region 2
        IState s1_r2 = new AtomicState("s1_r2");
        List<IState> states_r2 = new ArrayList<IState>();
        states_r2.add(s1_r2);
        Region r2 = new Region("r2", states_r2, s1_r2, Collections.EMPTY_LIST, false);
        

        //Composite
        List<Region> regions = new ArrayList<Region>();
        regions.add(r);
        regions.add(r2);
        
        CompositeState c = new CompositeState("c", states, s1, transitions, new NullStateAction(), regions, false);
        
        
        //Root composite
        //Default region of root composite
        IState s1_root = new AtomicState("s1_root");
        IState s2_root = new AtomicState("s2_root");
        Transition t1_root = new Transition("t1_root", new NullHandlerAction(), nullEventType, null, s1_root, s2_root);
        Transition t2_root = new Transition("t2_root", new NullHandlerAction(), nullEventType, null, s2_root, c);

        List<IState> states_root = new ArrayList<IState>();
        states_root.add(s1_root);
        states_root.add(s2_root);
        states_root.add(c);

        List<Handler> transitions_root = new ArrayList<Handler>();
        transitions_root.add(t1_root);
        transitions_root.add(t2_root);
        
        CompositeState root = new CompositeState("root", states_root, s1_root, transitions_root, new NullStateAction(), Collections.EMPTY_LIST, false);
        
        
        
        root.onEntry();
        Assert.assertEquals(root.getRegions().get(0).getCurrent(), s1_root);
        Assert.assertEquals(c.getRegions().get(0).getCurrent(), s1);
        root.dispatch(nullEventType.instantiate(), null);//s1_root --> s2_root
        Assert.assertEquals(root.getRegions().get(0).getCurrent(), s2_root);
        Assert.assertEquals(c.getRegions().get(0).getCurrent(), s1);
    }
}
