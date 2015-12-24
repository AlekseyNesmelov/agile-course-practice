package ru.unn.agile.Complex.infrastructure;

import ru.unn.agile.Complex.viewmodel.ILogger;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Logger implements ILogger {
    private final String fileName;
    private final BufferedWriter writer;
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        return dateFormat.format(calendar.getTime());
    }

    public Logger(final String fileName) {
        this.fileName = fileName;

        BufferedWriter tmpWriter = null;

        try {
            tmpWriter = new BufferedWriter(new FileWriter(fileName));
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        writer = tmpWriter;
    }

    @Override
    public void log(final String message) {
        try {
            writer.write(getCurrentTime() + " : " + message + "\n");
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    @Override
    public List<String> getLog() {
        BufferedReader reader;
        ArrayList<String> log = new ArrayList<>();

        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            while (line != null) {
                log.add(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }

        return log;
    }
}
