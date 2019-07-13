package com.kraftlabs.crm_new.globalexceptionhandler;

import android.app.Activity;

/**
 * Activity based Exception handler ...
 */
public class MyExceptionHandler implements Thread.UncaughtExceptionHandler {

    public static final String EXTRA_MY_EXCEPTION_HANDLER = "EXTRA_MY_EXCEPTION_HANDLER";
    private final Activity context;
    private final Thread.UncaughtExceptionHandler rootHandler;

    public MyExceptionHandler(Activity context) {
        this.context = context;
        // we should store the current exception handler -- to invoke it for all not handled exceptions ...
        rootHandler = Thread.getDefaultUncaughtExceptionHandler();
        // we replace the exception handler now with us -- we will properly dispatch the exceptions ...
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(final Thread thread, final Throwable ex) {

            rootHandler.uncaughtException(thread, ex);

    }
}
