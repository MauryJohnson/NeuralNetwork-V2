package Statistics;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.awt.BasicStroke; 

import org.jfree.chart.ChartPanel; 
import org.jfree.chart.JFreeChart; 
import org.jfree.data.xy.XYDataset; 
import org.jfree.data.xy.XYSeries; 
import org.jfree.ui.ApplicationFrame; 
import org.jfree.ui.RefineryUtilities; 
import org.jfree.chart.plot.XYPlot; 
import org.jfree.chart.ChartFactory; 
import org.jfree.chart.plot.PlotOrientation; 
import org.jfree.data.xy.XYSeriesCollection; 
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

public class LineChart extends ApplicationFrame {

   /**
	 * 
	 */
	private static final long serialVersionUID = -3468178876134318249L;

public LineChart(String[] ATNXY,HMap Map) {
	super(ATNXY[0]);
	
	String[] NXY = new String[3];
	 for(int i=2;i<5;i+=1)
		 NXY[i-2]=ATNXY[i];
      
      JFreeChart xylineChart = ChartFactory.createXYLineChart(
         ATNXY[1] ,
         ATNXY[3] ,
         ATNXY[4] ,
         createDataset(ATNXY,Map) ,
         PlotOrientation.VERTICAL ,
         true , true , false);
         
      ChartPanel chartPanel = new ChartPanel( xylineChart );
      //chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
      final XYPlot plot = xylineChart.getXYPlot( );
      
      XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
      
      //Random R = new Random();
      //int C = R.nextInt(10);
      
      renderer.setSeriesPaint( 0 , Graph.RandColor() );
      
      //renderer.setSeriesPaint( 1 , Color.GREEN );
      //renderer.setSeriesPaint( 2 , Color.YELLOW );
      
      renderer.setSeriesStroke( 0 , new BasicStroke( 4.0f ) );
      
      //renderer.setSeriesStroke( 1 , new BasicStroke( 3.0f ) );
      //renderer.setSeriesStroke( 2 , new BasicStroke( 2.0f ) );
      plot.setRenderer( renderer ); 
      setContentPane( chartPanel ); 
   }
   
/**
 * Given name, x label, y label, Map
 * @param Name
 * @param Map
 * @return
 */
   public XYDataset createDataset(String[] NXY,HMap Map) {
      
	  XYSeries D = new XYSeries( NXY[0] );    
      
      Map.Map.entries().stream().forEach(A->{
    	  if(A.getKey().length==1)
    	  D.add(A.getKey()[0],A.getValue());
    	  else
    	  System.err.println("Must have X-Y Pair values, size of AList>0");
      });
	  
      final XYSeriesCollection dataset = new XYSeriesCollection( );          
      dataset.addSeries( D );          
      return dataset;
   }
   
   public static void CreateChart(String[] ABCDE,HMap Map) {
	   LineChart chart = new LineChart(ABCDE,Map);
				  
	   chart.pack( );          
	   RefineryUtilities.centerFrameOnScreen( chart );          
	   chart.setVisible( true ); 
   }

   public static void main( String[ ] args ) {
      HMap Map = new HMap("A");
      
      /*
      ArrayList<Double> A = new ArrayList<Double>();
      A.add(0.1);
      Map.Map.put(A,0.5);
      
	  LineChart chart = new LineChart(new String[] {
      "B","B","C","D","E"		  
      
	  },Map);
	  
      chart.pack( );          
      RefineryUtilities.centerFrameOnScreen( chart );          
      chart.setVisible( true ); 
       */
   }
}
