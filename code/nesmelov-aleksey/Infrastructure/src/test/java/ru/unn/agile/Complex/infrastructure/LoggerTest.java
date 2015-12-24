package ru.unn.agile.Complex.infrastructure;

import org.junit.Before;
import org.junit.Test;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

public class LoggerTest {
    private Logger logger;
    private static final String FILE_NAME = "./LoggerTest.log";
    private String message;

    @Before
    public void setUp() {
        logger = new Logger(FILE_NAME);
    }

    @Test
    public void canCreateLogger() {
        assertNotNull(new Logger(FILE_NAME));
    }

    @Test
    public void canWriteLog() {
        message = ".* Test message..*";

        logger.log("Test message.");

        assertTrue(logger.getLog().get(0).matches(message));
    }

    @Test
    public void logIsEmptyIfDoNotAddMessages() {
        assertTrue(logger.getLog().isEmpty());
    }

    @Test
    public void canWriteSeveralLogs() {
        for (int i = 0; i < 10; i++) {
            logger.log("Test message " + i);
        }

        for (int i = 0; i < 10; i++) {
            assertTrue(logger.getLog().get(i).matches(".* Test message " + i + ".*"));
        }
    }

    @Test
    public void logContainDataAndTime() {
        message = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.*";

        logger.log("Test message.");

        assertTrue(logger.getLog().get(0).matches(message));
    }
}
