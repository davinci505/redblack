package expriments;

public class RedBlackTree {

	public static final boolean RED = false;
	public static final boolean BLACK = true;

    public class Node {
        int key;
        Node left, right, parent;
        boolean color;

        Node(int key) {
            this.key = key;
            this.color = RED;
        }
    }

    public Node root;
    public Node TNULL;

    public RedBlackTree() {
        TNULL = new Node(0);
        TNULL.color = BLACK;
        TNULL.left = null;
        TNULL.right = null;
        root = TNULL;
    }

    // Preorder
    private void preOrderHelper(Node node) {
        if (node != TNULL) {
            System.out.print(node.key + "(" + (node.color == RED ? "R" : "B") + ") ");
            preOrderHelper(node.left);
            preOrderHelper(node.right);
        }
    }

    // Inorder
    private void inOrderHelper(Node node) {
        if (node != TNULL) {
            inOrderHelper(node.left);
            System.out.print(node.key + "(" + (node.color == RED ? "R" : "B") + ") ");
            inOrderHelper(node.right);
        }
    }

    // Postorder
    private void postOrderHelper(Node node) {
        if (node != TNULL) {
            postOrderHelper(node.left);
            postOrderHelper(node.right);
            System.out.print(node.key + "(" + (node.color == RED ? "R" : "B") + ") ");
        }
    }

    // Rotate left
    private void rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != TNULL) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    // Rotate right
    private void rotateRight(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != TNULL) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }

    // Fix the tree after insertion
    private void fixInsert(Node k) {
        Node u;
        while (k.parent.color == RED) {
            if (k.parent == k.parent.parent.right) {
                u = k.parent.parent.left; // uncle
                if (u.color == RED) {
                    u.color = BLACK;
                    k.parent.color = BLACK;
                    k.parent.parent.color = RED;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.left) {
                        k = k.parent;
                        rotateRight(k);
                    }
                    k.parent.color = BLACK;
                    k.parent.parent.color = RED;
                    rotateLeft(k.parent.parent);
                }
            } else {
                u = k.parent.parent.right; // uncle

                if (u.color == RED) {
                    u.color = BLACK;
                    k.parent.color = BLACK;
                    k.parent.parent.color = RED;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.right) {
                        k = k.parent;
                        rotateLeft(k);
                    }
                    k.parent.color = BLACK;
                    k.parent.parent.color = RED;
                    rotateRight(k.parent.parent);
                }
            }
            if (k == root) {
                break;
            }
        }
        root.color = BLACK;
    }

    private void fixDelete(Node x) {
        while (x != root && x.color == BLACK) {
            if (x == x.parent.left) {
                Node s = x.parent.right;
                if (s.color == RED) {
                    s.color = BLACK;
                    x.parent.color = RED;
                    rotateLeft(x.parent);
                    s = x.parent.right;
                }

                if ((s.left == null || s.left.color == BLACK) && (s.right == null || s.right.color == BLACK)) {  // <<<<<<< 변경
                    s.color = RED;
                    x = x.parent;
                } else {
                    if (s.right == null || s.right.color == BLACK) {  // <<<<<<< 변경
                        if (s.left != null) {  // <<<<<<< 변경
                            s.left.color = BLACK;  // <<<<<<< 변경
                        }
                        s.color = RED;
                        rotateRight(s);
                        s = x.parent.right;
                    }

                    s.color = x.parent.color;
                    x.parent.color = BLACK;
                    if (s.right != null) {  // <<<<<<< 변경
                        s.right.color = BLACK;  // <<<<<<< 변경
                    }
                    rotateLeft(x.parent);
                    x = root;
                }
            } else {
                Node s = x.parent.left;
                if (s.color == RED) {
                    s.color = BLACK;
                    x.parent.color = RED;
                    rotateRight(x.parent);
                    s = x.parent.left;
                }

                if ((s.right == null || s.right.color == BLACK) && (s.left == null || s.left.color == BLACK)) {  // <<<<<<< 변경
                    s.color = RED;
                    x = x.parent;
                } else {
                    if (s.left == null || s.left.color == BLACK) {  // <<<<<<< 변경
                        if (s.right != null) {  // <<<<<<< 변경
                            s.right.color = BLACK;  // <<<<<<< 변경
                        }
                        s.color = RED;
                        rotateLeft(s);
                        s = x.parent.left;
                    }

                    s.color = x.parent.color;
                    x.parent.color = BLACK;
                    if (s.left != null) {  // <<<<<<< 변경
                        s.left.color = BLACK;  // <<<<<<< 변경
                    }
                    rotateRight(x.parent);
                    x = root;
                }
            }
        }
        x.color = BLACK;
    }


    private void rbTransplant(Node u, Node v) {
        if (u.parent == null) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        v.parent = u.parent;
    }

    public void deleteNode(int key) {
        deleteNodeHelper(this.root, key);
    }

    private void deleteNodeHelper(Node node, int key) {
        Node z = TNULL;
        Node x, y;
        while (node != TNULL) {
            if (node.key == key) {
                z = node;
            }

            if (node.key <= key) {
                node = node.right;
            } else {
                node = node.left;
            }
        }

        if (z == TNULL) {
            System.out.println("Couldn't find key in the tree");
            return;
        }

        y = z;
        boolean yOriginalColor = y.color;
        if (z.left == TNULL) {
            x = z.right;
            rbTransplant(z, z.right);
        } else if (z.right == TNULL) {
            x = z.left;
            rbTransplant(z, z.left);
        } else {
            y = minimum(z.right);
            yOriginalColor = y.color;
            x = y.right;
            if (y.parent == z) {
                x.parent = y;
            } else {
                rbTransplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }

            rbTransplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }
        if (yOriginalColor == BLACK) {
            fixDelete(x);
        }
    }

    private Node minimum(Node node) {
        while (node.left != TNULL) {
            node = node.left;
        }
        return node;
    }

    public void insert(int key) {
        Node node = new Node(key);
        node.parent = null;
        node.left = TNULL;
        node.right = TNULL;
        node.color = RED;

        Node y = null;
        Node x = this.root;

        while (x != TNULL) {
            y = x;
            if (node.key < x.key) {
                x = x.left;
            } else {
                x = x.right;
            }
        }

        node.parent = y;
        if (y == null) {
            root = node;
        } else if (node.key < y.key) {
            y.left = node;
        } else {
            y.right = node;
        }

        if (node.parent == null) {
            node.color = BLACK;
            return;
        }

        if (node.parent.parent == null) {
            return;
        }

        fixInsert(node);
    }

    public void printTree() {
        preOrderHelper(this.root);
        System.out.println();
    }

    public void printInOrder() {
        inOrderHelper(this.root);
        System.out.println();
    }

    public void printPostOrder() {
        postOrderHelper(this.root);
        System.out.println();
    }

    public static void main(String[] args) {
        RedBlackTree tree = new RedBlackTree();
        tree.insert(55);
        tree.insert(40);
        tree.insert(65);
        tree.insert(60);
        tree.insert(75);
        tree.insert(57);

        System.out.println("Preorder traversal:");
        tree.printTree();

        System.out.println("\nInorder traversal:");
        tree.printInOrder();

        System.out.println("\nPostorder traversal:");
        tree.printPostOrder();

        System.out.println("\nAfter deleting an element");
        tree.deleteNode(40);
        System.out.println("Preorder traversal:");
        tree.printTree();
    }
}
