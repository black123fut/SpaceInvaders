package model;

public interface ListModel<T> {

    void add(T data);
    void remove(int index);
    T get(int index);
    int length();
    void setType(String type);
    String getType();
    CircularNode<T> getNode(int index);
}
