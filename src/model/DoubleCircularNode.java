package model;

public class DoubleCircularNode<T> {
    private T data;
    private DoubleCircularNode<T> next;
    private DoubleCircularNode<T> prev;

    public DoubleCircularNode(T data){
        this.data = data;
        this.next = this;
        this.prev = this;
    }

    public void setData(T data){
        this.data = data;
    }

    public T getData(){
        return data;
    }

    public void setNext(DoubleCircularNode<T> next){
        this.next = next;
    }

    public DoubleCircularNode<T> getNext(){
        return next;
    }

    public void setPrev(DoubleCircularNode<T> prev){
        this.prev = prev;
    }

    public DoubleCircularNode<T> getPrev(){
        return prev;
    }
}