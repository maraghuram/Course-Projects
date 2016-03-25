import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
import java.util.TreeMap;


public class SelectOperation {
	TreeBooleanEvaluator evaluator;
	Relation Operand,Result;	
	String CondExpr;
	Map<String,String> input;
	int NMAX=10000;
	String[] ConstVal,Opr;
	int[] End;
	int[] Lhs,Rhs;
	int OpCount;

	public int Pass(char c){
		if(Character.isAlphabetic(c)||Character.isDigit(c)) return 1;
		
		switch(c){
		case '<' :	return 2;
		case '>' :	return 3;
		case '=' :	return 4;
		case '"' : 	return 5;
		}
		return -1;
	}
	
	public SelectOperation(String RelationName,String CondExpr){
		evaluator = new TreeBooleanEvaluator();
		Operand=new Relation(RelationName);
		this.CondExpr=CondExpr;
		ConstVal=new String[NMAX];
		Opr=new String[NMAX];
		End=new int[NMAX];
		Lhs=new int[NMAX];
		Rhs=new int[NMAX];
		OpCount=0;
		
		for(int i=0;i<CondExpr.length();++i){
			boolean OpFlag=false;
			boolean ConstFlag=false;
			int type;
			StringBuilder left,right,op;
			left=new StringBuilder();
			right=new StringBuilder();
			op=new StringBuilder();
			
			if(Character.isAlphabetic(CondExpr.charAt(i))){
				Rhs[OpCount]=1;
				while(i<CondExpr.length() && (type=Pass(CondExpr.charAt(i)))!=-1){
					char c=CondExpr.charAt(i);
					switch(type){
					case 1:	if(OpFlag) right.append(c);
							else left.append(c);
							break;
					case 2:	
					case 3:
					case 4:	OpFlag=true;
							op.append(c);
							break;
						
					case 5:	ConstFlag=(!ConstFlag);
							Rhs[OpCount]=-1;
							break;
					}
					++i;					
				}
				
				End[OpCount]=i;
				
				if(!OpFlag){
					System.err.println("No Operator Found!");
					System.exit(1);
				}
				
				//System.out.println(left.toString()+" "+op+" "+right.toString());
				String opr=op.toString();
				if(!(opr.equals("<") || opr.equals(">") || opr.equals("=") || opr.equals("<=") || opr.equals(">="))){
					System.err.println("No Valid Operator Found!");
					System.exit(1);
				}
				Opr[OpCount]=op.toString();
				
				if(ConstFlag){
					System.err.println("Invalid Syntax (Unmatched \")");
					System.exit(1);
				}
				
				if(!Operand.Attributes.containsKey(left.toString())){
					System.err.println("Attribute " +left.toString()+ " Does Not Exist!");
					System.exit(1);
				}				
				
				Lhs[OpCount]=Operand.Attributes.get(left.toString());
				Integer val;
				if(Rhs[OpCount]==-1)
					ConstVal[OpCount]=right.toString();
				else if((val=Operand.Attributes.get(right.toString())) != null)
					Rhs[OpCount]=val;
				else{
					System.err.println("Attribute " +right.toString()+ " Does Not Exist!");
					System.exit(1);
				}
					
				++OpCount;
				if(OpCount>=NMAX){
					System.err.println("Too many conditions!");
					System.exit(1);
				}
				
			}
		//	System.err.println(i);
		}
	}
	
	public void PrintResult(){
		System.out.println(OpCount);
		for(int i=0;i<OpCount;++i){
			System.out.println(Lhs[i]+" "+Rhs[i]+((Rhs[i]!=-1)?"":ConstVal[i]));
			System.out.println(End[i]);
		}
	}
	
	public boolean lessThan(String x,String y){
		if(Character.isDigit(x.charAt(0))&& Character.isDigit(y.charAt(0))) return (Integer.parseInt(x)<Integer.parseInt(y))?true:false;
		return x.compareTo(y)<0?true:false;
	}
	
