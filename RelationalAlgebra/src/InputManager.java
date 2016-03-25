import java.io.IOException;
import java.util.Scanner;


public class InputManager {
	public static void main(String args[]) throws NumberFormatException, IOException{
		Scanner reader=new Scanner(System.in);
		String res,name,mapping[];
		int columns;
		System.out.println("\tINPUT MANAGER\n");
		System.out.println("Create New Relation(Y/N) ? :");
		res=reader.nextLine();
		if(res.equalsIgnoreCase("N")||res.equalsIgnoreCase("NO"))
		{
			System.exit(0);
		}
		
		System.out.println("Enter Relation Details : "+"\nName:");
		res=reader.nextLine();
		name=res;
		System.out.println("No. of Attributes : ");
		columns=Integer.parseInt(reader.nextLine());
		mapping=new String[columns];
		System.out.println("Enter the Attributes (Capitalize any one of the letters of attribute to specify it as integer type) : ");
		for(int i=0;i<columns;++i){
			System.out.println("Attribute "+String.valueOf(i+1));
			res=reader.nextLine();
			mapping[i]=res;
		}
		
		Relation r=new Relation(name,columns,mapping);
		
		System.out.println("Generate Random Records(Y/N) ? :");
		res=reader.nextLine();
		if(res.equalsIgnoreCase("Y")||res.equalsIgnoreCase("Yes"))
		{
			System.out.println("How many records ? ");
			res=reader.nextLine();
			r.WriteRandomRecords(Integer.parseInt(res));
		}
		else
		{
			r.OpenWriteMode();
			while(true){
				
				System.out.println("Insert record of Length ="+columns+"(-1 to exit)");
				res=reader.nextLine();
				if(res.equals("-1")){
					r.CloseWriteMode();
					break;
				}
				r.WriteRecord(res.split(" "));
			}
			
			
		}
		r.PrintRelation();
		System.out.println("Enter any key to exit");
		reader.nextLine();
		reader.close();
	}
}
