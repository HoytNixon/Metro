package parsing;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class ParseCSV {
    public static List<StationDates> parseDates(File file) throws IOException {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = CsvSchema.emptySchema().withHeader();
        List <StationDates> datesList = new ArrayList<>() ;

        ObjectReader oReader = mapper.reader(StationDates.class).with(schema);
        try(Reader reader = new FileReader(file)){
            MappingIterator<StationDates> mi = oReader.readValues(reader);
            while (mi.hasNext()) {
                StationDates current = mi.next();
                datesList.add(current);
            }
        }
        return datesList;
    }
    public static class StationDates{
        private String name;
        private String date;
        public StationDates(){
            //no arg constructor - без него ошибка
        }

//        public StationDates(String stationName, String date) {
//            this.name = stationName;
//            this.date = date;
//        }

        public String getName() {
            return name;
        }

        public String getDate() {
            return date;
        }

        public void setName(String stationName) {
            this.name = stationName;
        }

        public void setDate(String date) {
            this.date = date;
        }

        @Override
        public String toString() {
            return name +" ---- "+ date;
        }
    }
}
