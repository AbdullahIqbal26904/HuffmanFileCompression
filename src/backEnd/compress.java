package backEnd;

import java.io.*;
import java.util.*;
public class compress implements Serializable {
    private Map<Byte, String> huffmanCodes = new HashMap<>();
    private priorityQueue q;
    private String inputFileName;
    public String outputFileName;
    public long fileLength;
    private huffman h1 ;
    private huffmanNode root;
    private Map<Byte, Integer> freqMap = new HashMap<>();

    public compress(String inputFileName,String serializeFileName) throws IOException {
        this.inputFileName=inputFileName;
        this.outputFileName=(inputFileName.substring(0, inputFileName.length()-4))+"zip.txt";
        createFile(outputFileName);
        fileReader(inputFileName);
        this.q = priorityAdd(new priorityQueue());
        h1 = new huffman(q);
        h1.buildHuffmanTree();
        this.root= h1.getRoot();
        huff();
        serrialize(root);
    }
    public compress(String inputFileName) throws IOException, ClassNotFoundException {
        this.inputFileName = inputFileName;
        this.outputFileName=(inputFileName.substring(0, inputFileName.length()-4))+"zip.txt";
        createFile(outputFileName);
        this.root = (huffmanNode) Serializationutil.deserialize();
        huff();
    }

    public static void serrialize(huffmanNode huff) throws IOException {
        Serializationutil.serialize(huff);
    }
    public void fileReader(String fileName) throws IOException {
        String filePath = fileName;
        FileInputStream inStream = new FileInputStream(filePath);
        byte[] b = new byte[inStream.available()];
        inStream.read(b);
        for (byte b1 : b) {
            Integer value = freqMap.get(b1);
            if (value == null)
                freqMap.put(b1, 1);
            else
                freqMap.put(b1, value + 1);
        }
    }
    public void createFile(String filename){
        try {
            File myObj = new File(filename);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public priorityQueue priorityAdd(priorityQueue q){
        for (Map.Entry<Byte, Integer> entry : freqMap.entrySet())
        {
            huffmanNode hn = new huffmanNode(entry.getKey(), entry.getValue());
            hn.left = null;
            hn.right = null;
            q.Enqueue(hn);
        }
        return q;
    }

    public void huff() throws IOException {
        printCode(this.root,"",huffmanCodes);
    }
    public void printCode(huffmanNode root, String code, Map<Byte, String> huffmanCodes)
    {
        if (root == null) {
            return;
        }
        if (root.left==null && root.right==null ) {
            huffmanCodes.put(root.data, code);
        }
        printCode(root.left, code + "0", huffmanCodes);
        printCode(root.right, code + "1", huffmanCodes);
    }
    public void Compress(String inputFile) throws IOException{
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             DataOutputStream output = new DataOutputStream(new FileOutputStream(outputFileName))) {
            int bitBuffer = 0;
            int bitCount = 0;

            int c;
            while ((c = br.read()) != -1) {
                String code = huffmanCodes.get((byte) c);
                    for (int i = 0; i < code.length(); i++) {
                        if (code.charAt(i) == '1') {
                            bitBuffer = (bitBuffer << 1) | 1;
                        } else {
                            bitBuffer = (bitBuffer << 1);
                        }
                        bitCount++;
                        if (bitCount == 8) {
                            output.writeByte(bitBuffer);
                            bitBuffer = 0;
                            bitCount = 0;
                        }
                    }
            }
            while (bitCount > 0) {
                bitBuffer = (bitBuffer << 1);
                bitCount++;
                if (bitCount == 8) {
                    output.writeByte(bitBuffer);
                    break;
                }
            }
        }
        File f = new File(this.outputFileName);
        this.fileLength = f.length();
        System.out.println(fileLength);
        System.out.println("Compression complete.");
    }
    }


