package fr.sle;

import java.nio.file.Path;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author slemoine
 */
public class CsvFile {

    private Path filePath;

    private Pattern pattern;

    public CsvFile(Path filePath, Pattern pattern) {
        this.filePath = filePath;
        this.pattern = pattern;
    }

    public Path getFilePath() {
        return filePath;
    }

    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CsvFile csvFile = (CsvFile) o;
        return Objects.equals(filePath, csvFile.filePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filePath);
    }


}
