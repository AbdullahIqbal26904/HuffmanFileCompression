package backEnd;

import java.io.*;

public class Serializationutil {
    static String filename = "src/serialize2.txt";

    public static Object deserialize() throws IOException,
            ClassNotFoundException {
        FileInputStream fis = new FileInputStream(filename);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object obj = ois.readObject();
        ois.close();
        return obj;
    }

    public static void serialize(Object obj)
            throws IOException {
        FileOutputStream fos = new FileOutputStream(filename);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(obj);

        fos.close();
    }

}
