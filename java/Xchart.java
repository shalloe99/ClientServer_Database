

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors; 
import java.util.stream.Stream; 
import org.knowm.xchart.*;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.style.markers.SeriesMarkers;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.demo.charts.date.DateChart04;
import org.knowm.xchart.demo.charts.bar.BarChart05;
import org.knowm.xchart.PieSeries.PieSeriesRenderStyle;
import org.knowm.xchart.style.PieStyler.AnnotationType;


import java.util.*;
import java.awt.Color;
import java.util.Random;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
/**
 * Create a Chart matrix
 *
 * @author timmolter
 */
public class Xchart {
  // public Xchart()
  // {

  // }

  public static void dateTimeGraph(String[] date, double[] data, String ytitle)
  {
    List<String> newdate = Arrays.asList(date);
    List<Date> dates = new ArrayList<>(newdate.size());
    List<Double> newdata = new ArrayList<Double>();

    // define the date format used in the String
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    for (double datas : data) {
        // sdf.parse(dateString) - convert the String into a Date accoring the pattern
        // dates.add(...) - add the Date to the list
        newdata.add(datas);
    }
    // loop over all String values
    for (String dateString : newdate) {
        // sdf.parse(dateString) - convert the String into a Date accoring the pattern
        // dates.add(...) - add the Date to the list
      try {
        dates.add(sdf.parse(dateString));
      } catch (ParseException e) {
        e.printStackTrace();
      }

        
    }

    
    XYChart chart = new XYChartBuilder().xAxisTitle("date").yAxisTitle(ytitle).width(800).height(600).build();
    XYSeries series = chart.addSeries("test", dates, newdata);
    new SwingWrapper<XYChart>(chart).displayChart();
     //chart = new CategoryChartBuilder().width(800).height(600).title("Score Histogram").xAxisTitle("Mean").yAxis
     //chart.addSeries("test 1", date, data);
  }

  public static void topTen(String[] product, double[] data, String month)
  {
    PieChart chart = new PieChartBuilder().width(800).height(600).title("top ten product").build();
 
    // Customize Chart
    chart.getStyler().setLegendVisible(false);
    chart.getStyler().setAnnotationType(AnnotationType.Label);
    chart.getStyler().setAnnotationDistance(.82);
    chart.getStyler().setPlotContentSize(.9);
    chart.getStyler().setDefaultSeriesRenderStyle(PieSeriesRenderStyle.Donut);
    for(int i = 0; i < product.length; i++)
    {
      chart.addSeries(product[i],data[i]);
    }
    new SwingWrapper<PieChart>(chart).displayChart();
    

  }

  public static void getRegionPiechart(String[] region, double[] data)
  {
    PieChart chart = new PieChartBuilder().width(800).height(600).title("test").build();

    Color[] sliceColors = new Color[region.length];
    Random rand = new Random();
    for(int i = 0; i < region.length; i++)
    {
      sliceColors[i] = new Color(rand.nextInt(256),rand.nextInt(256),rand.nextInt(256));
    }
    chart.getStyler().setSeriesColors(sliceColors);
    for(int i = 0; i < region.length;i++)
      chart.addSeries(region[i], data[i]);
    new SwingWrapper<PieChart>(chart).displayChart();
 
  }

  public static void main(String[] args) throws IOException 
  {
    double ar1[] = new double[] {1,2,3,4,5};

    String date[] = new String[] {"1998-06-13","1998-12-02","2004-04-01","2048-05-01","1948-03-04" };
    dateTimeGraph(date,ar1,"test");
    double ar2[] = new double[] {87,56,45,34,23,13};
    String product[] = new String[] {"who","what","when","where","why","how"};
    topTen(product,ar2,"july");
    // getRegionPiechart(date, ar1);
    // ExampleChart<XYChart> exampleChart = new DateChart04();
    // XYChart chart = exampleChart.getChart();
    // new SwingWrapper<XYChart>(chart).displayChart();
    //ExampleChart<CategoryChart> exampleChart = new BarChart01();
    // double ar1[] = new double[] {1,2,3,4,5};
    // double ar2[] = new double[] {2,2,3,1,2};
    // double ar3[] = new double[] {1,3,1,1,1};
    // CategoryChart chart = histChart(ar1,ar2,ar3,ar3);

    //new SwingWrapper<CategoryChart>(chart).displayChart();

  }

  //@Override
  public static CategoryChart histChart(double[] age, double[] totleSalary, double[] meanSalary, double[] medianSalary) {
 
    // Create Chart
    CategoryChart chart = new CategoryChartBuilder().width(800).height(600).title("Age vs Salary").xAxisTitle("Age").yAxisTitle("Salary").build();
 
    // Customize Chart
    chart.getStyler().setHasAnnotations(true);
    chart.getStyler().setShowTotalAnnotations(true);
    chart.getStyler().setAnnotationsPosition(1);

    chart.addSeries("total Salary", age, totleSalary);
    chart.addSeries("mean Salary", age, meanSalary);
    chart.addSeries("median Salary", age, medianSalary);
 
    return chart;
  }


  /**
   * Generates a set of random walk data
   *
   * @param numPoints
   * @return
   */
  private static double[] getRandomWalk(int numPoints) 
  {

    double[] y = new double[numPoints];
    y[0] = 0;
    for (int i = 1; i < y.length; i++) {
      y[i] = y[i - 1] + Math.random() - .5;
    }
    return y;
  }
}