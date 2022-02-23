package leemos.astra.exception;

/**
 * 生命周期阶段内出现的异常。
 * 若抛出该异常，则代表该阶段无法正常完成。
 *
 * @author lihao
 * @date 2022年2月23日
 * @version 1.0
 */
public class LifecycleException extends Exception {

    private static final long serialVersionUID = 1L;

    public LifecycleException() {

    }

    public LifecycleException(String message) {
        super(message);
    }

    public LifecycleException(Throwable throwable) {
        super(throwable);
    }

    public LifecycleException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
