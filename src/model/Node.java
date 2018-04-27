package model;

class Node<AnyType> implements  NodeModel<AnyType>{
    public AnyType data;
    public Node<AnyType> next;

    /**
     * Constructor.
     * @param data Valor del nodo.
     * @param next Siguiente nodo.
     */
    public Node(AnyType data, Node<AnyType> next){
        this.data = data;
        this.next = next;
    }

    public String toString(){
        return data + "";
    }

    @Override
    public AnyType getData() {
        return null;
    }

    @Override
    public void setData(Object data) {

    }
}
