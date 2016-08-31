package com.jason.mini.node;

/**
 * sinle Linked
 * @author wufan
 *
 */
public class SimpleLinkedList {

	public class Node{
		
		public int content;
		
		public Node next;
		
	}
	
	Node head;
	
	public void addNode(Node node) {
		// Non recursion
		if(head == null) {
			head = node;
		}else {
			Node temp = head;
			
			while(temp.next != null) {
				temp = temp.next;
			}
			temp.next = node;
		}
		
		// recursion
		if(head == null) {
			head = node;
		}else {
			addNode(head, node);
		}
	}
	
	private void addNode(Node first, Node after) {
		if(first.next == null) {
			first.next = after;
			return;
		}
		
		addNode(first.next, after);
	}
	
}
