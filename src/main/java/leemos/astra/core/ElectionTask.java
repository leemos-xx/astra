package leemos.astra.core;

import leemos.astra.event.Event;
import leemos.astra.event.EventBus;
import leemos.astra.event.EventType;

import java.util.TimerTask;

/**
 * ElectionTask
 *
 * @author lihao
 * @date 2022-03-12
 * @version 1.0
 */
public class ElectionTask  extends TimerTask {
    @Override
    public void run() {
        EventBus.get().fireEvent(new Event(EventType.CONVERSION_TO_CANDIDATE));
    }
}
