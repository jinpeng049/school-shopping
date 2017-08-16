package com.zj.shop.utils;


import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggerFactory;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

public class ShoppingLogs extends Logger {

    public static ThreadLocal<LogInfo> threadLocal = new ThreadLocal<LogInfo>() {

    };

    public static LogInfo getLogInfo() {
        LogInfo logInfo = threadLocal.get();
        if (logInfo == null) {
            logInfo = new LogInfo();
            threadLocal.set(logInfo);
            return logInfo;
        }
        return logInfo;
    }

    protected ShoppingLogs(String name) {
        super(name);
    }

    public static ShoppingLogs getLogger(String name) {
        return (ShoppingLogs) LogManager.getLogger(name, new LogLoggerFactory());
    }

    public static ShoppingLogs getLogger(Class clazz) {
        return (ShoppingLogs) LogManager.getLogger(clazz.getName(),
                new LogLoggerFactory());
    }

    /**
     * DEBUG级别日志输出<br/>
     * 日志内容包含存于线程空间中的LogInfo的值，输出格式：<br/>
     * <code>[type:sid][key1:value1,...,keyN:valueN]message</code>
     *
     * @param message 日志主体内容
     */
    public void debug(Object message) {
        if (repository.isDisabled(Level.DEBUG_INT))//判断该日志级别是否启用
            return;
        if (Level.DEBUG.isGreaterOrEqual(getEffectiveLevel()))
            forcedLog(FQCN, Level.DEBUG, message, null);
    }

    /**
     * INFO级别日志输出<br/>
     * 日志内容包含存于线程空间中的LogInfo的值，输出格式：<br/>
     * <code>[type:sid][key1:value1,...,keyN:valueN]message</code>
     *
     * @param message 日志主体内容
     */
    public void info(Object message) {
        if (repository.isDisabled(Level.INFO_INT))//判断该日志级别是否启用
            return;
        if (Level.INFO.isGreaterOrEqual(getEffectiveLevel()))
            forcedLog(FQCN, Level.INFO, message, null);
    }

    /**
     * WARN级别日志输出<br/>
     * 日志内容包含存于线程空间中的LogInfo的值，输出格式：<br/>
     * <code>[type:sid][key1:value1,...,keyN:valueN]message</code>
     *
     * @param message 日志主体内容
     */
    public void warn(Object message) {
        if (repository.isDisabled(Level.WARN_INT))//判断该日志级别是否启用
            return;
        if (Level.WARN.isGreaterOrEqual(getEffectiveLevel()))
            forcedLog(FQCN, Level.WARN, message, null);
    }

    /**
     * ERROR级别日志输出<br/>
     * 日志内容包含存于线程空间中的LogInfo的值，输出格式：<br/>
     * <code>[type:sid][key1:value1,...,keyN:valueN]message</code>
     *
     * @param message 日志主体内容
     */
    public void error(Object message) {
        if (repository.isDisabled(Level.ERROR_INT))//判断该日志级别是否启用
            return;
        if (Level.ERROR.isGreaterOrEqual(getEffectiveLevel()))
            forcedLog(FQCN, Level.ERROR, message, null);
    }

    /**
     * ERROR级别日志输出，用于输出异常日志<br/>
     * 日志内容包含存于线程空间中的LogInfo的值，输出格式：<br/>
     * <code>[type:sid][key1:value1,...,keyN:valueN]message</code>
     *
     * @param message 日志主体内容
     * @param t       Throwable对象
     */
    public void error(Object message, Throwable t) {
        if (repository.isDisabled(Level.ERROR_INT))//判断该日志级别是否启用
            return;
        if (Level.ERROR.isGreaterOrEqual(getEffectiveLevel()))
            forcedLog(FQCN, Level.ERROR, message, t);
    }

    @Override
    protected void forcedLog(String fqcn, Priority level, Object message,
                             Throwable t) {
        super.forcedLog(fqcn, level, formatLogMessage(message), t);
    }

    protected Object formatLogMessage(Object message) {
        if (message != null && message instanceof String) {
            LogInfo logInfo = threadLocal.get();
            if (logInfo == null) {
                logInfo = new LogInfo();
                threadLocal.set(logInfo);
            }
            String rawText = message.toString();

            StringBuffer newText = new StringBuffer();
            newText.append("[transid:" + logInfo.getTransid() + "] ");

            newText.append("[");
            for (String key : logInfo.keywords.keySet()) {
                newText.append(key + ":" + logInfo.keywords.get(key) + ",");
            }
            if (null != logInfo.keywords && !logInfo.keywords.isEmpty()) {
                newText = new StringBuffer(newText.substring(0, newText.length() - 1));
            }

            newText.append("]");

            newText.append(rawText);

            return newText.toString();
        }

        return message;
    }

    static class LogLoggerFactory implements LoggerFactory {
        public Logger makeNewLoggerInstance(String s) {
            return new ShoppingLogs(s);
        }
    }


    private static final String FQCN;

    static {
        FQCN = (ShoppingLogs.class).getName();
    }


    public static class LogInfo implements Serializable {
        private static final long serialVersionUID = 7218603473727421030L;

        private final Hashtable<String, String> keywords = new Hashtable<>();
        private long startedOn;
        private String agentId;
        private String transid;

        public LogInfo(Map<String, String> keywords) {
            super();
            if (keywords != null)
                this.keywords.putAll(keywords);
            this.startedOn = System.currentTimeMillis();
            this.transid = UUID.randomUUID().toString();
        }

        public LogInfo() {
            super();
            this.startedOn = System.currentTimeMillis();
            this.transid = UUID.randomUUID().toString();
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("LogInfo{");
            sb.append("keywords=").append(keywords);
            sb.append(", startedOn=").append(startedOn);
            sb.append(", agentId='").append(agentId).append('\'');
            sb.append(", transid='").append(transid).append('\'');
            sb.append('}');
            return sb.toString();
        }

        public Hashtable<String, String> getKeywords() {
            return keywords;
        }

        public long getStartedOn() {
            return startedOn;
        }

        public void setStartedOn(long startedOn) {
            this.startedOn = startedOn;
        }

        public String getAgentId() {
            return agentId;
        }

        public void setAgentId(String agentId) {
            this.agentId = agentId;
        }

        public String getTransid() {
            return transid;
        }

        public void setTransid(String transid) {
            this.transid = transid;
        }
    }


}