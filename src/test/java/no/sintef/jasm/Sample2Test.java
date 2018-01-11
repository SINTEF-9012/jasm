/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.jasm;

import junit.framework.TestCase;

/**
 * @author bmori
 */
public class Sample2Test extends TestCase {

    public static void test() {
        //Event types
        final HelloEventType helloEventType = new HelloEventType();

        //Default region of composite
        AtomicState s1 = new AtomicState("s1");
        AtomicState s2 = new AtomicState("s2");
        AtomicState s3 = new AtomicState("s3");
        Transition t1 = new Transition();
        t1.from(s1).to(s2).event(helloEventType);
        Transition t2 = new Transition();
        t2.from(s2).to(s3).event(helloEventType);

        //Composite
        CompositeState c = new CompositeState("c");
        c.add(s1).add(s2).add(s3).initial(s1).build();

        //Root composite
        //Default region of root composite
        AtomicState s4 = new AtomicState("s4");
        Transition t1_root = new Transition();
        t1_root.from(c).to(s4).event(helloEventType);

        CompositeState root = new CompositeState("root");
        root.add(c).add(s4).initial(c).build();

        try {
            final Status status = new Status();
            root.onEntry.execute();//c.onEntry, s1.onEntry
            assertEquals(c, root.regions[0].current);
            assertEquals(s1, c.regions[0].current);
            root.handle(helloEventType.instantiate("world"), status);//s1 --> s2 (not c-->s4)
            assertEquals(c, root.regions[0].current);
            assertEquals(s2, c.regions[0].current);
            root.handle(helloEventType.instantiate("world"), status);//s2 --> s3 (not c-->s4)
            assertEquals(c, root.regions[0].current);
            assertEquals(s3, c.regions[0].current);
            root.handle(helloEventType.instantiate("world"), status);//c --> s4
            assertEquals(s4, root.regions[0].current);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
