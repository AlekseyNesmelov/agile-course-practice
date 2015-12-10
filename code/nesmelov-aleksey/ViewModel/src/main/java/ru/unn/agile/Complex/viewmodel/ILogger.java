package ru.unn.agile.Complex.viewmodel;

import java.util.List;

public interface ILogger {
    public void addToLog(final String message);

    public List<String> getLog();
}

