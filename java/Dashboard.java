

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
public class Dashboard {

  public String dateTime[];
  public double dateRelatedData[];
  public String donut[];
  public double donutData[];
  public double histgramX[];
  public double histgramYtotal[];
  public double histgramYmean[];
  public double histgramYmedian[];
  public String region[];
  public double regionData[];


  public Dashboard()
  {


  }

  /**
  * draw a date and time line graph
  * @param  date the date array input
  * @param  data data array input
  * @param  ytitle the title of the graph
  * @return a XYChart
  *
  */
  public static XYChart dateTimeGraph(double[] date, double[] data, String ytitle)
  {
    // List<String> newdate = Arrays.asList(date);
    // List<Date> dates = new ArrayList<>(newdate.size());
    // List<Double> newdata = new ArrayList<Double>();

    // // define the date format used in the String
    // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    // for (double datas : data) {
    //     // sdf.parse(dateString) - convert the String into a Date accoring the pattern
    //     // dates.add(...) - add the Date to the list
    //     newdata.add(datas);
    // }
    // // loop over all String values
    // for (String dateString : newdate) {
    //   try {
    //     dates.add(sdf.parse(dateString));
    //   } catch (ParseException e) {
    //     e.printStackTrace();
    //   }

        
    // }

    XYChart chart = new XYChartBuilder().xAxisTitle("date").yAxisTitle("sale").title(ytitle).width(800).height(600).build();
    chart.addSeries(ytitle, date, data);
    chart.getStyler().setXAxisTickMarkSpacingHint(200);
    //NumberAxis(double lowerBound, double upperBound, double tickUnit)
    return chart;
   // new SwingWrapper<XYChart>(chart).displayChart();
     //chart = new CategoryChartBuilder().width(800).height(600).title("Score Histogram").xAxisTitle("Mean").yAxis
     //chart.addSeries("test 1", date, data);
  } // end of dataTimeGraph


  /**
  * pie chart of product
  * @param  date the date array input
  * @param  data data array input
  * @param  ytitle the title of the graph
  * @return a piechart
  *
  */
  public static PieChart topTen(String[] product, double[] data, String month)
  {
    PieChart chart = new PieChartBuilder().width(800).height(600).title(month).build();
 
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
    return chart;
    //new SwingWrapper<PieChart>(chart).displayChart();
  }//end of topTen

  /**
  * pie chart of product
  * @param  region the region of the data
  * @param  data data array input
  * @return a pichart
  *
  */
  public static PieChart getRegionPiechart(String[] region, double[] data)
  {
    PieChart chart = new PieChartBuilder().width(800).height(600).title("sale by country").build();

    Color[] sliceColors = new Color[region.length];
    Random rand = new Random();
    for(int i = 0; i < region.length; i++)
    {
      sliceColors[i] = new Color(rand.nextInt(256),rand.nextInt(256),rand.nextInt(256));
    }
    chart.getStyler().setSeriesColors(sliceColors);
    for(int i = 0; i < region.length;i++)
      chart.addSeries(region[i], data[i]);
    return chart;
    //new SwingWrapper<PieChart>(chart).displayChart();
 
  }// end of getRegionPiechart

  public static void main(String[] args) throws IOException 
  {

  }

  /**
  * pie chart of product
  * @param  year the year of the data
  * @param  country1 data array input
  * @param  country2 data array input
  * @param  country3 data array input
  * @param  country4 data array input
  * @param  country5 data array input
  * @param  country6 data array input
  * @param  titlee the title of the histgram
  * @return a histogram of all the countries
  *
  */
  public static CategoryChart histChart(String[] year, double[] country1, double[] country2, double[] country3, double[] country4, double[] country5, double[] country6, String titlee) {
 
    // Create Chart
    CategoryChart chart = new CategoryChartBuilder().width(800).height(600).title(titlee).xAxisTitle("year").yAxisTitle("country").build();
 
    // Customize Chart
    // chart.getStyler().setHasAnnotations(true);
    // chart.getStyler().setShowTotalAnnotations(true);
    // chart.getStyler().setAnnotationsPosition(1);
    List<Double> newcountry1 = new ArrayList<Double>();
    List<Double> newcountry2 = new ArrayList<Double>();
    List<Double> newcountry3 = new ArrayList<Double>();
    List<Double> newcountry4 = new ArrayList<Double>();
    List<Double> newcountry5 = new ArrayList<Double>();
    List<Double> newcountry6 = new ArrayList<Double>();

    for (double datas : country1) {
        // sdf.parse(dateString) - convert the String into a Date accoring the pattern
        // dates.add(...) - add the Date to the list
        newcountry1.add(datas);
    }
    for (double datas : country2) {
        // sdf.parse(dateString) - convert the String into a Date accoring the pattern
        // dates.add(...) - add the Date to the list
        newcountry2.add(datas);
    }
    for (double datas : country3) {
        // sdf.parse(dateString) - convert the String into a Date accoring the pattern
        // dates.add(...) - add the Date to the list
        newcountry3.add(datas);
    }
    for (double datas : country4) {
        // sdf.parse(dateString) - convert the String into a Date accoring the pattern
        // dates.add(...) - add the Date to the list
        newcountry4.add(datas);
    }
    for (double datas : country5) {
        // sdf.parse(dateString) - convert the String into a Date accoring the pattern
        // dates.add(...) - add the Date to the list
        newcountry5.add(datas);
    }
    for (double datas : country6) {
        // sdf.parse(dateString) - convert the String into a Date accoring the pattern
        // dates.add(...) - add the Date to the list
        newcountry6.add(datas);
    }

    chart.addSeries("AU", new ArrayList<String>(Arrays.asList(year)), newcountry1);
    chart.addSeries("CA", new ArrayList<String>(Arrays.asList(year)), newcountry2);
    chart.addSeries("DE", new ArrayList<String>(Arrays.asList(year)), newcountry3);
    chart.addSeries("FR", new ArrayList<String>(Arrays.asList(year)), newcountry4);
    chart.addSeries("GB", new ArrayList<String>(Arrays.asList(year)), newcountry5);
    chart.addSeries("US", new ArrayList<String>(Arrays.asList(year)), newcountry6);
 
    return chart;
  }// end of the histChart

  /**
  * pie chart of product
  * @param  info the different category of the chart
  * @param  data data array input
  * @param  titlee the title of the histgram
  * @return a histogram of all the countries
  *
  */
  public static CategoryChart histChart(String[] infor, double[] data, String titlee) {
 
    // Create Chart
    CategoryChart chart = new CategoryChartBuilder().width(800).height(600).title(titlee).xAxisTitle("Age").yAxisTitle("Salary").build();
 
    // Customize Chart
    chart.getStyler().setHasAnnotations(true);
    chart.getStyler().setShowTotalAnnotations(true);
    chart.getStyler().setAnnotationsPosition(1);

    List<Double> newdata = new ArrayList<Double>();


    for (double datas : data) {
        // sdf.parse(dateString) - convert the String into a Date accoring the pattern
        // dates.add(...) - add the Date to the list
        newdata.add(datas);
    }
    // loop over all String values
    chart.addSeries(titlee, new ArrayList<String>(Arrays.asList(infor)),newdata);
    return chart;
  }// end of the histChart



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