package parsing;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParseHtmlTest {

    public static final DesMetro metro = new DesMetro();
    @Test
    public void ifCountOfParsedStationsCorrectThenOk(){
        int actual= metro.getStations().size();
        int expected = 330;
        assertEquals(expected,actual);
    }
    @Test
    public void ifCountOfParsedLinesCorrectThenOk(){
        int actual= metro.getLines().size();
        int expected = 17;
        assertEquals(expected,actual);
    }
}
