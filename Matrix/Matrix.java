package Matrix;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Random;

import NeuralNetwork.Layer;

//import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Matrix, can be dynamic or used for operations allows operations on matrices or a matrix object
 * @author Maury Johnson
 *
 */

/*
 * 
 *  	TEMPLATE TO CREATE NEW MATRIX
 		Matrix A = new Matrix(
 		Matrix.NewDoubleMatrix(new double[][]{
 		{0.0,1.1},
 		{0.2,1.3},
 		...
 		}
 		,"MATRIX_NAME");
 * 
 */
public class Matrix implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 591430990044685213L;

	/**
	 * Dynamic entries for a dynamic matrix
	 * 2D Array
	 */
	public ArrayList<ArrayList<double[]>> Entries;
	
	/**
	 * Assign a name to matrix
	 */
	public String Name = "";
	
	/**
	 * Create a new Matrix
	 * @param Entries
	 */
	public Matrix(ArrayList<ArrayList<double[]>> Entries,String Name) {
		if(Entries.size()==0) {
			System.err.println("No Rows for MAtrix");
			return;
		}
		if(Entries.get(0).size()==0) {
			System.err.println("No Columns for Matrix");
			return;
		}
		this.Entries=Entries;
		this.Name=Name;
	}
	
	/**
	 * Get Number of columms
	 * @return
	 */
	public int GetColumns() {
		return Entries.get(0).get(0).length;
	}
	
	/**
	 * Get Number of Rows
	 * @return
	 */
	public int GetRows() {
		return Entries.size();
	}
	
	/**
	 * Return double[][] Equivalent of matrix
	 * @return
	 */
	public double[][] ToDoublesArray(){
		double[][] D = new double[GetRows()][GetColumns()];
		for(int i=0; i<GetRows();i+=1) {
			for(int j=0; j<GetColumns();j+=1) {
				D[i][j]=Entries.get(i).get(0)[j];
			}
		}
		return D;
	}
	
	/**
	 * Complete new Object copy of Matrix
	 * @param M
	 * @return
	 */
	public static Matrix CopyMatrix(Matrix M,String Name) {
		double[][] Copies = new double[M.GetRows()][M.GetColumns()];
		for(int i=0; i<M.GetRows();i+=1) {
			for(int j=0; j<M.GetColumns();j+=1) {
				Copies[i][j]=M.Entries.get(i).get(0)[j];
			}
		}
		return new Matrix(Matrix.NewDoubleMatrix(Copies),Name);
	}
	
	
	/**
	 * 1 0  2 2  == 2 2 
	 * 2 1  3 4  == 7 8
	 * Multiplies two Matrices, returns new matrix of this result
	 * @param M1
	 * @param M2
	 * @return
	 */
	public static Matrix Multiply(Matrix M1, Matrix M2,boolean Normalized,boolean Biased,String Name) {
		System.out.println("Multiply\n"+M1+"\n and\n"+M2);
		
		/*
		if(M1.GetColumns()!=M2.GetRows() && Biased) {
			System.err.println(" MISMATCH M1 Rows and M2 Columns");
			return null;
		}
		*/
		
		double[][] AllSums = new double[M1.GetRows()][M2.GetColumns()];
		
		for(int i=0; i<M1.GetRows();i+=1) {
		
			double[] Sums = new double[M2.GetColumns()];
			
			// M1 -> Columns== M2->Rows,
			for(int j=0; j<M2.GetColumns();j+=1) {
				
				double sum = 0.0;
				
				//Where you sum M1's column with M2's Row
				for(int k=0; k< (Biased? M1.GetColumns():M1.GetColumns()-1) ;k+=1) {
				
					sum+=	M1.Entries.get(i).get(0)[k]
							*
							M2.Entries.get(k).get(0)[j];
				}
				
				double Ns = 1.0;
				
				if(Normalized) {
					
					System.out.println("Normalizing for Matrix TRANSPOSED!!!!!:"+M1.Name);
					
					Ns = 0.0;
					for(int l=0; l<M1.GetRows();l++) {
						Ns+=M1.Entries.get(l).get(0)[j];
					}
					
					if(Ns==0.0) {
						System.err.println("INF ENTRY at:["+i+","+j+"]");
						Ns = 1.0;
					}
					
				}
				
				Sums[j]=sum/Ns;
				
				sum=0.0;
			}
			
			AllSums[i]=Sums;
		}
		
		Matrix M3 = new Matrix(Matrix.NewDoubleMatrix(AllSums),Name);
		
		return M3;	
	}
	
	/**
	 * Alters Current Matrix Value to new multiplied matrix
	 * @param M1
	 * @return
	 */
	public void Multiply(Matrix M1,boolean Normalized,boolean Biased,String Name) {
		if(GetColumns()!=M1.GetRows()) {
			System.err.println(Name+"'s Columns:"+this.GetColumns()+" is not equal to "+M1.Name+"'s Rows:"+M1.GetRows());
			return;
		}
		Matrix M = Matrix.Multiply(this,M1,Normalized,Biased,Name);
		this.Entries=null;
		this.Entries=M.Entries;
		
		/*
		if(M1.GetColumns()==1)
		this.Bias =  M1.Entries.get(M1.GetRows()-1).get(0)[0];
		*/
		
	}
	
	/**
	 * Multiple Matrix by itself to a power
	 * @param Power
	 */
	public Matrix Power(int Power) {
		if(this.GetColumns()!=this.GetRows()) {
			System.err.print(Name+" is not an NXN matrix");
			return null;
		}
		if(Power==0) {
			return this.CreateIdentityMatrix();
		}
		//Take Inverse of matrix, multiply it by itself
		else if(Power<0) {
			Matrix InverseMatrix = Matrix.Inverse(this);
			Power=Power*-1;
			for(int i=0;i<Power;i+=1)
			InverseMatrix.Multiply(InverseMatrix,false,true,"Multiply Inverse X"+(i+1));
			return InverseMatrix;
		}
		else
		{
			for(int i=0; i<Power;i+=1)
			this.Multiply(this,false,true,"Multiply X"+(i+1));
			return this;
		}
	}
	
	/**
	 * Return New Matrix that is the transpose of this matrix
	 * @return
	 */
	public Matrix Transpose(String Name) {
		double[][] All = new double[GetColumns()][GetRows()];
		
		for(int i=0; i<GetRows();i+=1) {
			for(int j=0; j<GetColumns();j+=1) {
				All[j][i]=this.Entries.get(i).get(0)[j];
			}
		}

		return new Matrix(Matrix.NewDoubleMatrix(All),Name);
	}
	
	/**
	 * Get Echelon Matrix
	 * Get Inverse
	 * @param matrix
	 * @return
	 */
	public static Matrix Inverse(Matrix Matrix) {
	
		if(Matrix.GetRows()!=Matrix.GetColumns()) {
			System.err.println("Rows does not match columns");
			return null;
		}
		
		double divisor;
		int i=0;
		int j=0;
		int k=0;
		int NXN = Matrix.GetColumns();
		
		
		Matrix EMatrix=Matrix.CreateIdentityMatrix();
		
		// TODO Auto-generated method stub
		for(i=0;i<NXN;i++){
		    for(j=0;j<NXN;j++){
		    if(i==j){
		       if(Matrix.Entries.get(j).get(0)[i]!=1){
			//Make row such that == 1!
			
			divisor = Matrix.Entries.get(j).get(0)[i];
			if(divisor!=0){
			MultiplyRow(1.0/divisor,Matrix,i);
		    MultiplyRow(1.0/divisor,EMatrix,i);
			}
			else{
			System.out.println("\nCANNOT REDUCE\n");
			break;
			}
			}
			//PIVOT
			k=0;
			while(k<NXN){
			//Iterate down all rows above and below pivot column
			//NOT PIVOTS
			if(k!=j){   
			if(Matrix.Entries.get(j).get(0)[k]<0||Matrix.Entries.get(j).get(0)[k]>0){
			AddRows(-Matrix.Entries.get(j).get(0)[k],EMatrix,i,k);		
			AddRows(-Matrix.Entries.get(j).get(0)[k],Matrix,i,k);

			}
			else{
			//printf("\nRow:%LG Column:%ld is already 0, %LG \n",j,k,Matrix[j][k]);
			}
			
			if(Matrix.Entries.get(j).get(0)[j]!=1){
		        //Make row such that == 1!
		        divisor = Matrix.Entries.get(j).get(0)[j];
		        if(divisor!=0){
			MultiplyRow(1.0/divisor,Matrix,j);
		        //divisor=EMatrix[k][k];
			MultiplyRow(1.0/divisor,EMatrix,j);
		        }
			else{
			System.out.println("CANNOT REDUCE");
			break;
			}
			}
			
			}
			k++;   
			}
			
		        }
		    
		     System.out.println("\n MATRIX NOW:\n");
		     	System.out.println(Matrix);
		     
		     System.out.println("\n EEEMATRIX NOW:\n");
		        System.out.println(EMatrix);
		    
		    }
		
		}

		return EMatrix;
	}

	/**
	 * Multiplies entire row, given by j, by double d
	 * @param d
	 * @param matrix
	 * @param j
	 */
	public static void MultiplyRow(double d, Matrix Matrix, int j) {
		// TODO Auto-generated method stub
		int i=0;
		for(i=0;i<Matrix.GetColumns();i+=1) {
			Matrix.Entries.get(j).get(0)[i]*=d;
		}
	}

	/**
	 * Adds to entire row, given by i,k, with double d
	 * @param d
	 * @param eMatrix
	 * @param i
	 * @param k
	 */
	public static void AddRows(double d, Matrix Matrix, int column1, int column2) {
		// TODO Auto-generated method stub
		int i=0;
		while(i<Matrix.GetColumns()){
		Matrix.Entries.get(i).get(0)[column2]=Matrix.Entries.get(i).get(0)[column2]+(d*Matrix.Entries.get(i).get(0)[column1]);
		i++;
		}
	}

	/**
	 * Return string format of 2D array
	 */
	public String toString() {
		String s = "";
		for(ArrayList<double[]>DD:Entries) {
			for(double[]D:DD)
				for(double d:D)
					s+=d+" ";
			s+="\n";
		}
		return this.Name+"\n"+s;
	}
	
	/**
	 * Create Identity Matrix
	 * @return
	 */
	public Matrix CreateIdentityMatrix() {
		ArrayList<ArrayList<double[]>> A  = new ArrayList<ArrayList<double[]>>();
		
		for(int i=0; i<this.GetColumns();i+=1) {
			double[] ERow = new double[this.GetColumns()];
			int j=0;
			for(j=0;j<ERow.length;j+=1) {
				if(i==j) {
					ERow[j]=1.0;
				}
				else {
					ERow[j]=0.0;
				}
			}
			ArrayList<double[]> B = new ArrayList<double[]>();
			B.add(ERow);
			A.add(B);
		}
			
		return new Matrix(A,"Echelon Matrix");
	}
	
	/**
	 * Add two Matrices, with second matrix multiplied by value return new result
	 */
	public static Matrix Add(Matrix A, Matrix B, int Val) {
		if(A.GetColumns()!=B.GetColumns()||A.GetRows()!=B.GetRows()) {
			System.err.println("First Matrix:\n"+A+"does not match size of second Matrix:\n"+B);
			return null;
		}
		
		double[][] Result = new double[A.GetRows()][B.GetColumns()];
		
		for(int i=0; i<A.GetRows();i+=1) {
			for(int j=0; j<B.GetColumns();j+=1) {
				Result[i][j]=A.Entries.get(i).get(0)[j] + (Val*B.Entries.get(i).get(0)[j]);
			}
		}
		
		return new Matrix(Matrix.NewDoubleMatrix(Result),A.Name+"-"+B.Name);
	}
	
	/*
	 * Subtract value from entire matrix
	 */
	public static void Subtract(Matrix A,double Val) {
		for(int i=0; i<A.GetRows();i+=1) {
			for(int j=0; j<A.GetColumns();j+=1)
			A.Entries.get(i).get(0)[j]-=Val;
		}
	}
	
	/**
	 * Make new Arraylist of double values
	 * @param entries
	 * @return
	 */
	public static ArrayList<double[]> NewDoubleList(double[] entries){
		ArrayList<double[]> A = new ArrayList<double[]>();
		A.add(entries);
		return A;
	}
	
	/**
	 * Return entire Matrix object given double[][] all entries
	 * @param entries
	 * @return
	 */
	public static ArrayList<ArrayList<double[]>> NewDoubleMatrix(double[][] entries){
		ArrayList<ArrayList<double[]>> A = new ArrayList<ArrayList<double[]>>();
		for(int i=0; i<entries.length;i+=1) {
			A.add(NewDoubleList(entries[i]));
		}
		return A;
	}
	
	/**
	 * Perform Sigmoid operation on Matrix which is 1 column by n rows
	 * Used for hidden layer
	 * Typically used for activation
	 * @param M
	 * 1/(1+e^-x)
	 */
	public static void Sigmoid(Matrix M) {
		if(M.GetColumns()!=1) {
			System.err.println("Matrix must be a vector");
			return;
		}
		
		for(int i=0;i<M.GetRows();i+=1) {
			for(int l=0; l<M.GetColumns();l+=1) {
			
				M.Entries.get(i).get(0)[l] = 1/(1+Math.exp(-M.Entries.get(i).get(0)[l]));
			
			}
		}
		
	}
	
	/**
	 * Compute Derivative of Sigmoid operation
	 * e^-x / (1+e^-x)^2
	 * Typically used for activation
	 * @param M
	 * @return
	 */
	public static Matrix DerivSigmoid(Matrix M) {
		
		Matrix M2 = new Matrix(
				Matrix.NewDoubleMatrix
				(new double[M.GetRows()][M.GetColumns()]
						)
				,"DerivSigmoid");
		
		for(int i=0; i<M.GetRows();i+=1) {
			for(int l=0; l<M.GetColumns();l+=1) {
			
				M2.Entries.get(i).get(0)[l]= (Math.exp(-M.Entries.get(i).get(0)[l])) / Math.pow( ( 1 + Math.exp(-M.Entries.get(i).get(0)[l])   ) ,2);
			
			}
		}
		
		return M2;
	}
	
	/**
	 * Perform Dynamic operation on Matrix for Sigmoid
	 * @param M
	 */
	public static void DSigmoid(Matrix M,boolean Biased) {
		
		int Rows = Biased? M.GetRows()-1:M.GetRows();
		
		for(int i=0; i<Rows;i+=1) {
			for(int l=0; l<M.GetColumns();l+=1) {
				//Because rows are already sigmoided... just do this formula
				//M.Entries.get(i).get(0)[l]= (Math.exp(-M.Entries.get(i).get(0)[l])) / Math.pow( ( 1 + Math.exp(-M.Entries.get(i).get(0)[l])   ) ,2);
				M.Entries.get(i).get(0)[l] = M.Entries.get(i).get(0)[l]*(1-M.Entries.get(i).get(0)[l]);
			}
		}
	}
	
	/**
	 * Perform Relu operation on Matrix which is 1 column by n rows
	 * Used for hidden layer
	 * Typically used for activation
	 * @param M
	 * max(0,x)
	 */
	public static void Relu(Matrix M) {
		if(M==null) {
			System.err.println("Null Matrix");
			return;
		}
		if(M.GetColumns()!=1) {
			System.err.println("Matrix must be a vector");
			return;
		}
		
		for(int i=0; i<M.GetRows();i+=1) {
			for(int l=0; l<M.GetColumns();l+=1) {
				
				if(M.Entries.get(i).get(0)[l]<=0) {
					M.Entries.get(i).get(0)[l] = 0.0;
				}
			
			}
		}
		
	}
	
	/**
	 * Perform derivative operation of Relu
	 * max(0,x) -> 0,1
	 * Typically used for activation
	 * @param M
	 */
	public static Matrix DerivRelu(Matrix M) {
		if(M.GetColumns()!=1) {
			System.err.println("Matrix must be a vector");
			return null;
		}
		Matrix M2 = new Matrix(
				Matrix.NewDoubleMatrix
				(new double[M.GetRows()][M.GetColumns()]
						)
				,"DerivRelu");
		for(int i=0; i<M.GetRows();i+=1) {
			for(int l=0;l<M.GetColumns();l+=1) {
				
				if(M.Entries.get(i).get(0)[l]<=0) {
					M2.Entries.get(i).get(0)[l] = 0.0;
				}
				else {
					M2.Entries.get(i).get(0)[l]=1.0;
				}
				
			}
		}
		
		return M2;
	}
	
	/**8
	 * Perform dynamic operations on current matrix for Relu
	 */
	public static void DRelu(Matrix M,boolean Biased) {
		
		int Rows = Biased? M.GetRows()-1:M.GetRows();
		
		for(int i=0;i<Rows;i+=1) {
			for(int j=0; j<M.GetColumns();j+=1) {
				if(M.Entries.get(i).get(0)[j]<=0) {
					M.Entries.get(i).get(0)[j] = 0.0;
				}
				else {
					M.Entries.get(i).get(0)[j]=1.0;
				}		
			}
		}	
	}
	
	/**
	 * Perform SoftMax operation on Matrix which is 1 column by n rows
	 * Used for Outputs
	 * Typically used for activation
	 * @param M
	 * e^ak/sum(e^aj)
	 */
	public static void SoftMax(Matrix M) {
		if(M.GetColumns()!=1) {
			System.err.println("Matrix must be a vector");
			return;
		}
		
		//Ensures no double function
		double[][] ActualM = new double[M.GetRows()][M.GetColumns()];
		
		double sum;
		for(int i=0; i<M.GetRows();i+=1) {
			for(int l=0; l<M.GetColumns();l+=1) {
				
			sum = 0.0;
			for(int j=0; j<M.GetRows();j+=1) {
					sum+=Math.exp(M.Entries.get(j).get(0)[l]);
			}
			if(sum==0.0) {
				sum=1.0;
			}
			ActualM[i][l] = Math.exp(M.Entries.get(i).get(0)[l])/sum;
			
			}
		}
		
		//Store actual matrix values
		for(int i=0; i<M.GetRows();i+=1) {
			M.Entries.get(i).get(0)[0]=ActualM[i][0];
		}
	}
	
	/**
	 * Derivative of soft max function, easily SM(1-SM)
	 * @param M
	 */
	public static void DSoftMax(Matrix M,boolean Biased) {
		
		int Rows = Biased? M.GetRows()-1:M.GetRows();
		
		for(int i=0; i<Rows;i+=1) {
			for(int l=0; l<M.GetColumns();l+=1) {
				M.Entries.get(i).get(0)[l] = M.Entries.get(i).get(0)[l]*(1-M.Entries.get(i).get(0)[l]);
			}
		}
		
	}
	
	/**
	 * For printing out copy matrix for softmax, but not the original!
	 * @return
	 */
	public String toSoftMax() {
		Matrix M = Matrix.CopyMatrix(this, "Copy SM");
		Matrix.SoftMax(M);
		return M.toString();
	}
	
	public Matrix toSoftMax(int Dummy) {
		Matrix SMM = Matrix.CopyMatrix(this, "Copy SM");
		Matrix.SoftMax(SMM);
		return SMM;
	}
	
	/**
	 * Compute Derivative of soft max operation
	 * EX d/d2(SM) for N = 3 = d/d2(e^x/(e^x +e^y +e^z)) == (e^y+e^z)/(e^x+e^y+e^z)^2
	 * Typically used on activation function
	 * @param M
	 */
	public static Matrix DerivSoftMax(Matrix M) {
		
		Matrix M2 = new Matrix(
				Matrix.NewDoubleMatrix
				(new double[M.GetRows()][M.GetColumns()]
						)
				,"DerivSoftMax");
		
		double sum;
		
		for(int i=0; i<M.GetRows();i+=1) {
			for(int l=0; l<M.GetColumns();l+=1) {
				
			sum=0.0;
			for(int j=0; j<M.GetRows();j+=1) {
				if(i!=j) {
					//sum+=e^j
					sum+=Math.exp(M.Entries.get(j).get(0)[l]);
				}
			}
				//(e^i)*(e^j+e^k)/(e^i+e^j+e^k)^2
				M2.Entries.get(i).get(0)[l] = 
						Math.exp(M.Entries.get(i).get(0)[l])*sum / 
						Math.pow(Math.exp(M.Entries.get(i).get(0)[l]) + sum,2); 
			}
		}
		
		return M2;
	}
	
	/**
	 * Test Matrix Operations
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<ArrayList<double[]>> A = NewDoubleMatrix(
				new double[][] {
					{0.0,2.1,5.1,06,26.2222222,5.55555555555,6.60101}
				}
				);
				
		Matrix M = new Matrix(A,"TEST MATRIX");
		
		System.out.println(M);
		
		Matrix E = M.CreateIdentityMatrix();
		
		System.out.print(E);
	
		Matrix.Inverse(E);
	
		Matrix N = new Matrix( NewDoubleMatrix(
				new double[][] {
					{2,2},
					{3,2}
				}
				),"TEST 2");
	
		Matrix.Inverse(Matrix.CopyMatrix(N, "N COPIED"));
	}
	
	/**
	 * Normalize all entries of matrix!!!
	 */
	public void Normalize() {
		System.out.println("Matrix Before Normalize:"+toString());
		double sum = 0.0;
		
		for(int i=0;i<GetRows();i+=1) {
			for(int j=0; j<GetColumns();j+=1) {
				for(int k=0;k<GetColumns();k+=1) {
					if(k!=j) {
						sum+=Entries.get(i).get(0)[k];
					}
				}
				//Edge case
				if(sum==0.0) {
					sum=1.0;
				}
				Entries.get(i).get(0)[j]*=1/sum;
				sum = 0.0;
			}
		}
		
		
		System.out.println("Normalized Matrix:"+this.toString());
	}

	/**
	 * Randomize all entries in Matrix
	 * @return
	 */
	public void Randomize() {
		Random rand = new Random();
		for(int i=0;i<GetRows();i+=1) {
			for(int j=0; j<GetColumns();j+=1) {
				
				//if(j==GetColumns()-1) {
					//Entries.get(i).get(0)[j]=1.0;
				//}
				
				//else {
				
					Entries.get(i).get(0)[j]=rand.nextDouble();
				
				//}
			}
		}
	}

	/**
	 * Get Error, multiply without using bias yet
	 * @param getWeights
	 * @param object
	 * @return
	 */
	public static Matrix Error(Matrix Weights, Matrix Error,boolean Normalized) {
		// TODO Auto-generated method stub
			
			//Calculate error, bias appended means nothing!!!
			Matrix WTE = Matrix.Multiply(Weights.Transpose("WT"),Error,Normalized,true,"WTXE");
			
			System.out.println("Multiply Result:\n"+WTE.toString());
			
			
			/*
			if(Nor) {
			if(WTE!=null)
			WTE.Normalize();
			}
			*/
			
			/*
			WTE.Bias = Error.Entries.get(Error.GetRows()-1).get(0)[0];
			*/
			
			return WTE;
	}

	/**
	 * Multiple two matrices across
	 * @param getError
	 * @param string
	 */
	public void MultiplyAcross(Matrix GetError,String string) {
		
		if(GetError.Entries.size()==1) {
			MultiplyAcross(GetError.Entries.get(0).get(0)[0],string);
			return;
		}
		
		// TODO Auto-generated method stub
		
		/*
		if((GetError.GetRows()!=this.GetRows() ||GetError.GetColumns()!=this.GetColumns())) {
			System.err.println("Rows Don't match for Multiply Across");
			return;
		}
		*/
		
		for(int i=0; i<GetError.GetRows()&&i<this.GetRows();i+=1) {
			for(int j=0; j<GetError.GetColumns()&&j<this.GetColumns();j+=1)
			Entries.get(i).get(0)[j]*=GetError.Entries.get(i).get(0)[j];
		}
		this.Name = string;
	}

	/**
	 * Multiple every entry by lr
	 * @param learningRate
	 * @param string
	 */
	public void MultiplyAcross(double LearningRate, String string) {
		// TODO Auto-generated method stub
		for(int i=0;i<this.GetRows();i+=1) {
			for(int j=0; j<this.GetColumns();j+=1)
			Entries.get(i).get(0)[j]*=LearningRate;
		}
		this.Name=string;
	}

	public Double Sum() {
		// TODO Auto-generated method stub
		Double d = 0.0;
		
		//Iterate all of array, return sum of all entries
		for(int i=0; i<this.GetRows();i+=1) {
			for(int j=0; j<this.GetColumns();j+=1) {
				d+=this.Entries.get(i).get(0)[j];
			}
		}
		
		return d;
	}

	/**
	 * Adds Bias to entire matrix
	 * @param getActivation
	 * @param d
	 */
	public static void AddBias(Matrix M, double d) {
		// TODO Auto-generated method stub
		for(int i=0; i<M.GetRows();i+=1) {
			for(int j=0; j<M.GetColumns();j+=1) {
				M.Entries.get(i).get(0)[j]+=d;
			}
		}
	}

	/**
	 * Collect all biases for all columns of weights, initialized to 1
	 * @param weights
	 * @param biases
	 */
	public static void CollectBiases(Matrix W, ArrayList<double[]> Biases,int WIdx) {
		// TODO Auto-generated method stub
		
		double[] D = new double[W.GetRows()];
		
		for(int i=0; i<W.GetRows();i+=1) {
			for(int j=W.GetColumns()-1; j<W.GetColumns();j+=1) {
				//Append all TRUE biases from weight
				//Biases.get(WIdx)[j] = (W.Entries.get(i).get(0)[j]);
				D[i] = (W.Entries.get(i).get(0)[j]);
			}
		}
		Biases.set(WIdx, D);
	}

	/**
	 * Important function to collect all un calculated biases from W*A
	 * @param getWeights
	 * @param getActivation
	 * @param biases
	 */
	public static void CollectMultipliedBiases(Layer L,Matrix W, Matrix A) {
		
		L.Biases=null;
		L.Biases = new ArrayList<Double>();
		
		// TODO Auto-generated method stub
		double sum = 0.0;
		//double[] D = new double[W.GetRows()];
		for(int i=0; i<W.GetRows();i+=1) {
			for(int j=0; j<A.GetColumns();j+=1) {
				sum = 0.0;
				
				for(int l=W.GetColumns()-1; l<W.GetColumns();l+=1) {
					
					sum+=W.Entries.get(i).get(0)[l]*A.Entries.get(l).get(0)[j];
					
				}		
				//D[i] = sum;
				L.Biases.add(sum);
			}
		}
	}

	public static void Sigmoid(ArrayList<Double> B) {
		// TODO Auto-generated method stub
		for(int i=0;i<B.size();i+=1) {
		
				B.set(i, 1/(1+Math.exp(-B.get(i))));
			
		}
		
	}

	public static void Relu(ArrayList<Double> B) {
		// TODO Auto-generated method stub
		for(int i=0; i<B.size();i+=1) {
			if(B.get(i)<=0) {
				B.set(i, 0.0);
			}
		}
		
	}

	public static void SoftMax(ArrayList<Double> B) {
		// TODO Auto-generated method stub
		//Ensures no double function
				double[][] ActualM = new double[B.size()][1];
				
				double sum;
				for(int i=0; i<B.size();i+=1) {
					//for(int l=0; l<M.GetColumns();l+=1) {
						
					sum = 0.0;
					for(int j=0; j<B.size();j+=1) {
							sum+=Math.exp(B.get(j));
					}
					if(sum==0.0) {
						sum=1.0;
					}
					ActualM[i][0] = Math.exp(B.get(i))/sum;
					
					//}
				}
				
				//Store actual matrix values
				for(int i=0; i<B.size();i+=1) {
					B.set(i,ActualM[i][0]);
				}
	}

	/**
	 * Collect error biases to keep track of biases
	 * operated on
	 * remember if they will be normalized, as noted there,
	 * then just normalize the biases...
	 * @param layer
	 * @param WT
	 * @param E
	 * @param normalized
	 */
	public static void CollectErrorBiases(Layer L, Matrix W, Matrix A, boolean normalized) {
		// TODO Auto-generated method stub
		
		//double sum = 0.0;
		
		for(int i=W.GetRows()-1; i<W.GetRows();i+=1) {
			for(int j=0; j<A.GetColumns();j+=1) {
				//Only Accept final row...
				//Collect the individual biases by number of columns
				for(int l=0; l<W.GetColumns();l+=1) {
			
					L.Biases.set(l, W.Entries.get(i).get(0)[l]*A.Entries.get(l).get(0)[j]);
			
				}

			}
		
		}
	
	}

	public void SaveMatrix(BufferedWriter out) {
		// TODO Auto-generated method stub
		int i=0;
		int j=0;
		
		for(i=0;i<this.GetRows();i+=1) {
			for(j=0;j<this.GetColumns();j+=1) {
				try {
					out.write(String.format("%.7f ",this.Entries.get(i).get(0)[j]));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.exit(-2);
				}
				
			}
			try {
				out.write("\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(-2);
			}
		}
	}
	
}
