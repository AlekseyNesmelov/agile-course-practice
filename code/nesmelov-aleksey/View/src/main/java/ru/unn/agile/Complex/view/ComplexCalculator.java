package ru.unn.agile.Complex.view;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import ru.unn.agile.Complex.infrastructure.Logger;
import ru.unn.agile.Complex.viewmodel.*;

public class ComplexCalculator {
    @FXML
    private ComplexViewModel viewModel;
    @FXML
    private TextField firstRealTextField;
    @FXML
    private TextField firstImaginaryTextField;
    @FXML
    private TextField secondRealTextField;
    @FXML
    private TextField secondImaginaryTextField;
    @FXML
    private Text resultText;
    @FXML
    private Text errorsText;
    @FXML
    private TextArea logArea;
    @FXML
    private ComboBox<Operation> operationsComboBox;
    @FXML
    private Button calculateButton;

    @FXML
    void initialize() {
        viewModel.setLogger(new Logger("./Logger.log"));

        final ChangeListener<Boolean> focusInputChangeListener = new ChangeListener<Boolean>() {
            @Override
            public void changed(final ObservableValue<? extends Boolean> observable,
                                final Boolean oldValue, final Boolean newValue) {
                viewModel.onInputFocusChanged();
            }
        };

        StringProperty tmpProperty = viewModel.getFirstRealProperty();
        firstRealTextField.textProperty().bindBidirectional(tmpProperty);
        firstRealTextField.focusedProperty().addListener(focusInputChangeListener);

        tmpProperty = viewModel.getFirstImaginaryProperty();
        firstImaginaryTextField.textProperty().bindBidirectional(tmpProperty);
        firstImaginaryTextField.focusedProperty().addListener(focusInputChangeListener);

        tmpProperty = viewModel.getSecondRealProperty();
        secondRealTextField.textProperty().bindBidirectional(tmpProperty);
        secondRealTextField.focusedProperty().addListener(focusInputChangeListener);

        tmpProperty = viewModel.getSecondImaginaryProperty();
        secondImaginaryTextField.textProperty().bindBidirectional(tmpProperty);
        secondImaginaryTextField.focusedProperty().addListener(focusInputChangeListener);

        tmpProperty = viewModel.getResultProperty();
        resultText.textProperty().bindBidirectional(tmpProperty);

        tmpProperty = viewModel.getErrorsProperty();
        errorsText.textProperty().bindBidirectional(tmpProperty);

        tmpProperty = viewModel.getLogProperty();
        logArea.textProperty().bindBidirectional(tmpProperty);

        operationsComboBox.valueProperty().bindBidirectional(viewModel.getOperationProperty());
        operationsComboBox.valueProperty().addListener(new ChangeListener<Operation>() {
            @Override
            public void changed(final ObservableValue<? extends Operation> observable,
                                final Operation oldValue,
                                final Operation newValue) {
                viewModel.onOperationChanged(oldValue, newValue);
            }
        });

        calculateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent actionEvent) {
                viewModel.calculate();
            }
        });
    }
}
