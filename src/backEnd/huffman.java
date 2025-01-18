package backEnd;

import java.io.Serializable;

public class huffman  {

    private priorityQueue q;
    private huffmanNode root;
    public huffman(priorityQueue q){
        this.q=q;
    }

    public huffmanNode getRoot() {
        return root;
    }

    public huffmanNode buildHuffmanTree(){
        while (q.Front.next!=null){
            huffmanNode x = q.Dequeue();
            huffmanNode y = q.Dequeue();
            int a1 =x.frequency + y.frequency;
            byte a2 = (byte) (x.data+y.data);
            huffmanNode f = new huffmanNode(a2,a1);
            f.left = x;
            f.right = y;
            this.root = f;
            q.Enqueue(f);
        }
        return root;
    }
}
