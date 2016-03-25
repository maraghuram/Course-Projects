import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;


	public class GroupAggregation{
				
		Relation Operand,Result;
		String Aggregate,GroupAttr;;
		String[] Mapping,Operations,Attr,OpCode;
		long [] Res;
		int [] AttrIndex;
		int Ops;
		
		public GroupAggregation(String RelationName,String Cond){
			if(!Cond.contains(";")){
				System.err.println("Invalid Syntax in Grouping Attributes");
				System.err.println("Correct Syntax : <Grouping_Attribute>;<Aggregator_Function>(<Attribute>),..");
				System.exit(1);
			}
			Aggregate=Cond.split(";")[1];
			GroupAttr=Cond.split(";")[0];
			
			Operand= new Relation(RelationName);			
			if(!Operand.Attributes.containsKey(GroupAttr)){
				System.err.println("Relation "+RelationName+" does not contain attribute : "+GroupAttr);
				System.exit(1);
			}
			Operand.SortAttribute(GroupAttr);
			
			Operations=Aggregate.split(",");		
			Mapping= new String[1+Operations.length];
			Attr=new String[Operations.length];
			OpCode=new String[Operations.length];
			Res=new long[Operations.length];
			AttrIndex=new int[Operations.length];			
			
			Mapping[0]=GroupAttr;
			
			for(int i=0;i<Operations.length;++i){
				OpCode[i]=Operations[i].substring(0, Operations[i].indexOf("("));				
				Attr[i]=Operations[i].substring(Operations[i].indexOf("(")+1,Operations[i].indexOf(")"));
				if(!Operand.Attributes.containsKey(Attr[i])){
					System.err.println("Relation "+RelationName+" does not contain attribute : "+Attr[i]);
					System.exit(1);
				}
				AttrIndex[i]=Operand.Attributes.get(Attr[i]);
				Mapping[i+1]=Operations[i];
			}
			
		}
		
		public void ResetAggregates(){
			for(int i=0;i<Operations.length;++i)
			{	
				if(OpCode[i].equals("MAX"))
					Res[i]=Integer.MIN_VALUE;
				else if(OpCode[i].equals("MIN"))
					Res[i]=Integer.MAX_VALUE;
				else 
					Res[i]=0;
			}
		}
		
		public void UpdateAggregates(String Record[]){
			for(int i=0;i<Operations.length;++i){
				if(!Character.isDigit(Record[AttrIndex[i]].charAt(0))){
					System.err.println("Invalid Inputs to Aggregator Function : "+OpCode[i]);
					System.err.println("Not An Integer!");
					System.exit(1);
				}
				
				int input=Integer.parseInt(Record[AttrIndex[i]]);
				
				if(OpCode[i].equals("MAX"))					
					MaxGp(i,input);
				else if(OpCode[i].equals("MIN"))
					MinGp(i,input);
				else if(OpCode[i].equals("COUNT"))
					CountGp(i,input);
				else if(OpCode[i].equals("SUM"))
					SumGp(i,input);
				else{
					System.err.println("Invalid Grouping Aggregator !"+OpCode[i]);
					System.exit(1);
				}
			}
		}
		
		private void SumGp(int i,int input) {
			// TODO Auto-generated method stub
			Res[i]+=input;
		}

		private void CountGp(int i,int input) {
			// TODO Auto-generated method stub
			Res[i]+=1;
		}

		private void MinGp(int i,int input) {
			// TODO Auto-generated method stub
			Res[i]=Math.min(Res[i], input);
		}

		private void MaxGp(int i,int input) {
			// TODO Auto-generated method stub
			Res[i]=Math.max(Res[i], input);
		}

		public void DoOperation(String ResultName){
			Result=new Relation(ResultName,Mapping.length,Mapping);
			Result.OpenWriteMode();
			try {								
				BufferedReader reader1=new BufferedReader(new FileReader(Operand.RelationName+GroupAttr+".info"));
				StringBuilder ResultBuilder=new StringBuilder();
				String prevRecord="";
				long count=0;String Record1;
				ResetAggregates();
				while((Record1=reader1.readLine())!=null){
						String comp=Record1.split(";")[0];
						if(comp.equals(prevRecord)){
							UpdateAggregates(Record1.split(";")[1].split(" "));
						}
						else if(!prevRecord.equals("")){
							ResultBuilder.append(prevRecord);
							for(int i=0;i<Operations.length;++i)
								ResultBuilder.append(" "+Res[i]);
							ResultBuilder.append("\n");
							ResetAggregates();
							UpdateAggregates(Record1.split(";")[1].split(" "));
							++count;
						}
						else{
							UpdateAggregates(Record1.split(";")[1].split(" "));
						}
						
									 
						
						if(count>100000) {									
								Result.MainFileWriter.write(ResultBuilder.toString());					
								count=0;	
								ResultBuilder=new StringBuilder();
						}	
						prevRecord=comp;
				}
				
				ResultBuilder.append(prevRecord);
				for(int i=0;i<Operations.length;++i)
					ResultBuilder.append(" "+Res[i]);
				ResultBuilder.append("\n");
				++count;
				
				if(count>0){		
					Result.MainFileWriter.write(ResultBuilder.toString());
				}
						
				
				reader1.close();
					
			}catch (IOException e) {
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
		GroupAggregation ga=new GroupAggregation(params[0],params[1]);
		//so.PrintResult();
		//po.DoOperation("RandomName");
		ga.DoOperation("RandomName");
		long estimatedTime = System.nanoTime() - startTime;
		System.out.println(estimatedTime/1000000);		
		read.close();
	}
}