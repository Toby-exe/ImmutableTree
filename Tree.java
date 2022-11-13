import java.util.NoSuchElementException;

public class Tree implements ImmutableSortedSet {
    private final TreeNode root;

    public Tree() {
        this.root = null;
    }

    private Tree(TreeNode node) {
        this.root = node;
    }

    @Override
    public ImmutableSortedSet add(String key){
        if(contains(key)) {
            return this;
        }
        else {
            //create new node with given key
            TreeNode newNode = new TreeNode(key, null, null);
            //insert current newnode with current root
            return new Tree(insert(newNode, this.root));
        }
    }


    private TreeNode insert(TreeNode newNode, TreeNode root) {
        //base case
        if (root == null) {
            return newNode;
        }
        //if new node is less than root (or if it's key is null)
        else if (stringCompare(newNode.getData(), root.getData()) < 0) {
            //insert new node to the left
            return new TreeNode(root.getData(), insert(newNode, root.getLeft()), root.getRight());
        }
        //if new node is greater than root
        else {
            //insert new node to the right
            return new TreeNode(root.getData(), root.getLeft(), insert(newNode, root.getRight()));
        }
    }

    private int stringCompare(String s1, String s2) {
        //if both strings are null
        if (s1 == null && s2 == null) {
            return 0;
        }
        //if s1 is null
        else if (s1 == null) {
            return -1;
        }
        //if s2 is null
        else if (s2 == null) {
            return 1;
        }
        //if both strings are not null
        else {
            return s1.compareTo(s2);
        }
    }

    @Override
    public ImmutableSortedSet remove(String key) {
        if(contains(key)) {
            return new Tree(removeNode(key, this.root));
        }
        else {
            return this;
        }
    }

    private TreeNode removeNode(String key, TreeNode root){
        //base case
        if (root == null) {
            return null;
        }
        //if key is less than root
        else if (stringCompare(key, root.getData()) < 0) {
            //remove from left
            return new TreeNode(root.getData(), removeNode(key, root.getLeft()), root.getRight());
        }
        //if key is greater than root
        else if (stringCompare(key, root.getData()) > 0) {
            //remove from right
            return new TreeNode(root.getData(), root.getLeft(), removeNode(key, root.getRight()));
        }
        //if key is equal to root
        else {
            //if root has no children
            if (root.getLeft() == null && root.getRight() == null) {
                return null;
            }
            //if root has only left child
            else if (root.getLeft() != null && root.getRight() == null) {
                return root.getLeft();
            }
            //if root has only right child
            else if (root.getLeft() == null && root.getRight() != null) {
                return root.getRight();
            }
            //if root has two children
            else {
                //find the smallest node in the right subtree
                TreeNode smallestNode = findSmallest(root.getRight());
                //remove the smallest node from the right subtree
                TreeNode newRight = removeNode(smallestNode.getData(), root.getRight());
                //return a new node with the smallest node as the root
                return new TreeNode(smallestNode.getData(), root.getLeft(), newRight);
            }
        }
    }

    private TreeNode findSmallest(TreeNode root) {
        //base case
        if (root.getLeft() == null) {
            return root;
        }
        //if root has a left child
        else {
            return findSmallest(root.getLeft());
        }
    }


    @Override
    public boolean contains(String key) {
        return contains(key, this.root);
    }

    private boolean contains(String key, TreeNode root) {
        //base case
        if (root == null) {
            return false;
        }
        //if key is less than root
        else if (stringCompare(key, root.getData()) < 0) {
            //search left
            return contains(key, root.getLeft());
        }
        //if key is greater than root
        else if (stringCompare(key, root.getData()) > 0) {
            //search right
            return contains(key, root.getRight());
        }
        //if key is equal to root
        else {
            return true;
        }
    }

