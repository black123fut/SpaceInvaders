package model;

public class DoubleLinkedList<T> implements ListModel<T>{
    private DoubleNode<T> head;
    private String type;
    private int size;

    /**
     * Constructor.
     */
    public DoubleLinkedList(){
        this.head = null;
        this.size = 0;
    }

    /**
     * Agrega un nodo al fianl de la lista.
     * @param data El objeto del nuevo nodo.
     */
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

    /**
     * Elimina un nodo de la lista.
     * @param index Indice del nodo.
     */
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

    /**
     * Obtiene el objeto que almacena el nodo.
     * @param index Indice del nodo.
     * @return El objeto del nodo.
     */
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

    /**
     * Obtiene el largo de la lista.
     * @return El largo de la lista.
     */
    public int length(){
        return size;
    }

    /**
     * Asigna el tipo de la lista.
     * @param type El tipo de la lista.
     */
    @Override
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Obtiene el tipo de la lista.
     * @return El tipo de la lista.
     */
    @Override
    public String getType() {
        return type;
    }

    @Override
    public CircularNode<T> getNode(int index) {
        return null;
    }
}

