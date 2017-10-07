package dictionary;

import java.util.Comparator;
/**
 * @author Minja Joana
 * @author Pasho Saimir
 * @author Lubiejewski Mateusz
 *
 * @param <T> tipo generico comparabile
 * 
 * BinSearchTreee è una semplice struttura dati di un albero binario
 * dove i nodi sono inseriti nelle foglie
 * Non bilanciato
 */

public class BinSearchTree<T> {
	
    Node<T> root = null;

    /**
     * Crea un nuovo albero con una radice
     * 
     * @param cmp istanzia un Comparator
     * @param i chiave del primo elemento
     */
    public BinSearchTree(Comparator<T> cmp, T i) {
        root = new Node<>(cmp, i, null);
    }
    
    /**
     * Stampa i nodi
     */
    public void print() {
        root.print();

    }
    
    /**
     * inserisce il nodo
     *
     * @param i valore dell'elemento da inserire
     * 
     * nel caso di duplicati, sono sovrascritti
     */
    public void insert(T i) {
        root.insert(i);

    }
    
    /**
     * cancella un nodo
     *
     * @param i valore dell'elementod a cancellare
     */
    public void delete(T i) {
        Node<T> x = findNode(i);
        if(x!=null){
            treeDelete(x);
        }
    }
    /**
     * cerca se l'elemento è contenuto 
     *
     * @param key
     * @return true se lo trova
     *         false altrimenti
     */
    public boolean contains(T key){
        return findNode(key)!=null;
    }
    
    /**
     * cerca se l'elemento esiste e lo restituisce
     *
     * @param key
     * @return item, Node, se il nondo è contenuto
     *         root, Node, altrimenti
     */
    public Node<T> findNode(T key){
        Node<T> x=root;
        while(x!=null && x.comparator.compare(key, x.key)!=0){
            if(x.comparator.compare(key, x.key)<0){
                x = x.left;
            }
            else{
                x = x.right;
            }
        }
        return x;
    }
    
    /**
     * inserisce un figlio nel sottoalbero
     *
     * @param u nodo del parent tree
     * @param v root del sottoalbero
     */
    public void transplant(Node<T> u, Node<T> v){
        if(u.parent==null){
            root=v;
        }
        else if(u==u.parent.left){
            u.parent.left=v;
        }
        else{
            u.parent.right=v;
        }
        if(v!=null){
            v.parent=u.parent;
        }
    }
    
    /**
     * cancella il nodo z e il suo sottoalbero
     *
     * @param z radice del sottoalbero
     */
    public void treeDelete(Node<T> z){
        if(z.left==null){
            transplant(z,z.right);
        }
        else if(z.right==null){
            transplant(z,z.left);
        }
        else{
            Node<T> y = z.right.findMinValue();
            if(y.parent!=z){
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
        }
    }

    public Node<T> getRoot() {
        return this.root;
    }
    /**
     * Private class Node
     * @param <T>
     */
    public class Node<T>{
        Node<T> left;
        Node<T> right;
        Node<T> parent;
        T key;

        public Node<T> getLeft() {
            return left;
        }

        public Node<T> getRight() {
            return right;
        }   

        public Node<T> getParent() {
            return parent;
        }

        public T getKey() {
            return key;
        }

        public Comparator<T> getComparator() {
            return comparator;
        }
        private Comparator<T> comparator;

        public Node(Comparator<T> comp, T element){
            comparator=comp;
            key = element;
            left=null;
            right=null;
        }

        public Node(Comparator<T> comp, T element, Node<T> parentOfTree){
            comparator=comp;
            key=element;
            left=null;
            right=null;
            parent=parentOfTree;
        }

        /**
         * Trova l'elemento T nell'albero 
         *
         * @param k elemento T da trovare
         * @return item T se trovato
         *         null altrimenti
         */
        public T find(T k){
            T foundLeft = null;
            T foundRight = null;
            if(comparator.compare(this.key, k) == 0){
                return key;
            }
            //else if(comparator.compare(key, k) < 0){
            else if(comparator.compare(key, k) > 0){
                foundLeft=left.find(k);
            }
            //else if(comparator.compare(key, k) > 0){
            else if(comparator.compare(key, k) < 0){
                foundRight = right.find(k);
            }
            if(foundLeft != null){
                return foundLeft;
            }
            else if(foundRight != null){
                return foundRight;
            }
            else{
                return null;
            }

        }
        
        /**
         * stampa ricorsiva dell'albero
         */
        public void print(){
            System.out.print("parent ");
            System.out.print(""+(parent!=null?parent.key:"orphan"));
            System.out.println(" value "+this.key);
            if(left!=null){
                left.print();
            }
            if(right!=null){
                right.print();
            }
        }
        /**
         * Trova il nodo con il valore più alto e lo cancella
         * 
         * @return nodo, Node, padre del nodo cancellato
         */
        private Node<T> findMaxValueAndDelete(){
            if(right==null){
                return this;
            }
            else{
                return right.findMaxValueAndDelete();
            }
        }
    
        /**
         * trova il valore minimo contenuto 
         *
         * @return il nodo che contiene il valore minore
         */
        private Node<T> findMinValue(){
            if(left==null){
                return this;
            }
            else{
                return left.findMinValue();
            }
        }

        /**
         * inserisce un elemento, i duplcati sono ammessi e inseiriti nel sottoalbero sinistro
         * 
         * @param element
         * @return true se inserito
         *         false altrimenti
         */
        public boolean insert(T element){
            if(comparator.compare(key, element)>=0){
                if(left == null){
                    left = new Node<T>(comparator, element, this);
                    return true;
                }
                else{
                    left.insert(element);
                }
            }
            else{
                if(right ==null){
                    right = new Node<T>(comparator, element, this);
                    return true;
                }
                else{
                    right.insert(element);	
                }
            }
            return false;
        }

        /**
         * inserisce senza duplicati
         * 
         * @param element
         * @return true se inserito
         *         false se già inserito
         */
        public boolean insertNoDup(T element){
            boolean duplicateLeft = false;
            boolean duplicateRight = false;
            if(comparator.compare(key, element) > 0){
                if(left == null){
                    left = new Node<>(comparator, element, this);
                }
                else{
                    duplicateLeft = left.insert(element);
                }
            }
            else if (comparator.compare(key, element) > 0){
                if(right == null){
                    right = new Node<>(comparator, element, this);
                }
                else{
                    duplicateRight = right.insert(element);
                }
            }
            else{
                return false;
            }
            if(duplicateLeft){
                return true;
            }
            if(duplicateRight){
                return true;
            }
            return true;
        }
        /**
         *
         * @return true se è una foglia
         *         false altrimenti
         */
        private boolean isLeaf(){
            return left==null && right==null;
        }        
    }
}