    /**
     * Returns the key at the given index, throwing an exception if the index
     * is not in the range from <code>0</code> to <code>this.size() - 1</code>.
     * A return value of <code>null</code> indicates that <code>null</code> is
     * the value at the given index.
     *
     * @param index The index whose associated key is to be returned.
     * @return The associated key for the given index.
     * @throws NoSuchElementException Thrown if the given index is not in the
     *             valid range.
     */
    @Override
    public String getAtIndex(int index) throws NoSuchElementException{
        if (index < 0 || index >= size()) {
            throw new NoSuchElementException();
        }
        else {
            return getAtIndex(index, this.root);
        }
    }

    private String getAtIndex(int index, TreeNode root) {
        //if root is null
        if (root == null) {
            return null;
        }
        //if index is less than the size of the left subtree
        else if (index < size(root.getLeft())) {
            //search left
            return getAtIndex(index, root.getLeft());
        }
        //if index is equal to the size of the left subtree
        else if (index == size(root.getLeft())) {
            return root.getData();
        }
        //if index is greater than the size of the left subtree
        else {
            //search right
            return getAtIndex(index - size(root.getLeft()) - 1, root.getRight());
        }
    }


    /**
     * Returns the index of the given key, throwing an exception if
     * the key is not in the set. A return value of <code>null</code> indicates
     * that <code>null</code> is associated with the given key.
     *
     * @param key The key whose index is to be returned.
     * @return The index for the given key.
     * @throws NoSuchElementException Thrown if the given key is not in the
     *             set.
     */
    @Override
    public int getIndex(String key) throws NoSuchElementException{
        return getIndex(key, this.root);
    }

    private int getIndex(String key, TreeNode root) throws NoSuchElementException{
        //base case
        if (root == null) {
            throw new NoSuchElementException();
        }
        //if key is less than root
        else if (stringCompare(key, root.getData()) < 0) {
            //search left
            return getIndex(key, root.getLeft());
        }
        //if key is greater than root
        else if (stringCompare(key, root.getData()) > 0) {
            //search right
            return getIndex(key, root.getRight());
        }
        //if key is equal to root
        else {
            return 0;
        }
    }

    /**
     * Returns the number of keys in the set.
     *
     * @return The number of keys in the set.
     */
    @Override
    public int size(){
        return size(this.root);
    }

    private int size(TreeNode root) {
        //base case
        if (root == null) {
            return 0;
        }
        //if root has no children
        else if (root.getLeft() == null && root.getRight() == null) {
            return 1;
        }
        //if root has only left child
        else if (root.getLeft() != null && root.getRight() == null) {
            return 1 + size(root.getLeft());
        }
        //if root has only right child
        else if (root.getLeft() == null && root.getRight() != null) {
            return 1 + size(root.getRight());
        }
        //if root has two children
        else {
            return 1 + size(root.getLeft()) + size(root.getRight());
        }
    }

    /**
     * Returns an array with all of the keys in the set in their natural order.
     * If <code>null</code> has an associated value, then it should be first in
     * the array.
     *
     * @return An array with all of the keys in the set in their natural order.
     */
    @Override
    public String[] keys(){
        String[] keys = new String[size()];
        keys(keys, this.root, 0);
        return keys;
    }

    private int keys(String[] keys, TreeNode root, int index) {
        //base case
        if (root == null) {
            return index;
        }
        //if root has no children
        else if (root.getLeft() == null && root.getRight() == null) {
            keys[index] = root.getData();
            return index + 1;
        }
        //if root has only left child
        else if (root.getLeft() != null && root.getRight() == null) {
            keys[index] = root.getData();
            return keys(keys, root.getLeft(), index + 1);
        }
        //if root has only right child
        else if (root.getLeft() == null && root.getRight() != null) {
            keys[index] = root.getData();
            return keys(keys, root.getRight(), index + 1);
        }
        //if root has two children
        else {
            keys[index] = root.getData();
            int newIndex = keys(keys, root.getLeft(), index + 1);
            return keys(keys, root.getRight(), newIndex);
        }
    }


}
