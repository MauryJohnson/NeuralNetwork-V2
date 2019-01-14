package NeuralNetwork;
import java.io.Serializable;
import java.util.ArrayList;

import Matrix.Matrix;

/**
 * Object which stores information about layer
 * @author Maury Johnson
 *
 */
public class Layer implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2712269618908124544L;

	/**
	 * Weights for hidden layer connections
	 */
	private Matrix Weights;
	
	/**
	 *Weights computed after taking the Gradient
	 */
	private Matrix GradientWeights;
	
	/**
	 * Activation of this layer
	 */
	private Matrix Activation;
	
	/**
	 * Error imposed AFTER BackPropogating
	 */
	private Matrix Error;
	
	/**
	 * Contains all biases from Weight Matrix!!!!
	 */
	public ArrayList<Double> Biases = new ArrayList<Double>();
	
	/**
	 * Type of Layer Indication
	 */
	char Type;
	
	/**
	 * Whether this layer was normalized (Matrix entries divided by total row)
	 */
	boolean Normalized;
	
	/**
	 * Indicate type of activation function to use for layer
	 * 0 is none
	 * 1 is Sigmoid, 2 is Relu, 3 is SoftMax
	 */
	public int ActivationFunction;
	
	/**
	 * Bias to keep track of
	 */
	//private double Bias = 1.0;
	
	/**
	 * When creating a new layer, can only have weight matrices then Input
	 */
	public Layer(Matrix M,int Type,int ActivationType) {
		switch(Type) {
		//Special Input case
		case 0:
			this.Activation=M;
			Type = 'I';
			break;
		//Hidden Layer case, hidden layer adds weights
		case 1:
			this.Weights=M;
			Type = 'H';
			break;
		//Special Output case
		case 2:
			this.Activation=M;
			Type = 'O';
			break;
		default:
			System.err.println("Bad Layer Type Input");
			return;
		}
		//CURRENTLY ONLY have 4 types of activations, linear, nonlinear1,2,3
		if(ActivationType>3||ActivationType<0) {
			System.err.println("No Activation Function Specified");
			return;
		}
		this.ActivationFunction=ActivationType;
	}
	
	/*
	public void SetBias(double B) {
		this.Bias = B;
	}
	public double GetBias() {
		return this.Bias;
	}
	*/
	
	/**
	 * Set activation, typically done during training
	 * @param A
	 */
	public void SetActivation(Matrix A) {
		this.Activation=null;
		this.Activation=A;
	}
	
	public Matrix GetActivation() {
		return this.Activation;
	}
	
	/**
	 * Set Gradient Weights
	 * @param W
	 */
	public void SetGradientWeights(Matrix W) {
		this.GradientWeights=null;
		this.GradientWeights=W;
	}
	
	public Matrix GetGradientWeights() {
		return this.GradientWeights;
	}
	
	/**
	 * Set Weights, typicaly done during gradient
	 * @param W
	 */
	public void SetWeights(Matrix W) {
		this.Weights=null;
		this.Weights=W;
	}
	
	public Matrix GetWeights() {
		return this.Weights;
	}
	
	/**
	 * Set error, done during BackPropogation
	 * @param E
	 */
	public void SetError(Matrix E) {
		this.Error=null;
		this.Error=E;
	}
	
	public Matrix GetError() {
		return this.Error;
	}
	/**
	 * Print out Layer Type, Layer Matrix
	 */
	public String toString() {
		return this.Type+
	"\n WEIGHTS \n"+
	(Weights!=null? Weights.toString():"")+
	"\n GRADIENT WEIGHTS \n"+
	(GradientWeights!=null? GradientWeights+"":"")+
	"\n ACTIVATION \n"+
	(Activation!=null? (Activation.toString()+GetActivationType()):"")+
	"\n BIASES FOR WTXE \n" +
	this.Biases +
	" \n ERROR \n"+
	(Error!=null? Error.toString():"");
	}

	/**
	 * Gets Activation Type from ActivationFunction
	 * @return
	 */
	private String GetActivationType() {
		// TODO Auto-generated method stub
		String s = "";
		switch(this.ActivationFunction) {
		case 0:
			s = "NONE";
			break;
		case 1:
			s = "SIGMOID";
			break;
		case 2:
			s = "RELU";
			break;
		case 3:
			s = "SOFTMAX";
			break;
			
		default:
			break;
		}
		
		return s;
	}

	/**
	 * Use activation function, if set
	 */
	public void Activate() {
		switch(this.ActivationFunction) {
			case 1:
				Matrix.Sigmoid(Activation);
				break;
			case 2:
				Matrix.Relu(Activation);
				break;
			case 3:
				Matrix.SoftMax(Activation);
				break;
			default:
				return;
		}
	}

	/**
	 * Activate all biases of current layer
	 */
	public void ActivateB() {
		// TODO Auto-generated method stub
		switch(this.ActivationFunction) {
		case 1:
			Matrix.Sigmoid(Biases);
			break;
		case 2:
			Matrix.Relu(Biases);
			break;
		case 3:
			Matrix.SoftMax(Biases);
			break;
		default:
			return;
	}
	}
	
	/**
	 * Sum up all biases of current layer
	 * @return
	 */
	public double BSum() {
		// TODO Auto-generated method stub
		double d = 0.0;
		for(int i=0; i<Biases.size();i+=1)
			d+=Biases.get(i);
		return d;
		
	}
	
}
