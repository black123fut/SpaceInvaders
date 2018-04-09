package model;

public class DoubleNode<T> implements NodeModel<T> {
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

    public T getData(){
        return this.data;
    }

    @Override
    public void setData(T data) {
        this.data = data;
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
