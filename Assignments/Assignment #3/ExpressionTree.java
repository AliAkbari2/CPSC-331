//Package for Gradescope marking and other files given to us 
package hw3.student;


//Imports needed for my code
//ParseException used to throw
import java.text.ParseException;
import java.util.*;


public class ExpressionTree implements ExpressionTreeInterface {
	
	private ExpressionTreeNode root;
	
	/**
	 * Constructor to build an empty parse tree.
	 */

	public ExpressionTree() {
	  root = null;
	}

  
	//String list of operators used for evaluate
	public String[] operatorStrings = {"+","-","*","/" };
	
	//Initializing expression tree nodes need for parsing
	public ExpressionTreeNode exp;
	public ExpressionTreeNode expNodeRight;
	public ExpressionTreeNode expNodeLeft;
	
	
	//Stack and Queue used for Dijkstra's Shunting Yard Algorithm and parsing method 
	public Stack<String> postStack = new Stack<String>();
	public Stack<ExpressionTreeNode> newStack = new Stack<ExpressionTreeNode>();
	public Queue<String> postQueue = new LinkedList<String>();
	
	public int result = 0;


	/**
	 *Parse Algorithm, calls Dijkstra's Shunting Yard Algorithm
	 *Use the returned post ordered Queue to make expression tree in root  	
	 *@param line String containing the expression
	 *@throws ParseException If an error occurs during parsing. 
	 */
	public void parse( String line ) throws ParseException {
		//Splitting the string into an array	
		String[] arr = line.split(" ");
	
		//Result from the algorithm in post order
		postQueue = dijkstraShunting(arr, postStack, postQueue);
		
		//Call Parser method to make a tree expression in root  
		root = parser(postQueue);
	}
	
	/**
	 *Parser Algorithm, uses the returned post ordered Queue to make expression tree for root  	
	 *@param postQueue containing the post ordered expression
	 *@throws ParseException If an error occurs during parsing.
	 *@return A expression tree  
	 */
	public ExpressionTreeNode parser(Queue<String> postQueue) throws ParseException {
		//While the queue is not empty
		while (!postQueue.isEmpty()) {
			//nextToken is equal to Queue head/ queue =- 1 
			String nextToken = postQueue.remove();
			//If nextToken is a numeric 
			if(isNumeric(nextToken)) {
				//Make a node and store it in stack
				exp = new ExpressionTreeNode(nextToken, null, null);
				newStack.push(exp);
			}else {
				//Else if nextToken is an operator, check if size of stack is >= 2, i.e at least two operands for one operator
				if (newStack.size() < 2) {
				//Throw parse exception otherwise	
					throw new ParseException("Not enough operands for operators, size = ", postQueue.size());
				}else {
				//If stack size is >= 2 then pop two elemtns and make a node with operator as parent node and the other elements as left/right child nodes respectively	
					expNodeRight = newStack.pop();
					expNodeLeft = newStack.pop();
					exp = new ExpressionTreeNode(nextToken, expNodeLeft, expNodeRight);
					newStack.push(exp);
				}
			}
		}
		//Once parser is done only one element(expression tree) should be in the stack
		if(newStack.size() == 1) {
			root = newStack.pop();
			return root;
		//If not then throw parse exception	
		}else {
			throw new ParseException("Does not make a tree, stack size != 1", 0);
		}
  }
  
	/**
	 *Method for Dijkstra's Shunting Yard Algorithm
	 *@param arr, postQueue, postStacke
	 *Takes in an initialized stack and queue as well as the string expression in an string array
	 *@throws ParseException If an error occurs during parsing.
	 *@return A queue with post order of the expression  
	 */
	public Queue<String> dijkstraShunting(String[] arr, Stack<String> postStack, Queue<String> postQueue) throws ParseException {
		//For loop to go through the string elements 
		for(int i = 0; i <  arr.length; i++) {
		//Token is equal to array element of index i from loop iterations	
			String token = arr[i];
						
			//If token/element is numeric, then add to queue
			if(isNumeric(token)) {
				postQueue.add(token);
			}
			
			//If it is an operator(checking if token is equal to an element in operatorStrings list)
			if(isOperator(token)){
				//While stack is not empty
				if(!postStack.empty()) {
					//If there is an operator in stack and it has precedence
					while(precedence(postStack.peek(), token) && isOperator(postStack.peek())){
						//Then pop that operator and add it to queue
						String tempVal = postStack.pop();
						postQueue.add(tempVal);
						
						//If stack becomes empty then throw parse exception
						if (postStack.empty()) {
							break;
							//throw new ParseException("Not enough operands for operators, size = " + postQueue.size(), 0);
						}
					}
				}
				//Otherwise if there is no precedence, then push token to stack
				postStack.push(token);
			}
			
			//If token is open bracket then push to stack	
			if(token.equals("(")) {
				postStack.push(token);
			}
			
			//If token is close bracket then keep removing from stack and adding to queue until you find the open bracket
			if(token.equals(")")) {
				//If stack is empty then break
				if(postStack.empty()) {
					break;
				}
				while (!postStack.peek().equals("(")) {
					String tempVal = postStack.pop();
					postQueue.add(tempVal);
				}
				//Remove last close bracket
				postStack.pop();
			}
		}
	//While stack is not empty
	while( !postStack.empty()){
		//Remove elements from stack and add to Queue
		String tempVal = postStack.pop();
		if (tempVal.equals("(")){
			throw new ParseException("",0);
			}
		postQueue.add(tempVal);
	}
	
	//Return result of post-fix expression in postQueue
	System.out.print("Post fix postqueue " + postQueue);
	return postQueue;
}
		
