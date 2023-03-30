package metro;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Connection {

    public List <Station> conectedStations;
    public Connection(){
        conectedStations = new ArrayList<>();
    }
    public void addStationToConnection(Station s){
        conectedStations.add(s);
    }

    @Override
    public String toString() {
        var sb= new StringBuilder("Connected stations :\n");
        for (Station st: conectedStations){
            sb.append(st).append("\n");
        }
        return  sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Connection that)) return false;
        boolean flag = true;
        for (Station st: conectedStations){
            if (!that.conectedStations.contains(st)) {
                flag = false;
                break;
            }
        }
        for (Station st: that.conectedStations){
            if (!conectedStations.contains(st)) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    @Override
    public int hashCode() {
        return Objects.hash(conectedStations.size());
    }
}
