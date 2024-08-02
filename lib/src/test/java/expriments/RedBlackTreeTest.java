package expriments;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RedBlackTreeTest {
    private RedBlackTree tree;

    @BeforeEach
    public void setUp() {
        tree = new RedBlackTree();
    }

    @Test
    public void testInsertAndDelete() {
        // 삽입 테스트
        int[] insertValues = {55, 40, 65, 60, 75, 57};
        for (int value : insertValues) {
            tree.insert(value);
        }

        // 삽입 후 트리 검증
        System.out.println("Tree after insertions:");
        tree.printTree();
        assertTrue(isValidRedBlackTree(tree), "Tree is invalid after insertions");

        // 삭제 테스트
        int[] deleteValues = {40, 65};
        for (int value : deleteValues) {
            tree.deleteNode(value);
        }

        // 삭제 후 트리 검증
        System.out.println("Tree after deletions:");
        tree.printTree();
        assertTrue(isValidRedBlackTree(tree), "Tree is invalid after deletions");
    }

    private boolean isValidRedBlackTree(RedBlackTree tree) {
        return (tree.root.color == RedBlackTree.BLACK) &&
               validateProperties(tree.root, tree);
    }

    private boolean validateProperties(RedBlackTree.Node node, RedBlackTree tree) {
        if (node == tree.TNULL) {
            return true;
        }

        // Property 1: 노드는 레드 또는 블랙이어야 한다.
        assertTrue(node.color == RedBlackTree.RED || node.color == RedBlackTree.BLACK, "Node color is invalid");

        // Property 2: 루트 노드는 항상 블랙이어야 한다.
        if (node == tree.root) {
            assertTrue(node.color == RedBlackTree.BLACK, "Root is not black");
        }

        // Property 3: 모든 리프 노드(NIL)는 블랙이어야 한다.
        if( node.left == tree.TNULL )
            assertTrue(node.left.color == RedBlackTree.BLACK, "Left child of NIL is not black");
        if( node.right == tree.TNULL )
            assertTrue( node.right.color == RedBlackTree.BLACK, "Right child of NIL is not black");


        // Property 4: 레드 노드의 자식은 항상 블랙이어야 한다.
        if (node.color == RedBlackTree.RED) {
            assertTrue(node.left.color == RedBlackTree.BLACK, "Left child of red node is not black");
            assertTrue(node.right.color == RedBlackTree.BLACK, "Right child of red node is not black");
        }

        // Property 5: 어떤 노드에서든 그 노드로부터 자손인 모든 리프 노드까지의 모든 경로에는 동일한 개수의 블랙 노드가 있어야 한다.
        int leftBlackHeight = blackHeight(node.left, tree);
        int rightBlackHeight = blackHeight(node.right, tree);
        assertEquals(leftBlackHeight, rightBlackHeight, "Black height mismatch");

        return validateProperties(node.left, tree) && validateProperties(node.right, tree);
    }

    private int blackHeight(RedBlackTree.Node node, RedBlackTree tree) {
        if (node == tree.TNULL) {
            return 0;
        }

        int leftBlackHeight = blackHeight(node.left, tree);
        int rightBlackHeight = blackHeight(node.right, tree);
        assertEquals(leftBlackHeight, rightBlackHeight, "Black height mismatch in subtree");

        return (node.color == RedBlackTree.BLACK) ? leftBlackHeight + 1 : leftBlackHeight;
    }
}
