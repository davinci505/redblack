package expriments;

import java.util.Stack;

class RBTree {
	static final boolean RED = true;
	static final boolean BLACK = false;

	class Node {
		int data;
		boolean color;
		Node left, right, parent;

		Node(int data) {
			this.data = data;
			this.left = null;
			this.right = null;
			this.parent = null;
			this.color = RED;
		}
		
		Node(int data, boolean color) {
			this.data = data;
			this.left = nil;
			this.right = nil;
			this.parent = null;
			this.color = color;
		}
		
		boolean isNil() {
			return parent == null ? true : false;
		}
	}

	final Node nil = new Node(-1, BLACK);
	Node root = null;
	
	void insert(Node newNode) {
		
		Node currentNode = root;
		
		Stack<Node> inorderStack = new Stack<RBTree.Node>();
		
		if ( currentNode == null ) {
			root = newNode;
			currentNode = newNode;
			newNode.left = nil;
			newNode.right = nil;
			newNode.parent = nil;
		}else {
			
			while( !currentNode.isNil() ) {
				
				if( currentNode.data < newNode.data ) { //새노드가 현재 노드보다 크다면 오른쪽 순회
					
					inorderStack.push(currentNode);
					currentNode = currentNode.right;
				}else if ( currentNode.data > newNode.data ) { //작다면 왼쪽 순회
					
					inorderStack.push(currentNode);
					currentNode = currentNode.left;
				}else {
					throw new RuntimeException("같은 값 존재 삽입 불가.");
				}
				
			}
			
			currentNode = inorderStack.lastElement();
			
			
			if ( currentNode.data < newNode.data ) { //새노드가 현재 노드보다 크다면 오른쪽 삽입
				currentNode.right = newNode;
				
			}else if ( currentNode.data > newNode.data ) { //작다면 왼쪽 삽입
				currentNode.left = newNode;
			}else {
				throw new RuntimeException("같은 값 존재 삽입 불가.");
			}
			
			inorderStack.push(newNode);
			
	
			checkRBTreeViolation(inorderStack);
			
		}		
		
	}
	
	void checkRBTreeViolation(Stack<Node> stack) {
		
		Node currentNode = null;
		
		while (!stack.isEmpty()) {

			currentNode = stack.pop();

			if (currentNode.color == RED && currentNode.parent.color == RED) { // 더블 레드 상황 발생.

			}

		}
		
		
		Node uncleNode = currentNode.parent.left == currentNode ? currentNode.parent.right : currentNode.parent.left;
		
		if( uncleNode.color == RED ) { //부모의 형제 노드(삼촌노드)가 레드 일경우, recoloring 수행
			
		}else { //삼촌노드가 black 이거나 nil(nil노드의 칼라도 black임)
			
		}
	}
}
