package org.thingml.java.ext;

/**
 * This interface is an extension point for user-defined actions to be execute
 * on entry/exit of a state
 */
public interface IStateAction {

    void onEntry();

    void onExit();
}
