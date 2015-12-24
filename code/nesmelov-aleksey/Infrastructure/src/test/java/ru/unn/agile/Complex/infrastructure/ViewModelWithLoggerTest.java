package ru.unn.agile.Complex.infrastructure;

import ru.unn.agile.Complex.viewmodel.ComplexViewModel;
import ru.unn.agile.Complex.viewmodel.ViewModelTest;

public class ViewModelWithLoggerTest extends ViewModelTest {
    @Override
    public void setUp() {
        Logger realLogger = new Logger("./ViewModelWithLoggerTest.log");
        super.setViewModel(new ComplexViewModel(realLogger));
    }
}
