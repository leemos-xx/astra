package leemos.astra;

import lombok.Getter;
import lombok.Setter;

/**
 * Instruction 指令
 * 
 * @author lihao
 * @date 2022-02-27
 * @version 1.0
 */
@Setter
@Getter
public class Instruction<T> {

    private String key;
    private T value;
    
    public Instruction(String key, T value) {
        this.key = key;
        this.value = value;
    }
}
