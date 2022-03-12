package leemos.astra.core;

import java.util.ArrayList;

import leemos.astra.Log;
import leemos.astra.LogEntry;

/**
 * StandardLog
 * 
 * @author lihao
 * @date 2022-03-08
 * @version 1.0
 */
public class StandardLog implements Log {

    private static final LogEntry EMPTY = LogEntry.builder().logIndex(-1).term(0).build();
    private static volatile StandardLog singleton;

    private ArrayList<LogEntry> entries = new ArrayList<LogEntry>();

    public static StandardLog get() {
        if (singleton == null) {
            synchronized (StandardLog.class) {
                if (singleton == null) {
                    singleton = new StandardLog();
                }
            }
        }
        return singleton;
    }

    private StandardLog() {}

    @Override
    public boolean contains(long logIndex) {
        return logIndex >= 0 && logIndex <= entries.size();
    }

    @Override
    public synchronized void write(LogEntry logEntry) {
        entries.add(logEntry);
    }

    @Override
    public synchronized LogEntry read(long logIndex) {
        if (!contains(logIndex)) {
            return null;
        }
        return entries.get((int) logIndex);
    }

    @Override
    public synchronized LogEntry last() {
        if (entries.size() == 0) {
            return EMPTY;
        }
        return entries.get(entries.size() - 1);
    }

    @Override
    public synchronized void truncate(long logIndex) {
        for (int i = entries.size() - 1; i >= logIndex; i--) {
            entries.remove(i);
        }
    }

}
