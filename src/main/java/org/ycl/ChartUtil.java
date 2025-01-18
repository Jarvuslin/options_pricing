package org.ycl;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ChartUtil {
    public static void createChart(String title, String xLabel, String yLabel, List<String> categories, List<Double> values, String fileName) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < categories.size(); i++) {
            dataset.addValue(values.get(i), title, categories.get(i));
        }

        JFreeChart chart = ChartFactory.createBarChart(title, xLabel, yLabel, dataset);
        try {
            ChartUtils.saveChartAsPNG(new File(fileName), chart, 800, 600);
            System.out.println("Chart saved: " + fileName);
        } catch (IOException e) {
            System.err.println("Failed to save chart: " + e.getMessage());
        }
    }
}
