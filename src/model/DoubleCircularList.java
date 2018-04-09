package model;

public class DoubleCircularList<T> implements ListModel<T> {
    private DoubleCircularNode<T> head;
    private String type;
    private int size;

    public DoubleCircularList(){
        this.head = null;
        this.size = 0;
    }

    public void add(T data){
        DoubleCircularNode<T> newNode = new DoubleCircularNode<>(data);

        if (this.head != null){
            if (size == 1){
                this.head.setNext(newNode);
                this.head.setPrev(newNode);
                newNode.setPrev(this.head);
                newNode.setNext(this.head);
            } else {
                DoubleCircularNode<T> tmp = head;

                while (tmp.getNext() != head){
                    tmp = tmp.getNext();
                }
                tmp.setNext(newNode);
                newNode.setNext(this.head);
                this.head.setPrev(newNode);
                newNode.setPrev(tmp);
            }

        } else {
            this.head = newNode;
            this.head.setNext(this.head);
            this.head.setPrev(this.head);
        }
        size++;
    }

    public void remove(int index){
        if (head != null){
            DoubleCircularNode<T> tmp = head;
            int cont = 0;

            if (index == 0){
                this.head = this.head.getNext();

                while(tmp.getNext() != head){
                    tmp = tmp.getNext();
                }
                tmp.setNext(tmp.getNext().getNext());
                tmp.getNext().getNext().setPrev(tmp);
                size--;

            } else {
                cont++;
                while (tmp.getNext() != head){
                    if (cont == index){
                        tmp.setNext(tmp.getNext().getNext());
                        tmp.getNext().getNext().setPrev(tmp);
                        size--;
                        return;
                    }
                    tmp = tmp.getNext();
                    cont++;
                }
            }

        }
    }

    public T get(int index){
        int cont = 0;

        if (this.head != null){
            DoubleCircularNode<T> tmp = head;

            while(cont <= size){
                if (cont == index)
                    return tmp.getData();
                tmp = tmp.getNext();
                cont++;
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
    @SuppressWarnings("Duplicates")
    public DoubleCircularNode<T> getNode(int index) {
        int counter = 0;

        if (head != null){
            DoubleCircularNode<T> tmp = head;

            while (counter <= size){
                if (counter == index){
                    return tmp;
                }
                tmp = tmp.getNext();
                counter++;
            }
        }
        return null;
    }
}
