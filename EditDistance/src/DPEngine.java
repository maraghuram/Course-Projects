import java.util.*;

public class DPEngine {

	public static	int costMatrix[][];
	public static  	int parentMatrix[][][];	
	public static 	String A,B;
	public static 	int rows,columns;
	public static 	Object adjacencyList[][];
	public static 	int editDistance;
	
	public static void ComputeCosts(String first,String second)
	{
		A=first;
		B=second;
		
		rows=A.length()+1;
		columns=B.length()+1;
				
		costMatrix=new int[rows][columns];
		parentMatrix=new int[rows][columns][2];
		adjacencyList=new Object[rows][columns];
		
		parentMatrix[0][0][0]=parentMatrix[0][0][1]=0;
		adjacencyList[0][0]=new Vector<MyNode>();
		
		for(int i=1;i<rows;++i)
		{
			costMatrix[i][0]=i;
			parentMatrix[i][0][0]=i-1;
			parentMatrix[i][0][1]=0;
			adjacencyList[i][0]=new Vector<MyNode>();
			((Vector<MyNode>)adjacencyList[i-1][0]).add(new MyNode(i,0));
		}
		for(int j=1;j<columns;++j)
		{
			costMatrix[0][j]=j;
			parentMatrix[0][j][0]=0;
			parentMatrix[0][j][1]=j-1;
			adjacencyList[0][j]=new Vector<MyNode>();
			((Vector<MyNode>)adjacencyList[0][j-1]).add(new MyNode(0,j));
		}
			
		
		for(int i=1;i<rows;++i)
			for(int j=1;j<columns;++j)
			{
				boolean flag=false;
				costMatrix[i][j]=Math.min(costMatrix[i-1][j]+1, Math.min(costMatrix[i][j-1]+1, (A.charAt(i-1)==B.charAt(j-1))?costMatrix[i-1][j-1]:costMatrix[i-1][j-1]+1));
				adjacencyList[i][j]=new Vector<MyNode>();
				
				if((costMatrix[i][j]==costMatrix[i-1][j-1] && A.charAt(i-1)==B.charAt(j-1)) || costMatrix[i][j]==costMatrix[i-1][j-1]+1)
				{
					if(!flag){
					parentMatrix[i][j][0]=i-1;
					parentMatrix[i][j][1]=j-1;
					flag=true;
					}
					((Vector<MyNode>)adjacencyList[i-1][j-1]).add(new MyNode(i,j));
				}
				
				if(costMatrix[i][j]==costMatrix[i-1][j]+1)
				{				
					if(!flag){
					parentMatrix[i][j][0]=i-1;
					parentMatrix[i][j][1]=j;					
					flag=true;
					}
					((Vector<MyNode>)adjacencyList[i-1][j]).add(new MyNode(i,j));
					
				}
				if(costMatrix[i][j]==costMatrix[i][j-1]+1)
				{
					if(!flag){
					parentMatrix[i][j][0]=i;
					parentMatrix[i][j][1]=j-1;
					flag=true;
					}
					((Vector<MyNode>)adjacencyList[i][j-1]).add(new MyNode(i,j));
				}

			}
		
		editDistance=costMatrix[rows-1][columns-1];
	}
	
	public static void PrintCostMatrix()
	{
		for(int i=0;i<rows;++i)
		{
			System.out.println();
			for(int j=0;j<columns;++j)		
				System.out.print(costMatrix[i][j]+" ");
		}
	}
	
	public static void PrintParentMatrix()
	{
		for(int i=0;i<rows;++i)
		{
			System.out.println();
			for(int j=0;j<columns;++j)
				System.out.print(parentMatrix[i][j][0]+","+parentMatrix[i][j][1]+" ");
		}
	}
	
	public static int[][] GetCostMatrix()
	{
		return costMatrix;
	}
	
	public static int[][][] GetParentMatrix()
	{
		return parentMatrix;
	}
	
	public static Object[][] GetAdjacencyList()
	{
		return adjacencyList;
	}
	
	public static void main(String args[])
	{		
		Scanner sc=new Scanner(System.in);
		String str1,str2;
		
		str1=sc.nextLine();
		str2=sc.nextLine();
		
		ComputeCosts(str1, str2);
		PrintCostMatrix();
		PrintParentMatrix();
	}
	
}
