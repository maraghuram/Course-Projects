import java.util.Vector;


public class Operation {
	private String opCode;
	private Vector<Relation> operands;
	private String arguments;
	private String id;
	
	public Operation(String id)
	{
		operands=new Vector<Relation>();
		opCode=arguments="";
		this.id=id;
	}
	
	public void SetOpCode(String op)
	{
		opCode=op;
	}
	
	public void AddOperand(Relation operand)
	{
		operands.addElement(operand);
	}
	
	public void SetArguments(String args)
	{
		arguments=args;
	}
	
	public String toString()
	{
		return opCode+" "+operands+" "+arguments;
	}
	
	public Relation Evaluate(){
		
		if(opCode.equals("SELECT")){
			SelectOperation so=new SelectOperation(operands.elementAt(0).RelationName,arguments);
			so.DoOperation(id);
			return so.Result;
		}
		
		if(opCode.equals("PROJECT")){
			ProjectOperation po=new ProjectOperation(operands.elementAt(0).RelationName,arguments);
			po.DoOperation(id);
			return po.Result;
		}
		
		if(opCode.equals("JOIN")){
			JoinOperation jo=new JoinOperation(operands.elementAt(0).RelationName,operands.elementAt(1).RelationName,arguments);
			jo.DoOperation(id);
			return jo.Result;
		}
		
		if(opCode.equals("UNION")){
			UnionOperation uo=new UnionOperation(operands.elementAt(0).RelationName,operands.elementAt(1).RelationName);
			uo.DoOperation(id);
			return uo.Result;
		}
		
		if(opCode.equals("DIFF")){
			SetDifferenceOperation sdo=new SetDifferenceOperation(operands.elementAt(0).RelationName,operands.elementAt(1).RelationName);
			sdo.DoOperation(id);
			return sdo.Result;
		}
		
		if(opCode.equals("CROSS")){
			CrossOperation co=new CrossOperation(operands.elementAt(0).RelationName,operands.elementAt(1).RelationName);
			co.DoOperation(id);
			return co.Result;
		}
		
		if(opCode.equals("MIN") || opCode.equals("MAX") || opCode.equals("AVG") || opCode.equals("SUM") || opCode.equals("COUNT")){
			Aggregation a=new Aggregation(opCode,operands.elementAt(0).RelationName,arguments);
			a.DoOperation(id);
			return a.Result;
		}
		
		if(opCode.equals("RENAME")){
			RenameOperation ro=new RenameOperation(operands.elementAt(0).RelationName,arguments);
			return ro.Result;
		}
		
		if(opCode.equals("GROUP")){
			GroupAggregation go=new GroupAggregation(operands.elementAt(0).RelationName,arguments);
			go.DoOperation(id);
			return go.Result;
		}
			
		System.err.println("Invalid Opcode !");
		System.exit(1);
		return operands.elementAt(0);
	}
}
