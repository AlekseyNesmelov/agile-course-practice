package ru.unn.agile.Complex.viewmodel;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import ru.unn.agile.Complex.model.Complex;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.List;

public class ViewModel {
    public enum Errors {
        ZERO_DIVIDER("Divider can't be zero!"),
        BAD_FORMAT("Invalid format!"),
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
    private final ObjectProperty<Operation> operation = new SimpleObjectProperty<Operation>();
    private final BooleanProperty canCalculate = new SimpleBooleanProperty();
    private final List<ChangedValueListener> changedValueListeners = new ArrayList<>();
    private final ObjectProperty<ObservableList<Operation>> operations =
            new SimpleObjectProperty<>(FXCollections.observableArrayList(Operation.values()));
    final List<StringProperty> fields = new ArrayList<StringProperty>() {
        {
            add(firstReal);
            add(firstImaginary);
            add(secondReal);
            add(secondImaginary);
        }
    };

    public ViewModel() {
        for (StringProperty field : fields) {
            field.set("0.0");
        }
        result.set("");
        errors.set(Errors.NOT_ERROR.toString());
        operation.set(Operation.ADD);
        canCalculate.set(false);

        for (StringProperty field : fields) {
            final ChangedValueListener listener = new ChangedValueListener();
            field.addListener(listener);
            changedValueListeners.add(listener);
        }
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

    public ObjectProperty<Operation> getOperationProperty() {
        return operation;
    }

    public BooleanProperty canCalculateProperty() {
        return canCalculate;
    }

    public ObjectProperty<ObservableList<Operation>> getOperationsProperty() {
        return operations;
    }

    public void calculate() {
        if (!canCalculate.get()) {
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
                    errors.set("Divider can't be zero!");
                }
                break;
            default:
                break;
        }
    }

    private void refreshInputErrors() {
        errors.set("");
        canCalculate.set(true);
        try {
            for (StringProperty field : fields) {
                if (!field.get().isEmpty()) {
                    Double.parseDouble(field.get());
                } else {
                    canCalculate.set(false);
                }
            }
        } catch (NumberFormatException e) {
            canCalculate.set(false);
            errors.set(Errors.BAD_FORMAT.toString());
        }
    }

    private class ChangedValueListener implements ChangeListener<String> {
        @Override
        public void changed(final ObservableValue<? extends String> observable,
                            final String oldValue, final String newValue) {
            refreshInputErrors();
        }
    }
}
