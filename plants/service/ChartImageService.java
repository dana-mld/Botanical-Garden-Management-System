package com.example.demo.service;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.Base64;
import java.util.List;

@Service
public class ChartImageService {

    public String generateTypeChartBase64(Map<String, Object> distribution) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        Collection<String> labelsCollection = (Collection<String>) distribution.get("labels");
        Collection<Integer> dataCollection = (Collection<Integer>) distribution.get("data");

        List<String> labels = new ArrayList<>(labelsCollection);
        List<Integer> data = new ArrayList<>(dataCollection);

        for (int i = 0; i < labels.size(); i++) {
            dataset.addValue(data.get(i), "Plants", labels.get(i));
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Distribution of plants by type",
                "Plant type",
                "Number of plants",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        chart.setBackgroundPaint(Color.WHITE);
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(245, 245, 245));
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setRangeGridlinePaint(Color.GRAY);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(102, 126, 234));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ChartUtils.writeChartAsPNG(out, chart, 600, 400);
        return Base64.getEncoder().encodeToString(out.toByteArray());
    }

    public String generateCarnivoreChartBase64(Map<String, Object> carnivore) throws IOException {
        DefaultPieDataset dataset = new DefaultPieDataset();

        Collection<Integer> dataCollection = (Collection<Integer>) carnivore.get("data");
        List<Integer> data = new ArrayList<>(dataCollection);

        dataset.setValue("Carnivorous", data.get(0));
        dataset.setValue("Non-carnivorous", data.get(1));

        JFreeChart chart = ChartFactory.createPieChart(
                "Carnivorous vs Non-carnivorous plants",
                dataset,
                true,
                true,
                false
        );

        chart.setBackgroundPaint(Color.WHITE);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(new Color(245, 245, 245));
        plot.setSectionPaint("Carnivorous", new Color(255, 99, 132));
        plot.setSectionPaint("Non-carnivorous", new Color(54, 162, 235));
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1} ({2})"));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ChartUtils.writeChartAsPNG(out, chart, 500, 400);
        return Base64.getEncoder().encodeToString(out.toByteArray());
    }

    public String generateTopSpeciesChartBase64(Map<String, Object> topSpecies) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        Collection<String> labelsCollection = (Collection<String>) topSpecies.get("labels");
        Collection<Integer> dataCollection = (Collection<Integer>) topSpecies.get("data");

        List<String> labels = new ArrayList<>(labelsCollection);
        List<Integer> data = new ArrayList<>(dataCollection);

        for (int i = 0; i < labels.size(); i++) {
            dataset.addValue(data.get(i), "Plants", labels.get(i));
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Top species by number of plants",
                "Number of plants",
                "Species",
                dataset,
                PlotOrientation.HORIZONTAL,
                true,
                true,
                false
        );

        chart.setBackgroundPaint(Color.WHITE);
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(245, 245, 245));
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setRangeGridlinePaint(Color.GRAY);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(75, 192, 192));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ChartUtils.writeChartAsPNG(out, chart, 600, 400);
        return Base64.getEncoder().encodeToString(out.toByteArray());
    }
}