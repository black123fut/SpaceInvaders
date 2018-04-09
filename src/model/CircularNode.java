package model;

public class CircularNode<T> implements NodeModel<T>{
    private T data;
    private CircularNode<T> next;

    public CircularNode(T data){
        this.data = data;
        this.next = this;
    }

    public void setData(T data){
        this.data = data;
    }

    public T getData() {
        return data;
    }

    void setNext(CircularNode<T> next) {
        this.next = next;
    }

    CircularNode<T> getNext() {
        return next;
    }
}