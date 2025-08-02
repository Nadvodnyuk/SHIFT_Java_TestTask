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
                    if (line.matches("[-+]?\\d+")) {
                        currStats.addInteger(Long.parseLong(line));
                        if (integersContent.length() > 0) {
                            integersContent.append("\n");
                        }
                        integersContent.append(line);

                    } else if(line.matches("[-+]?\\d*\\.\\d+") ||
                            line.matches("[-+]?\\d+\\.\\d*") ||
                            line.matches("[-+]?\\d+(\\.\\d+)?[eE][-+]?\\d+")){
                        currStats.addFloat(Double.parseDouble(line));
                        if (floatsContent.length() > 0) {
                            floatsContent.append("\n");
                        }
                        floatsContent.append(line);

                    } else {
                        currStats.addString(line);
                        if (stringsContent.length() > 0) {
                            stringsContent.append("\n");
                        }
                        stringsContent.append(line);
                    }
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
        }
    }
}