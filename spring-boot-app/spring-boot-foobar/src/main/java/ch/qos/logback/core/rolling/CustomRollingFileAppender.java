package ch.qos.logback.core.rolling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author zhanghang
 * @date 2020/12/5 10:21 上午
 * *****************
 * function:
 */
@SuppressWarnings({"rawtypes"})
public class CustomRollingFileAppender<E> extends RollingFileAppender<E> {

    private static final Logger LOG = LoggerFactory.getLogger(CustomRollingFileAppender.class);

    private static final List<CustomRollingFileAppender> APPENDERS = new CopyOnWriteArrayList<>();

    static {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            String name = Thread.currentThread().getName();
            try {
                Thread.currentThread().setName("CustomLogbackTimer");
                for (CustomRollingFileAppender appender : APPENDERS) {
                    appender.checkRollover();
                }
            } catch (Exception e) {
                LOG.error("Try rollover failed, will retry after 1 minute.", e);
            } finally {
                Thread.currentThread().setName(name);
            }
        }, 1, 1, TimeUnit.MINUTES);
    }

    public CustomRollingFileAppender() {
        APPENDERS.add(this);
    }

    /**
     * see {@link #subAppend(Object)}
     */
    private void checkRollover() {
        if (!isStarted()) {
            return;
        }

        synchronized (triggeringPolicy) {
            if (triggeringPolicy.isTriggeringEvent(super.currentlyActiveFile, null)) {
                // time arrived, trigger
                rollover();
            }
        }
    }
}
