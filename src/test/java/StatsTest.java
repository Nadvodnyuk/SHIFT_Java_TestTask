import org.junit.jupiter.api.Test;
import org.example.Stats;

import static org.junit.jupiter.api.Assertions.*;

public class StatsTest {
    @Test
    void addIntegerStatsTest() {
        Stats stats = new Stats();
        stats.addInteger(5);
        stats.addInteger(-10);
        stats.addInteger(20);

        String fullStats = stats.getFullStats();
        assertTrue(fullStats.contains("Количество = 3"));
        assertTrue(fullStats.contains("min = -10"));
        assertTrue(fullStats.contains("max = 20"));
        assertTrue(fullStats.contains("сумма = 15"));
        assertTrue(fullStats.contains("среднее = 5"));
    }

    @Test
    void addFloatStatsTest() {
        Stats stats = new Stats();
        stats.addFloat(1.5);
        stats.addFloat(2.0);
        stats.addFloat(3.5);

        String fullStats = stats.getFullStats();
        assertTrue(fullStats.contains("Количество = 3"));
        assertTrue(fullStats.contains("min = 1,50"));
        assertTrue(fullStats.contains("max = 3,50"));
        assertTrue(fullStats.contains("сумма = 7,00"));
        assertTrue(fullStats.contains("среднее = 2,33"));
    }

    @Test
    void addStringStatsTest() {
        Stats stats = new Stats();
        stats.addString("test");
        stats.addString("te");
        stats.addString("testing");

        String fullStats = stats.getFullStats();
        assertTrue(fullStats.contains("Количество = 3"));
        assertTrue(fullStats.contains("размер самой короткой строки = 2"));
        assertTrue(fullStats.contains("самой длинной = 7"));
    }

    @Test
    void emptyInputStatsTest() {
        Stats stats = new Stats();
        String fullStats = stats.getFullStats();
        assertTrue(fullStats.contains("Нет целых чисел."));
        assertTrue(fullStats.contains("Нет вещественных чисел."));
        assertTrue(fullStats.contains("Нет строк."));
    }

    @Test
    void shortStatsTest() {
        Stats stats = new Stats();
        stats.addInteger(10);
        stats.addFloat(3.14);
        stats.addString("abc");

        String shortStats = stats.getShortStats();
        assertEquals("Целые: 1, Вещественные: 1, Строки: 1", shortStats);
    }

    @Test
    void totalCountStatsTest() {
        Stats stats = new Stats();
        stats.addInteger(10);
        stats.addFloat(2.5);
        stats.addString("abc");

        assertEquals(3, stats.getTotalCount());
    }

    @Test
    void oneTypeStatsTest() {
        Stats stats = new Stats();
        stats.addInteger(100);

        String fullStats = stats.getFullStats();
        assertTrue(fullStats.contains("Количество = 1"));
        assertTrue(fullStats.contains("Нет вещественных чисел."));
        assertTrue(fullStats.contains("Нет строк."));
    }
}