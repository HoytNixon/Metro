package parsing;

import metro.Connection;
import metro.Line;
import metro.Station;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static parsing.ParseCSV.parseDates;

public class DesMetro {
    private static final Logger logger = LogManager.getLogger(DesMetro.class);
    public static final File file = new File("H:\\j files\\ru.homework\\src\\main\\resources\\stations-data.zip");
    private static final String pathToExtractJsons = "src/main/Jsons/";

    private List <Station> stations;
    private List <Line> lines;
    private Set<Connection> connections;
    public DesMetro(){
        stations = ParseHTML.getStations();
        lines=ParseHTML.getLines();
        connections= ParseHTML.getConnections();
        fullDeserializeMetro();
    }

    public DesMetro(List<Station> stations, List<Line> lines, Set<Connection> connections) {
        this.stations = stations;
        this.lines = lines;
        this.connections = connections;
    }

    public List<Station> getStations() {
        return stations;
    }

    public List<Line> getLines() {
        return lines;
    }

    public Set<Connection> getConnections() {
        return connections;
    }

    private static void fullDeserializeMetro () {
        List<String> jsons = null;
        List<ParseJson.JsonDepth> depths= null;
        List<ParseCSV.StationDates> dates = null;
        List<Station> stations = ParseHTML.getStations();
        try {
            jsons = ParseArchive.getFileNamesFromArchieve(file,pathToExtractJsons);
        } catch (IOException ex) {
            logger.log(Level.ERROR,"Ошибка распаковки объекта");
        }
        assert jsons != null;
        for (String jsonFileName : jsons) {
            if (jsonFileName.endsWith(".csv")){
                File file = new File("src/main/Jsons/" + jsonFileName);
                try {
                    dates = parseDates(file);
                } catch (IOException e) {
                    logger.log(Level.ERROR, "Ошибка парсинга CSV");
                }
                for (Station station: stations){
                    if (station.getDate() != null) continue;
                    assert dates != null;
                    Iterator<ParseCSV.StationDates> it = dates.iterator();
                    while (it.hasNext()){
                        var dateOfConstruction = it.next();
                        if (dateOfConstruction.getName().equalsIgnoreCase(station.getName())){
                            station.setDate(dateOfConstruction.getDate());
                            it.remove();
                        }
                    }
                }

            }
            else {
                File file = new File("src/main/Jsons/" + jsonFileName);
                try {
                    depths = ParseJson.parseDepths(file);
                } catch (IOException ex){
                    logger.log(Level.ERROR, "Ошибка парсинга JSON depths");
                }
                for (Station station : stations) {
                    assert depths != null;
                    String newDepth = depths.stream()
                            .filter(depth -> depth.getName().equalsIgnoreCase(station.getName()))
                            .filter(depth -> !depth.getDepth().equals("?"))
                            .min(Comparator.comparing(s -> {
                                return Double.parseDouble(s.getDepth().replace(",", "."));
                            }))
                            .orElse(new ParseJson.JsonDepth(null, null))
                            .getDepth();
                    if (newDepth!=null)  {
                        newDepth= newDepth.replace(",", ".");
                        if (station.getDepth() == null) station.setDepth(newDepth);
                        else if (Double.parseDouble(station.getDepth()) < Double.parseDouble(newDepth)) {
                            station.setDepth(newDepth);
                        }
                    }
                }
            }
        }
    }
}
