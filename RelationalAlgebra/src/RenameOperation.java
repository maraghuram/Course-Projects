import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class RenameOperation {
	Relation Operand1,Result;
	String ResultName;
	String[] attr;
	
	public RenameOperation(String relation1,String CondExpr)
	{		
		Operand1=new Relation(relation1);
		attr=new String[Operand1.Columns];		
		ResultName=CondExpr.split(";")[0];		
		
		if(CondExpr.indexOf(";")!=-1)
			attr= CondExpr.split(";")[1].split(",");
		else
			attr= Operand1.Attr;
		DoOperation();
	}
	
	public void DoOperation(){

		try {
			
			Result = new Relation(ResultName,Operand1.Columns,attr);
			Result.OpenWriteMode();
			BufferedReader reader1=new BufferedReader(new FileReader(Operand1.MainFile));

			StringBuilder ResultRecord=new StringBuilder();
			int count=0;String Record1;
			while((Record1=reader1.readLine())!=null){
				
					ResultRecord.append(Record1+"\n");					 
					++count;
					if(count>100000) {									
							Result.MainFileWriter.write(ResultRecord.toString());					
							count=0;	
							ResultRecord=new StringBuilder();
					}				
			}
			if(count>0){		
				Result.MainFileWriter.write(ResultRecord.toString());
			}
			reader1.close();
				
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Result.CloseWriteMode();
		
	}
}
