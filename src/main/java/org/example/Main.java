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

            Stats currStats = new Stats();
            ArgsParser options = new ArgsParser(args);

            StringBuilder integersContent = new StringBuilder();
            StringBuilder floatsContent = new StringBuilder();
            StringBuilder stringsContent = new StringBuilder();

            String outputDir = options.getOutputDir();
            OutputFiles.outputDirCheck(outputDir);

            String prefix = options.getPrefix();
            String outputFileName = outputDir + "/" + prefix;

            boolean addToFileFlag = options.isAddToFileFlag();
            boolean shortStatsFlag = options.isShortStatsFlag();
            boolean fullStatsFlag = options.isFullStatsFlag();

            List<String> inputFiles = options.getInputFiles();

            OutputFiles.lineParser(inputFiles, currStats, integersContent, floatsContent, stringsContent);

            OutputFiles.writeToFile(outputFileName + "integers.txt", integersContent, addToFileFlag);
            OutputFiles.writeToFile(outputFileName + "floats.txt", floatsContent, addToFileFlag);
            OutputFiles.writeToFile(outputFileName + "strings.txt", stringsContent, addToFileFlag);

            //"-s Краткая статистика содержит только количество элементов, записанных в исходящие файлы"
            if(shortStatsFlag || fullStatsFlag){
                System.out.println("Количество элементов, записанных в исходящие файлы: " + currStats.getTotalCount());
            }

            if(shortStatsFlag){
                System.out.println(currStats.getShortStats());
            }

            //"-f Полная статистика для чисел дополнительно содержит минимальное и максимальное значения, сумма и среднее."
            //"Полная статистика для строк, помимо их количества, содержит также размер самой короткой строки и самой длинной."
            if(fullStatsFlag){
                System.out.println(currStats.getFullStats());
            }
        }catch (IllegalArgumentException e) {
            System.err.println("Ошибка с аргументами: " + e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Ошибка: ввод/вывод: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Другая ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}