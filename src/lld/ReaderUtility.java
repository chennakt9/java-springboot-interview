package lld;

import java.util.*;

// =====================
// SOURCE STRATEGY
// =====================
interface DataSource {
    String read();
}

class FileSource implements DataSource {
    private final String filePath;

    public FileSource(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String read() {
        return "file-data-from-" + filePath;
    }
}

class DBSource implements DataSource {
    private final String connectionString;

    public DBSource(String connectionString) {
        this.connectionString = connectionString;
    }

    @Override
    public String read() {
        return "db-data-from-" + connectionString;
    }
}

class APISource implements DataSource {
    private final String endpoint;

    public APISource(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String read() {
        return "api-response-from-" + endpoint;
    }
}

// =====================
// PARSER STRATEGY
// =====================
interface DataParser {
    Object parse(String rawData);
}

class JsonParser implements DataParser {
    @Override
    public Object parse(String rawData) {
        return "Parsed JSON -> " + rawData;
    }
}

class XmlParser implements DataParser {
    @Override
    public Object parse(String rawData) {
        return "Parsed XML -> " + rawData;
    }
}

class CsvParser implements DataParser {
    @Override
    public Object parse(String rawData) {
        return "Parsed CSV -> " + rawData;
    }
}

// =====================
// READER (CORE)
// =====================
class Reader {
    private final DataSource source;
    private final DataParser parser;

    public Reader(DataSource source, DataParser parser) {
        this.source = source;
        this.parser = parser;
    }

    public Object read() {
        String rawData = source.read();
        return parser.parse(rawData);
    }
}

// =====================
// FACTORIES
// =====================
class SourceFactory {
    public static DataSource createSource(String type, String config) {
        switch (type.toUpperCase()) {
            case "FILE":
                return new FileSource(config);
            case "DB":
                return new DBSource(config);
            case "API":
                return new APISource(config);
            default:
                throw new IllegalArgumentException("Unknown source type: " + type);
        }
    }
}

class ParserFactory {
    public static DataParser createParser(String format) {
        switch (format.toUpperCase()) {
            case "JSON":
                return new JsonParser();
            case "XML":
                return new XmlParser();
            case "CSV":
                return new CsvParser();
            default:
                throw new IllegalArgumentException("Unknown format: " + format);
        }
    }
}

// =====================
// DEMO / MAIN
// =====================
public class ReaderUtility {
    public static void main(String[] args) {

        DataSource source = SourceFactory.createSource("API", "https://users-service");
        DataParser parser = ParserFactory.createParser("JSON");

        Reader reader = new Reader(source, parser);
        Object result = reader.read();

        System.out.println(result);
    }
}

