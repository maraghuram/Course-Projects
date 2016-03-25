import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.Scanner;


public class JoinOperation {
	
	Relation leftOperand,rightOperand,Result;
	String CondExpr[],Mapping[];
	int CompareI,CompareJ;
	int OpCount,ops[][];
	boolean marking[];
	private int leftCol;
	private int rightCol;
	
	public static Comparator<String> StringNameComparator = new Comparator<String>() 
			{
				public int compare(String arg0, String arg1) {
					int l,r,max1,max2;
					l=r=0;
					max1=arg0.length();
					max2=arg1.length();
					char c1,c2;			
					//if(Character.isDigit(arg0.charAt(0)) && Character.isDigit(arg1.charAt(1)))
						//return (int)( Integer.parseInt(arg0.split(";")[0])-Integer.parseInt(arg1.split(";")[0]));
					
					while(l!=max1 && r!=max2){
						c1=arg0.charAt(l);
						c2=arg1.charAt(r);
						if(c1==';' && c2==';')
							return 0;
						else if(c1==';')
							return -1;
						else if(c2==';')
							return 1;
						else if(c1<c2)
							return -1;
						else if(c1>c2)
							return 1;
						else if(c1==c2){
							++l;
							++r;
						}					
					}
					
					System.err.println("ABNORMAL");
					System.exit(1);
					return 0;			
				}
			};

	
	public JoinOperation(String LHS,String RHS,String CondExpr){
		leftOperand=new Relation(LHS);
		rightOperand=new Relation(RHS);
		this.CondExpr=CondExpr.split(",");		
		
		marking=new boolean[rightOperand.Columns];
		for(int i=0;i<marking.length;++i) marking[i]=false;
		
		int flag=0;
		ops=new int[this.CondExpr.length][2];
		
		OpCount=0;
		
		for(int i=0;i<this.CondExpr.length;++i){
			String leftAttr,rightAttr;
			leftAttr=this.CondExpr[i].split("=")[0];
			rightAttr=this.CondExpr[i].split("=")[1];
			
			if(!leftOperand.Attributes.containsKey(leftAttr)){
				System.err.println("Attribute "+leftAttr+" does not exist in "+leftOperand.RelationName);
				System.exit(1);
			}
			if(!rightOperand.Attributes.containsKey(rightAttr)){
				System.err.println("Attribute "+rightAttr+" does not exist in "+rightOperand.RelationName);
				System.exit(1);
			}
			leftCol=leftOperand.Attributes.get(leftAttr);
			rightCol=rightOperand.Attributes.get(rightAttr);

			if(FileExistsLeft(leftAttr) && FileExistsRight(rightAttr)){				
				flag=1;		
				CompareI=leftCol;
				CompareJ=rightCol;
			}		
			
			else if(flag!=1 &&FileExistsLeft(leftAttr)){			
				flag=2;
				CompareI=leftCol;
				CompareJ=rightCol;
			}
			else if(FileExistsRight(rightAttr)){				
				flag=3;
				CompareI=leftCol;
				CompareJ=rightCol;
			}		
			else{
				CompareI=leftCol;
				CompareJ=rightCol;
			}
			
			ops[OpCount][0]=leftCol;
			ops[OpCount][1]=rightCol;
			++OpCount;
			marking[CompareJ]=true;

		}
		
		
		
		switch(flag){
		case 0 :	leftOperand.SortAttribute(leftOperand.Attr[CompareI]);
					rightOperand.SortAttribute(rightOperand.Attr[CompareJ]);
					break;
		case 2 :	rightOperand.SortAttribute(rightOperand.Attr[CompareJ]);
					break;
		case 3 :	leftOperand.SortAttribute(leftOperand.Attr[CompareI]);
					break;
					
		}

		StringBuilder map=new StringBuilder();
		
		for(int i=0;i<leftOperand.Columns;++i)
			map.append(leftOperand.Attr[i]+" ");
		for(int i=0;i<rightOperand.Columns;++i)
			if(!marking[i]) map.append(rightOperand.Attr[i]+" ");
		
		Mapping=map.toString().split(" ");
	}
	
	public String CheckRecord(String left,String right){
		left=left.split(";")[1];
		right=right.split(";")[1];
		
		String[] leftSplit=left.split(" ");
		String[] rightSplit=right.split(" ");
		StringBuilder res=new StringBuilder();
		
		for(int i=0;i<OpCount;++i){
			if(!leftSplit[ops[i][0]].equals(rightSplit[ops[i][1]]))
			return "";
		}
		
		res.append(left);
		for(int i=0;i<rightSplit.length;++i)
			if(!marking[i]) res.append(rightSplit[i]+" ");
		res.append("\n");
		return res.toString();
	}
	
	public void DoOperation(String ResultName){
		Result=new Relation(ResultName,Mapping.length,Mapping);
		try {
			BufferedReader leftReader=new BufferedReader(new FileReader(leftOperand.AttrFiles[CompareI]));
			BufferedReader rightReader=new BufferedReader(new FileReader(rightOperand.AttrFiles[CompareJ]));
			String leftRecord,rightRecord;
			leftRecord=leftReader.readLine();
			rightRecord=rightReader.readLine();
			int count=0;
			String[] leftArray,rightArray;
			leftArray=new String[100005];
			rightArray=new String[100005];
			
			StringBuilder ResultBuilder=new StringBuilder();
			Result.OpenWriteMode();
			
			while(true){
				int comp=StringNameComparator.compare(leftRecord, rightRecord);
				if(comp<0){
					
					leftRecord=leftReader.readLine();
					//System.out.println("less");
					if(leftRecord==null) break;
					
				}
				else if(comp>0){
					rightRecord=rightReader.readLine();
					//System.out.println("more");
					if(rightRecord==null) break;
				}
				else{
					
					leftArray[count]=leftRecord;
					rightArray[count]=rightRecord;
					++count;
					if(count>100000){
						for(int i=0;i<count;++i)
						{
							ResultBuilder.append(CheckRecord(leftArray[i],rightArray[i]));
						
						}
						Result.MainFileWriter.write(ResultBuilder.toString());
						ResultBuilder=new StringBuilder();
						count=0;
						//System.out.println("HERE");
					}
					//System.out.println(leftRecord+rightRecord);
					leftRecord=leftReader.readLine();
					rightRecord=rightReader.readLine();
					if(leftRecord==null || rightRecord==null) break;
				}			
			}
			if(count>0){
				for(int i=0;i<count;++i)
				{
					ResultBuilder.append(CheckRecord(leftArray[i],rightArray[i]));
				
				}				
				Result.MainFileWriter.write(ResultBuilder.toString());			
			}
			
			Result.CloseWriteMode();			
			leftReader.close();
			rightReader.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private boolean FileExistsLeft(String leftAttr) {
		// TODO Auto-generated method stub
		File f=new File(leftOperand.RelationName+leftAttr+".info");		
		return f.exists();
	}
	private boolean FileExistsRight(String rightAttr) {
		// TODO Auto-generated method stub
		File f=new File(rightOperand.RelationName+rightAttr+".info");		
		return f.exists();	
	}

	public static void main(String args[]){
		Scanner read=new Scanner(System.in);
		String input=read.nextLine();
		String params[]=input.split(" ");
		long startTime = System.nanoTime();		
		JoinOperation jo=new JoinOperation(params[0],params[1],params[2]);
		//so.PrintResult();
		//po.DoOperation("RandomName");
		jo.DoOperation("RandomName");
		long estimatedTime = System.nanoTime() - startTime;
		System.out.println(estimatedTime/1000000);		
	}
}
