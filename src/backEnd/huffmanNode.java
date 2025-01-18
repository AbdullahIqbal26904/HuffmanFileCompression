package backEnd;

import java.io.Serializable;

public class huffmanNode implements Serializable, Comparable<huffmanNode> {
    private static final long serialversionUID = 129348938L;
    byte data;
    int frequency;
    huffmanNode left;
    huffmanNode right;

    public huffmanNode(Byte data, int value) {
        this.data = data;
        this.frequency = value;
    }

    @Override
    public int compareTo(huffmanNode o) {
       if(o.frequency > frequency){
           return -1;
       }else if(o.frequency==frequency){
           return 0;
       }
       return 1;
    }
}
