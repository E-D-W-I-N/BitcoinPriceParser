import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Parser {
    public static void main(String[] args) {

        // URL for parsing
        final String url = "https://cryptowat.ch/assets/btc";

        // Create and configuring the window
        JFrame window = new JFrame();
        window.setTitle("Bitcoin Price Parser");
        window.setSize(600, 400);
        window.setLayout(new BorderLayout());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the linear chart
        final XYSeries series = new XYSeries("Bitcoin");
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart("Bitcoin Price Parser", "Time",
                "Price (USD)", dataset);
        window.add(new ChartPanel(chart), BorderLayout.CENTER);

        // Create the task to repeatedly parse Bitcoin price
        TimerTask timerTask = new TimerTask() {
            int x = 0;

            public void run() {
                try {
                    Document html = Jsoup.connect(url).get(); // Getting HTML page
                    for (Element row : html.getElementsByClass("_1mXQHhVzjkPfKzIHVhIxs6")) {
                        // If the required string is not empty printing current price and fill dataset for linear chart
                        if (!row.text().isEmpty()) {
                            chart.getXYPlot().getRangeAxis().setRange(Double.parseDouble(row.text()) - 50L,
                                    Double.parseDouble(row.text()) + 50L);
                            System.out.println("Current Bitcoin Price is - " + row.text() + " USD");
                            series.add(x++, Double.parseDouble(row.text()));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        // Create timer for TimerTask
        Timer timer = new Timer("Timer");

        // Parsing delay
        long delay = 0L;
        // Period of parsing
        long period = 1000L;
        timer.scheduleAtFixedRate(timerTask, delay, period);

        // Show the window
        window.setVisible(true);
    }
}
