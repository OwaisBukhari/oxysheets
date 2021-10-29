package com.mafaz.googlesheets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LogsUtil {

    public static StringBuilder readLogs() {
        StringBuilder logBuilder = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"logcat", "-s", "OxyfitDashboardFragment:D", "*:S"});
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
           while ((line=bufferedReader.readLine())!=null) {
                line=bufferedReader.readLine();

               logBuilder.append(line + "\n");
            }
        } catch (IOException e) {
        }
        return logBuilder;
    }
}