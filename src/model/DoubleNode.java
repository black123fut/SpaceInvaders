package model;

public class DoubleNode<T> {
    private T data;
    private DoubleNode<T> next;
    private DoubleNode<T> prev;

    DoubleNode(T data){
        this(data, null, null);
    }

     DoubleNode(T data, DoubleNode<T> next, DoubleNode<T> prev){
        this.data = data;
        this.next = next;
        this.prev = prev;
    }

    T getData(){
        return this.data;
    }

    DoubleNode<T> getNext(){
        return next;
    }

    public DoubleNode<T> getPrev(){
        return prev;
    }

    void setNext(DoubleNode<T> next) {
        this.next = next;
    }

    void setPrev(DoubleNode<T> prev) {
        this.prev = prev;
    }


}
