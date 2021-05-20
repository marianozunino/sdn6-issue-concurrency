package com.example.demo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.Callable;

@Component
@Scope("prototype")
public class WorkerThread implements Callable<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkerThread.class);

    private final String request;

    public WorkerThread(String request) {
        this.request = request;
    }

    @Override
    public String call() throws Exception {
        LOGGER.debug("Thread started [" + request + "]");
        return doWork();
    }

    private String doWork() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            LOGGER.error("An unexpected interrupt exception occurred!", e);
        }

        return "Request [" + request + "] " + createUUID();
    }

    private String createUUID() {
        UUID id = UUID.randomUUID();
        return id.toString();
    }

}
