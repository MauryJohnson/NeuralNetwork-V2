package NeuralNetwork;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import Matrix.Matrix;
import MatrixFileStream.MatrixFileStream;

public class NNFileStream extends MatrixFileStream{

	//FileInputStream F;
	
	//char Delimiter;
	
	//String result;
	
	//int Position = 0;
	
	public NNFileStream(FileInputStream F,char Delimiter) {
		super(F,Delimiter);
		
		
		//this.F=F;
		//this.Delimiter=Delimiter;
	
		/*
		InputStreamReader I = new InputStreamReader(F);
		this.result = new BufferedReader(I)
				  .lines().collect(Collectors.joining("\n"));
		
		if(result==null) {
			System.err.println("No File input");
		}
		*/
		/*
		try {
			F.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
	
	static public NeuralNetwork ParseLayers(String FileName,char Delim) {

		NNFileStream NFS = null;
		
		try {
			NFS = new NNFileStream(new FileInputStream(FileName),Delim);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-3);
		}
		
		NeuralNetwork NN = new NeuralNetwork();
		
		String str;
		int i=0;
		int p;
		
		do {
		str = NFS.NextString();	
		if(str!=null) {
			System.out.println("Indicator:"+str);
		if(i==0) {
		NN.Name=str;	
		}
		else if(i==1) {
			p = Integer.parseInt(str);
			if(p<0||p>1) {
				System.err.println("Bad Indicator parsed");
				System.exit(-1);
			}
		NN.Error=p;
		}
		else if(i==2) {
		p = Integer.parseInt(str);
			if(p<0||p>1) {
				System.err.println("Bad Indicator parsed");
				System.exit(-1);
			}
		NN.Normalized=(p==1? true:false);
		}
		str="";
		}
		i+=1;
		}while(i<=2);
		
		NFS.NextString();
		NFS.NextString();
		
		NFS.ReadFile();
		
		NN.AddLayer(new Layer(NFS.M,0,0));
		
		NFS.NextString();
		
		str = "";
		String str2 = "";
		int k=0;
		boolean brk= false;
		while(true) {
			k=0;
			brk=false;
			while(k<1) {
			str=NFS.NextString();
			if(str=="") {
			System.out.println("Break "+(k+1));
				brk=true;
				break;
			}
			System.out.println("Made it "+(k+1));
			k+=1;
			}
			if(brk)
			break;
		//str = NFS.NextString();
		k=0;
		System.out.println("Activation:"+str);
		
		NFS.ReadFile();
		
		Matrix B = NFS.M;
		
		str2 = "";
		while(k<str.length()) {
			if(Character.isDigit(str.charAt(k))) {
				str2+=str.charAt(k);
			}
			k+=1;
		}
		System.out.println("Activation2:"+str2);
		
		Layer L = new Layer(B,0,Integer.parseInt(str2));
		
		NN.AddLayer(L);
		//break;
		str=NFS.NextString();
		if(str=="") {
			System.out.println("Break 3");
			break;
		}
			System.out.println("Made it 3:"+str);
		
			NFS.ReadFile();
			Matrix W = NFS.M;
		
			L.SetWeights(W);
		
			str=NFS.NextString();
			if(str=="") {
				System.out.println("Break 4");
				break;
			}
				System.out.println("Made it 4:"+str);
			NFS.ReadFile();
			Matrix E = NFS.M;
			L.SetError(E);
			
			str=NFS.NextString();
			if(str=="") {
				System.out.println("Break 5");
				break;
			}
			System.out.println("Made it 5:"+str);
			
			NFS.ReadFile();
			
			Matrix GW = NFS.M;
			L.SetGradientWeights(GW);
			
			str = "";
			
		}
		
		
		System.out.println("Neural Network Parsed:\n"+NN);
		
		NN.Delimiter=Delim;
		
		return NN;
	}
	
	public static void main(String[] args) {
		
		NeuralNetwork NN = NNFileStream.ParseLayers("./Model/Model1", '_');
		
		NN.SaveNetwork();
		
	}
	
}
