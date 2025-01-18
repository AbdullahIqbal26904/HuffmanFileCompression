package backEnd;

import java.io.*;

public class decompress {
    private huffmanNode tree;
    private String filename;
    private String destinationfilename;
    private long fileLen;
    public decompress( String compressedFilename) throws IOException, ClassNotFoundException {
        this.filename = compressedFilename;
        this.tree=(huffmanNode) Serializationutil.deserialize();
        this.destinationfilename = compressedFilename.substring(0,compressedFilename.length()-4)+"unzipped.txt";
        createFile(destinationfilename);
        decompress2(filename);
    }
    public void createFile(String filename){
        try {
            File myObj = new File(filename);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public void decompress2(String filename) throws IOException {
        String binary="";
        System.out.println();
        FileInputStream inStream = new FileInputStream(filename);
        byte[] b = new byte[inStream.available()];
        inStream.read(b);
        for (int i=0;i< b.length;i++){
            String s1 = decToBin(b[i]);
            binary+=s1;
        }
        writeToFile(binary,destinationfilename);
    }
    public void writeToFile(String encoded,String filename) throws IOException {
        FileWriter myWriter = new FileWriter(filename);
        huffmanNode temp=tree;
       for (int i=0;i<encoded.length();i++){
            if(temp.left==null && temp.right==null){
                myWriter.append((char) temp.data);
                 temp =tree;
            }
            if(encoded.charAt(i)=='0'){
                temp=temp.left;
            } else if(encoded.charAt(i)=='1'){
                temp=temp.right;
            }
        }
       myWriter.close();
       File f = new File(filename);
       fileLen=f.length();
    }

    public long getFileLen() {
        return fileLen;
    }

    public String decToBin(int decimalNumber){
        int x=0;
        if(decimalNumber<0){
            x = 256+decimalNumber;
            StringBuilder binary = new StringBuilder();
            for (int i = 0; i < 8; i++) {
                int remainder = x % 2;
                binary.insert(0, remainder);
                x = x / 2;
            }
            return String.valueOf(binary);
        }
        StringBuilder binary = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int remainder = decimalNumber % 2;
            binary.insert(0, remainder);
            decimalNumber = decimalNumber / 2;
        }
        return String.valueOf(binary);
    }

}

