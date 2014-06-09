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
    private HelloEventType helloEventType;

    //Ports
    private Port p1;

    public HelloWorld(String name) {
        super(name);
    }

    public Component buildBehavior() {
        helloEventType = new HelloEventType();

        //Initialize ports
        List<EventType> in = new ArrayList<EventType>();
        in.add(helloEventType);
        p1 = new Port(PortType.PROVIDED, "hello", in, Collections.EMPTY_LIST);

        //Default region of composite
        IState s1 = new AtomicState("start");
        IState s2 = new AtomicState("hello");
        Handler t1 = new InternalTransition("sayHello", new HelloHandlerAction(), helloEventType, p1, s1);
        //Transition t1 = new Transition("sayHello", new HelloHandlerAction(), helloEventType, p1, s1, s2);

        List<IState> states = new ArrayList<IState>();
        states.add(s1);
        states.add(s2);

        List<Handler> transitions = new ArrayList<Handler>();
        transitions.add(t1);

        behavior = new CompositeState("root", states, s1, transitions, new NullStateAction(), Collections.EMPTY_LIST, false);
        return this;
    }

    public static void main(String args[]) {
         HelloWorld hw = (HelloWorld) new HelloWorld("HelloWorld").buildBehavior();
         hw.start();
         hw.receive(hw.helloEventType.instantiate(hw.p1, "world"));
    }

    private final class HelloHandlerAction implements IHandlerAction {

        public HelloHandlerAction() { }

        public boolean check(final Event e, final EventType t) {
            return true;
        }

        public void execute(final Event e) {
            HelloEvent ce = (HelloEvent) e;
            System.out.println("hello " + ce.who + "!");
        }

    }
}
