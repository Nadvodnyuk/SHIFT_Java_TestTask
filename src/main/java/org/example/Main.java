package org.example;

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                System.err.println("Ошибка: не передано ничего.");
                System.exit(1);
            }

            String outputDir = ".";
            String prefix = "";
            String line;

            boolean addToFileFlag = false;

            List<String> inputFiles = new ArrayList<>();

            StringBuilder intContent = new StringBuilder();
            StringBuilder strContent = new StringBuilder();

            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "-o":
                        if (i + 1 < args.length) {
                            outputDir = args[++i];
                        } else {
                            System.err.println("Ошибка: после -o не передан путь.");
                            return;
                        }
                        break;
                    case "-a":
                        addToFileFlag = true;
                        break;
                    case "-p":
                        if (i + 1 < args.length) {
                            prefix = args[++i];
                        } else {
                            System.err.println("Ошибка: после -p не передан префикс.");
                            return;
                        }
                        break;
                    default:
                        inputFiles.add(args[i]);
                        break;
                }
            }

            String intFile = outputDir + "/" + prefix + "integers.txt";
            String strFile = outputDir + "/" + prefix + "strings.txt";

            if (!outputDir.equals(".")){
                File dir = new File(outputDir);
                if (!dir.exists()) {
                    boolean created = dir.mkdirs();
                    if (!created) {
                        System.err.println("Не получилось создать каталог: " + outputDir);
                        System.exit(1);
                    }
                }
            }

            for (String inputFile: inputFiles){
                try (
                        BufferedReader reader = new BufferedReader(new FileReader(inputFile))
                ) {
                    while ((line = reader.readLine()) != null) {
                        if (line.matches("-?\\d+")) {
                            intContent.append("Целые числа: ").append(line).append("\n");
                        } else {
                            strContent.append("Строки: ").append(line).append("\n");
                        }
                    }
                }
            }

            if (intContent.length() > 0) {
                try (BufferedWriter intWriter = new BufferedWriter(
                        new FileWriter(intFile, addToFileFlag))) {
                    intWriter.write(intContent.toString());
                }
            }

            if (strContent.length() > 0) {
                try (BufferedWriter strWriter = new BufferedWriter(
                        new FileWriter(strFile, addToFileFlag))) {
                    strWriter.write(strContent.toString());
                }
            }

        } catch (IOException e) {
            System.err.println("Ошибка: ввод/вывод: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Другая ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }

}