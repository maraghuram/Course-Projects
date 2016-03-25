import java.util.Scanner;
import java.util.Vector;


public class Parser {

	Scanner s;
	Vector<Operation> operations;
	
	public Parser()
	{
		s =new Scanner(System.in);
		operations=new Vector<Operation>();
	}
	
	public Parser(String input)
	{
		s =new Scanner(input);
		operations=new Vector<Operation>();
	}
	
	private boolean IsOpCode(String token)
	{
		if(token.equals("SELECT") || token.equals("PROJECT") || token.equals("JOIN")) return true;
		if(token.equals("CROSS") || token.equals("UNION") || token.equals("DIFF")) return true;
		if(token.equals("MAX") || token.equals("MIN") || token.equals("COUNT") || token.equals("AVG") || token.equals("SUM")) return true;
		if(token.equals("RENAME") || token.equals("GROUP")) return true;
		return false;
	}
	
	private int NoOfRelations(String opCode)
	{
		if(opCode.equals("SELECT") || opCode.equals("PROJECT") || opCode.equals("RENAME")) return 1;
		if(opCode.equals("MAX") || opCode.equals("MIN") || opCode.equals("COUNT") || opCode.equals("AVG") || opCode.equals("SUM")) return 1;
		if(opCode.equals("JOIN") || opCode.equals("CROSS") || opCode.equals("UNION") || opCode.equals("DIFF")) return 2;
		if(opCode.equals("GROUP")) return 1;
		return 0;
	}
	
	public void ParserHelper()
	{
		String start=s.next();
		if(!IsOpCode(start))
		{
			PrintErrorMessage("START OF INPUT");
			return;
		}
		Relation res=Parse(start,0,0);
		res.PrintRelation();
		
		//System.out.println(operations);
	}
	
	private void PrintErrorMessage(String token) {
		// TODO Auto-generated method stub
		System.err.println("Invalid Syntax @ token : "+token);
	}
	
	
	private boolean RequiresArgs(String token) {
		// TODO Auto-generated method stub
		if(token.equals("CROSS")||token.equals("UNION")||token.equals("DIFF"))
			return false;
		return true;	
	}

	public Relation Parse(String token,int depth,int pos)
	{
		Operation newOperation=new Operation(String.valueOf(depth)+pos);
		newOperation.SetOpCode(token);
		
		for(int i=0;i<NoOfRelations(token);++i)
		{
			if(!s.hasNext()){
				PrintErrorMessage(token);
				operations.clear();
				System.exit(1);
			}
			String operand=s.next();
			Relation Result;
			
			if(IsOpCode(operand))
			{				
				Result=Parse(operand,depth+1,i);
				
			}
			else{
				Result=new Relation(operand);				
			}
			newOperation.AddOperand(Result);
			
		}
		if(RequiresArgs(token)){
			if(!s.hasNext()){
				PrintErrorMessage(token+" arguments");
				operations.clear();
				System.exit(1);
			}
			newOperation.SetArguments(s.next());
		}
		
		return newOperation.Evaluate();
		//operations.add(newOperation);
	}


	public static void main(String args[])
	{
		Scanner reader=new Scanner(System.in);
		String input=reader.nextLine();
		Parser p = new Parser(input);	
		p.ParserHelper();
		System.out.println("Enter any key to exit");
		reader.nextLine();
		reader.close();
	}
}
