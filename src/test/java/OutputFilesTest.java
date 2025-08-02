import org.example.Stats;
import org.example.OutputFiles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OutputFilesTest {
    @TempDir
    Path tempDir;

    private Stats stats;
    private StringBuilder integers, floats, strings;

    @Test
    void doesNothingOutputFilesTest() {
        assertDoesNotThrow(() -> OutputFiles.outputDirCheck("."));
    }

    @Test
    void nestedDirectoryOutputFilesTest() {
        File nested = new File(tempDir.toFile(), "nested/child");
        String dirPath = nested.getPath();

        assertFalse(nested.exists());
        OutputFiles.outputDirCheck(dirPath);
        assertTrue(nested.isDirectory());
    }

    @Test
    void existingDirectoryOutputFilesTest() {
        File nested = new File(tempDir.toFile(), "nested");
        String dirPath = nested.getPath();

        OutputFiles.outputDirCheck(dirPath);
        assertTrue(nested.isDirectory());

        assertDoesNotThrow(() -> OutputFiles.outputDirCheck(dirPath));
        assertTrue(nested.isDirectory());
    }

    @Test
    void weirdNameDirectoryOutputFilesTest() {
        File nested = new File(tempDir.toFile(), "file1.txt");
        String dirPath = nested.getPath();

        OutputFiles.outputDirCheck(dirPath);
        assertTrue(nested.isDirectory());

        assertDoesNotThrow(() -> OutputFiles.outputDirCheck(dirPath));
        assertTrue(nested.isDirectory());
    }

    @Test
    void throwsCreateDirectoryOutputFilesTest() throws IOException {
        File conflict = new File(tempDir.toFile(), "file");
        assertTrue(conflict.createNewFile());
        String dirPath = conflict.getPath();

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> OutputFiles.outputDirCheck(dirPath)
        );
        assertTrue(ex.getMessage().contains("Ошибка: не получилось создать каталог, не директория"));
    }

    @BeforeEach
    void setUp() {
        stats   = new Stats();
        integers = new StringBuilder();
        floats  = new StringBuilder();
        strings = new StringBuilder();
    }

    private Path writeFile(String name, String content) throws IOException {
        Path p = tempDir.resolve(name);
        Files.writeString(p, content);
        return p;
    }

    @Test
    void singleIntegerLine() throws IOException {
        Path f = writeFile("test1.txt", " 12345 ");
        OutputFiles.lineParser(List.of(f.toString()), stats, integers, floats, strings);

        assertEquals(1, stats.getIntegersCount());
        assertEquals(12345, stats.getIntegersSum());
        assertEquals(0, stats.getFloatsCount());
        assertEquals(0, strings.length());

        assertTrue(integers.toString().contains("12345"));
    }

    @Test
    void singleNegativeIntegerAndPlus() throws IOException {
        Path f = writeFile("integers.txt", "-7\n+42\n0");
        OutputFiles.lineParser(List.of(f.toString()), stats, integers, floats, strings);

        assertEquals(3, stats.getIntegersCount());
        assertEquals(35, stats.getIntegersSum());
    }

    @Test
    void decimalAndScientific() throws IOException {
        Path f = writeFile("floats.txt", "0.5\n+.25\n-10.\n1e3\n-2.5E-1\n+3.0E+2");
        OutputFiles.lineParser(List.of(f.toString()), stats, integers, floats, strings);

        assertEquals(0, stats.getIntegersCount());
        assertEquals(6, stats.getFloatsCount());

        String out = floats.toString();
        assertTrue(out.contains("0.5"));
        assertTrue(out.contains("+.25"));
        assertTrue(out.contains("-10."));
        assertTrue(out.contains("1e3"));
        assertTrue(out.contains("-2.5E-1"));
        assertTrue(out.contains("+3.0E+2"));
    }

    @Test
    void nonNumericLines() throws IOException {
        Path f = writeFile("strings.txt", "test\n123test\n\n   \ntest1");
        OutputFiles.lineParser(List.of(f.toString()), stats, integers, floats, strings);

        assertEquals(0, stats.getIntegersCount());
        assertEquals(0, stats.getFloatsCount());
        assertEquals(3, stats.getStringsCount());

        String out = strings.toString();
        System.out.println(out);
        assertTrue(out.contains("test"));
        assertTrue(out.contains("123test"));
        assertTrue(out.contains("test1"));
    }

    @Test
    void multipleFilesCombined() throws IOException {
        Path f1 = writeFile("file1.txt", "1\n2.0\ntest");
        Path f2 = writeFile("file2.txt", "file\n3\n4.5");
        OutputFiles.lineParser(List.of(f1.toString(), f2.toString()), stats, integers, floats, strings);

        assertEquals(2, stats.getIntegersCount());
        assertEquals(2, stats.getFloatsCount());
        assertEquals(2, stats.getStringsCount());
    }

    @Test
    void testFileReadError() throws IOException {
        OutputFiles.lineParser(List.of("file.txt"), stats, integers, floats, strings);

        assertTrue(integers.isEmpty());
        assertTrue(floats.isEmpty());
        assertTrue(strings.isEmpty());
        assertEquals(0, stats.getTotalCount());
    }

    @Test
    void writeToFileOutputFilesTest() throws IOException {
        File tempFile = File.createTempFile("file", ".txt");
        tempFile.deleteOnExit();

        StringBuilder content = new StringBuilder("test\nlines\n");
        OutputFiles.writeToFile(tempFile.toString(), content, false);

        try (BufferedReader reader = new BufferedReader(new FileReader(tempFile))) {
            String line1 = reader.readLine();
            String line2 = reader.readLine();
            String line3 = reader.readLine();

            assertEquals("test", line1);
            assertEquals("lines", line2);
            assertNull(line3);
        }
    }

    @Test
    void rewriteToFileOutputFilesTest() throws IOException {
        File tempFile = File.createTempFile("file", ".txt");
        tempFile.deleteOnExit();

        StringBuilder content = new StringBuilder("test\nlines\n");
        OutputFiles.writeToFile(tempFile.toString(), content, true);

        content = new StringBuilder("test1\n");
        OutputFiles.writeToFile(tempFile.toString(), content, true);

        try (BufferedReader reader = new BufferedReader(new FileReader(tempFile))) {
            String line1 = reader.readLine();
            String line2 = reader.readLine();
            String line3 = reader.readLine();
            String line4 = reader.readLine();

            assertEquals("test", line1);
            assertEquals("lines", line2);
            assertEquals("test1", line3);
            assertNull(line4);
        }
    }

    @Test
    void emptyContentOutputFilesTest() throws IOException {
        File tempFile = File.createTempFile("file", ".txt");
        tempFile.deleteOnExit();

        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("test\n");
        }
        StringBuilder content = new StringBuilder();
        OutputFiles.writeToFile(tempFile.toString(), content, true);

        try (BufferedReader reader = new BufferedReader(new FileReader(tempFile))) {
            String line1 = reader.readLine();
            String line2 = reader.readLine();

            assertEquals("test", line1);
            assertNull(line2);
        }
    }
}