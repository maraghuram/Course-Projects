

import java.util.Stack;
import java.util.Vector;

public class MyNode {
	
	String ch;	
	Stack<Boolean> positionEnds;
	Vector<Integer> positionLocs;
	MyNode keys[];
	boolean isRoot,flag;
	
	public MyNode(String c)
	{
		ch=c;
		keys=new MyNode[256];		
		positionLocs=new Vector<Integer>();
		positionEnds=new Stack<Boolean>();
		isRoot=false;
		flag=false;
	}
	
	public String PrintNode()
	{
		String res="";
		for(int i=0;i<256;++i)
		{
			if(keys[i]!=null)
				res=res+" "+keys[i].ch;
		}
		return res;
	}
	
	public String toString()
	{
		return ch;
	}

}
