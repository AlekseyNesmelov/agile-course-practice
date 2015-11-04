package ru.unn.agile.HypothecsCalculator.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import javax.swing.*;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class HypothecTableTest {

    JTable rightGraphicForAnnuity = new JTable(new Object[][]{
                    {1, "11.2015", 1080.79, 970.50  },
                    {2, "12.2015", 1070.89, 989.90  },
                    {3, "01.2016", 1060.79, 1009.70  },
                    {4, "02.2016", 1050.50, 1029.90 }},
            new String[]{"№ платежа", "Дата платежа", "Сумма платежа", "Платеж по основному долгу"});


    @Test
    public void canCreateTable() {
        Hypothec credit = new Hypothec.Builder(4000.0, 4)
                .setInterestRate(2.0)
                .setInterestRateType(Hypothec.InterestRateType.MONTHLY)
                .setMonthlyFee(1.0)
                .setMonthlyFeeType(Hypothec.MonthlyFeeType.CREDIT_BALANCE_PERCENT)
                .setStartDate(new GregorianCalendar(2015, Calendar.NOVEMBER, 4))
                .setCurrency(Hypothec.CurrencyType.RUBLE)
                .build();

        JTable graphic = credit.getGraphicOfPayments();

        for (int i = 0; i < rightGraphicForAnnuity.getColumnCount(); i++) {
             for (int j = 0; j < rightGraphicForAnnuity.getRowCount(); j++) {
                assertEquals(rightGraphicForAnnuity.getValueAt(j,i), graphic.getValueAt(j,i));
            }
            assertEquals(rightGraphicForAnnuity.getColumnName(i), graphic.getColumnName(i));
        }




    }
}