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
	
	Matrix M;
	
	//To rewind, use FileChannel fx = F.getChannel();
	//fx.position(x)
	
	public MatrixFileStream(FileInputStream F, char Delimiter) {
	if(F==null) {
		System.out.println("No file");
	}
	this.F=F;
	this.Delimiter=Delimiter;
	}

	public static MatrixFileStream ReadFile(MatrixFileStream MFS1, char Delimiter) {
		if(MFS1.F==null) {
			System.out.println("Unable to open file");
			System.exit(-4);
		}
		FileInputStream F = MFS1.F;
		
		int COLUMNS = GetColumns(F);
		int ROWS = GetRows(F,Delimiter);
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
			R = NextDouble(F,Delimiter);
		
			if(R[0]==2||R[0]==1) {
				
				if(R[0]==1) {
					
					if(R[1]!=-88888888) {
					
						System.out.printf("RESULT FINAL:%f",R[1]);
					
						Entries[Rows][Columns]=R[1];
						
					}
					
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
			
		}
		
		Rows+=1;
		System.out.printf("\n SIZE:%f ROWS:%d COLUMNS:%d\n",Size,Rows,COLUMNS);
		
		if(COLUMNS==0) {
			return null;
		}
		
		Matrix MNew = new Matrix(Matrix.NewDoubleMatrix(Entries),"Read M");
		
		MFS1.M=MNew;
		
		return MFS1;
	}
	
	private static double[] NextDouble(FileInputStream F, char Delimiter) {
		// TODO Auto-generated method stub
	
		boolean Dot=false;
		
		double[] R = new double[2];
		R[1]=-88888888;
		
		int ASize = 1;
		String app = "";
		char c;
		int i=0;
		String result = new BufferedReader(new InputStreamReader(F))
				  .lines().collect(Collectors.joining("\n"));
		
		while(i<result.length()) {
			c = result.charAt(i);
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
					R[1] = Double.parseDouble(app);
					R[0]=1;
					return R;
				}
				else {
					R[1] = Double.parseDouble(app);
					R[0]=0;
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
		R[1]=-999999999;
		app=null;
		result=null;
		return R;
	}

	public static int GetColumns(FileInputStream F) {
		FileChannel fc = F.getChannel();
		long PrevPosition = 0;
		try {
			PrevPosition = fc.position();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-4);
		}
	
		char c;
		int C=0;
		int i = 0;
		boolean Spaces=false;
		boolean Dot=false;
		
		String result = new BufferedReader(new InputStreamReader(F))
				  .lines().collect(Collectors.joining("\n"));
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
		
		try {
			fc.position(PrevPosition);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-4);
		}
		
		result=null;
		return C;
	}
	
	public static int GetRows(FileInputStream F,char Delimiter) {
		FileChannel fc = F.getChannel();
		long PrevPosition = 0;
		try {
			PrevPosition = fc.position();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-4);
		}
		
		char c;
		int R=0;
		boolean DelimFound=false;
		
		int i=0;

		String result = new BufferedReader(new InputStreamReader(F))
				  .lines().collect(Collectors.joining("\n"));
		
		while(i<result.length()) {
		c = result.charAt(i);
		if(c=='\n') {
			if(!DelimFound) {
				R+=1;
			}
			else {
				DelimFound=false;
			}
		}
		else if(c==Delimiter) {
			DelimFound=true;
			R+=1;
		}
		i+=1;
		}
		
		try {
			fc.position(PrevPosition);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-4);
		}
		
		result=null;
		return R;
	}
	
	public static void main(String[] args) {
		MatrixFileStream MFS1 = null;
		try {
			MFS1 = new MatrixFileStream(new FileInputStream("/Model/Model1"),'_');
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
