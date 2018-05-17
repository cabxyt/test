package com.fl21.cloud.api.util;


import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private boolean infoEnable = true;
    private boolean debugEnable = true;

    private Class<?> clazz;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Logger getLogger(Class<?> clazz) {
        Logger log = new Logger();
        log.clazz = clazz;
        return log;
    }

    private Logger(){
    }

    public void info(String s, String... args) {
        if(infoEnable) {
            System.out.println(format.format(new Date()) + " (" + clazz.getSimpleName() + ") [INFO] " + String.format(s, args));
        }
    }

    public void debug(String s, String... args) {
        if (debugEnable) {
            System.out.println(format.format(new Date()) + "(" + clazz.getSimpleName() + ") [DEBUG] " + String.format(s, args));
        }
    }

    public void error(String s, String... args) {
        System.err.println(format.format(new Date()) + "(" + clazz.getSimpleName() + ") [ERROR] " + String.format(s, args));
    }


}
