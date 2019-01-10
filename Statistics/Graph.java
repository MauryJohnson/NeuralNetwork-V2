package Statistics;

import java.util.Hashtable;

//import org.jfree.*;

/**
 * Will be used to show bar graphs, loss, charts, etc
 * @author Maury Johnson
 *
 */
public class Graph {
	
	/**
	 * Hashtable
	 * first entry is x, second is y
	 */
	private Hashtable<double[],double[]> HT= new Hashtable<double[],double[]>();
	
	/**
	 * Add x,y,....., coordinates to HT
	 * @param D
	 */
	public void Push(double D,double D2) {
		double[] DD = new double[] {D};
		double[] DD2 = new double[] {D2};
		if(HT.get(new double[] {D})==null) {
			HT.put(DD,DD2);
		}
		else {
			System.err.println("Duplicate values:"+D+"-"+D2);
		}
	}
	
	/**
	 * Print a bar graph given HT and a title
	 */
	public void PrintGraph(String title,String X, String Y) {
		
		
		
	}
	
	public static void main(String[] args) {
		
		
		
	}
	
}