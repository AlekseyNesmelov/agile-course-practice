package ru.unn.agile.Complex.viewmodel;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import ru.unn.agile.Complex.model.Complex;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.List;

public class ComplexViewModel {
    public enum Errors {
        ZERO_DIVIDER("Divider can't be zero!"),
        BAD_FORMAT("Invalid format!"),
        EMPTY_LINE("Empty line!"),
        NOT_ERROR("");

        private String errorMessage;

        Errors(final String errorMessage) {
            this.errorMessage = errorMessage;
        }

        @Override
        public String toString() {
            return errorMessage;
        }
    }
    private final StringProperty firstReal = new SimpleStringProperty();
    private final StringProperty firstImaginary = new SimpleStringProperty();
    private final StringProperty secondReal = new SimpleStringProperty();
    private final StringProperty secondImaginary = new SimpleStringProperty();
    private final StringProperty result = new SimpleStringProperty();
    private final StringProperty errors = new SimpleStringProperty();
    private final StringProperty log = new SimpleStringProperty();
    private final ObjectProperty<Operation> operation = new SimpleObjectProperty<Operation>();
    private final BooleanProperty disabledCalculate = new SimpleBooleanProperty();
    private final List<ChangedValueListener> changedValueListeners = new ArrayList<>();
    private final ChangedErrorListener changedErrorListener = new ChangedErrorListener();
    private final ObjectProperty<ObservableList<Operation>> operations =
            new SimpleObjectProperty<>(FXCollections.observableArrayList(Operation.values()));
    private final List<StringProperty> fields = new ArrayList<StringProperty>() {
        {
            add(firstReal);
            add(firstImaginary);
            add(secondReal);
            add(secondImaginary);
        }
    };

    private ILogger logger = null;

    public ComplexViewModel(final ILogger logger) {
        setDefaultParameters();
        setLogger(logger);
    }

    public ComplexViewModel() {
        setDefaultParameters();
    }

    public void setLogger(final ILogger logger) {
        if (logger == null) {
            throw new IllegalArgumentException("Logger can't be null");
        }
        this.logger = logger;
    }

    private void setDefaultParameters() {
        for (StringProperty field : fields) {
            field.set("0.0");
        }
        result.set("");
        log.set("Log.");
        errors.set(Errors.NOT_ERROR.toString());
        operation.set(Operation.ADD);
        disabledCalculate.set(false);

        for (StringProperty field : fields) {
            final ChangedValueListener listener = new ChangedValueListener();
            field.addListener(listener);
            changedValueListeners.add(listener);
        }

        errors.addListener(changedErrorListener);
    }

    public StringProperty getFirstRealProperty() {
        return firstReal;
    }

    public StringProperty getFirstImaginaryProperty() {
        return firstImaginary;
    }

    public StringProperty getSecondRealProperty() {
        return secondReal;
    }

    public StringProperty getSecondImaginaryProperty() {
        return secondImaginary;
    }

    public StringProperty getResultProperty() {
        return result;
    }

    public StringProperty getErrorsProperty() {
        return errors;
    }

    public StringProperty getLogProperty() {
        return log;
    }

    public ObjectProperty<Operation> getOperationProperty() {
        return operation;
    }

    public BooleanProperty disabledCalculateProperty() {
        return disabledCalculate;
    }

    public ObjectProperty<ObservableList<Operation>> getOperationsProperty() {
        return operations;
    }

    public final ObservableList<Operation> getOperations() {
        return operations.get();
    }

    public final boolean getDisabledCalculate() {
        return disabledCalculate.get();
    }

    private void updateLog() {
        List<String> allFromLog = logger.getLog();
        String record = new String();
        for (String logString : allFromLog) {
            record += logString + "\n";
        }
        log.set(record);
    }

