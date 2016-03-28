
import java.util.Vector;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;



public class GraphGenerator {
	
	int edgeCount=0;
	Graph g;  
	MyNode allNodes[][];
		    
    public GraphGenerator(int costMatrix[][],int parentMatrix[][][])
    {
    	g = new DirectedSparseMultigraph<MyNode, MyLink>();
    	edgeCount=0;
    	String type;
    	int parentI,parentJ;
    	allNodes=new MyNode[DPEngine.rows][DPEngine.columns];
    	MyNode parentNode,thisNode;
    	Object adjList[][]=DPEngine.GetAdjacencyList();
    	
    	for(int i=0;i<DPEngine.rows;++i)
    		for(int j=0;j<DPEngine.columns;++j)
    		{
    			if(!(i==0||j==0))allNodes[i][j]=new MyNode(i,j);
    			else if(i==0 && j==0) allNodes[i][j]=new MyNode(i,j,"Start");
    			else if(i==0) allNodes[i][j]=new MyNode(i,j,String.valueOf(DPEngine.B.charAt(j-1)));
    			else allNodes[i][j]=new MyNode(i,j,String.valueOf(DPEngine.A.charAt(i-1)));
    		}	

    	for(int i=0;i<DPEngine.rows;++i)
    		for(int j=0;j<DPEngine.columns;++j)
    		{
    			thisNode=allNodes[i][j];
    			g.addVertex(thisNode);
    			for(MyNode node:(Vector<MyNode>)adjList[i][j])
    			{
    				if(node.i==i && node.j==j+1) type="Delete";
    				else if(node.i==i+1 && node.j==j) type="Insert";
    				else if(costMatrix[i][j]==costMatrix[node.i][node.j]) type="Free";
    				else type="Substitute";    				
    				g.addEdge(new MyLink(++edgeCount,type), thisNode,allNodes[node.i][node.j],EdgeType.DIRECTED);
    			}
    			
    			
    		}
    	
    	/*for(int i=1;i<DPEngine.rows;++i)
    		g.addEdge(new MyLink(++edgeCount,""),allNodes[i-1][0],allNodes[i][0],EdgeType.DIRECTED);
    	for(int j=1;j<DPEngine.columns;++j)
    		g.addEdge(new MyLink(++edgeCount,""),allNodes[0][j-1],allNodes[0][j],EdgeType.DIRECTED);
    	*/
    	
    }
    
    public Graph GetGraph()
    {
    	return g;
    }
    public MyNode[][] GetAllNodes()
    {
    	return allNodes;
    }
    public void SetAllNodes(MyNode[][] nodes)
    {
    	for(int i=0;i<DPEngine.rows;++i)
    		for(int j=0;j<DPEngine.columns;++j)
    		{
    			allNodes[i][j].flag=nodes[i][j].flag;
    			allNodes[i][j].highlight=false;
    		}
    }

}
