package parsing;

import metro.Connection;
import metro.Line;
import metro.Station;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


import java.io.IOException;
import java.util.*;

public class ParseHTML {
    private static final Logger logger = LogManager.getLogger(ParseHTML.class);

    private static final String urlAddress = "https://skillbox-java.github.io/";
    private static final Document doc;

    private static final List <Line> metroLines = new ArrayList<>();
    private static final List <Station> metroStations = new ArrayList<>();
    private static final Set<Connection> connections = new HashSet<>();

    static {
        try {
            doc = Jsoup.connect(urlAddress).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void parseStations(){
        if (metroLines.isEmpty()) {
            parseLines();
        }
        if (metroStations.isEmpty()){
            var lines = doc.select("div[data-depend-set]");
            for (Element e : lines) {
                String lineNumber = e.select("div[data-line]").attr("data-line");
                Line line = metroLines.stream()
                        .filter(l -> l.getNumber().equals(lineNumber))
                        .findFirst()
                        .get();
                var stationsOnTheLine = e.select("span[class=name]");
                for (Element st : stationsOnTheLine) {
                    Station station = new Station(st.text(), line);

                    var std = st.siblingElements().select("[title]");
                        for (Element element:std){
                            String connectionName = connectedStationName(element.attr("title"));
                            String connectionLineNumber = element.attr("class").split("-")[3];

                        }
                    metroStations.add(station);
                    line.addStation(station);
                }
            }
        }
    }
    private static void parseConnections() {
        if (metroLines.isEmpty()) {
            parseLines();
        }
        if (metroStations.isEmpty()){
            parseStations();
        }
        if (connections.isEmpty()){
        var lines = doc.select("div[data-depend-set]");
        for (Element e : lines) {
            String lineNumber = e.select("div[data-line]").attr("data-line");
            Line line = metroLines.stream()
                    .filter(l -> l.getNumber().equals(lineNumber))
                    .findFirst()
                    .get();
            var stationsOnTheLine = e.select("span[class=name]");
            for (Element st : stationsOnTheLine) {
                var fd=st.siblingElements().stream()
                        .filter(element -> element.hasAttr("title"))
                        .toList();
                if (fd.isEmpty()) continue;
                Station station = line.getStation(st.text());
                Connection connection = new Connection();
                connection.addStationToConnection(station);
                station.hasConnection=true;
                for (Element element :fd){
                    String numberOfConnectedLine = element.attr("class").split("-")[3];
                    Line l =metroLines.stream()
                            .filter(p->p.getNumber().equals(numberOfConnectedLine))
                            .findFirst().get();
                    String nameOfConnectedStation = connectedStationName(element.attr("title"));
                    Station connectedStation = l.getStation(nameOfConnectedStation);
                    connectedStation.hasConnection=true;
                    connection.addStationToConnection(connectedStation);
                }
                connections.add(connection);
            }
        }
        }
    }
    private static void parseLines(){
        if (metroLines.isEmpty()){
            var lines = doc.select("span[data-line]");
            for (Element e: lines ) {
                Line line = new Line(e.attr("data-line"), e.text());
                metroLines.add(line);
            }
        }
    }

    protected static List <Line> getLines(){
        parseLines();
        parseStations();
        parseConnections();
        return metroLines;
    }
    protected static List <Station> getStations(){
        parseLines();
        parseStations();
        parseConnections();
        return metroStations;
    }
    protected static Set <Connection> getConnections(){
        parseConnections();
        return connections;
    }


    private static String connectedStationName(String s){
        logger.traceEntry(s);
        String stationName = s.split("«")[1].split("»")[0];
        return logger.traceExit(stationName);
    }

}
