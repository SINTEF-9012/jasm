/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.thingml.java;

import junit.framework.TestCase;
import org.thingml.java.ext.NullEventType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author bmori
 */
public class Sample1Test extends TestCase {

    CompositeState c;
    AtomicState s2, s2_r, s1_r2;

    public void test() {
        final Test1Component cpt = new Test1Component("test1");
        cpt.buildBehavior().init().start();
        //cpt.receive(new NullEventType().instantiate(), null);
        try {
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(c, cpt.behavior.regions[0].getCurrent());
        assertEquals(s2, c.regions[0].getCurrent());
        assertEquals(s2_r, c.regions[1].getCurrent());
        assertEquals(s1_r2, c.regions[2].getCurrent());
    }

    private class Test1Component extends Component {

        public Test1Component(String name) {
            super(name);
        }

        @Override
        public Component buildBehavior() {
            //Event types
            final NullEventType nullEventType = new NullEventType();


            //Default region of composite
            AtomicState s1 = new AtomicState("s1");
            s2 = new AtomicState("s2");
            Transition t1 = new Transition("t1", nullEventType, null, s1, s2);

            List<AtomicState> states = new ArrayList<AtomicState>();
            states.add(s1);
            states.add(s2);

            List<Handler> transitions = new ArrayList<Handler>();
            transitions.add(t1);


            //region 1
            AtomicState s1_r = new AtomicState("s1_r");
            s2_r = new AtomicState("s2_r");
            Transition t1_r = new Transition("t1_r", nullEventType, null, s1_r, s2_r);

            List<AtomicState> states_r = new ArrayList<AtomicState>();
            states_r.add(s1_r);
            states_r.add(s2_r);

            List<Handler> transitions_r = new ArrayList<Handler>();
            transitions_r.add(t1_r);
            Region r = new Region("r", states_r, s1_r, transitions_r, false);

            //region 2
            s1_r2 = new AtomicState("s1_r2");
            List<AtomicState> states_r2 = new ArrayList<AtomicState>();
            states_r2.add(s1_r2);
            Region r2 = new Region("r2", states_r2, s1_r2, Collections.EMPTY_LIST, false);


            //Composite
            List<Region> regions = new ArrayList<Region>();
            regions.add(r);
            regions.add(r2);

            c = new CompositeState("c", states, s1, transitions, regions, false);


            //Root composite
            //Default region of root composite
            AtomicState s1_root = new AtomicState("s1_root");
            AtomicState s2_root = new AtomicState("s2_root");
            Transition t1_root = new Transition("t1_root", nullEventType, null, s1_root, s2_root);
            Transition t2_root = new Transition("t2_root", nullEventType, null, s2_root, c);

            List<AtomicState> states_root = new ArrayList<AtomicState>();
            states_root.add(s1_root);
            states_root.add(s2_root);
            states_root.add(c);

            List<Handler> transitions_root = new ArrayList<Handler>();
            transitions_root.add(t1_root);
            transitions_root.add(t2_root);

            behavior = new CompositeState("root", states_root, s1_root, transitions_root, Collections.EMPTY_LIST, false);
            return this;
        }
    }
}
