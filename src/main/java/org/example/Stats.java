package org.example;

public class Stats {
    // Integers
    private int integersCount = 0;
    private int integersSum = 0;
    private int integersMin = Integer.MIN_VALUE;
    private int integersMax = Integer.MAX_VALUE;

    //Floats
    private int floatsCount = 0;
    private double floatsSum = 0.0;
    private double floatsMin = Double.POSITIVE_INFINITY;
    private double floatsMax = Double.NEGATIVE_INFINITY;

    //Strings
    private int stringsCount = 0;
    private int stringsMinLength = Integer.MAX_VALUE;
    private int stringsMaxLength = Integer.MIN_VALUE;
}
