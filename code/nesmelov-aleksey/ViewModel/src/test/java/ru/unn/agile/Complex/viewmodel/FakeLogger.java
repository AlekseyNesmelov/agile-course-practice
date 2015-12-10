package ru.unn.agile.Complex.viewmodel;

import java.util.*;

public class FakeLogger implements ILogger {

    private ArrayList<String> log = new ArrayList<>();

    @Override
    public void addToLog(String message) {
        log.add(message);
    }

    @Override
    public List<String> getLog() {
        return log;
    }
}
