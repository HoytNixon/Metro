import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import metro.Connection;
import metro.Line;
import metro.Station;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import parsing.DesMetro;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class WriteStations {
    private static final Logger logger = LogManager.getLogger(WriteStations.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void writeStationsToJSON(File file, DesMetro metro){
        List <Station> stations= metro.getStations();
        try {
            ArrayNode stationsNode = mapper.createArrayNode();
            for (Station s: stations) {
                ObjectNode station = mapper.createObjectNode().objectNode();
                station.put("name", s.getName());
                station.put("line", s.getLine().toString());
                if (s.getDepth()!=null) {
                    station.put("depth", s.getDepth());
                }
                if (s.getDate()!=null) {
                    station.put("date", s.getDate());
                }
                station.put("hasConnection", s.hasConnection);
                stationsNode.add(station);
            }
            JsonNode stat =mapper.createObjectNode().set("stations", stationsNode);
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, stat);
        } catch (IOException ex) {
            logger.log(Level.ERROR, ex.getStackTrace());
        }
    }
    public static void writeMetroToJSON(File file, DesMetro metro){
        DefaultPrettyPrinter.Indenter indenter =
                new DefaultIndenter("  ", DefaultIndenter.SYS_LF);
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.indentArraysWith(indenter);
        try{
           ObjectNode metroNode = mapper.createObjectNode().objectNode();
           ObjectNode linesWithStations =mapper.createObjectNode().objectNode();
           for (Line line:metro.getLines()){
                ArrayNode stationsOnLine = mapper.createArrayNode();
                for (Station station: line.getStations()){
                    stationsOnLine.add(station.getName());
                }
               linesWithStations.set(line.getNumber(), stationsOnLine);
           }
           ArrayNode conectionsContainer= mapper.createArrayNode();

           for (Connection c: metro.getConnections()){
               ArrayNode coonectionsArray = mapper.createArrayNode();

               for (Station station: c.conectedStations) {
                   ObjectNode stationInConnection = mapper.createObjectNode();
                   stationInConnection.put("line", station.getLine().getNumber());
                   stationInConnection.put("station", station.getName());
                   coonectionsArray.add(stationInConnection);
               }
               conectionsContainer.add(coonectionsArray);
           }
           ArrayNode linesNode = mapper.createArrayNode();
           for(Line line:metro.getLines()){
               ObjectNode lineNode=mapper.createObjectNode();
               lineNode.put("number", line.getNumber());
               lineNode.put("name", line.getName());
               linesNode.add(lineNode);
           }


           metroNode.set("stations",linesWithStations);
           metroNode.set("connections", conectionsContainer);
           metroNode.set("lines", linesNode);
           mapper.writer(printer).writeValue(file, metroNode);

        }catch (IOException ex) {
            logger.log(Level.ERROR, ex.getStackTrace());
        }
    }
}
