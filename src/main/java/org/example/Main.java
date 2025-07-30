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

            int lineCount = 0;

            boolean addToFileFlag = false;
            boolean shortStatsFlag = false;
            boolean fullStatsFlag = false;

            List<String> inputFiles = new ArrayList<>();

            StringBuilder integersContent = new StringBuilder();
            StringBuilder floatsContent = new StringBuilder();
            StringBuilder stringsContent = new StringBuilder();

            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    //"Дополнительно с помощью опции -o нужно уметь задавать путь для результатов."
                    case "-o":
                        if (i + 1 < args.length) {
                            outputDir = args[++i];
                        } else {
                            System.err.println("Ошибка: после -o не передан путь.");
                            return;
                        }
                        break;
                    //"Опция -p задает префикс имен выходных файлов."
                    case "-p":
                        if (i + 1 < args.length) {
                            prefix = args[++i];
                        } else {
                            System.err.println("Ошибка: после -p не передан префикс.");
                            return;
                        }
                        break;
                    //"С помощью опции -a можно задать режим добавления в существующие файлы."
                    case "-a":
                        addToFileFlag = true;
                        break;
                    //Статистика двух видов: краткая и полная. "Выбор статистики производится опциями -s и -f соответственно:"
                    case "-s":
                        shortStatsFlag = true;
                        break;
                    case "-f":
                        fullStatsFlag = true;
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
                        lineCount++;
                        if (line.matches("-?\\d+")) {
                            integersContent.append("Целые числа: ").append(line).append("\n");
                        } else {
                            stringsContent.append("Строки: ").append(line).append("\n");
                        }
                    }
                }
            }

            if (integersContent.length() > 0) {
                try (BufferedWriter intWriter = new BufferedWriter(
                        new FileWriter(intFile, addToFileFlag))) {
                    intWriter.write(integersContent.toString());
                }
            }

            if (stringsContent.length() > 0) {
                try (BufferedWriter strWriter = new BufferedWriter(
                        new FileWriter(strFile, addToFileFlag))) {
                    strWriter.write(stringsContent.toString());
                }
            }


            //"-s Краткая статистика содержит только количество элементов, записанных в исходящие файлы"
            if(shortStatsFlag == true){
                System.out.println("Количество элементов, записанных в исходящие файлы" + lineCount);
            }

            //"-f Полная статистика для чисел дополнительно содержит минимальное и максимальное значения, сумма и среднее."


            //"-f Полная статистика для строк, помимо их количества, содержит также размер самой короткой строки и самой длинной."



        } catch (IOException e) {
            System.err.println("Ошибка: ввод/вывод: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Другая ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }

}