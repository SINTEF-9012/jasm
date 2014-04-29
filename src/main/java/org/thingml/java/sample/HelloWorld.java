/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.thingml.java.sample;

import org.thingml.java.*;
import org.thingml.java.ext.Event;
import org.thingml.java.ext.EventType;
import org.thingml.java.ext.IHandlerAction;
import org.thingml.java.ext.NullStateAction;

import java.util.*;
import java.util.logging.Logger;

/**
 *
 * @author bmori
 */
public class HelloWorld extends Component {

    //Event types
    private final HelloEventType helloEventType = new HelloEventType();

    //Ports
    private Port p1;

    protected CompositeState buildBehavior() {
        //Initialize ports
        List<EventType> in = new ArrayList<>();
        in.add(helloEventType);
        p1 = new Port(PortType.PROVIDED, "hello", in, Collections.EMPTY_LIST);

        //Default region of composite
        IState s1 = new AtomicState("start");
        IState s2 = new AtomicState("hello");
        Transition t1 = new Transition("sayHello", new HelloHandlerAction(), helloEventType, p1, s1, s2);

        List<IState> states = new ArrayList<>();
        states.add(s1);
        states.add(s2);

        List<Handler> transitions = new ArrayList<>();
        transitions.add(t1);

        return new CompositeStateST("root", states, s1, transitions, new NullStateAction(), Collections.EMPTY_LIST, false);
    }

    public static void main(String args[]) {
         HelloWorld hw = new HelloWorld();
         hw.start();
         hw.receive(hw.helloEventType.instantiate("world"), hw.p1);
    }

    private final class HelloHandlerAction implements IHandlerAction {

        public HelloHandlerAction() { }

        public void execute(final Event e) {
            System.out.println("hello " + e.get("who") + "!");
        }

    }
}
