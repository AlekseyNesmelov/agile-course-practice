package ru.unn.agile.HexBinOctCalculator.viewmodel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.unn.agile.HexBinOctCalculator.Model.NumeralSystem;
import ru.unn.agile.HexBinOctCalculator.Model.HexBinOctCalculator.Operation;

import static org.junit.Assert.*;

public class ViewModelTests {
    private ViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new ViewModel();
    }

    @After
    public void tearDown() {
        viewModel = null;
    }

    @Test
    public void canSetDefaultFirstValue() {
        assertEquals("", viewModel.value1Property().get());
    }

    @Test
    public void canSetDefaultSecondValue() {
        assertEquals("", viewModel.value2Property().get());
    }

    @Test
    public void canSetDefaultSystemOfFirstValue() {
        assertEquals(NumeralSystem.BIN, viewModel.system1Property().get());
    }

    @Test
    public void canSetDefaultSystemOfSecondValue() {
        assertEquals(NumeralSystem.BIN, viewModel.system2Property().get());
    }

    @Test
    public void canSetDefaultOperation() {
        assertEquals(Operation.ADD, viewModel.operationProperty().get());
    }

    @Test
    public void canSetDefaultResult() {
        assertEquals("", viewModel.calcResultProperty().get());
    }

    @Test
    public void canSetDefaultStatus() {
        assertEquals(Status.WAITING.toString(), viewModel.calcStatusProperty().get());
    }

    @Test(expected = IllegalStateException.class)
    public void statusIsWaitingWhenCalculateAndFieldsAreEmpty() {
        viewModel.calculate();
        assertEquals(Status.WAITING.toString(), viewModel.calcStatusProperty().get());
    }

    @Test
    public void statusIsReadyWhenAllFieldsAreFill() {
        setInputData();

        assertEquals(Status.READY.toString(), viewModel.calcStatusProperty().get());
    }

    @Test
    public void statusIsWaitingIfNotEnoughCorrectData() {
        viewModel.value1Property().set("1");

        assertEquals(Status.WAITING.toString(), viewModel.calcStatusProperty().get());
    }

    @Test
    public void doReportAboutBadFormat() {
        viewModel.value1Property().set("a");

        assertEquals(Status.BAD_FORMAT.toString(), viewModel.calcStatusProperty().get());
    }

    @Test
    public void calculateButtonIsDisabledAtStart() {
        assertTrue(viewModel.calcDisabledProperty().get());
    }

    @Test
    public void calculateButtonIsDisabledWhenFormatIsBad() {
        setInputData();
        viewModel.value1Property().set("abc");

        assertTrue(viewModel.calcDisabledProperty().get());
    }

    @Test
    public void calculateButtonIsDisabledWhenSomeFieldsAreEmpty() {
        viewModel.value1Property().set("101");

        assertTrue(viewModel.calcDisabledProperty().get());
    }

    @Test
    public void calculateButtonIsEnabledWhenInputIsCorrect() {
        setInputData();

        assertFalse(viewModel.calcDisabledProperty().get());
    }

    @Test
    public void canSetMultiplyOperation() {
        viewModel.operationProperty().set(Operation.MULTIPLY);

        assertEquals(Operation.MULTIPLY, viewModel.operationProperty().get());
    }

    @Test
    public void addIsDefaultOperation() {
        assertEquals(Operation.ADD, viewModel.operationProperty().get());
    }

    @Test
    public void operationAddHasCorrectResult() {
        viewModel.system1Property().set(NumeralSystem.OCT);
        viewModel.system2Property().set(NumeralSystem.OCT);
        viewModel.finalSystemProperty().set(NumeralSystem.OCT);
        viewModel.value1Property().set("14");
        viewModel.value2Property().set("55");

        viewModel.calculate();

        assertEquals("71", viewModel.calcResultProperty().get());
    }

    @Test
    public void canSetSuccessInStatus() {
        setInputData();

        viewModel.calculate();

        assertEquals(Status.SUCCESS.toString(), viewModel.calcStatusProperty().get());
    }

    @Test
    public void operationMulHasCorrectResultInBin() {
        viewModel.system1Property().set(NumeralSystem.HEX);
        viewModel.system2Property().set(NumeralSystem.HEX);
        viewModel.finalSystemProperty().set(NumeralSystem.BIN);
        viewModel.value1Property().set("3");
        viewModel.value2Property().set("9");
        viewModel.operationProperty().set(Operation.MULTIPLY);

        viewModel.calculate();

        assertEquals("11011", viewModel.calcResultProperty().get());
    }

    @Test
    public void operationDivideFirstPositiveSecondNegativeOctNumbersHasCorrectResult() {
        viewModel.system1Property().set(NumeralSystem.OCT);
        viewModel.system2Property().set(NumeralSystem.OCT);
        viewModel.finalSystemProperty().set(NumeralSystem.OCT);
        viewModel.value1Property().set("55");
        viewModel.value2Property().set("37777777761");
        viewModel.operationProperty().set(Operation.DIVIDE);

        viewModel.calculate();

        assertEquals("37777777775", viewModel.calcResultProperty().get());
    }

    private void setInputData() {
        viewModel.value1Property().set("111");
        viewModel.value2Property().set("101");
    }
}
