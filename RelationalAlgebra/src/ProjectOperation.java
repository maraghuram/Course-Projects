import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

class ProjectOperation{
	private static final int NMAX = 10000;
	Relation Result,Operand;
	String CondExpr[];
	
	public ProjectOperation(String RelationName,String CondExpr){
		this.Operand=new Relation(RelationName);
		this.CondExpr=CondExpr.split(",");
	}
	
	public void DoOperation(String ResultName){
			
		for(int i=0;i<CondExpr.length;++i){
			if(!Operand.Attributes.containsKey(CondExpr[i])){
				System.err.println("Attribute "+CondExpr[i]+" Does Not Exist in "+Operand.RelationName);
				System.exit(1);
			}
		}
		
		Result=new Relation(ResultName,CondExpr.length,CondExpr);
		Result.OpenWriteMode();
		try {
			BufferedReader reader=new BufferedReader(new FileReader(Operand.MainFile));
			String record,recordArray[];
			recordArray=new String[NMAX];
			int count=0;
			while((record=reader.readLine())!=null){
				recordArray[count]=(record);				
				++count;			
				//evaluator.evaluate("T &&( F || T)");
				if(count>100000) {
					StringBuilder ResultRecord=new StringBuilder();
					for(int i=0;i<count;++i){
						{
							String input[]=recordArray[i].split(" ");
							for(int j=0;j<CondExpr.length;++j){				
								ResultRecord.append(input[Operand.Attributes.get(CondExpr[j])]+" ");
							}
							ResultRecord.append("\n");
						}
					}
					count=0;
					Result.MainFileWriter.write(ResultRecord.toString());
				}				
			}
			
			StringBuilder ResultRecord=new StringBuilder();
			for(int i=0;i<count;++i){				
				String input[]=recordArray[i].split(" ");
				for(int j=0;j<CondExpr.length;++j){				
					ResultRecord.append(input[Operand.Attributes.get(CondExpr[j])]+" ");
				}				
				ResultRecord.append("\n");				
			}	
			
			Result.MainFileWriter.write(ResultRecord.toString());
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Result.CloseWriteMode();
	}
	
	public static void main(String args[]){
		Scanner read=new Scanner(System.in);
		String input=read.nextLine();
		String params[]=input.split(" ");
		long startTime = System.nanoTime();
		ProjectOperation po=new ProjectOperation(params[0],params[1]);
		//so.PrintResult();
		po.DoOperation("RandomName");
		long estimatedTime = System.nanoTime() - startTime;
		System.out.println(estimatedTime/1000000);		
		read.close();
	}
}