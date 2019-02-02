package MatrixFileStream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.stream.Collectors;

import Matrix.Matrix;

public class MatrixFileStream {

	char Delimiter;
	
	FileInputStream F;
	
	String result = "";
	
	int Position = 0;
	
	FileChannel FC;
	
	Matrix M;
	
	//To rewind, use FileChannel fx = F.getChannel();
	//fx.position(x)
	
	public MatrixFileStream(FileInputStream F, char Delimiter) {
	if(F==null) {
		System.out.println("No file");
	}
	this.F=F;
	FC = F.getChannel();

	/*
	try {
		FC.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	*/
	InputStreamReader I = new InputStreamReader(F);
	this.result = new BufferedReader(I)
			  .lines().collect(Collectors.joining("\n"));
	
	if(result==null) {
		System.err.println("No File input");
	}
	try {
		F.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	this.Delimiter=Delimiter;
	}

	public MatrixFileStream ReadFile() {
		if(this.F==null) {
			System.out.println("Unable to open file");
			System.exit(-4);
		}
		char Delimiter = this.Delimiter;
		//FileInputStream F = this.F;
		
		int COLUMNS = this.GetColumns();
		int ROWS = this.GetRows(Delimiter);
		
		System.out.printf("\n ROWS:%d",ROWS);
		
		int Columns = 0;
		int Rows = 0;
		
		int Size = 1;
		
		if(ROWS<=0 || COLUMNS<=0) {
			System.out.println("No Rows or Columns");
			return null;
		}
		
		double [][] Entries = new double[ROWS][COLUMNS];
		
		double[]R;
		
		while(true) {
			R = this.NextDouble(Delimiter);
		
			System.out.println("Return Status: ["+R[0]+","+R[1]+"]");
			
			if(R[0]==2||R[0]==1) {
				
				//if(R[0]==1) {
					
					if(R[1]!=-88888888 && R[1]!=-999999999) {
					
						System.out.printf("RESULT FINAL:%f",R[1]);
					
						Entries[Rows][Columns]=R[1];
						
				//}
					
					}
				
				System.out.println("DONE READING");
				break;
			}
		
			if(R[1]!=-88888888) {
				if(Columns>=COLUMNS) {
					Columns=0;
					Rows+=1;
				}
				if(Rows>=Size) {
					Size+=1;
				}
				
				Entries[Rows][Columns]=R[1];
				
				Columns+=1;
			}
			/*
			else {
				this.Position+=1;
			}
			*/
			
		}
		
		Rows+=1;
		System.out.printf("\n SIZE:%d ROWS:%d COLUMNS:%d\n",Size,Rows,COLUMNS);
		
		if(COLUMNS==0) {
			return null;
		}
		
		this.NextLine();
		
		Matrix MNew = new Matrix(Matrix.NewDoubleMatrix(Entries),"Read M");
		
		this.M=MNew;
		
		return this;
	}
	
	private void NextLine() {
		// TODO Auto-generated method stub
		int i=this.Position;
		char c;
		while(i<result.length()) {
			c = result.charAt(i);
			if(c=='\n') {
				i+=1;
				break;
			}
			i+=1;
		}
		this.Position = i;
	}

	private double[] NextDouble(char Delimiter) {
		// TODO Auto-generated method stub
		FileInputStream F = this.F;
		boolean Dot=false;
		
		double[] R = new double[2];
		R[1]=-88888888;
		
		int ASize = 1;
		String app = "";
		char c;
		int i=this.Position;
	
		System.out.println("File:\n "+result+" Position:"+i);
		
		while(i<result.length()) {
			c = result.charAt(i);
			System.out.println("CHAR:"+c);
			System.out.printf("\nApp:%s Size:%d\n ", app,ASize);
			if(!Character.isDigit(c)) {
				if(c=='.') {
					if(Dot) {
						System.err.printf("\nError! TWO Dots in Double NOT ALLOWED!!! Position:%d",i);
						System.exit(-1);
					}
					ASize+=1;
					app+=c;
					Dot=true;
				}
				else if(c==Delimiter) {
					System.out.println(c + "1==" +Delimiter);
					try {
						R[1] = Double.parseDouble(app);
						}
						catch(Exception e){
						R[1]=-88888888;
					}
					R[0]=1;
					this.Position=i+1;
					return R;
				}
				else {
					System.out.println(c + "2==" +Delimiter);
					try {
					R[1] = Double.parseDouble(app);
					}
					catch(Exception e){
					R[1]=-88888888;
					}
					R[0]=0;
					this.Position=i+1;
					return R;
				}
			}
			else {
				ASize+=1;
				app+=c;
			}
			i+=1;
		}
		
		R[0]=2;
		try {
			R[1] = Double.parseDouble(app);
			}
			catch(Exception e){
			R[1]=-999999999;
		}
		//app=null;
		//result=null;
		this.Position=i;
		return R;
	}

	public int GetColumns() {
		/*
		FileInputStream F = this.F;
		FileChannel fc = this.FC;
		long PrevPosition = 0;
		try {
			PrevPosition = fc.position();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-4);
		}
		 */
		char c;
		int C=0;
		int i = this.Position;
		boolean Spaces=false;
		boolean Dot=false;
		
		while(i<result.length()) {
			c = result.charAt(i);
			if(Character.isDigit(c)) {
				C = 1;
				break;
			}
			i+=1;
		}
			
		//String app = "";
		
		while(i<result.length()) {
			c= result.charAt(i);
			if(!Character.isDigit(c)) {
				if(c=='.') {
					if(Dot) {
						System.out.println("Double dots!!!");
						Dot=false;
						continue;
					}
					Dot=true;
				}
				else {
					Dot=false;
					if(c==' ') {
						Spaces=true;
					}
					
					if(c=='\n') {
						System.out.printf("\nMade it to final column:%d\n",C);
						break;
					}
					
				}
			}
			else {
				if(Spaces)
					C+=1;
				Spaces=false;
			}
			i+=1;
		}
		
		/*
		try {
			fc.position(PrevPosition);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-4);
		}
		*/
		//result=null;
		//this.Position=i;
		return C;
	}
	
	public int GetRows(char Delimiter) {
		//FileInputStream F = this.F;
		//FileChannel fc = this.FC;
		
		/*
		long PrevPosition = 0;
		try {
			PrevPosition = fc.position();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-4);
		}
		*/
		
		char c;
		int R=0;
		boolean DelimFound=false;
		
		boolean nums = false;
		
		int i=Position;

		while(i<this.result.length()) {
		c = this.result.charAt(i);
		if(c=='\n' || i==this.result.length()-1) {
			if(nums) {
				R+=1;
			}
			if(DelimFound) {
				break;
			}
			nums=false;
		}
		else if(c==Delimiter) {
			DelimFound=true;
		}
		else if(Character.isDigit(c)) {
			nums=true;
		}
		i+=1;
		}
		
		
		/*
		try {
			fc.position(PrevPosition);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-4);
		}
		*/
		/*
		try {
			fc.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		//result=null;
		//this.Position=PrevPosition;
		return R;
	}
	
	public static void main(String[] args) {
		MatrixFileStream MFS1 = null;
		try {
			MFS1 = new MatrixFileStream(new FileInputStream("./TestMatrix"),'_');
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MFS1.ReadFile();
	System.out.println(MFS1.M);
	MFS1.ReadFile();
	System.out.println(MFS1.M);
		
	}
	
}
