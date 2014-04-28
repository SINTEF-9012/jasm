/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.thingml.java;

/**
 *
 * @author bmori
 */
public class DispatchStatus {
    boolean status = false;
    
    public synchronized void update(boolean update) {
        status |= update;
    }
}
