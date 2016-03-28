import java.util.Stack;
import java.util.Vector;

import edu.uci.ics.jung.graph.Graph;


public class MyUtils {

	private static Object adjacencyList[][];
	private static boolean isExplored[][];
	private static int rows,columns;
	private static Stack<MyNode> pathFind;
	private static MyNode allNodes[][];
	private static Graph<MyNode, ?> g;
	private static int paths;
	private static Vector<Vector<MyNode>> allPaths;
	
	@SuppressWarnings("unchecked")
	public static void MapAdjacencyList(MyNode Graph[][])
	{		
		allNodes=Graph;
		
		rows=DPEngine.rows;
		columns=DPEngine.columns;
		adjacencyList=DPEngine.GetAdjacencyList();
		
		for(int i=0;i<rows;++i)
			for(int j=0;j<columns;++j)
				for(MyNode node:(Vector<MyNode>)adjacencyList[i][j])
					node=allNodes[node.i][node.j];		
	}
	
	public static Vector<MyNode> GetOrderedPath()
	{
		Vector<MyNode> orderPath=new Vector<MyNode>();
		
		int parentMatrix[][][]=DPEngine.GetParentMatrix();
		
		int i,j;
		
		i=DPEngine.rows-1;
		j=DPEngine.columns-1;
		
		orderPath.add(new MyNode(i,j));
		
		
		 while(!(i==0 && j==0))
		{
			i=parentMatrix[i][j][0];
			j=parentMatrix[i][j][1];
			orderPath.add(new MyNode(i,j));			
		}
		
		System.out.println(orderPath);
		
		return orderPath;
	}
	
	public static void DFSHelper(Graph<MyNode, ?> editDistanceGraph)
	{
		g=editDistanceGraph;
		paths=0;
		allPaths=new Vector<Vector<MyNode>>();
		
		isExplored=new boolean[rows][columns];
		for(int i=0;i<rows;++i)
			for(int j=0;j<columns;++j)
				isExplored[i][j]=false;
		
		pathFind=new Stack<MyNode>();
		
		isExplored[0][0]=true;
		pathFind.push(allNodes[0][0]);
		DepthFirstSearch(0,0);
		
	}
	
	@SuppressWarnings("unchecked")
	public static void DepthFirstSearch(int nodeI,int nodeJ)
	{
		if(nodeI==rows-1 && nodeJ==columns-1)
		{
			SetStackPath();
			return;
		}
		
		for(MyNode adj:(Vector<MyNode>)adjacencyList[nodeI][nodeJ])
		{
			int adjI,adjJ;
			adjI=adj.i;
			adjJ=adj.j;
			
			if(!isExplored[adjI][adjJ])
			{			
				isExplored[nodeI][nodeJ]=true;
				pathFind.push(allNodes[adj.i][adj.j]);
				DepthFirstSearch(adjI,adjJ);
				//isExplored[adjI][adjJ]=false;
				//pathFind.pop();
			}
		}
		
	}
	
	public static Vector<Vector<MyNode>> getPaths()
	{
		return allPaths;
	}
	
	static void SetStackPath()
	{
		MyNode prev,next;
		MyLink edge;
		System.out.println("PATH FOUND!!!!");
		
		allPaths.add(new Vector<MyNode>(pathFind));		
		++paths;
		
		for(MyNode node:pathFind)
		{
			System.out.println(node);
			allNodes[node.i][node.j].flag=true;	
		}
		
		prev=pathFind.elementAt(0);
		
		for(int i=1;i<pathFind.size();++i)
		{
			next=pathFind.elementAt(i);
			edge=(MyLink) g.findEdge( allNodes[prev.i][prev.j] ,allNodes[next.i][next.j]);			
			edge.flag=true;
			prev=next;
			
		}
		//System.out.println(g.findEdge(allNodes[0][0],allNodes[1][1]));
		
		
	}
	public static String RemoveCharacter(String s,int index)
	{
		String x="";
				
		for(int i=0;i<s.length();++i)
		{
			if(i+1==index) continue;
			x=x+String.valueOf(s.charAt(i));
		}
		return x;
	}
	
	public static String InsertCharacter(String s,int index,char c)
	{
		String x="";
		for(int i=0;i<s.length();++i)
		{
			if(i+1==index) x=x+String.valueOf(c);
			x=x+String.valueOf(s.charAt(i));
		}
		
		return x;
	}

	public static String SubstituteCharacter(String s, int index, char c) {
		
		String x="";
		for(int i=0;i<s.length();++i)
		{
			if(i+1==index)
			{
				x=x+String.valueOf(c);
				continue;
			}
			x=x+String.valueOf(s.charAt(i));
		}
		
		return x;
	}
}

