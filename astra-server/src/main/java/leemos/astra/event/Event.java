package leemos.astra.event;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Event
 *
 * @author lihao
 * @date 2022-03-12
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public class Event {
    private EventType type;
}
