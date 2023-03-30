package parsing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ParseCsvAndJsonTest {
    @Test
    public void ifObjectFromCsvHasAllFieldsThenOK() throws IOException {
        File csv =new File("src/test/resources/dates.csv");
        List<ParseCSV.StationDates> datesList = ParseCSV.parseDates(csv);
        ParseCSV.StationDates acc = datesList.get(0);
        String expectedStationName="Красные Ворота";
        String expectedStationDate = "15.05.1935";
        String actualStationName = acc.getName();
        String actualStationDate = acc.getDate();
        assertEquals(expectedStationName, actualStationName);
        assertEquals(expectedStationDate,actualStationDate);
    }
    @Test
    public void ifObjectFromJsonHasAllFieldsThenOK() throws IOException {
        File json = new File("src/test/resources/depths.json");
        List<ParseJson.JsonDepth> depths = ParseJson.parseDepths(json);
        ParseJson.JsonDepth depth = depths.get(0);
        String expectedStationName="Красные Ворота";
        String expectedStationDepth = "-31";
        String actualStationName = depth.getName();
        String actualStationDate = depth.getDepth();
        assertEquals(expectedStationName, actualStationName);
        assertEquals(expectedStationDepth,actualStationDate);
    }
}
