package leemos.astra.core;

import leemos.astra.event.Event;
import leemos.astra.event.EventBus;
import leemos.astra.event.EventType;

import java.util.TimerTask;

public class ElectionTask  extends TimerTask {
    @Override
    public void run() {
        EventBus.get().fireEvent(new Event(EventType.CONVERSION_TO_CANDIDATE));
    }
}
