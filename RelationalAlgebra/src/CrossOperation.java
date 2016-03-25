import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class CrossOperation {

	public Relation Result;
	private Relation Operand2;
	private Relation Operand1;
	String[] attr;

	public CrossOperation(String relationName, String relationName2) {
		// TODO Auto-generated constructor stub

		Operand1 = new Relation(relationName);
		Operand2 = new Relation(relationName2);
		attr = new String[Operand1.Columns+Operand2.Columns];
		for(int i=0;i<Operand1.Columns+Operand2.Columns;i++)
		{
			if(i<Operand1.Columns)
				attr[i] = Operand1.Attr[i];
			else
				attr[i] = Operand2.Attr[i-Operand1.Columns];
		}
	}

	public void DoOperation(String id) {
		// TO
		try {
		String ResultName = id;
		Result=new Relation(ResultName,Operand1.Columns+Operand2.Columns,attr);
		Result.OpenWriteMode();
		BufferedReader reader1=new BufferedReader(new FileReader(Operand1.MainFile));
	
		StringBuilder ResultRecord=new StringBuilder();
		
		int count=0;String Record1,Record2;
		while((Record1=reader1.readLine())!=null){
			BufferedReader reader2=new BufferedReader(new FileReader(Operand2.MainFile));
			while((Record2=reader2.readLine())!=null)
			{			
				ResultRecord.append(Record1+Record2+"\n");
				++count;
				if(count>100000) {										
					Result.MainFileWriter.write(ResultRecord.toString());					
					count=0;					
					ResultRecord=new StringBuilder();
				}		
				//System.out.println(ResultRecord);
			}			

			reader2.close();						
		}
		if(count>0){										
			Result.MainFileWriter.write(ResultRecord.toString());
		}

		reader1.close();
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	Result.CloseWriteMode();
		
	}

}
