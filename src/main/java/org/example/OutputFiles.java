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

        for (String inputFile: inputFiles){
            try (
                    BufferedReader reader = new BufferedReader(new FileReader(inputFile))
            ) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.matches("[-+]?\\d+")) {
                        currStats.addInteger(Long.parseLong(line));
                        integersContent.append(line).append("\n");

                    } else if(line.matches("[-+]?\\d*\\.\\d+") || line.matches("[-+]?\\d+(\\.\\d+)?[eE][-+]?\\d+")){
                        currStats.addFloat(Double.parseDouble(line));
                        floatsContent.append(line).append("\n");

                    } else {
                        currStats.addString(line);
                        stringsContent.append(line).append("\n");
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