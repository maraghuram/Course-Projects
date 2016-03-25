import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


	public class Aggregation{
		
		String Operation;
		Relation Operand,Result;
		String Cond;
		String[] Mapping;
		
		public Aggregation(String Operation,String RelationName,String Cond){
			this.Operation=Operation;
			this.Cond=Cond;
			Operand= new Relation(RelationName);
			Mapping= new String[1];
			Mapping[0]=Operation+"("+Cond+")";
		}
		
		public void DoOperation(String ResultName){
			Result=new Relation(ResultName,1,Mapping);
			
			if(Operation.equals("SUM"))
				SumOperation();
			else if(Operation.equals("COUNT"))
				CountOperation();
			else if(Operation.equals("MAX"))
				MaxOperation();
			else if(Operation.equals("MIN"))
				MinOperation();
			else if(Operation.equals("AVG"))
				AvgOperation();
			else{
				System.err.println("Invalid Operation !");
				System.err.println("Valid Aggregetor Functions: MIN,MAX,COUNT,SUM,AVG");
				System.exit(1);
			}				
			
		}

	public void SumOperation() {
	
		long sum=0,count=0;
		int j=0;
		String attr[] = new String [Operand.Columns];
		String record = new String();
		int at = Operand.Attributes.get(Cond);
		if(!Operand.Attributes.containsKey(Cond))
		{
			System.err.println("Relation "+Operand.RelationName+" doesn't contain "+Cond);
			System.exit(1);
		}
		
		Result.OpenWriteMode();
		try {
			BufferedReader reader1=new BufferedReader(new FileReader(Operand.MainFile));
			
			while((record=reader1.readLine())!=null)
			{
				count++;
				attr =  record.split(" ");
				for(int i=0;i<attr[at].length();i++)
				{
					if(!Character.isDigit(attr[at].charAt(i)))
					{						
						System.err.println("Invalid operands supplied to Aggregator Function :"+Operation);
						System.err.println("Not an integer!");
						System.exit(1);
						return;
					}
				}
				
				sum += Integer.parseInt(attr[at]);

			}
			Result.MainFileWriter.write(String.valueOf(sum)+"\n");			
			reader1.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Result.CloseWriteMode();
	}
	
	public void AvgOperation() {
		double avg=0.0;
		long sum=0,count=0;
		int j=0;
		String attr[] = new String [Operand.Columns];
		String record = new String();
		int at = Operand.Attributes.get(Cond);
		if(!Operand.Attributes.containsKey(Cond))
		{
			System.err.println("Relation "+Operand.RelationName+" doesn't contain "+Cond);
			System.exit(1);
		}
		
		Result.OpenWriteMode();
		try {
			BufferedReader reader1=new BufferedReader(new FileReader(Operand.MainFile));
			
			while((record=reader1.readLine())!=null)
			{
				count++;
				attr =  record.split(" ");
				for(int i=0;i<attr[at].length();i++)
				{
					if(!Character.isDigit(attr[at].charAt(i)))
					{						
						System.err.println("Invalid operands supplied to Aggregator Function :"+Operation);
						System.err.println("Not an integer!");
						System.exit(1);
						return;
					}
				}
				
				sum += Integer.parseInt(attr[at]);

			}
			if(count!=0)	avg=sum/count;			
			Result.MainFileWriter.write(String.valueOf(avg)+"\n");			
			reader1.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Result.CloseWriteMode();
	}

	public void CountOperation() {
		
		long count=0;
		String record;
		
		if(!Operand.Attributes.containsKey(Cond))
		{
			System.err.println("Relation "+Operand.RelationName+" doesn't contain "+Cond);
			System.exit(1);
		}
		
		Result.OpenWriteMode();
		try {
			BufferedReader reader1=new BufferedReader(new FileReader(Operand.MainFile));
			
			while((record=reader1.readLine())!=null)
			{
				count++;
			}
			Result.MainFileWriter.write(String.valueOf(count)+"\n");			
			reader1.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Result.CloseWriteMode();
	}

	private void MinOperation() {
		
		String min = new String();
		int j=0;
		String attr[] = new String [Operand.Columns];
		String record = new String();
		int at = Operand.Attributes.get(Cond);
		if(!Operand.Attributes.containsKey(Cond))
		{
			System.err.println("Relation "+Operand.RelationName+" doesn't contain "+Cond);
			System.exit(1);
			return;
		}
		
		Result.OpenWriteMode();
		try {
			BufferedReader reader1=new BufferedReader(new FileReader(Operand.MainFile));
			while((record=reader1.readLine())!=null)
			{
				
				attr =  record.split(" ");
				for(int i=0;i<attr[at].length();i++)
				{
					if(!Character.isDigit(attr[at].charAt(i)))
					{
						System.err.println("Invalid operands supplied to Aggregator Function :"+Operation);
						System.err.println("Not an integer!");
						System.exit(1);
						return;
					}
				}

				if(j==0)
					min = attr[at];
				else
				{
					if(min.compareTo(attr[at])>0)
						min = attr[at];
				}
				j++;
			}
			Result.MainFileWriter.write(String.valueOf(min)+"\n");	
			reader1.close();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Result.CloseWriteMode();
		
	}
	
	private void MaxOperation() {
		
		String max = new String();
		int j=0;
		String attr[] = new String [Operand.Columns];
		String record = new String();
		
		if(!Operand.Attributes.containsKey(Cond))
		{
			System.err.println("Relation "+Operand.RelationName+" doesn't contain "+Cond);
			System.exit(1);
			return;
		}
		int at = Operand.Attributes.get(Cond);
		
		Result.OpenWriteMode();
		try {
			BufferedReader reader1=new BufferedReader(new FileReader(Operand.MainFile));
			while((record=reader1.readLine())!=null)
			{
				
				attr =  record.split(" ");
				for(int i=0;i<attr[at].length();i++)
				{
					if(!Character.isDigit(attr[at].charAt(i)))
					{
						System.err.println("Invalid operands supplied to Aggregator Function :"+Operation);
						System.err.println("Not an integer!");
						System.exit(1);
						return;
					}
				}

				if(j==0)
					max = attr[at];
				else
				{
					if(max.compareTo(attr[at])<0)
						max = attr[at];
				}
				j++;
			}
			Result.MainFileWriter.write(String.valueOf(max)+"\n");	
			reader1.close();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Result.CloseWriteMode();
		
	}


	
}