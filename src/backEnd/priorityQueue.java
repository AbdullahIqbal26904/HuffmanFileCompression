package backEnd;


public class priorityQueue {
    Node Front;
    Node Rear;
    public void Enqueue(huffmanNode obj){
        if(Front == null){
            Front = new Node(obj);
            return;
        }
        Node N = new Node(obj);
        if(obj.frequency<Front.data.frequency){
            N.next=Front;
            Front=N;
            return;
        }
        Node start = Front;
        while (start.next != null &&
                start.next.data.frequency < obj.frequency) {
            start = start.next;
        }
        if(start.next==null){
            start.next=N;
            return;
        }
        N.next = start.next;
        start.next = N;

    }
    public huffmanNode Dequeue(){
         if(Front != null) {
            huffmanNode x = Front.data;
            Front = Front.next;
            return x;
         }
        return null;
    }
    public boolean isEmpty(){
        if(Rear == null && Front == Rear){
            return true;
        }
        return false;
    }
    public String toString(){
        Node temp = Front;
        StringBuilder a= new StringBuilder();
        while (temp!=Rear){
            a.append(temp.data.data).append(temp.data.frequency).append(" ");
            temp=temp.next;
        }
        a.append(temp.data.data).append(temp.data.frequency).append(" ");
        return a.toString();
    }

}
