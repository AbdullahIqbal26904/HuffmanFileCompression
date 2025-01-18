package backEnd;

import java.io.IOException;

public class driver {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        compress c = new compress("src/hufmmanNodeFile.txt","src/serialize2.txt");
    }
}
