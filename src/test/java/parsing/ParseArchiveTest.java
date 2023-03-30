package parsing;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParseArchiveTest {
    public static final File file = new File("src/test/resources/stations-data.zip");

    @Test
    public void getFilesFromArchieve() throws IOException {
        List<String> expected =List.of(
                "dates-3.csv",
                "dates-2.csv",
                "depths-2.json",
                "depths-3.json",
                "dates-1.csv",
                "depths-1.json");
        List <String> actual = ParseArchive.getFileNamesFromArchieve(file);
        assertEquals(expected,actual);
    }
}