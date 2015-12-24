package ru.unn.agile.Complex.viewmodel;

public enum LogMessage {
    PRESS_CALCULATE("Calculate pressed. "),
    CHANGE_OPERATION("Operation was changed. Now it is "),
    UPDATE_INPUT("Updated input. Now it is "),
    GET_ERROR ("Get error: ");

    private final String name;

    private LogMessage(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
