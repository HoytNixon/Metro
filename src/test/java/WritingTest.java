import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import metro.Connection;
import metro.Line;
import metro.Station;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import parsing.DesMetro;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class WritingTest {

    DesMetro metro= new DesMetro();
    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void ifCreatingJsonFileStationsThenOK(){
        File file =new File("src/test/resources/stations.json");
        int count = 1;
        int actual= 0;
        WriteStations.writeStationsToJSON(file, metro);

        for (File fileInDir: Objects.requireNonNull(file.getParentFile().listFiles())){
            if (fileInDir.getName().equals("stations.json")){
                actual+=1;
            }
        }
        file.delete();
        assertEquals(count,actual);

    }
    @Test
    public void ifCreatingJsonFileMetroThenOK(){
        File file =new File("src/test/resources/metro.json");
        int count = 1;
        int actual= 0;
        WriteStations.writeStationsToJSON(file, metro);
        for (File fileInDir: Objects.requireNonNull(file.getParentFile().listFiles())){
            if (fileInDir.getName().equals("metro.json")){
                actual+=1;
            }
        }
        file.delete();
        assertEquals(count,actual);
    }
    @Test
    public void ifCreatedFileJsonStationsHasCorrectStructureThenOK() throws IOException {
        File stations = new File("src/test/resources/stations.json");
        Line first = new Line("1","Сокольническая линия");
        Station one = new Station("Бульвар Рокоссовского", first );
        one.setDate("01.08.1990");
        one.setDepth("0");
        one.hasConnection= true;
        Station two = new Station("Черкизовская", first);
        two.setDate("01.08.1990");
        two.setDepth("-9");
        two.hasConnection= true;
        DesMetro customMetro = new DesMetro(List.of(one,two),List.of(first), null);
        WriteStations.writeStationsToJSON(stations, customMetro);
        JsonNode actual= mapper.readTree(stations);
        JsonNode expected = mapper.readTree("""
                {
                  "stations" : [ {
                    "name" : "Бульвар Рокоссовского",
                    "line" : "Сокольническая линия",
                    "depth" : "0",
                    "date" : "01.08.1990",
                    "hasConnection" : true
                  }, {
                    "name" : "Черкизовская",
                    "line" : "Сокольническая линия",
                    "depth" : "-9",
                    "date" : "01.08.1990",
                    "hasConnection" : true
                  } ]
                }""");
        stations.delete();
        assertEquals(expected,actual);
    }
    @Test
    public void ifCreatedFileJsonMetroHasCorrectStructureThenOK() throws IOException {
        File metro = new File("src/test/resources/metro.json");
        Line first = new Line("1","Сокольническая линия");
        Line second = new Line("2", "Замоскворецкая линия");
        Station one = new Station("Бульвар Рокоссовского", first );
        one.setDate("01.08.1990");
        one.setDepth("0");
        one.hasConnection= false;
        first.addStation(one);

        Station two = new Station("Черкизовская", first);
        two.setDate("01.08.1990");
        two.setDepth("-9");
        two.hasConnection= true;
        first.addStation(two);

        Station three = new Station("Ховрино", second);
        three.setDate("31.12.2017");
        three.setDepth("-14");
        three.hasConnection= false;
        second.addStation(three);

        Station four= new Station("Речной вокзал", second);
        four.setDate("31.12.1964");
        four.setDepth("-6");
        four.hasConnection= true;
        second.addStation(four);

        Connection connection = new Connection();
        connection.addStationToConnection(four);
        connection.addStationToConnection(two);

        DesMetro customMetro = new DesMetro(List.of(one,two, three, four),List.of(first, second), Set.of(connection));
        WriteStations.writeMetroToJSON(metro,customMetro);

        JsonNode actual= mapper.readTree(metro);
        JsonNode expected = mapper.readTree("""
                {
                  "stations" : {
                    "1" : [
                      "Бульвар Рокоссовского",
                      "Черкизовская"
                    ],
                    "2" : [
                      "Ховрино",
                      "Речной вокзал"
                    ]
                  },
                  "connections" : [
                    [
                      {
                        "line" : "2",
                        "station" : "Речной вокзал"
                      },
                      {
                        "line" : "1",
                        "station" : "Черкизовская"
                      }
                    ]
                  ],
                  "lines" : [
                    {
                      "number" : "1",
                      "name" : "Сокольническая линия"
                    },
                    {
                      "number" : "2",
                      "name" : "Замоскворецкая линия"
                    }
                  ]
                }""");
        assertEquals(expected,actual);
    }
}
