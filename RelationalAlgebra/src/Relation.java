import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import com.google.code.externalsorting.ExternalSort;


public class Relation {
	String RelationName;
	int Columns,Rows;
	Map<String,Integer> Attributes;
	File MainFile,DescpFile,AttrFiles[];
	FileWriter MainFileWriter;
	FileWriter AttrFilesWriter[];
	String Attr[];
	int NMAX=100;
	int SMAX=10;
	
	public Relation(String RelationName){
		this.RelationName=RelationName;
		LoadRelation();
	}
	
	public Relation(String RelationName,int Columns,String Mapping[]){
		this.RelationName=RelationName;
		this.Columns=Columns;
		Rows=0;
		Attr=Mapping;
		Attributes=new TreeMap<String,Integer>();
		
		for(int i=0;i<Columns;++i){
			Attributes.put(Attr[i], i);
		}		
		
		WriteDescpFile();		
	}
	
	void WriteDescpFile(){
		DescpFile=new File(RelationName+"Descp.info");
		try {
			FileWriter writer=new FileWriter(DescpFile);
			writer.write(Columns+"\n");
			for(int i=0;i<Columns;++i){
				writer.write(Attr[i]+";"+i+"\n");
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}	
	}
	
	void OpenWriteMode(){
		MainFile=new File(RelationName+"Main.info");
		AttrFiles=new File[Columns];
		for(int i=0;i<Columns;++i){
			AttrFiles[i]=new File(RelationName+Attr[i]+".info");
		}
		try {
			MainFileWriter=new FileWriter(MainFile);
			/*AttrFilesWriter=new FileWriter[Columns];
			for(int i=0;i<Columns;++i){
				AttrFilesWriter[i]=new FileWriter(AttrFiles[i]);
			}*/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}		
	}
	
	void WriteRecord(String[] Records){		
		StringBuilder Output=new StringBuilder();
		for(int i=0;i<Columns;++i){
			Output.append(Records[i]+" ");					
		}
		
		try {			
			MainFileWriter.write(Output.toString()+"\n");
		} catch (IOException e) {			
			e.printStackTrace();
			System.exit(1);
		}
		/*for(int i=0;i<Columns;++i){
			try {
				AttrFilesWriter[i].write(Records[i]+";"+Output.toString()+"\n");
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}*/
	}
	
	void CloseWriteMode(){
		try {
			MainFileWriter.close();
			/*for(int i=0;i<Columns;++i){
				AttrFilesWriter[i].close();
			}*/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
	}
	
		
	void WriteRandomRecords() throws IOException{	
		
		OpenWriteMode();
		int count=0;
		StringBuilder res=new StringBuilder();
		
		for(int i=0;i<NMAX;++i){				
			StringBuilder WriteString=new StringBuilder();			
			String record[]=new String[Columns];			
			for(int j=0;j<Columns;++j){
				String RandomString=GenerateRandomString();
				if(Attr[j].compareTo(Attr[j].toLowerCase())!=0)
					RandomString=GenerateRandomInt();
				record[j]=RandomString;
				WriteString.append(RandomString+" ");
			}
			
			res.append(WriteString.toString()+"\n");
			++count;
			if(count>100000){
				MainFileWriter.write(res.toString());
				count=0;
				res=new StringBuilder();
			}	
			
		}	
		
		MainFileWriter.write(res.toString());

		CloseWriteMode();
	}
	
	void WriteRandomRecords(int NMAX) throws IOException{	
		
		OpenWriteMode();
		int count=0;
		StringBuilder res=new StringBuilder();
		
		for(int i=0;i<NMAX;++i){				
			StringBuilder WriteString=new StringBuilder();			
			String record[]=new String[Columns];			
			for(int j=0;j<Columns;++j){
				String RandomString=GenerateRandomString();
				if(Attr[j].compareTo(Attr[j].toLowerCase())!=0)
					RandomString=GenerateRandomInt();
				record[j]=RandomString;
				WriteString.append(RandomString+" ");
			}
			
			res.append(WriteString.toString()+"\n");
			++count;
			if(count>100000){
				MainFileWriter.write(res.toString());
				count=0;
				res=new StringBuilder();
			}	
			
		}	
		
		MainFileWriter.write(res.toString());

		CloseWriteMode();
	}
	
	String GenerateRandomString(){
		StringBuilder Output=new StringBuilder();
		Random rand=new Random();
		int j;
		for(j=0;j<rand.nextInt(SMAX)+1;++j){
			Output.append((char)(rand.nextInt(26)+'a'));
		}		
		return Output.toString();
	}
		
	String GenerateRandomInt(){	
		StringBuilder Output=new StringBuilder();
		Random rand=new Random();
		int j;
		for(j=0;j<rand.nextInt(SMAX-1)+1;++j){
			char c=(char)(rand.nextInt(10)+'0');
			if(j==0 && c=='0'){		
				--j;
				continue;
			}
			Output.append(c);
		}		
		return Output.toString();
	}
	
	void LoadRelation(){
		DescpFile=new File(RelationName+"Descp.info");
		
		if(!DescpFile.exists()){
			System.err.println(RelationName+" meta data missing ! "+RelationName+"Descp.info");
			System.exit(1);
		}
		
		MainFile=new File(RelationName+"Main.info");
		if(!MainFile.exists()){
			System.err.println(RelationName+" records missing ! "+RelationName+"Main.info");
			System.exit(1);
		}
		
		Attributes=new TreeMap<String,Integer>();
		
		try {
			BufferedReader reader=new BufferedReader(new FileReader(DescpFile));			
			Columns=Integer.valueOf(reader.readLine());
			Attr=new String[Columns];
			
			for(int i=0;i<Columns;++i)
			{
				String attr;				
				attr=reader.readLine();				
				Integer index = Integer.valueOf(attr.split(";")[1]);
				attr=attr.split(";")[0];
				Attributes.put(attr, index);
				Attr[i]=attr;
			}		
			
			reader.close();
						
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}		
		
		AttrFiles=new File[Columns];		
		for(int i=0;i<Columns;++i){
			AttrFiles[i]=new File(RelationName+Attr[i]+".info");
		}
	}
	
	public static Comparator<String> StringNameComparator = new Comparator<String>() 
	{
		public int compare(String arg0, String arg1) {
			int l,r,max1,max2;
			l=r=0;
			max1=arg0.length();
			max2=arg1.length();
			char c1,c2;			
			//if(Character.isDigit(arg0.charAt(0)) && Character.isDigit(arg1.charAt(1)))
				//return (int)( Integer.parseInt(arg0.split(";")[0])-Integer.parseInt(arg1.split(";")[0]));
			
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

	
	void SortAllAttributes(){
		
		ExternalSort.defaultcomparator=StringNameComparator;
		
		for(int i=0;i<Columns;++i){
			File temp=new File("temp.info");
			System.out.println("Sorting File : "+AttrFiles[i].getName());
			try {
				ExternalSort.sort(AttrFiles[i],temp);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			AttrFiles[i].delete();
			temp.renameTo(AttrFiles[i]);		
			System.out.println("Sorting "+temp.getName()+" Completed !");
		}		
	}
	
	
	
	void CreateAttributeFile(String AttrName){
				
		int i=Attributes.get(AttrName);
		try {
			FileWriter writer=new FileWriter(AttrFiles[i]);
			BufferedReader reader=new BufferedReader(new FileReader(MainFile));
			String record;
			StringBuilder res=new StringBuilder();
			int count=0;
			while((record=reader.readLine())!=null){
				StringBuilder readRecord=new StringBuilder();
				readRecord.append(record.split(" ")[i]+";");
				readRecord.append(record);
				++count;
				res.append(readRecord+"\n");
				if(count>100000){
					writer.write(res.toString());					
					count=0;
					res=new StringBuilder();
				}				
			}
			if(count>0)	writer.write(res.toString());	
				
			writer.close();
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void SortAttribute(String AttrName){
		int i=Attributes.get(AttrName);
		if(!AttrFiles[i].exists()){
			System.out.println("Building Attribute File : "+AttrFiles[i].getName());
			CreateAttributeFile(AttrName);
			System.out.println("Finished Building Attribute File : "+AttrFiles[i].getName()+"!");
		}
		else{
			return;
		}
		try {
		/*	List<File> l = ExternalSort.sortInBatch(AttrFiles[Attributes.get(AttrName)], ExternalSort.defaultcomparator);
			File sf = new File("sorted.tmp");
			ExternalSort.mergeSortedFiles(l, sf, ExternalSort.defaultcomparator, Charset.defaultCharset(),false);
			*/
			System.out.println("Sorting File : "+AttrFiles[i].getName());
			File temp=new File("temp.info");
			ExternalSort.defaultcomparator=StringNameComparator;
			ExternalSort.sort(AttrFiles[i], temp);			
			AttrFiles[i].delete();
			temp.renameTo(AttrFiles[i]);
			System.out.println("Finished Sorting File : "+AttrFiles[i].getName()+"!");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void PrintRelation(){
		System.out.println("\nRelation Name : "+RelationName);
		for(int i=0;i<Columns;++i)
			System.out.print(Attr[i]+"\t");
		System.out.print("\n");
		for(int i=0;i<Columns;++i)
		{
			for(int j=0;j<Attr[i].length();++j)		
				System.out.print("-");
			System.out.print("\t");
		}
		System.out.print("\n");
		
		try {
			BufferedReader reader=new BufferedReader(new FileReader(RelationName+"Main.info"));
			String record;
			while((record=reader.readLine())!=null){				
				String []recordArray=record.split(" ");
				for(int i=0;i<Columns;++i)
				{
					System.out.print(recordArray[i]+"\t");
				}
				System.out.print("\n");				
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	 public static void main(String args[]) throws IOException{
		 long startTime = System.nanoTime();
		 String[] map=new String[4];
		 map[0]="ssn";
		 map[1]="name";
		 map[2]="Salary";
		 map[3]="address";
		 Relation r=new Relation("Employee",4,map);
		 r.WriteRandomRecords();
		 
		 //Relation r=new Relation("Employee");
		 //r.SortAttribute("Salary");
		 //r.SortAllAttributes();
		 /*r.OpenWriteMode();
		 String record[]={"1234","raghuram","100","asdfg"};
		 String record2[]={"234","ram","asdf","azcxvvxccxv"};
		 //String record3[]={"45678","akshay","1000","zxcvccbvc"};
		 r.WriteRecord(record);
		 r.WriteRecord(record2);
		 //r.WriteRecord(record3);
		 r.CloseWriteMode();
		 */
		 /*Relation r=new Relation("Employee");
		 System.out.println(r.Columns);
		 System.out.println(r.Attributes.toString());*/
		 /*File Read=new File("EmployeeSalary.info");
		 try {
			BufferedReader reader=new BufferedReader(new FileReader(Read));
			String s;
			while((s=reader.readLine())!=null){
				//System.out.println(s);
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 /*
		 ExternalSort.defaultcomparator=StringNameComparator;
		 try {
			ExternalSort.sort(new File("Employeename.info"),new File("Temp"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		 long estimatedTime = System.nanoTime() - startTime;
		 System.out.println(estimatedTime/1000000);		 
		
	 }
	
}
