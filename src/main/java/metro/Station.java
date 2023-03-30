package metro;

import java.time.LocalDate;
import java.util.Objects;

public class Station {
    private String name;
    private Line line;
    private String depth = null;
    private String date = null;
    public boolean hasConnection= false;

    public Station(String name, Line line) {
        this.name = name;
        this.line = line;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Station station)) return false;
        return Objects.equals(depth, station.depth) && getName().equals(station.getName()) && getLine().equals(station.getLine()) && date.equals(station.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getLine(), depth, date);
    }



    @Override
    public String toString() {
        return name  +  "--- " + line.getName() +  "--- " + date +  "--- "+  "Глубина " +depth ;
    }

    public String getDepth() {
        return depth;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public Line getLine() {
        return line;
    }
    public void setDepth(String depth){
        this.depth= depth;
    }
}
