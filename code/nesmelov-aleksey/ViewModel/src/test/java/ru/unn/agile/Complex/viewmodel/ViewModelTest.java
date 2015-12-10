package test.java.ru.unn.agile.Complex.viewmodel;

import ru.unn.agile.Complex.viewmodel.FakeLogger;
import ru.unn.agile.Complex.viewmodel.Operation;
import ru.unn.agile.Complex.viewmodel.ViewModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ViewModelTest {
    private ViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new ViewModel(new FakeLogger());
    }

    @After
    public void tearDown() {
        viewModel = null;
    }

    @Test
    public void canSetDefaultFirstReal() {
        assertEquals("0.0", viewModel.getFirstRealProperty().get());
    }

    @Test
    public void canSetDefaultFirstImaginary() {
        assertEquals("0.0", viewModel.getFirstImaginaryProperty().get());
    }

    @Test
    public void canSetDefaultSecondReal() {
        assertEquals("0.0", viewModel.getSecondRealProperty().get());
    }

    @Test
    public void canSetDefaultSecondImaginary() {
        assertEquals("0.0", viewModel.getSecondImaginaryProperty().get());
    }

    @Test
    public void canSetDefaultResult() {
        assertEquals("", viewModel.getResultProperty().get());
    }

    @Test
    public void canSetDefaultErrors() {
        assertEquals("", viewModel.getErrorsProperty().get());
    }

    @Test
    public void canSetDefaultOperation() {
        assertEquals(Operation.ADD.toString(), viewModel.getOperationProperty().get().toString());
    }

    @Test
    public void canSetDefaultDisabledCalculate() {
        assertFalse(viewModel.disabledCalculateProperty().get());
    }

    @Test
    public void canNotCalculateWhenFieldsAreEmpty() {
        cleanData();
        assertTrue(viewModel.disabledCalculateProperty().get());
    }

    @Test
    public void canCalculateWhenFieldsAreFill() {
        setPositiveData();
        assertFalse(viewModel.disabledCalculateProperty().get());
    }

    @Test
    public void canNotCalculateWhenEnterNumbersAndDeleteIt() {
        setPositiveData();
        cleanData();
        assertTrue(viewModel.disabledCalculateProperty().get());
    }

    @Test
    public void canNotCalculateWhenFormatIsBad() {
        setPositiveData();
        viewModel.getFirstRealProperty().set("a");
        assertTrue(viewModel.disabledCalculateProperty().get());
    }

    @Test
    public void errorIsInvalidFormatWhenFormatIsBad() {
        viewModel.getFirstRealProperty().set("a");
        assertEquals("Invalid format!", viewModel.getErrorsProperty().get());
    }

    @Test
    public void errorsIsEmptyLineWhenDeleteNumberWithBadFormat() {
        viewModel.getFirstRealProperty().set("a");
        viewModel.getFirstRealProperty().set("");
        assertEquals("Empty line!", viewModel.getErrorsProperty().get());
    }

    @Test
    public void errorsIsEmptyWhenEnterNumberWithGoodFormat() {
        viewModel.getFirstRealProperty().set("a");
        viewModel.getFirstRealProperty().set("5");
        assertEquals("", viewModel.getErrorsProperty().get());
    }

    @Test
    public void canChangeOperation() {
        viewModel.getOperationProperty().set(Operation.MULTIPLY);
        assertEquals(Operation.MULTIPLY.toString(),
                     viewModel.getOperationProperty().get().toString());
    }

    @Test
    public void resultIsCorrectWhenAddPositiveComplexNumbers() {
        setPositiveData();
        viewModel.getOperationProperty().set(Operation.ADD);

        viewModel.calculate();

        assertEquals("3.0+4.0i", viewModel.getResultProperty().get());
    }

    @Test
    public void resultIsCorrectWhenAddNegativeComplexNumbers() {
        setNegativeData();
        viewModel.getOperationProperty().set(Operation.ADD);

        viewModel.calculate();

        assertEquals("-3.0-4.0i", viewModel.getResultProperty().get());
    }

    @Test
    public void resultIsCorrectWhenAddComplexNumberAndZero() {
        setComplexNumberAndZero();
        viewModel.getOperationProperty().set(Operation.ADD);

        viewModel.calculate();

        assertEquals("2.0+3.0i", viewModel.getResultProperty().get());
    }

    @Test
    public void resultIsCorrectWhenSubtractPositiveComplexNumbers() {
        setPositiveData();
        viewModel.getOperationProperty().set(Operation.SUBTRACT);

        viewModel.calculate();

        assertEquals("1.0+2.0i", viewModel.getResultProperty().get());
    }

    @Test
    public void resultIsCorrectWhenSubtractNegativeComplexNumbers() {
        setNegativeData();
        viewModel.getOperationProperty().set(Operation.SUBTRACT);

        viewModel.calculate();

        assertEquals("-1.0-2.0i", viewModel.getResultProperty().get());
    }

    @Test
    public void resultIsCorrectWhenSubtractComplexNumberAndZero() {
        setComplexNumberAndZero();
        viewModel.getOperationProperty().set(Operation.SUBTRACT);

        viewModel.calculate();

        assertEquals("2.0+3.0i", viewModel.getResultProperty().get());
    }

    @Test
    public void resultIsCorrectWhenMultiplyPositiveComplexNumbers() {
        setPositiveData();
        viewModel.getOperationProperty().set(Operation.MULTIPLY);

        viewModel.calculate();

        assertEquals("-1.0+5.0i", viewModel.getResultProperty().get());
    }

    @Test
    public void resultIsCorrectWhenMultiplyNegativeComplexNumbers() {
        setNegativeData();
        viewModel.getOperationProperty().set(Operation.MULTIPLY);

        viewModel.calculate();

        assertEquals("-1.0+5.0i", viewModel.getResultProperty().get());
    }

    @Test
    public void resultIsCorrectWhenMultiplyComplexNumberAndZero() {
        setComplexNumberAndZero();
        viewModel.getOperationProperty().set(Operation.MULTIPLY);

        viewModel.calculate();

        assertEquals("0.0", viewModel.getResultProperty().get());
    }

    @Test
    public void resultIsCorrectWhenDividePositiveComplexNumbers() {
        setPositiveData();
        viewModel.getOperationProperty().set(Operation.DIVIDE);

        viewModel.calculate();

        assertEquals("2.5+0.5i", viewModel.getResultProperty().get());
    }

    @Test
    public void resultIsCorrectWhenDivideNegativeComplexNumbers() {
        setNegativeData();
        viewModel.getOperationProperty().set(Operation.DIVIDE);

        viewModel.calculate();

        assertEquals("2.5+0.5i", viewModel.getResultProperty().get());
    }

    @Test
    public void resultIsEmptyWhenDivideComplexNumberAndZero() {
        setComplexNumberAndZero();
        viewModel.getOperationProperty().set(Operation.DIVIDE);

        viewModel.calculate();

        assertEquals("", viewModel.getResultProperty().get());
    }

    @Test
    public void errorMessageWhenDivideComplexNumberAndZero() {
        setComplexNumberAndZero();
        viewModel.getOperationProperty().set(Operation.DIVIDE);

        viewModel.calculate();

        assertEquals("Divider can't be zero!", viewModel.getErrorsProperty().get());
    }

    private void setPositiveData() {
        viewModel.getFirstRealProperty().set("2");
        viewModel.getFirstImaginaryProperty().set("3");
        viewModel.getSecondRealProperty().set("1");
        viewModel.getSecondImaginaryProperty().set("1");
    }
    private void setNegativeData() {
        viewModel.getFirstRealProperty().set("-2");
        viewModel.getFirstImaginaryProperty().set("-3");
        viewModel.getSecondRealProperty().set("-1");
        viewModel.getSecondImaginaryProperty().set("-1");
    }
    private void  setComplexNumberAndZero() {
        viewModel.getFirstRealProperty().set("2");
        viewModel.getFirstImaginaryProperty().set("3");
        viewModel.getSecondRealProperty().set("0");
        viewModel.getSecondImaginaryProperty().set("0");
    }
    private void cleanData() {
        viewModel.getFirstRealProperty().set("");
        viewModel.getFirstImaginaryProperty().set("");
        viewModel.getSecondRealProperty().set("");
        viewModel.getSecondImaginaryProperty().set("");
    }
}
