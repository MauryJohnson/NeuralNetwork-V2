package NeuralNetwork;
import java.io.Serializable;

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
	 * Activation of this layer
	 */
	private Matrix Activation;
	
	/**
	 * Error imposed AFTER BackPropogating
	 */
	private Matrix Error;
	
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
	 * When creating a new layer, can only have weight matrices then Input
	 */
	public Layer(Matrix M,int Type,int ActivationType) {
		switch(Type) {
		//Special Input case
		case 0:
			this.Activation=M;
			Type = 'I';
			break;
		//Hiden Layer case, hidden layer adds weights
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
	
	/**
	 * Set activation, typically done during training
	 * @param A
	 */
	public void SetActivation(Matrix A) {
		this.Activation=A;
	}
	
	public Matrix GetActivation() {
		return this.Activation;
	}
	
	/**
	 * Set Weights, typicaly done during gradient
	 * @param W
	 */
	public void SetWeights(Matrix W) {
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
		this.Error=E;
	}
	
	public Matrix GetError() {
		return this.Error;
	}
	/**
	 * Print out Layer Type, Layer Matrix
	 */
	public String toString() {
		return this.Type+"\n WEIGHTS \n"+(Weights!=null? Weights.toString():"")+"ACTIVATION \n"+(Activation!=null? Activation.toString():"")+"ERROR \n"+(Error!=null? Error.toString():"");
	}
}
