import org.junit.jupiter.api.Test;
import org.example.ArgsParser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ArgsParserTest {

    @Test
    public void correctArgsParserTest(){
        String[] args = {
                "-o", "/tmp/output",
                "-p", "res_",
                "-a",
                "-s",
                "file1.txt", "file2.txt"
        };

        ArgsParser parser = new ArgsParser(args);

        assertEquals("/tmp/output", parser.getOutputDir());
        assertEquals("res_", parser.getPrefix());
        assertTrue(parser.isAddToFileFlag());
        assertTrue(parser.isShortStatsFlag());
        assertFalse(parser.isFullStatsFlag());
        assertEquals(List.of("file1.txt","file2.txt"), parser.getInputFiles());
    }

    @Test
    public void diffOrderArgsParserTest(){
        String[] args = {
                "-o", "/tmp/output",
                "-p", "res_",
                "-f",
                "file1.txt",
                "-a",
                "file2.txt"
        };

        ArgsParser parser = new ArgsParser(args);

        assertEquals("/tmp/output", parser.getOutputDir());
        assertEquals("res_", parser.getPrefix());
        assertTrue(parser.isAddToFileFlag());
        assertFalse(parser.isShortStatsFlag());
        assertTrue(parser.isFullStatsFlag());
        assertEquals(List.of("file1.txt","file2.txt"), parser.getInputFiles());
    }

    @Test
    public void noArgsParserTest(){
        String[] args = { "file1.txt", "file2.txt" };

        ArgsParser parser = new ArgsParser(args);

        assertEquals(".", parser.getOutputDir());
        assertEquals("", parser.getPrefix());
        assertFalse(parser.isAddToFileFlag());
        assertFalse(parser.isShortStatsFlag());
        assertFalse(parser.isFullStatsFlag());
        assertEquals(List.of("file1.txt","file2.txt"), parser.getInputFiles());
    }

    @Test
    public void doublePathAndPrefixParserTest(){
        String[] args = {
                "-o", "/tmp/output1",
                "-o", "/tmp/output2",
                "-p", "res1_",
                "-p", "res2_",
                "file1.txt","file2.txt"
        };

        ArgsParser parser = new ArgsParser(args);

        assertEquals("/tmp/output2", parser.getOutputDir());
        assertEquals("res2_", parser.getPrefix());
    }

    @Test
    public void argAsPathArgsParserTest(){
        String[] args = {
                "-o", "-p",
                "file1.txt", "file2.txt"
        };

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, () -> new ArgsParser(args)
        );
        assertTrue(ex.getMessage().contains("Ошибка: после -o должен быть путь, а не флаг."));
    }

    @Test
    public void noPathArgsParserTest(){
        String[] args = {
                "file1.txt", "file2.txt",
                "-o"
        };

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, () -> new ArgsParser(args)
        );
        assertTrue(ex.getMessage().contains("Ошибка: после -o не передан путь."));
    }

    @Test
    public void weirdPathArgsParserTest(){
        String[] args0 = {
                "-o", "file1.txt",
                "file2.txt"
        };

        ArgsParser parser0 = new ArgsParser(args0);

        assertEquals("file1.txt", parser0.getOutputDir());
        assertEquals(List.of("file2.txt"), parser0.getInputFiles());

        String[] args1 = {
                "-o", "\\tmp\\output",
                "file1.txt"
        };

        ArgsParser parser1 = new ArgsParser(args1);

        assertEquals("\\tmp\\output", parser1.getOutputDir());

        String[] args2 = {
                "-o", "//tmp//output",
                "file1.txt"
        };

        ArgsParser parser2 = new ArgsParser(args2);

        assertEquals("//tmp//output", parser2.getOutputDir());
    }

    @Test
    public void argAsPrefixArgsParserTest(){
        String[] args = {
                "-p", "-o",
                "file1.txt", "file2.txt"
        };

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, () -> new ArgsParser(args)
        );
        assertTrue(ex.getMessage().contains("Ошибка: после -p должен быть путь, а не флаг."));
    }

    @Test
    public void noPrefixArgsParserTest(){
        String[] args = {
                "file1.txt", "file2.txt",
                "-p"
        };

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, () -> new ArgsParser(args)
        );
        assertTrue(ex.getMessage().contains("Ошибка: после -p не передан префикс."));
    }

    @Test
    public void noInputArgsParserTest(){
        String[] args = {"-a"};

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, () -> new ArgsParser(args)
        );
        assertTrue(ex.getMessage().contains("Ошибка: не переданы входные файлы."));
    }

    @Test
    void unknownFlagAsInputArgsParserTest() {
        String[] args = {"-x", "file1.txt"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(List.of("-x","file1.txt"), parser.getInputFiles());
    }
}