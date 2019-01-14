package Statistics;

import java.awt.Color;
import java.awt.Paint;
import java.util.Hashtable;
import java.util.Random;

import org.jfree.*;

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
	private Hashtable<double[],double[]> HT = new Hashtable<double[],double[]>();
	
	/**
	 * Label name for graph
	 */
	public String Name;
	
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

	public static Paint RandColor() {
		// TODO Auto-generated method stub
		switch(new Random().nextInt(10)) {
			case 0:
				return Color.BLACK;
			case 1:
				return Color.BLUE;
			case 2:
				return Color.CYAN;
			case 3:
				return Color.DARK_GRAY;
			case 4:
				return Color.GRAY;
			case 5:
				return Color.GREEN;
			case 6:
				return Color.YELLOW;
			case 7:
				return Color.RED;
			case 8:
				return Color.ORANGE;
			case 9:
				return Color.MAGENTA;
			default:
				return Color.RED;
		}
	}

	
}