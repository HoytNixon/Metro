package parsing;
import com.fasterxml.jackson.annotation.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ParseJson {
    private static final Logger logger = LogManager.getLogger(ParseJson.class);
    public static List<JsonDepth> parseDepths(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, new TypeReference<>() {
        });
    }
    public static class JsonDepth {
        private String name;
        private String depth;
        @JsonCreator
        public JsonDepth(@JsonProperty("station_name")String name,
                         @JsonProperty("depth")String depth) {
            this.name = name;
            this.depth = depth;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDepth() {
            return depth;
        }

        public void setDepth(String depth) {
            this.depth = depth;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof JsonDepth jsonDepth)) return false;
            return getName().equals(jsonDepth.getName());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getName());
        }

        @Override
        public String toString() {
            return name + " ---- " + depth;
        }
    }



}
