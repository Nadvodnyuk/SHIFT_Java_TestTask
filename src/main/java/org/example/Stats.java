package org.example;

public class Stats {
    // Integers
    private int integersCount = 0;
    private long integersSum = 0;
    private long integersMin = Long.MAX_VALUE;
    private long integersMax = Long.MIN_VALUE;

    //Floats
    private int floatsCount = 0;
    private double floatsSum = 0.0;
    private double floatsMin = Double.POSITIVE_INFINITY;
    private double floatsMax = Double.NEGATIVE_INFINITY;

    //Strings
    private int stringsCount = 0;
    private int stringsMinLength = Integer.MAX_VALUE;
    private int stringsMaxLength = Integer.MIN_VALUE;

    public void addInteger(long value) {
        integersCount++;
        integersSum += value;
        integersMin = Math.min(integersMin, value);
        integersMax = Math.max(integersMax, value);
    }

    public void addFloat(double value) {
        floatsCount++;
        floatsSum += value;
        floatsMin = Math.min(floatsMin, value);
        floatsMax = Math.max(floatsMax, value);
    }

    public void addString(String value) {
        stringsCount++;
        stringsMinLength = Math.min(stringsMinLength, value.length());
        stringsMaxLength = Math.max(stringsMaxLength, value.length());
    }

    public String getFullStats() {
        StringBuilder results = new StringBuilder();
        if (integersCount == 0) {
            results.append("Нет целых чисел.\n");
        } else {
            results.append(String.format("Целые: \n Количество = %d, min = %d, max = %d, сумма = %d, среднее = %.2f\n",
                    integersCount, integersMin, integersMax, integersSum, integersSum / (double) integersCount));
        }

        if (floatsCount == 0) {
            results.append("Нет вещественных чисел.\n");
        } else {
            results.append(String.format("Вещественные: \n Количество = %d, min = %.2f, max = %.2f, сумма = %.2f, среднее = %.2f\n",
                    floatsCount, floatsMin, floatsMax, floatsSum, floatsSum / (double) floatsCount));
        }

        if (stringsCount == 0) {
            results.append("Нет строк.\n");
        } else {
            results.append(String.format("Строки: \n Количество = %d, размер самой короткой строки = %d, самой длинной = %d\n",
                    stringsCount, stringsMinLength, stringsMaxLength));
        }

        return results.toString();
    }

    public int getTotalCount() {
        return integersCount + floatsCount + stringsCount;
    }
}
