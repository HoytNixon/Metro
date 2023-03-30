import metro.Connection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import parsing.DesMetro;
import parsing.ParseHTML;

import java.io.File;
import java.util.Set;


public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    private static DesMetro metro= new DesMetro();

    public static void main(String[] args) {
        File file = new File("src/main/Jsons/stations.json");
        WriteStations.writeStationsToJSON(file,metro);
        WriteStations.writeMetroToJSON(new File("src/main/Jsons/metro.json"), metro);
    }
}
