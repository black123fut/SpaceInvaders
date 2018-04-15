package model;

public class CircularList<T> implements ListModel<T>{
    private CircularNode<T> head;
    private String type;
    private int size;

    /**
     * Constructor
     */
    public CircularList(){
        this.head = null;
        this.size = 0;
    }

    /**
     * Crea un nuevo nodo antes del head.
     * @param data Objeto que se va a guardar en el nodo.
     */
    @Override
    public void add(T data){
        CircularNode<T> newNode = new CircularNode<>(data);

        if (this.head != null){
            if (size == 1){
                this.head.setNext(newNode);
                newNode.setNext(this.head);
            } else{
                CircularNode<T> tmp = head;

                while (tmp.getNext() != head){
                    tmp = tmp.getNext();
                }
                tmp.setNext(newNode);
                newNode.setNext(this.head);
            }
        } else {
            this.head = newNode;
            this.head.setNext(this.head);
        }
        size++;
    }

    /**
     * Remueve un nodo de la lista.
     * @param index Indice del nodo.
     */
    @Override
    public void remove(int index){
        if (head != null){
            CircularNode<T> tmp = head;
            int cont = 0;

            if (index == 0){
                this.head = this.head.getNext();

                while (tmp.getNext() != head){
                    tmp = tmp.getNext();
                }

                tmp.setNext(this.head.getNext());
                size--;
                if (size == -1)
                    size = 0;

            } else {
                cont++;

                while (tmp.getNext() != head){
                    if (cont == index){
                        tmp.setNext(tmp.getNext().getNext());
                        size--;
                        if (size == -1)
                            size = 0;
                        return;
                    }

                    cont++;
                    tmp = tmp.getNext();
                }
            }
        }
    }

    /**
     * Obtiene la informacion del nodo.
     * @param index Indice del nodo.
     * @return El objeto que se buscaba.
     */
    @Override
    public T get(int index){
        int count = 0;

        if (this.head != null){
            CircularNode<T> tmp = head;

            while (count <= size){
                if (count == index){
                    return tmp.getData();
                }
                tmp = tmp.getNext();
                count++;
            }
        }
        return null;
    }

    /**
     * Obtiene un nodo.
     * @param index Indice del nodo.
     * @return El nodo que se buscaba.
     */
    @SuppressWarnings("Duplicates")
    public CircularNode<T> getNode(int index){
        int cont = 0;

        if (this.head != null){
            CircularNode<T> tmp = head;

            while (cont <= size){
                if (cont == index){
                    return tmp;
                }
                tmp = tmp.getNext();
                cont++;
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
     * Da el tipo que es la lista.
     * @param type Tipo de hilera que se va a usar.
     */
    @Override
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Obtiene el tipo de hilera de la lista.
     * @return Tipo de hilera de la lista.
     */
    @Override
    public String getType() {
        return type;
    }
}