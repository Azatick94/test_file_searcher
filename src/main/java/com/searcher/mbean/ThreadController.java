package com.searcher.mbean;

import com.searcher.Main;

public class ThreadController implements ThreadControllerMBean {
    @Override
    public void shutdownThread() {
        Main.exit = true;
    }
}
