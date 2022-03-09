package leemos.astra.event;

import java.util.ArrayList;

public class EventBus {
    private ArrayList<EventListener> listeners = new ArrayList<>();

    public synchronized void addListener(EventListener listener) {
        listeners.add(listener);
    }
}
