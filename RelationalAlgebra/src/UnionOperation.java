import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.Scanner;


public class UnionOperation {
	
	Relation leftOperand,rightOperand,Result;
		
	public UnionOperation(String LHS,String RHS){
		leftOperand=new Relation(LHS);
		rightOperand=new Relation(RHS);	
		
		if(leftOperand.Columns!=rightOperand.Columns){
			System.err.println("Relations "+LHS+" and "+RHS+" are not union compatible");
			System.exit(1);
		}
		
		leftOperand.SortAttribute(leftOperand.Attr[0]);
		rightOperand.SortAttribute(rightOperand.Attr[0]);
	}
	public static Comparator<String> StringNameComparator = new Comparator<String>() 
			{
				public int compare(String arg0, String arg1) {
					int l,r,max1,max2;
					l=r=0;
					max1=arg0.length();
					max2=arg1.length();
					char c1,c2;			

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


	
	public void DoOperation(String ResultName){
		Result=new Relation(ResultName,leftOperand.Columns,leftOperand.Attr);
		try {
			BufferedReader leftReader=new BufferedReader(new FileReader(leftOperand.AttrFiles[0]));
			BufferedReader rightReader=new BufferedReader(new FileReader(rightOperand.AttrFiles[0]));
			String leftRecord,rightRecord;
			leftRecord=leftReader.readLine();
			rightRecord=rightReader.readLine();
			int count=0;			
			
			StringBuilder ResultBuilder=new StringBuilder();
			Result.OpenWriteMode();
			
			while(true){
				int comp=StringNameComparator.compare(leftRecord, rightRecord);
				if(comp<0){
					ResultBuilder.append(leftRecord.split(";")[1]+"\n");					
					++count;					
					leftRecord=leftReader.readLine();
					//System.out.println("less");
					if(leftRecord==null) break;
					
				}
				else if(comp>0){
					ResultBuilder.append(rightRecord.split(";")[1]+"\n");					
					++count;					
					rightRecord=rightReader.readLine();					
					//System.out.println("more");
					if(rightRecord==null) break;
				}
				else{
					if(leftRecord.equals(rightRecord)){
						ResultBuilder.append(leftRecord.split(";")[1]+"\n");					
						++count;					
					}
					else{
						ResultBuilder.append(leftRecord.split(";")[1]+"\n");
						ResultBuilder.append(rightRecord.split(";")[1]+"\n");
						++count;
					}
										
					leftRecord=leftReader.readLine();
					rightRecord=rightReader.readLine();
					if(leftRecord==null || rightRecord==null) break;
				}	
				
				if(count>100000){
					Result.MainFileWriter.write(ResultBuilder.toString());
					count=0;
					ResultBuilder=new StringBuilder();
				}
			}
			if(count>0){
				Result.MainFileWriter.write(ResultBuilder.toString());			
			}
			
			ResultBuilder=new StringBuilder();
			while(leftRecord!=null){
				ResultBuilder.append(leftRecord.split(";")[1]+"\n");
				++count;
				if(count>100000){
					Result.MainFileWriter.write(ResultBuilder.toString());
					count=0;
					ResultBuilder=new StringBuilder();
				}
				leftRecord=leftReader.readLine();
			}
			if(count>0){
				Result.MainFileWriter.write(ResultBuilder.toString());			
			}
			
			ResultBuilder=new StringBuilder();
			while(rightRecord!=null){
				ResultBuilder.append(rightRecord.split(";")[1]+"\n");
				++count;
				if(count>100000){
					Result.MainFileWriter.write(ResultBuilder.toString());
					count=0;
					ResultBuilder=new StringBuilder();
				}
				rightRecord=rightReader.readLine();
			}
			
			if(count>0){
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
	
	public static void main(String args[]){
		Scanner read=new Scanner(System.in);
		String input=read.nextLine();
		String params[]=input.split(" ");
		long startTime = System.nanoTime();		
		UnionOperation uo=new UnionOperation(params[0],params[1]);
		//so.PrintResult();
		//po.DoOperation("RandomName");
		uo.DoOperation("RandomName");
		long estimatedTime = System.nanoTime() - startTime;
		System.out.println(estimatedTime/1000000);		
	}
}
