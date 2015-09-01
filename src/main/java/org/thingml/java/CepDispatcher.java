package org.thingml.java;

import org.thingml.java.ext.Event;
import org.thingml.java.ext.EventType;
import rx.subjects.PublishSubject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ludovic
 */
public class CepDispatcher {
    private Map<EventType,List<PublishSubject<Event>>> rxRegister;

    public CepDispatcher() {
        rxRegister = new HashMap<EventType, List<PublishSubject<Event>>>();
    }

    public void addSubs(EventType event, PublishSubject subs) {
        List<PublishSubject<Event>> subjectList = rxRegister.get(event);
        if(subjectList == null) {
            subjectList = new ArrayList<PublishSubject<Event>>();
            subjectList.add(subs);
            rxRegister.put(event,subjectList);
        } else {
            subjectList.add(subs);
        }
    }

    public void dispatch(Event event) {
        List<PublishSubject<Event>> listSubjects = rxRegister.get(event.getType());
        if(listSubjects != null) {
            for (PublishSubject subject : rxRegister.get(event.getType())) {
                subject.onNext(event);
            }
        }
    }
}
