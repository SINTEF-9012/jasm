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
        cpt.buildBehavior(null, null).init().start();
        try {
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(c, cpt.behavior.regions[0].current);
        assertEquals(s2, c.regions[0].current);
        assertEquals(s2_r, c.regions[1].current);
        assertEquals(s1_r2, c.regions[2].current);
    }

    private class Test1Component extends Component {

        public Test1Component(String name) {
            super(name);
        }

        @Override
        public Component buildBehavior(String session, Component root) {
            //Event types
            final NullEventType nullEventType = new NullEventType();

            //Default region of composite
            AtomicState s1 = new AtomicState("s1");
            s2 = new AtomicState("s2");
            Transition t1 = new Transition();
            t1.from(s1).to(s2);

            //region 1
            AtomicState s1_r = new AtomicState("s1_r");
            s2_r = new AtomicState("s2_r");
            Transition t1_r = new Transition();
            t1_r.from(s1_r).to(s2_r);

            Region r = new Region("r");
            r.add(s1_r).add(s2_r).initial(s2_r);

            //region 2
            s1_r2 = new AtomicState("s1_r2");
            Region r2 = new Region("r2");
            r2.add(s1_r2).initial(s1_r2);

            //Composite
            c = new CompositeState("c");
            c.add(s1).add(s2).initial(s1).add(r).add(r2).build();

            //Root composite
            //Default region of root composite
            AtomicState s1_root = new AtomicState("s1_root");
            AtomicState s2_root = new AtomicState("s2_root");
            Transition t1_root = new Transition();
            t1_root.from(s1_root).to(s2_root);
            Transition t2_root = new Transition();
            t2_root.from(s2_root).to(c);

            behavior = new CompositeState("root");
            behavior.add(s1_root).add(s2_root).add(c).initial(s1_root).build();
            return this;
        }
    }
}