	/**
	 *Method to check if string is numeric
	 *@param takes a string
	 *@return A boolean, true if numeric else false
	 */
	public static boolean isNumeric(String str) {
		//Try and catch 
		try {  
			Integer.parseInt(str);  
			return true;
		  } catch(NumberFormatException e){  
		    return false;  
		  }  
		}
	/**
	 * Method to check if string is operator  
	 * @param aStr
	 * @return a boolean true if string is operator
	 */
	public boolean isOperator(String aStr) {
		if(aStr.equals("+")) {
			return true;
		}
		if(aStr.equals("-") & aStr.length() == 1) {
			return true;
		}

		if(aStr.equals("*")) {
			return true;
		}
		if(aStr.equals("/")) {
			return true;
		}
		
		return false;
	}
	
	
	/**
	 *Helper method for precedence
	 *@param takes two strings
	 *@return A boolean, true if precedence else false
	 */
	public boolean precedence(String a, String b) {
		//Integer value for the string 
		int strA;
		int strB;
		
		//If String a is +, - then strA equals to 1
		if((a.equals("+"))|(a.equals("-"))){
			strA = 1;
		//else strA = 2, i.e is *,/	
		}else {
			strA = 2;
		}
		
		//If String b is +, - then strB equals to 1
		if((b.equals("+"))|(b.equals("-"))){
			strB = 1;
		//else strB = 2, i.e is * , /	
		}else {
			strB = 2;
		}
		
		//If StrA is larger then return true for precedence
		if(strA >= strB) {
			return true;
		}else {
			return false;
		}

	}
	/**
	 * Evaluate the expression tree and return the integer result. An 
	 * empty tree returns 0. Calls calculate method.
	 * @return integer of result 
	 */
	public int evaluate() {
		return calculate(root);
		}
	/**
	 * Evaluate the expression tree and return the integer result. An 
	 * empty tree returns 0. 
	 * @return integer of result to evaluate 
	 */
	public int calculate(ExpressionTreeNode node) {
		if(node.left == null && node.right == null) {
			return Integer.parseInt(node.el);
			}
		int leftVal = calculate(node.left);
		int rightVal = calculate(node.right);
		if(node.el.equals("+")) {
			result = leftVal + rightVal;
			return result;
			}
		if(node.el.equals("-")) {
			result = leftVal - rightVal;
			return result;
			}
		if(node.el.equals("*")) {
			result = leftVal * rightVal;
			return result;
			}
		if(node.el.equals("/")) {
			result = leftVal / rightVal;
			return result;
			}
		return result;
		}
	
	
	/**
	 * Simplifies the tree to a specified height h (h >= 0). After simplification, the tree 
	 * has a height of h. Any subtree rooted at a height of h is replaced by a leaf node 
	 * containing the evaluated result of that subtree.  
	 * If the height of the tree is already less than the specified height, the tree is unaffected. 
	 * @param h The height to simplify the tree to.
	 */
	public void simplify(int h) {
		simplifyCal(root, h);	
	}
	/**
	 * Recursive call function for simplify
	 * @param aNode
	 * @param h
	 */
	public void simplifyCal(ExpressionTreeNode aNode, int h) {
		//If children of node == null return;
		if (aNode.left == null && aNode.right == null){ 
			  return;
		}
		//If h = 0 then calculate node change node children to null 
		if(h == 0){
			aNode.el = calculate(aNode) + "";
			aNode.left = null;
			aNode.right = null;
		}
		if (h > 0) {
			simplifyCal(aNode.left,h-1);
			simplifyCal(aNode.right,h-1);
		}
	}
		  
	/**
	 * Returns a parentheses-free postfix representation of the expression. Tokens 
	 * are separated by whitespace. An empty tree returns a zero length string.
	 * @return string of postfix expression
	 */
	public String postfix() {
		StringBuilder s = new StringBuilder();
		String postStr = postOrder(root, s);
		return postStr;
		}
	
	/**
	 * Recursive method to change the infix expression to postfix expression 
	 * @param aTree
	 * @return a String of postfix expression 
	 */
	public String postOrder(ExpressionTreeNode aNode, StringBuilder s) {
		//String aStr = "";
		if(aNode == null) {
			return "";
		}
		postOrder(aNode.left, s);
		postOrder(aNode.right, s);
		s.append(aNode.el + " ");
		return s.toString();
		}
	/**
	 * Returns a parentheses-free prefix representation of the expression. Tokens are 
	 * separated by whitespace. An empty tree returns a zero length string.
	 * @return string of prefix expression
	 */
	public String prefix() {
		StringBuilder s = new StringBuilder();
		String preStr = preOrder(root, s);
		return preStr;
		}
	/**
	 * Recursive method to change the infix expression to prefix expression 
	 * @param aTree
	 * @return a String of prefix expression 
	 */
	public String preOrder(ExpressionTreeNode aNode2, StringBuilder s) {
		//String aStr2 = "";
		if(aNode2 == null) {
			return "";
		}
		s.append(aNode2.el + " ");
		preOrder(aNode2.left, s);
		preOrder(aNode2.right, s);
		return s.toString();
		}

}



