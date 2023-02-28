import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Levels {
    public static String[] LevelList = {"level1.txt", "level2.txt", "level3.txt", "level4.txt", "level5.txt", "level6.txt", "level7.txt", "level8.txt", "TheCMPT276Level.txt"};
    public int numberOfLevels;
    public String levelPath = "levels/";

    public Levels() {
        numberOfLevels = LevelList.length;
    }

    // counts the number of files in a given directory might eventually be useful
    public int countLevels() {
        int count = 0;
        try (Stream<Path> files = Files.list(Paths.get(levelPath))) {
            count = (int) files.count();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    public String getLevel(int levelNumber) {
        assert (levelNumber < numberOfLevels);
        assert (levelNumber >= 0);

        return levelPath + LevelList[levelNumber];

    }
}
