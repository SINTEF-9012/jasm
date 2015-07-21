package org.thingml.java;

import org.thingml.java.ext.Event;
import rx.subjects.PublishSubject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ludovic
 */
public class CepDispatcher {
    private Map<Event,List<PublishSubject<Event>>> rxRegister;

    public CepDispatcher() {
        rxRegister = new HashMap<Event, List<PublishSubject<Event>>>();
    }

    public void addSubs(Event event, PublishSubject subs) {
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
        List<PublishSubject<Event>> listSubjects = rxRegister.get(event);
        if(listSubjects != null) {
            for (PublishSubject subject : listSubjects) {
                subject.onNext(event);
            }
        }
    }
}
