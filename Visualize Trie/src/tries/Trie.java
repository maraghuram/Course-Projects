package tries;

import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;

public class Trie {
	
	private static MyNode root;
	public static int counter,lineLength;
	private static DelegateTree g;
	private static int wordCount=0;
	private static JTextPane status;
	public static String actualText;
	
	public static void NewTrie(JTextPane jTextPane2)
	{
		root=new MyNode(' ');
		root.isRoot=true;
		counter=1;
		lineLength=0;
		status=jTextPane2;
		g = new DelegateTree<MyNode, MyLink>();
	}
	
	public static void InsertWord(String s)
	{
		MyNode ptr=root;
		char c;		
		
		for(int i=0;i<s.length();++i,++counter,++lineLength)
		{
			c=s.charAt(i);			
			if(ptr.keys[(int)c]==null)
				ptr.keys[(int)c]=new MyNode(c);
			ptr=ptr.keys[(int)c];			
			ptr.positionLocs.addElement(counter);
			ptr.positionEnds.push(false);
		}
		
		ptr.positionEnds.pop();
		ptr.positionEnds.push(true);
	}
	
	public static void BuildTrie(String input)
	{		
		Scanner myScanner=new Scanner(input);
		String word;
		StringBuilder sb=new StringBuilder();
		
						
		while(myScanner.hasNext())
		{		
			System.out.println(counter);
			word=myScanner.next();
			sb.append(word);
			sb.append(" ");
			InsertWord(word);
			++counter;
			//System.out.println(word);
    		++lineLength;
    		if(lineLength>20){ 
    			++counter;
    			lineLength=0;
    			sb.append("\n");
    		}
		}		
		myScanner.close();
		actualText=sb.toString();
	}


	public static void PrintTree(MyNode node)
	{
		System.out.println(node);
		
		for(int i=0;i<256;++i)		
			if(node.keys[i]!=null)
				PrintTree(node.keys[i]);		
			
	}
	
	public static void DepthFirstSearch(MyNode node)
	{		
		for(int i=0;i<256;++i)
			if(node.keys[i]!=null)
			{
				g.addChild(new MyLink(node,node.keys[i]),node, node.keys[i]);				
				DepthFirstSearch(node.keys[i]);
			}
	}
	
	public static DelegateTree BuildGraph()
	{		
		g.addVertex(root);
		DepthFirstSearch(root);
		return g;
	}

	public static MyNode GetRoot() {
		// TODO Auto-generated method stub
		return root;
	}
	

}
