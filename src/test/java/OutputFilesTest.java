import org.junit.jupiter.api.Test;
import org.example.OutputFiles;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class OutputFilesTest {
    @TempDir
    Path tempDir;

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
    // НУЖНЫ ТЕСТЫ ДЛЯ lineParser!!!!!!!!!!!!!!!!!!!!!

}
