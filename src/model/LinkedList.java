package model;

public class LinkedList<T> implements ListModel<T>{
    private Node<T> head;
    private int size;
    private String type;

    public LinkedList(){
        head = null;
    }

    public void add(T item){
        if (head == null)
            head = new Node<>(item, null);
        else{
            Node tmp = head;
            while(tmp.next != null)
                tmp = tmp.next;
            tmp.next = new Node<>(item, null);
        }
        size++;
    }

    public void remove(int index) {
        try{
            Node tmp = head;
            int counter = 0;

            if (head == null){
                return;
            }

            if (index == 0) {
                head = head.next;
                size--;
                return;
            }

            counter++;

            while (tmp.next != null) {
                if (counter == index) {
                    tmp.next = tmp.next.next;
                    size--;
                    return;
                }
                else{
                    tmp = tmp.next;
                    counter++;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public int length(){
        return size;
    }

    public T get(int indice){
        Node<T> tmp = head;
        int cont = 0;

        while (tmp != null){
            if (cont == indice) {
                return tmp.data;
            }

            cont++;
            tmp = tmp.next;
        }
        return null;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType(){
        return type;
    }

    @Override
    public CircularNode<T> getNode(int index) {
        return null;
    }
}
