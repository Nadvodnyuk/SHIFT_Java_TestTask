package org.example;

import java.io.*;
import java.util.List;

public class OutputFiles {
    public static void outputDirCheck(String outputDir){
        if (!outputDir.equals(".")){
            File dir = new File(outputDir);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    throw new IllegalArgumentException("Ошибка: не получилось создать каталог: " + outputDir);
                }
            }else{
                if (!dir.isDirectory()) {
                    throw new IllegalArgumentException("Ошибка: не получилось создать каталог, не директория:" + outputDir);
                }
            }
        }
    }

    private static void appendContent(StringBuilder content, String line) {
        if (content.length() > 0) {
            content.append("\n");
        }
        content.append(line);
    }

    public static void lineParser (List<String> inputFiles,
                            Stats currStats,
                            StringBuilder integersContent,
                            StringBuilder floatsContent,
                            StringBuilder stringsContent) throws IOException {
        String line;
        for (String inputFile: inputFiles){
            try (
                    BufferedReader reader = new BufferedReader(new FileReader(inputFile))
            ) {
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) {
                        continue;
                    }
                    try {
                        long value = Long.parseLong(line);
                        currStats.addInteger(value);
                        appendContent(integersContent, line);
                        continue;
                    } catch (NumberFormatException ignored) {}

                    try {
                        double value = Double.parseDouble(line);
                        if (Double.isFinite(value) && !line.matches("[-+]?\\d+")) {
                            currStats.addFloat(value);
                            appendContent(floatsContent, line);
                            continue;
                        }
                    } catch (NumberFormatException ignored) {}

                    currStats.addString(line);
                    appendContent(stringsContent, line);
                }
            } catch (IOException e) {
                System.err.println("Ошибка: чтение файла " + inputFile + ": " + e.getMessage());
            }
        }
    }

    public static void writeToFile(String filename, StringBuilder content, boolean addToFileFlag) throws IOException {
        if (content.length() == 0) return;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, addToFileFlag))) {
            writer.write(content.toString());
        } catch (IOException e) {
            System.err.println("Ошибка: нельзя записать в файл " + filename + ": " + e.getMessage());
        }
    }
}