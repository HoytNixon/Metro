package metro;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Line {
    private String number;
    private String name;
    private List<Station> stations;

    public Line(String number, String name) {
        stations= new ArrayList<>();
        this.number = number;
        this.name = name;
    }
    public void addStation(Station station){
        stations.add(station);
    }
    public void addStations(List <Station> stationsList){
        stations.addAll(stationsList);
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }
    public Station getStation(String station){
        return stations.stream().filter(s-> s.getName().equals(station)).findFirst().get();
    }

    public List<Station> getStations() {
        return stations;
    }

    @Override
    public String toString() {
        return name ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Line line)) return false;
        return Objects.equals(getNumber(), line.getNumber()) && getName().equals(line.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNumber(), getName());
    }
}
