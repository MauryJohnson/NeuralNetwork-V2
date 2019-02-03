package NeuralNetwork;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

import Matrix.Matrix;
import Statistics.Graph;
import Statistics.*;

/**
 * Neural Network object, contains ArrayList of Layer Objects which store date about network 
 * like input layer, hidden layer, output layer
 * Activation function for hidden/output layer
 * @author Maury Johnson
 *
 */
public class NeuralNetwork implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7210106539963594255L;

	/**
	 * Used for layers
	 */
	public ArrayList<Layer> Layers = new ArrayList<Layer>();
	
	/**
	 * Used for user inputs
	 */
	private Scanner Scanner = new Scanner(System.in);
	
	public String Name = "";
	
	/**
	 * Used to determine type of error to evaluate
	 * 0 - Basic 1-Cross Entropy,...
	 */
	public int Error;
	
	/**
	 * Default bias MULTIPLIER!!!, may need adjustment to satisfy edge case
	 */
	public double Bias = 1.0;
	
	/**
	 * List of all biases for layer 1 to layer n
	 */
	//ArrayList<double[]> Biases = new ArrayList<double[]>();
	
	/**
	 * Normalized error or not?
	 */
	public boolean Normalized;
	
	/**
	 * Store all graphs
	 */
	public ArrayList<Graph> G = new ArrayList<Graph>();
	
	/**
	 * ArrayList of Maps to Graph using Charts
	 */
	private ArrayList<HMap> HMaps = new ArrayList<HMap>();

	public char Delimiter;
	
	/**
	 * Plain Neural Network
	 */
	public NeuralNetwork() {
		
	}
	
	/**
	 * User Preference
	 * @param Layers
	 */
	public NeuralNetwork(ArrayList<Layer> Layers) {
		this.Layers=Layers;
	}
	
	/**
	 * Add a New Layer to neural Network
	 * If layer is duplicate input/output layer, throw exception
	 * @param L
	 */
	public void AddLayer(Layer L) {
		//IF first layer is equal Input layer and add another input layer
		// OR last layer is output layer and add another output layer
		if(Layers.size()>0)
		if((Layers.get(0).Type==L.Type&&L.Type=='I')||Layers.get(Layers.size()-1).Type==L.Type&&L.Type=='O') {
			System.err.println("Duplicate Input/Output Layers Added!");
			return;
		}
		Layers.add(L);
	}
	
	/**
	 * Return Iteration through neural net
	 */
	public String toString() {
		System.out.println("Network SIZE:"+Layers.size());
		return this.Name+"\n"+Layers.toString();
	}
	
	/**
	 * STAGE 0
	 * Create Neural Network with Weights, which are randomized values
	 * Prompts the user
	 */
	public void CreateNeuralNetwork(double[][] NetworkBase) {
		int Layers = -1;
		int LayerSize=  -1;
		int ActivationFunction=-1;
		
		if(NetworkBase==null) {
		
			//	Scanner scanner = new Scanner(System.in);
		
		System.out.println("Enter Number of Layers:");
		Layers=Scanner.nextInt();
		Scanner.nextLine();
		if(Layers<2) {
			System.err.println("Impossible Layer Size");
			return;
		}
		
		System.out.println("Normalized? Enter >0 Yes, <=0 No");
		
		this.Normalized = this.Scanner.nextInt()>0? true:false;
		
		//Iterate through all layers, prompt user for layer sizes (Future versions would learn the right sizes)
		for(int i=0; i<Layers;i+=1) {
			if(i==0) {
				System.out.println("Enter Input Layer Size:");
			}
			else if(i!=Layers-1) {
				System.out.println("Enter Hidden Layer Size:");
			}
			else {
				System.out.println("Enter Output Layer Size:");
			}
			
			LayerSize=Scanner.nextInt();
			Scanner.nextLine();
			
			if(LayerSize<1) {
				System.err.println("Impossible Layer Size");
				return;
			}
			
			if(i>0) {
				System.out.println("Enter Activation Function type 0-NONE 1-Sigmoid 2-Relu 3-SoftMax");
				ActivationFunction = Scanner.nextInt();
				Scanner.nextLine();
			
				//	+1 IS FOR BIAS!!!!!!!!!!!!!!!
				// Rows for weight Matrix
				int Rows = LayerSize;
				
				//Get columns from previous weight matrix
				int Columns = -1;
				
				//Activation is input for input layer, so that will be checked first
				//Weights are handles for each hidden layer, so each preceding weight matrix will be checked
				if(i==1)
				Columns = this.Layers.get(i-1).GetActivation().GetRows();
				else
				Columns = this.Layers.get(i-1).GetWeights().GetRows();
				
				//Add hidden layer->1 weights with activation function Sigmoid->1
				Matrix Weights = new Matrix(
						Matrix.NewDoubleMatrix(
								//Columns+1 handles the BIAS
								new double[Rows][Columns+1]
								)
						,"Hidden Layer Weights:"+i);
				
				//Random weights to maximize chance of success
				Weights.Randomize();
			
				AddLayer((new Layer(
						Weights
						,1,ActivationFunction)));
				
			}
			else if(i==0) {
				Matrix Input = new Matrix(
						Matrix.NewDoubleMatrix(
								new double[LayerSize][1]
								)
						,"Input");
				AddLayer(new Layer(Input,0,0));
			}
			//this.Layers.get(i).SetBias(1.0);
		}
		
		//Scanner.close();
		
		}
		else {
			//Given input for neural network schematic
			
			if(NetworkBase.length<1) {
				System.err.println("Network Input Layer does not Exist");
				return;
			}
			
			if(NetworkBase[0].length!=2) {
				System.err.println("Network must be atleast size 2 IN-OUT");
				return;
			}
			
			int Size = NetworkBase[0].length;
			
			for(int i=0; i<Size; i+=1) {
				if(i>0) {
					int Rows = NetworkBase[i].length;
					int Columns = -1;
					
					if(i==1) 
					Columns=this.Layers.get(i-1).GetActivation().GetRows();
					else
					Columns=this.Layers.get(i-1).GetWeights().GetRows();
					
					Matrix Weights = new Matrix(
							Matrix.NewDoubleMatrix(
									//Columns+1 handles the BIAS
									new double[Rows][Columns+1]
									)
							,"Hidden Layer Weights:"+i);
					
					//Random weights to maximize chance of success
					Weights.Randomize();
					
					AddLayer((new Layer(
							Weights
							,1,1)));
					
				}
				else if(i==0) {
					Matrix Input = new Matrix(
							Matrix.NewDoubleMatrix(
									new double[Size][1]
									)
							,"Input");
					AddLayer(new Layer(Input,0,0));
				}
			}
		}
		System.out.println("Finished Creating Neural Network");
		System.out.println(this.toString());
	}
	
	/**
	 * STAGE 1
	 * Send input into NN, feed it forward and set activations
	 * @param N
	 */
	public static void FeedForward(NeuralNetwork N,double[][]Input) {
		if(Input==null) {
			//Scanner Scanner = new Scanner(System.in);
			
			double D = 0.0;
			
			//Input layer
			for(int i=0; i<N.Layers.get(0).GetActivation().GetRows();i+=1) {
				System.out.println("Enter input "+i+":");
				//Input layer is default an activation
				
				D = Double.parseDouble(N.Scanner.nextLine());
				
				
				N.Layers.get(0).GetActivation().Entries.get(i).get(0)[0]=D;
			}
			
		}
		else {
			
			for(int i=0; i<N.Layers.get(0).GetActivation().GetRows()&&i<Input.length;i+=1) {
				System.out.println("Inserting Input "+(i+1)+":"+Input[i][0]);
				N.Layers.get(0).GetActivation().Entries.get(i).get(0)[0]=Input[i][0];
			}
		
		}
		
			//Add Bias to Input layer, 1.0
			
			N.Layers.get(0).GetActivation().Entries.add(Matrix.NewDoubleList(
					new double[] {
							1.0
						}
						));
			
			//Hidden/Output layer
			for(int i=0; i<N.Layers.size();i+=1) {
				
				if(i>=1) {
				
				Matrix A = Matrix.Multiply(N.Layers.get(i).GetWeights(),
						N.Layers.get(i-1).GetActivation(),false,true,
						"Activation "+(i+1));
				
				Matrix.CollectMultipliedBiases(N.Layers.get(i),N.Layers.get(i).GetWeights(),N.Layers.get(i-1).GetActivation());
				
				if(A==null) {
					System.err.println("Null Activation Matrix!");
					System.exit(-2);
				}
				//Create Activation for layers >0
				N.Layers.get(i).SetActivation(
							A
				);
				
				N.Layers.get(i).Activate();
				
				if(i!=N.Layers.size()-1)
				N.Layers.get(i).GetActivation().Entries.add(Matrix.NewDoubleList(
						new double[] {
							1.0
						}
				));
				
				}
				
			}
			
			//Strip NN of biases, store them
			//N.StripBiases();
			
			System.out.println("Finished Feeding Neural Network:");
			System.out.println(N.toString());
			
	}
	
	/**
	 * STAGE 2
	 * Assess Error by comparing output to Expected
	 * Propagate this error back, if error!=0
	 * @param N
	 */
	public static int BackPropagate(NeuralNetwork N, double[][] ExpectedOutput) {
		
		//Prop back rows-1, because not using bias multiple yet
		int Rows = N.Layers.get(N.Layers.size()-1).GetActivation().GetRows();
		
		//Error matrix will be expected value - Actual output
		double[][] Error = new double[Rows][1];
		
		Matrix SMM = N.Layers.get(N.Layers.size()-1).GetActivation();
		
		
		
		//Activate all layer biases in order to compute correct error
		for(int i=0; i<N.Layers.size();i+=1) {
			
			N.Layers.get(i).ActivateB();
			
		}
		
		if(ExpectedOutput==null) {
		
		double E = 0.0;
		
		for(int i=0;i<Rows;i+=1) {
			System.out.println("Enter expected output "+(i+1)+":");
			
			E=N.Scanner.nextDouble();
			
			Error[i][0] = E-SMM.Entries.get(i).get(0)[0];
			
			System.out.println("Error With Bias:"+(E-SMM.Entries.get(i).get(0)[0]));
			
			//System.out.println("Actual Error:"+(E-SMM.Entries.get(i).get(0)[0] - N.Layers.get(N.Layers.size()-1).Biases.get(i) ));
		
		}
		
		}
		else {	
		
			for(int i=0;i<Rows;i+=1) {
				System.out.println("Enter expected output "+(i+1)+":");
			
				Error[i][0] = ExpectedOutput[i][0]-SMM.Entries.get(i).get(0)[0];
				
				System.out.println("Error With Bias:"+(ExpectedOutput[i][0]-SMM.Entries.get(i).get(0)[0]));
				
				//System.out.println("Actual Error:"+(ExpectedOutput[i][0]-SMM.Entries.get(i).get(0)[0] - N.Layers.get(N.Layers.size()-1).Biases.get(i) ));
				
			}
			
		}
		
		boolean NoError = true;
		for(int i=0; i<Error.length;i+=1) {
			double e = Error[i][0] <0? Error[i][0]*-1.0:Error[i][0];
			if(!(e<0.0001)) {
				System.err.println("Still error");
				NoError=false;
			}
			else {
				//N.Bias = 0.0;
			}
		}
		
		if(NoError) {
			return 1;
		}
		
		Matrix FirstError = new Matrix(
				Matrix.NewDoubleMatrix(Error)
				,"EM");
		
		N.Layers.get(N.Layers.size()-1).SetError(FirstError);
		
		for(int i = N.Layers.size()-1;i>1;i-=1) {
			
			//Append to weight matrix 1's
			
			//Back Error w/o using bias...
			Matrix BackError = Matrix.Error(N.Layers.get(i).GetWeights(),i==N.Layers.size()-1? 
					FirstError:N.Layers.get(i).GetError(),N.Normalized);
			
			//Biases are set already, must then allow gradient to take care of them
			//Collect all biases to BE when multiplying wT*E!!
			
			Matrix.CollectErrorBiases(
					
					N.Layers.get(i),
					
					N.Layers.get(i).GetWeights().Transpose("WT"),
					
					i==N.Layers.size()-1? FirstError:(i==1? BackError:N.Layers.get(i).GetError()),
					
					N.Normalized
			
			);
			
			N.Layers.get(i-1).SetError(BackError);
			
		}
		
		System.out.println("BPROP\n"+N.toString());
		
		return 0;
	}
	
	/**
	 * Given error and all weight matrices, compute the gradient for each layer
	 * Depending on type of activation, gradient will change
	 * @param N
	 */
	public static void Gradient(NeuralNetwork N,double Lr) {
		
		double LearningRate = 0.0;
		if(Lr==0) {
		System.out.println("Enter Learning Rate:");
		LearningRate = N.Scanner.nextDouble();
		}
		else {
			LearningRate=Lr;
		}
		
		//lr * E * (derivAct) * H^T
		for(int i=N.Layers.size()-1; i>=1;i-=1) {
			
			Matrix GradientWeight = null;
			
			Matrix ActivationTranspose = null;
			
			switch(N.Layers.get(i).ActivationFunction) {
			
			case 0:
				System.out.println("Using No Derivative, GUESSING GAME!!");
				
				ActivationTranspose = N.Layers.get(i-1).GetActivation().Transpose("Step 4");
				
				//NO DERIVATIVE! Get randomized version of activation!
				N.Layers.get(i).GetActivation().Randomize();
				
				N.Layers.get(i).GetActivation().MultiplyAcross(N.Layers.get(i).GetError(), "Step 2");
				
				System.out.println("ACTIVATION STAT3:\n"+N.Layers.get(i).GetActivation());
				
				//multiply learning rate
				
				N.Layers.get(i).GetActivation().MultiplyAcross(LearningRate,"Step 3");
				
				break;
				
			case 1:
				System.out.println("Using Derivative of Sigmoid");
			
				//Get transpose of PREVIOUS activation before changes
				ActivationTranspose = N.Layers.get(i-1).GetActivation().Transpose("Step 4");
				
				System.out.println("ACTIVATION STAT1:\n"+N.Layers.get(i).GetActivation());
				
				//DSig of activation
				Matrix.DSigmoid(N.Layers.get(i).GetActivation(),true);
				//DSigmoid of error
				//Matrix.DSigmoid(N.Layers.get(i).GetError());
				
				System.out.println("ACTIVATION STAT2:\n"+N.Layers.get(i).GetActivation());
				
				//Multiply error by dsig of activation
				N.Layers.get(i).GetActivation().MultiplyAcross(N.Layers.get(i).GetError(), "Step 2");
				
				System.out.println("ACTIVATION STAT3:\n"+N.Layers.get(i).GetActivation());
				
				//multiply learning rate
				N.Layers.get(i).GetActivation().MultiplyAcross(LearningRate,"Step 3");
				
				//GradientWeight = Matrix.Multiply(N.Layers.get(i).GetActivation(),ActivationTranspose,false,"Step 5");
				
				//^^ Version from yt, ONLY works on relu and nXn size layers!!!!!!1
				
				break;
			
			case 2:
				System.out.println("Using Derivative of Relu");
				
				//get transpose of PREVIOUS activation before changes
				ActivationTranspose = N.Layers.get(i-1).GetActivation().Transpose("Step 4");
				
				//DRelu of activation
				Matrix.DRelu(N.Layers.get(i).GetActivation(),true);
				//DRelu of error
				//Matrix.DRelu(N.Layers.get(i).GetError());
				
				//multiple dRelu of activation by error
				N.Layers.get(i).GetActivation().MultiplyAcross(N.Layers.get(i).GetError(), "Step 2");
				
				//Multiply by learning rate
				N.Layers.get(i).GetActivation().MultiplyAcross(LearningRate,"Step 3");
				
				//Gradient
				//GradientWeight = Matrix.Multiply(N.Layers.get(i).GetActivation(),ActivationTranspose,false,"Step 5");
				
				//^^ Version from yt, ONLY works on relu and nXn size layers!!!!!!1
				
				break;
				
			case 3:
				System.out.println("Using Derivative of SoftMax");
				
				//Transpose of PREVIOUS activation before change
				ActivationTranspose = N.Layers.get(i-1).GetActivation().Transpose("Step 4");
				
				//Derive soft max of activation
				Matrix.DSoftMax(N.Layers.get(i).GetActivation(),true);
				//DSoftMax of error
				//Matrix.DSoftMax(N.Layers.get(i).GetError());
				
				//Multiply by error of that
				N.Layers.get(i).GetActivation().MultiplyAcross(N.Layers.get(i).GetError(), "Step 2");
				
				//Multiply by learning rate
				N.Layers.get(i).GetActivation().MultiplyAcross(LearningRate,"Step 3");
				
				//^^ Version from yt, ONLY works on relu and nXn size layers!!!!!!1
							
				break;
				
			default:
				System.err.println("No other Activation function to use");
				System.exit(-1);
				
			}
			
			//Full multiplication now
			GradientWeight = Matrix.Multiply(N.Layers.get(i).GetActivation(),ActivationTranspose,false,true,"Step 5");
				
			//if(i!=1)
			N.Layers.get(i-1).GetActivation().Entries.remove(N.Layers.get(i-1).GetActivation().Entries.size()-1);
			N.Layers.get(i-1).Biases = null;
			
			N.Layers.get(i).SetGradientWeights(GradientWeight);
			
		}
		
		System.out.println("FINISHED GETTING GRADIENT WEIGHTS!");
		
		System.out.println(N.toString());

		N.Scanner.close();
	}
	
	/**
	 * Learn by adjusting all weights by the gradient weights -> W=W+GW
	 * @param NN
	 */
	public static void Learn(NeuralNetwork NN) {
		
		for(int i=1; i<NN.Layers.size();i+=1) {
			
			//Adding W from GW, THIS TIME THIS WILL HANDLE ENTIRE WEIGHT MATRIX!!!!!!!!!!!!!!!!!!!!!! XD XD XD XD
			for(int j=0; j<NN.Layers.get(i).GetWeights().GetRows();j+=1)
				for(int k=0; k<NN.Layers.get(i).GetWeights().GetColumns();k+=1) {
					
					NN.Layers.get(i).GetWeights().Entries.get(j).get(0)[k]+=NN.Layers.get(i).GetGradientWeights().Entries.get(j).get(0)[k];
					
				}
		}
		
		System.out.println("Network New Learned Weights:");
		
		System.out.println(NN.toString());
	}
	
	public static void main(String[] args) {
		//Test input layer for neural network
		
		
		NeuralNetwork NN = NNFileStream.ParseLayers("./Model/Model2", '_');
		
		
		
		
		NN.SaveNetwork();
		
		/*Matrix Input = new Matrix(
				Matrix.NewDoubleMatrix(
						new double[][]
								{
								{1}
								,
								{1}
								}
						)
				,"Input Matrix"
				);	
		//Create a 3 Layer Network
		//Input Layer
		Layer InputLayer = new Layer(Input,0,0);
		*/
		
		/*
		NeuralNetwork NN = new NeuralNetwork();
		NN.CreateNeuralNetwork(null);

		NN.HMaps.add(new HMap("Loss"));
		
		NN.HMaps.add(new HMap("Graph"));
		
		NN.HMaps.add(new HMap("Gradient"));
		
		int i=0;
		
		while(i<10000) {
	
		//Feeding input
		NeuralNetwork.FeedForward(NN,
				new double[][] {
				{1.0},
				{1.0},
				{1.0}
				}
				);
		//break;
		
		//Returned output -> Sum of all activations... flattened result
		
		NN.HMaps.get(1).Map.put(new Double[] {
				3.0	
		},NN.Layers.get(NN.Layers.size()-1).GetActivation().Sum());	
		
		if(NeuralNetwork.BackPropagate(NN, 
				new double[][] {
				{20.0},
				{0.0},
				{40.0}
				}
				)==1) {
			
			System.out.println("Network Cannot learn any more");
			System.out.println("NET:"+NN.toString());
			System.out.println("Iterations:"+(i+1));
			break;
		}
		//break;
		
		//Compute Gradient Descent
		//Graph? Iteration x gradient sum y
		NeuralNetwork.Gradient(NN,0.1);
		NN.HMaps.get(2).Map.put(new Double[] {
				i*1.0
		},NN.Layers.get(NN.Layers.size()-1).GetGradientWeights().Sum());
		
		NeuralNetwork.Learn(NN);
		
		//NN.AddBiases();
		
		//Put Loss values IE error
		NN.HMaps.get(0).Map.put(new Double[] {
				i*1.0
		},NN.Layers.get(NN.Layers.size()-1).GetError().Sum());
		
		i+=1;
		//break;
		}
		
		System.out.println(NN);
		
		//Create Error Loss Chart
		LineChart.CreateChart(new String[] {"Loss","Error Loss","LOSS","Trial","Error"},NN.HMaps.get(0));
		//Create Graph of feed forward in and out
		LineChart.CreateChart(new String[] {"IN vs OUT","Function","IN vs OUT","Iteration","Output"}, NN.HMaps.get(1));
		//Create gradient value vs iteration
		LineChart.CreateChart(new String[] {"Gradient","Gradient","Gradient vs iteration","Iteration","Gradient"}, NN.HMaps.get(2));
		*/
	}

	/**
	 * Store neural network into a model number
	 */
	public void SaveNetwork() {
		// TODO Auto-generated method stub
		char Delim = this.Delimiter;
		
		Scanner sc = new Scanner(System.in);
		
		//boolean Quit=false;
		boolean Write=false;
		int i=0;
		//int intSize=0;
		String Model = "Model";
		//boolean Neg = false;
		
		//do {
		Write=false;
		//Neg=false;
		System.out.println("Enter Model#:");
		i = sc.nextInt();
		
			Model+=i;
			File F = new File("./Model/"+Model);
			if(F.exists()) {
				System.out.println("File exists... Overwrite >0 Yes <=0 NO");
				i = sc.nextInt();
				if(i>0) {
					Write=true;
				}
			}
			else {
				Write=true;
			}
		
		//String Act = "";
		
		//NNFileStream NFS;
		
		if(Write) {
			//File F = new File("./Model/"+Model);
			System.out.println("Write to File:"+Model);
			F.delete();
			try {
				F.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(-1);
			}
			
			F.setWritable(true);
			
			/*
			try {
				NFS = new NNFileStream(new FileInputStream(F),Delim);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(-1);
			}
			*/
			BufferedWriter out=null;
			try {
				out = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(F.getAbsolutePath()), StandardCharsets.UTF_8 ));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(-2);
			}
			try {
				
			out.write(Model+Delim);
			out.newLine();
			//System.out.println("This error:"+this.Error);
			out.write(this.Error+""+Delim+"\n");
			//out.newLine();
			out.write((this.Normalized? "1":"0")+Delim+"\n");
			//out.newLine();
			
			/*
			System.out.println("Write 1:"+Model+Delim+"\n"+
			this.Error+Delim+"\n"
			+(this.Normalized? "1":"0")+Delim+"\n");
			*/
			
			out.write("I"+Delim+"\n"
			+"A"+this.Layers.get(0).ActivationFunction+Delim+"\n");
			
			this.Layers.get(0).GetActivation().SaveMatrix(out);
			
			out.write(Delim+"\n");
			
			i=1;
			while(i<this.Layers.size()) {
				if(i==this.Layers.size()-1) {
					out.write("O");
				}
				else {
					out.write("H");
				}
				
				out.write(Delim+"\n");
				
				out.write("A"+this.Layers.get(i).ActivationFunction+Delim+"\n");
				
				this.Layers.get(i).GetActivation().SaveMatrix(out);
				
				out.write(Delim+"\n");
				
				out.write("W"+Delim+"\n");
				
				this.Layers.get(i).GetWeights().SaveMatrix(out);
				
				out.write(Delim+"\n");
				
				out.write("E"+Delim+"\n");
				
				this.Layers.get(i).GetError().SaveMatrix(out);
				
				out.write(Delim+"\n");
				
				out.write("GW"+Delim+"\n");
				
				this.Layers.get(i).GetGradientWeights().SaveMatrix(out);
				
				out.write(Delim+"\n");
				
				i+=1;
			}
			
			out.flush();
			
			out.close();
			
			}
			catch(Exception e) {
				e.printStackTrace();
				System.exit(-2);
			}
		}
		
		sc.close();

	}
	
}
