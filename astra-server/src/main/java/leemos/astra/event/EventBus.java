package leemos.astra.event;

import leemos.astra.Consensus;
import leemos.astra.NodeConfig;
import leemos.astra.core.StandardLog;
import leemos.astra.core.StandardNode;
import leemos.astra.core.StandardStateMachine;

import java.util.ArrayList;

/**
 * EventBus
 *
 * @author lihao
 * @date 2022-03-12
 * @version 1.0
 */
public class EventBus {

    private static volatile EventBus singleton;

    private ArrayList<EventListener> listeners = new ArrayList<>();

    public static EventBus get() {
        if (singleton == null) {
            synchronized (EventBus.class) {
                if (singleton == null) {
                    singleton = new EventBus();
                }
            }
        }
        return singleton;
    }

    public synchronized void addListener(EventListener listener) {
        listeners.add(listener);
    }

    public synchronized void fireEvent(Event event) {
        for (EventListener listener: listeners) {
            listener.fireEvent(event);
        }
    }
}
