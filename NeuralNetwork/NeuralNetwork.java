package NeuralNetwork;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

import Matrix.Matrix;

/**
 * Neural Network object, contains ArrayList of Layer Objects which store date about network 
 * like input layer, hidden layer, output layer
 * Activation function for hidden/output layer
 * @author Maury Johnson
 *
 */
public class NeuralNetwork {
	
	/**
	 * Used for layers
	 */
	public ArrayList<Layer> Layers = new ArrayList<Layer>();
	
	/**
	 * Used for user inputs
	 */
	private Scanner Scanner = new Scanner(System.in);
	
	/**
	 * Used to determine type of error to evaluate
	 * 0 - Basic 1-Cross Entropy,...
	 */
	public int Error;
	
	/**
	 * Normalized error or not?
	 */
	public boolean Normalized;
	
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
		return Layers.toString();
	}
	
	/**
	 * STAGE 0
	 * Crate Neural Network with Weights, which are randomized values
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
		
	
		/*
		this.Layers.add(new Layer(
				new Matrix(Matrix.NewDoubleMatrix(
						//+1 is for bias
				new double[Layers][0]),"Input")
				,0,0));
		*/
		
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
			//Check if input dimensions match
			if(Input.length!=N.Layers.get(0).GetActivation().GetRows()||Input[0].length!=N.Layers.get(0).GetActivation().GetColumns()) {
				System.err.println("Input Dimensions don't match Activation Dimensions");
				return;
			}
			
			for(int i=0; i<N.Layers.get(0).GetActivation().GetRows();i+=1) {
				System.out.println("Inserting Input "+(i+1)+":"+Input[i][0]);
				N.Layers.get(0).GetActivation().Entries.get(i).get(0)[0]=Input[i][0];
			}
		}
				//Add Bias to Input layer
				N.Layers.get(0).GetActivation().Entries.add(Matrix.NewDoubleList(
						new double[] {
								1.0
							}
							));
			
			//Hidden/Output layer
			for(int i=0; i<N.Layers.size();i+=1) {
				if(i>=1) {
				
				//Create Activation for layers >0
				N.Layers.get(i).SetActivation(
						Matrix.Multiply(N.Layers.get(i).GetWeights(),
								N.Layers.get(i-1).GetActivation(),false,
									"Activation "+(i+1)));
				
				//Activate Layer using Activation Function... Special case for softmax. don't activate it in order to do gradient on same
				//values, but show the user the result of doing the softmax function on the activation vector
				if(N.Layers.get(i).ActivationFunction!=3)
				N.Layers.get(i).Activate();
				
				if(i!=N.Layers.size()-1) {
				//Add Bias for Activation to end of Activation ROW ??? ONLY IF HIDDEN LAYER
				//OUTPUT LAYER NO BIAS!!! ANSWER IS THERE
				N.Layers.get(i).GetActivation().Entries.add(Matrix.NewDoubleList(
						new double[] {
							1.0
						}
						));
				}
				
				}
				
				
			}
			
			System.out.println("Finished Feeding Neural Network:");
			System.out.println(N.toString());
			
			//N.Scanner.close();
		
	}
	
	/**
	 * STAGE 2
	 * Assess Error by comparing output to Expected
	 * Propagate this error back, if error!=0
	 * @param N
	 */
	public static void BackPropagate(NeuralNetwork N, double[][] ExpectedOutput) {
		//Error matrix will be expected value - Actual output
		double[][] Error = new double[N.Layers.get(N.Layers.size()-1).GetActivation().GetRows()][1];
		
		if(ExpectedOutput==null) {
		
		double E = 0.0;
		
		for(int i=0;i<N.Layers.get(N.Layers.size()-1).GetActivation().GetRows();i+=1) {
			System.out.println("Enter expected output "+(i+1)+":");
			
			E=N.Scanner.nextDouble();
			
			Error[i][0] = E-N.Layers.get(N.Layers.size()-1).GetActivation().Entries.get(i).get(0)[0];
			
			System.out.println("Error:"+(E-N.Layers.get(N.Layers.size()-1).GetActivation().Entries.get(i).get(0)[0]));
		}
		
		}
		else {	
		
			for(int i=0;i<N.Layers.get(N.Layers.size()-1).GetActivation().GetRows();i+=1) {
				System.out.println("Enter expected output "+(i+1)+":");
			
				Error[i][0] = ExpectedOutput[i][0]-N.Layers.get(N.Layers.size()-1).GetActivation().Entries.get(i).get(0)[0];
				
				System.out.println("Error:"+(ExpectedOutput[i][0]-N.Layers.get(N.Layers.size()-1).GetActivation().Entries.get(i).get(0)[0]));
			}
			
		}
		
		Matrix FirstError = new Matrix(
				Matrix.NewDoubleMatrix(Error)
				,"EM");
		N.Layers.get(N.Layers.size()-1).SetError(FirstError);
		
		System.out.println("Normalized? Enter >0 Yes, <=0 No");
		
		boolean Normalized = N.Scanner.nextInt()>0? true:false;
		
		//Loop backwards through Neural Network
		
		for(int i = N.Layers.size()-1;i>1;i-=1) {
		
			//Error of current layer
			Matrix BackError = Matrix.Error(N.Layers.get(i).GetWeights(),i==N.Layers.size()-1? FirstError:N.Layers.get(i+1).GetError(),Normalized);
					
			N.Layers.get(i-1).SetError(BackError);
		}
		
	
	}
	
	/**
	 * Given error and all weight matrices, compute the gradient for each layer
	 * Depending on type of activation, gradient will change
	 * @param N
	 */
	public static void Gradient(NeuralNetwork N) {
		
		double LearningRate = 0.0;
		
		System.out.println("Enter Learning Rate:");
		
		LearningRate = N.Scanner.nextDouble();
		
		//lr * E * (derivAct) * H^T
		for(int i=1; i<N.Layers.size();i+=1) {
			
			Matrix GradientWeight = null;
			
			Matrix ActivationTranspose;
			
			switch(N.Layers.get(i).ActivationFunction) {
			case 0:
				System.out.println("Using No Derivative, Const");
				GradientWeight = N.Layers.get(i).GetWeights();
				break;
			case 1:
				System.out.println("Using Derivative of Sigmoid");
			
				//Get transpose of activation before changes
				ActivationTranspose = N.Layers.get(i).GetActivation().Transpose("Step 4");
				
				//DSig of activation
				Matrix.DSigmoid(N.Layers.get(i).GetActivation());
				//Multiply error by dsig of activation
				N.Layers.get(i).GetActivation().MultiplyAcross(N.Layers.get(i).GetError(), "Step 2");
				//multiply learning rate
				N.Layers.get(i).GetActivation().MultiplyAcross(LearningRate,"Step 3");
				
				GradientWeight = Matrix.Multiply(N.Layers.get(i).GetActivation(),ActivationTranspose,false,"Step 5");
				
				break;
			
			case 2:
				System.out.println("Using Derivative of Relu");
				
				//get transpose of activation before changes
				ActivationTranspose = N.Layers.get(i).GetActivation().Transpose("Step 4");
				
				//DRelu of activation
				Matrix.DRelu(N.Layers.get(i).GetActivation());
				//multiple dRelu of activation by error
				N.Layers.get(i).GetActivation().MultiplyAcross(N.Layers.get(i).GetError(), "Step 2");
				//Multiply by learning rate
				N.Layers.get(i).GetActivation().MultiplyAcross(LearningRate,"Step 3");
				
				//Gradient
				GradientWeight = Matrix.Multiply(N.Layers.get(i).GetActivation(),ActivationTranspose,false,"Step 5");
				
				
				break;
				
			case 3:
				System.out.println("Using Derivative of SoftMax");
				//Transpose of activation before change
				ActivationTranspose = N.Layers.get(i).GetActivation().Transpose("Step 4");
				//Derive soft max of activation
				Matrix SMA = Matrix.DerivSoftMax(N.Layers.get(i).GetActivation());
				//Set Activation to Derived Activation NOW
				N.Layers.get(i).SetActivation(SMA);
				//Multiply by error of that
				N.Layers.get(i).GetActivation().MultiplyAcross(N.Layers.get(i).GetError(), "Step 2");
				//Multiply by learning rate
				N.Layers.get(i).GetActivation().MultiplyAcross(LearningRate,"Step 3");
				
				GradientWeight = Matrix.Multiply(N.Layers.get(i).GetActivation(),ActivationTranspose,false,"Step 5");
				
				break;
			default:
				System.err.println("No other Activation function to use");
				System.exit(-1);
				
			}
			
			N.Layers.get(i).SetGradientWeights(GradientWeight);
			
		}
		
		System.out.println("FINISHED GETTING GRADIENT WEIGHTS!");
		
		System.out.println(N.toString());

		N.Scanner.close();
	}
	
	
	public static void main(String[] args) {
		//Test input layer for neural network
		
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
		
		NeuralNetwork NN = new NeuralNetwork();
		NN.CreateNeuralNetwork(null);
		
		NeuralNetwork.FeedForward(NN,null);
		
		NeuralNetwork.BackPropagate(NN, null);
		
		NeuralNetwork.Gradient(NN);
		
		System.out.println(NN);
		
		/*
		Matrix Hidden = new Matrix(
				Matrix.NewDoubleMatrix(
				new double[][] {
					
				}	
				)
				,"Hidden Layer Matrix");
		//Hidden Layer
		Layer HiddenLayer = new Layer
		*/
	}
	
}
