package parsing;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ParseArchive {
    private static final Logger logger = LogManager.getLogger(ParseArchive.class);


    public static List<String> getFileNamesFromArchieve(File file) throws IOException {
        return getFileNamesFromArchieve(file,null);
    }

    public static List<String> getFileNamesFromArchieve(File file, String directoryToExtract) throws IOException {
        logger.traceEntry(file.toString());
        List<String> jsonFiles = new ArrayList<>();
        try (ZipFile zf = new ZipFile(file)) {
            for (Enumeration<? extends ZipEntry> iter = zf.entries(); iter.hasMoreElements(); ) {
                ZipEntry ze = iter.nextElement();
                if (!ze.isDirectory() && (ze.getName().endsWith("json") || ze.getName().endsWith("csv"))) {
                    String jsonFileName = Arrays.stream(ze.getName().split("/"))
                            .filter(s -> s.endsWith("json") || s.endsWith("csv"))
                            .findFirst()
                            .get();
                    jsonFiles.add(jsonFileName);
                    if (directoryToExtract!=null) {
                        File fileJson = new File(directoryToExtract+jsonFileName);
                        write(zf.getInputStream(ze), new BufferedOutputStream(new FileOutputStream(
                                new File(directoryToExtract+jsonFileName)
                        )));
                    }
                }
            }
            return logger.traceExit(jsonFiles);
        }
    }

    private static void write(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        while((len=in.read(buffer)) >= 0){
            out.write(buffer, 0, len);
        }
        out.close();
        in.close();
    }
}