    public void calculate() {
        if (disabledCalculate.get()) {
            return;
        }
        double tmpReal = Double.parseDouble(firstReal.get());
        double tmpImaginary = Double.parseDouble(firstImaginary.get());
        Complex first = new Complex(tmpReal, tmpImaginary);

        tmpReal = Double.parseDouble(secondReal.get());
        tmpImaginary = Double.parseDouble(secondImaginary.get());
        Complex second = new Complex(tmpReal, tmpImaginary);

        result.set("");
        switch (operation.get()) {
            case ADD:
                result.set(first.add(second).toString());
                break;
            case SUBTRACT:
                result.set(first.subtract(second).toString());
                break;
            case MULTIPLY:
                result.set(first.multiply(second).toString());
                break;
            case DIVIDE:
                try {
                    result.set(first.divide(second).toString());
                } catch (IllegalArgumentException e) {
                    errors.set(Errors.ZERO_DIVIDER.toString());
                }
                break;
            default:
                break;
        }

        String message = LogMessage.PRESS_CALCULATE.toString();
        message += "Arguments were:"
                + " Re1 = " + firstReal.get()
                + " Im1 = " + firstImaginary.get()
                + " Re2 = " + secondReal.get()
                + " Im2 = " + secondImaginary.get() + "."
                + " Operation was: " + operation.get().toString() + ". ";
        if (errors.get().toString() == Errors.ZERO_DIVIDER.toString()) {
            message += "Result is unavailable due to the zero divider.";
        } else {
            message += "Result was: " + result.get() + ".";
        }
        logger.log(message);
        updateLog();
    }

    public List<String> getLog() {
        return logger.getLog();
    }

    public void log(final String message) {
        logger.log(message);
    }

    private void refreshInputErrors() {
        boolean isErrorChanged = false;

        disabledCalculate.set(false);
        if (firstReal.get().isEmpty() || firstImaginary.get().isEmpty()
                || secondReal.get().isEmpty() || secondImaginary.get().isEmpty()) {
            disabledCalculate.set(true);
            errors.set(Errors.EMPTY_LINE.toString());
            isErrorChanged = true;
        }
        try {
            for (StringProperty field : fields) {
                if (!field.get().isEmpty()) {
                    Double.parseDouble(field.get());
                }
            }
        } catch (NumberFormatException e) {
            disabledCalculate.set(true);
            errors.set(Errors.BAD_FORMAT.toString());
            isErrorChanged = true;
        }

        if (!isErrorChanged) {
            errors.set(Errors.NOT_ERROR.toString());
        }
    }

    public void onOperationChanged(final Operation oldValue, final Operation newValue) {
        if (!oldValue.equals(newValue)) {
            String message = LogMessage.CHANGE_OPERATION.toString() + newValue.toString();
            logger.log(message);
            updateLog();
        }
    }

    public void onInputFocusChanged() {
        for (ChangedValueListener listener : changedValueListeners) {
            if (listener.inputIsChanged()) {
                String message = LogMessage.UPDATE_INPUT
                        + " Re1 = " + firstReal.get() + ","
                        + " Im1 = " + firstImaginary.get() + ","
                        + " Re2 = " + secondReal.get() + ","
                        + " Im2 = " + secondImaginary.get() + ".";
                logger.log(message);
                updateLog();
                listener.setDefault();
                break;
            }
        }
    }

    private class ChangedValueListener implements ChangeListener<String> {
        private String previousValue = "";
        private String currentValue = "";
        @Override
        public void changed(final ObservableValue<? extends String> observable,
                            final String oldValue, final String newValue) {
            refreshInputErrors();
            if (!oldValue.equals(newValue)) {
                currentValue = newValue;
            }
        }

        public boolean inputIsChanged() {
            return !currentValue.equals(previousValue);
        }

        public void setDefault() {
            previousValue = currentValue;
        }
    }

    private class ChangedErrorListener implements ChangeListener<String> {
        @Override
        public void changed(final ObservableValue<? extends String> observable,
                            final String oldError, final String newError) {
            if (!oldError.equals(newError) && newError != Errors.NOT_ERROR.toString()) {
                String message = LogMessage.GET_ERROR.toString() + newError;
                logger.log(message);
                updateLog();
            }
        }
    }
}
