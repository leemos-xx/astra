package leemos.astra.core;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import leemos.astra.LogEntry;
import leemos.astra.StateMachine;

/**
 * StandardStateMachine
 * 
 * @author lihao
 * @date 2022-03-08
 * @version 1.0
 */
public class StandardStateMachine implements StateMachine {

    private static volatile StandardStateMachine singleton;

    private ConcurrentHashMap<String, Object> sm = new ConcurrentHashMap<String, Object>();

    public static StandardStateMachine get() {
        if (singleton == null) {
            synchronized (StandardStateMachine.class) {
                if (singleton == null) {
                    singleton = new StandardStateMachine();
                }
            }
        }
        return singleton;
    }

    private StandardStateMachine() {}

    @Override
    public void apply(LogEntry logEntry) {
        sm.put(logEntry.getInstruction().getKey(), logEntry.getInstruction().getValue());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String key) {
        return (T) sm.get(key);
    }

}
