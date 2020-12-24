//CPSC 331 Assingment #2 
//Bubble Sort in reverse
//Ali Akbari
//Tutorial #03

//Package needed for assingment
package hw2.student;
//Bubble sort class
public class SLLBubbleSort implements BubbleSortInterface { 

  /**
   * A method that will sort a singly linked list that holds Comparable 
   * elements. Bubble sort is used to sort the elements. In this version of 
   * bubble sort, the largest element will bubble to the right in each pass.  
   * Note that the linked list is sorted in place, i.e. the specified list is
   * modified; a copy is not made. 
   * 
   * @param list The SLL list to sort
   * 
   */
	
	
	//Bubble sort method, extends comparable		
	public <T extends Comparable<? super T>> void BubbleSort( SLL<T> list ){

		//If list is of size 0 or one return nothing/unchanged 
		if (list.getLength() <= 1) {
			return;
		}
		//Outer loop to iterater through list
		for (int i = 1; i < list.getLength(); i++) {
			SLLNode<T> previousNode = list.head;
			SLLNode<T> currentNode = list.head;
			SLLNode<T> nextNode = list.head.next;
			//Do while loop to swap the nodes 
			do {
			//Compare current node to the next node
			//If bigger then swap
				if (currentNode.info.compareTo(nextNode.info) > 0) {
			//If head of list is current node		
					if (currentNode == list.head) {
						previousNode = nextNode;
						nextNode = nextNode.next;
						list.head = previousNode;
						list.head.next = currentNode;
						list.head.next.next = nextNode;
						}
			//Else if current node is anywhere else in the list		
					else{
						previousNode.next = nextNode;
						currentNode.next = nextNode.next;
						nextNode.next = currentNode;
						
						previousNode = nextNode;
						nextNode = currentNode.next;
									
						}
					}
			//If current node is not larger than next node, move up in the list
				else {
					previousNode = currentNode;
					currentNode = nextNode;
					nextNode = nextNode.next;
				}
			//Loop while next node is not null	
				} while(nextNode != null);
				}
			//Return nothing
		return;
			}
 
}