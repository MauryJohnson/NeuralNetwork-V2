package Statistics;

import java.io.Serializable;

import java.util.*;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.*;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.*;
import org.jfree.chart.plot.*; 
import org.jfree.chart.renderer.category.*;
import org.jfree.chart.urls.*; 
import org.jfree.data.category.*; 
import org.jfree.data.general.*;

/**
 * HashTable object. will be a hashtable of arraylist of objects
 * ArrayList key size will be the dimensions
 * Value of hashtable will be f(x,y,z...)
 * @author User
 */
public class HMap implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2919364168907687407L;
	
	/**
	 * Maps multiple entries to f(x) out
	 */
	public Multimap<Double[],Double> Map = ArrayListMultimap.create();

	/**
	 * Name of type of graph
	 */
	public String Name;
	
	//Function to add to HashMap this list
	
	public HMap(String s) {
		
		this.Name = s;
		
	}
	
	/**
	 * Adding multiple points for function f(x,y,z....)
	 * @param Entries
	 * @param D
	 */
	public void Add(Double[] Entries,Double D) {
		
		if(Map.get(Entries)!=null) {
			System.err.println("Entries:"+Entries+" Does exist");
		}
		else {
			Map.put(Entries, D);	
		}
		
	}
	
	//Test Graphs
	public static void main(String[] args) {
		
	ArrayList<Double> Entries = new ArrayList<Double>();
	
	Entries.add(5.0);
	Entries.add(3.322222222222222222);
	Entries.add(2.27);
		
	/*
	HMap HM = new HMap("");	
	
	HM.Map.put(Entries,31.4765);
		
	ArrayList<Double> Entries2 = new ArrayList<Double>();
	
	Entries2.add(5.0);
	Entries2.add(3.322222222222222222);
	Entries2.add(2.27);
	
	System.out.println(HM.Map.get(Entries2));
	*/
	}
	
}