	public boolean lessThanOrEqualTo(String x,String y){
		if(Character.isDigit(x.charAt(0))&& Character.isDigit(y.charAt(0))) return (Integer.parseInt(x)<=Integer.parseInt(y))?true:false;
		return x.compareTo(y)<=0?true:false;
	}
	public boolean greaterThan(String x,String y){
		if(Character.isDigit(x.charAt(0))&& Character.isDigit(y.charAt(0))) return (Integer.parseInt(x)>Integer.parseInt(y))?true:false;
		return x.compareTo(y)>0?true:false;
	}		
	public boolean greaterThanOrEqualTo(String x,String y){
		if(Character.isDigit(x.charAt(0))&& Character.isDigit(y.charAt(0))) return (Integer.parseInt(x)>=Integer.parseInt(y))?true:false;
		return x.compareTo(y)>=0?true:false;
	}
	public boolean equalTo(String x,String y){		
		if(Character.isDigit(x.charAt(0))&& Character.isDigit(y.charAt(0))) return (Integer.parseInt(x)==Integer.parseInt(y))?true:false;
		return x.equals(y);
	}
	
	public boolean Operate(String x,String y,String op){
		if(op.equals("<")){
			return lessThan(x,y);
		}
		else if(op.equals("<=")){
			return lessThanOrEqualTo(x,y);
		}
		else if(op.equals(">")){
			return greaterThan(x,y);
		}
		else if(op.equals(">=")){
			return greaterThanOrEqualTo(x,y);
		}
		else if(op.equals("=")){
			return equalTo(x,y);
		}
		else{
			System.err.println("Invalid Operator!");
			System.exit(1);
		}
		return false;	
	}

	public boolean CheckRecord(String[] input){
		
		StringBuilder expression=new StringBuilder();
		int ptr=0;		
		for(int i=0;i<CondExpr.length();++i){
			if(Character.isAlphabetic(CondExpr.charAt(i))){
				String left,right,op,res;			
				left=input[Lhs[ptr]];
				if(Rhs[ptr]==-1)
					right=ConstVal[ptr];
				else 
					right=input[Rhs[ptr]];
				op=Opr[ptr];
				res=Operate(left,right,op)==true?"T":"F";
				expression.append(res);
				i=End[ptr]-1;
				++ptr;
				if(ptr>OpCount){
					System.err.println("Error Occured!");
					System.exit(1);
				}
			}
			else
				expression.append(CondExpr.charAt(i));
		}		
		String res=evaluator.evaluate(expression.toString());
		if(res.equals("T")) return true;
		else return false;
	}
	
	Relation DoOperation(String ResultName){
		Result=new Relation(ResultName,Operand.Columns,Operand.Attr);
		Result.OpenWriteMode();
		List<String> sequence = new ArrayList<String>();
		try {
			BufferedReader reader=new BufferedReader(new FileReader(Operand.MainFile));
			String Record;
			//StringBuilder ResultString=new StringBuilder();
			String RecordArray[]=new String[100005];
			int count=0;
			while((Record=reader.readLine())!=null){
					RecordArray[count]=(Record);				
					++count;			
					//evaluator.evaluate("T &&( F || T)");
					if(count>100000) {
						StringBuilder ResultRecord=new StringBuilder();
						for(int i=0;i<count;++i){
							if(CheckRecord(RecordArray[i].split(" "))){
								ResultRecord.append(RecordArray[i]+"\n");							
							}													
						}
						count=0;
						Result.MainFileWriter.write(ResultRecord.toString());
					}				
			}		
			StringBuilder ResultRecord=new StringBuilder();
			for(int i=0;i<count;++i){
				if(CheckRecord(RecordArray[i].split(" "))){
					ResultRecord.append(RecordArray[i]+"\n");					
				}				
			}
			Result.MainFileWriter.write(ResultRecord.toString());
			reader.close();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		Result.CloseWriteMode();		
		return Result;
	}
	
	public static void main(String args[]){
		Scanner read=new Scanner(System.in);
		String input=read.nextLine();
		String params[]=input.split(" ");
		long startTime = System.nanoTime();
		SelectOperation so=new SelectOperation(params[0],params[1]);
		//so.PrintResult();
		so.DoOperation("RandomName");
		long estimatedTime = System.nanoTime() - startTime;
		System.out.println(estimatedTime/1000000);		
	}
}
