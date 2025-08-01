package org.example;

import java.util.ArrayList;
import java.util.List;

public class ArgsParser {
    private String outputDir = ".";
    private String prefix = "";

    private boolean addToFileFlag = false;
    private boolean shortStatsFlag = false;
    private boolean fullStatsFlag = false;

    private List<String> inputFiles = new ArrayList<>();

    public ArgsParser(String[] args) {
        parseAllArgs(args);
        if (inputFiles.isEmpty()) {
            throw new IllegalArgumentException("Ошибка: не переданы входные файлы.");
        }
    }

    private void parseAllArgs(String[] args){
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                //"Дополнительно с помощью опции -o нужно уметь задавать путь для результатов."
                case "-o":
                    if (i + 1 < args.length) {
                        String value = args[++i];
                        if (isFlag(value)) {
                            throw new IllegalArgumentException("Ошибка: после -o должен быть путь, а не флаг.");
                        }
                        outputDir = value;
                    } else {
                        throw new IllegalArgumentException("Ошибка: после -o не передан путь.");
                    }
                    break;
                //"Опция -p задает префикс имен выходных файлов."
                case "-p":
                    if (i + 1 < args.length) {
                        String value = args[++i];
                        if (isFlag(value)) {
                            throw new IllegalArgumentException("Ошибка: после -p должен быть путь, а не флаг.");
                        }
                        prefix = value;
                    } else {
                        throw new IllegalArgumentException("Ошибка: после -p не передан префикс.");
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
    }

    private boolean isFlag(String arg) {
        return arg.equals("-o") || arg.equals("-p") || arg.equals("-a") || arg.equals("-s") || arg.equals("-f");
    }

    public String getOutputDir() {
        return outputDir;
    }

    public String getPrefix() {
        return prefix;
    }

    public boolean isAddToFileFlag() {
        return addToFileFlag;
    }

    public boolean isShortStatsFlag() {
        return shortStatsFlag;
    }

    public boolean isFullStatsFlag() {
        return fullStatsFlag;
    }

    public List<String> getInputFiles() {
        return inputFiles;
    }
}