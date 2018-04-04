package model;

public class DoubleLinkedList<T> implements ListModel<T>{
    private DoubleNode<T> head;
    private String type;
    private int size;

    public DoubleLinkedList(){
        this.head = null;
        this.size = 0;
    }

    public void add(T data){
        if (this.head == null)
            head = new DoubleNode<>(data, null, null);

        else{
            DoubleNode<T> tmp = head;
            DoubleNode<T> newNode = new DoubleNode<>(data);

            while(tmp.getNext() != null){
                tmp = tmp.getNext();
            }

            tmp.setNext(newNode);
            newNode.setPrev(tmp);
        }
        size++;
    }

    public void remove(int index){
        int cont = 0;

        if (head != null){
            if (index == 0){
                head.getNext().setPrev(null);
                head = head.getNext();
                head.setPrev(null);
                size--;
            } else{
                DoubleNode<T> tmp = head;
                cont++;

                while (tmp.getNext() != null){
                    if (cont == index){
                        tmp.setNext(tmp.getNext().getNext());

                        if (tmp.getNext() != null)
                            tmp.getNext().setPrev(tmp);

                        size--;
                        return;
                    } else{
                        cont++;
                        tmp = tmp.getNext();
                    }
                }
            }
        }
    }

    public T get(int index){
        if (head != null){
            DoubleNode<T> tmp = head;
            int count = 0;

            while(tmp != null){
                if (count == index)
                    return tmp.getData();

                count++;
                tmp = tmp.getNext();
            }
        }
        return null;
    }


    public int length(){
        return size;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public CircularNode<T> getNode(int index) {
        return null;
    }
}

