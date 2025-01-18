package backEnd;

import java.util.LinkedList;

public class hashmaps {
    static class HashMap<K,V>{
        private class Node{
            K key;
            V value;

            public Node(K key, V value) {
                this.key = key;
                this.value = value;
            }
        }
        private int n;
        private int N;
        private LinkedList<Node> buck[];
        public HashMap(){
            this.N=4;
            this.buck=new LinkedList[4];
            for (int i=0;i<4;i++){
                this.buck[i]=new LinkedList<>();
            }
        }
        public int hashFunc(K key){
            int bi = key.hashCode();
            return Math.abs(bi)%N;
        }
        public int SearInLL(K key,int bi){
            LinkedList<Node> ll = buck[bi];
            for (int i=0;i<ll.size();i++){
                if(ll.get(i).key==key){
                    return i;
                }
            }
            return -1;
        }
        public void put(K key,V value){
            int buckIndex = hashFunc(key);
            int dataInd = SearInLL(key,buckIndex);
            if(dataInd==-1){
                buck[buckIndex].add(new Node(key,value));
            }else {
                Node data = buck[buckIndex].get(dataInd);
                data.value=value;
            }
            double x = (double) N/n;
            if(x>2.0){
                rehash();
            }
        }
        public void rehash(){
            LinkedList<Node> oldBuck[] = buck;
            buck = new LinkedList[N*2];
            for (int i=0;i<N*2;i++){
                buck[i] =new LinkedList<>();
            }
            for (int i=0;i< oldBuck.length;i++){
                LinkedList<Node> l = oldBuck[i];
                for (int j=0;j<l.size();j++){
                    Node n = l.get(j);
                    put(n.key,n.value);
                }
            }
        }
        public V get(K key){
            int bi = hashFunc(key);
            int di = SearInLL(key,bi);
            if(di==-1){
                return null;
            }else {
                Node n =buck[bi].get(di);
                return n.value;
            }
        }

    }
}
