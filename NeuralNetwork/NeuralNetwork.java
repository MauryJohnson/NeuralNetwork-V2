package NeuralNetwork;

import java.util.ArrayList;
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
	
	public ArrayList<Layer> Layers = new ArrayList<Layer>();
	
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
	 */
	public void CreateNeuralNetwork() {
		int Layers = -1;
		int LayerSize=  -1;
		int ActivationFunction=-1;
		
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Enter Number of Layers:");
		Layers=scanner.nextInt();
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
			
			LayerSize=scanner.nextInt();
			
			if(LayerSize<1) {
				System.err.println("Impossible Layer Size");
				return;
			}
			
			if(i>0) {
				System.out.println("Enter Activation Function type 0-NONE 1-Sigmoid 2-Relu 3-SoftMax");
				ActivationFunction = scanner.nextInt();
			
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
						,1,1)));
				
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
		
		System.out.println("Finished Creating Neural Network");
		System.out.println(this.toString());
	}
	
	/**
	 * STAGE 1
	 * Send input into NN, feed it forward and set activations
	 * @param N
	 */
	public static void FeedForward(NeuralNetwork N) {
		
		
		
		
	}
	
	/**
	 * STAGE 2
	 * Assess Error by comparing output to Expected
	 * Propagate this error back, if error!=0
	 * @param N
	 */
	public static void BackPropagate(NeuralNetwork N,Matrix ExpectedOutput) {
		
		
		
		
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
		NN.CreateNeuralNetwork();
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
