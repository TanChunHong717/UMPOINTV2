package my.edu.um.umpoint.modules.utils;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import my.edu.um.umpoint.common.constant.Constant;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class PdfGenerator {

    public static void generateDashboardReport(Map<String, Object> data, String ai) throws Exception {
        String startDate = data.get(Constant.START_TIME).toString().split("T")[0];
        String endDate = data.get(Constant.END_TIME).toString().split("T")[0];

        PdfWriter writer = new PdfWriter(new FileOutputStream("C:\\Users\\chunhong\\IdeaProjects\\UMPOINTV2\\umpoint\\umpoint-application\\src\\main\\resources\\public\\report.pdf"));
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        document.add(
                new Paragraph("Data Analytics Report (" + startDate + " To: " + endDate)
                        .setBold()
                        .setUnderline()
                        .setFontSize(14)
        );

        List<Double> spaceData = (List<Double>) data.get("spaceData");
        List<Double> serviceData = (List<Double>) data.get("serviceData");
        List<Double> accommodationData = (List<Double>) data.get("accommodationData");
        BufferedImage chartImage = generateLineChart(spaceData, serviceData, accommodationData, startDate, endDate);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(chartImage, "png", baos);
        byte[] imageBytes = baos.toByteArray();

        Image chart = new Image(ImageDataFactory.create(imageBytes));
        document.add(chart);

        Table table = new Table(2);
        table.addCell("Metric");
        table.addCell("Value");

        table.addCell("Booking Count");
        table.addCell(data.get("bookingCount").toString());

        table.addCell("Booking Amount");
        table.addCell(data.get("bookingAmount").toString());

        table.addCell("Total Spaces");
        table.addCell(data.get("spaceNumber").toString());

        table.addCell("Total Services");
        table.addCell(data.get("serviceNumber").toString());

        table.addCell("Total Accommodations");
        table.addCell(data.get("accommodationNumber").toString());

        table.addCell("Space Booking Amount");
        table.addCell(data.get("spaceBookingAmount").toString());

        table.addCell("Service Booking Amount");
        table.addCell(data.get("serviceBookingAmount").toString());

        table.addCell("Accommodation Booking Amount");
        table.addCell(data.get("accommodationBookingAmount").toString());

        document.add(table);

        document.add(new Paragraph(ai));
        document.close();
    }

    private static BufferedImage generateLineChart(List<Double> spaceData, List<Double> serviceData, List<Double> accommodationData, String startDate, String endDate) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(startDate.split("T")[0], formatter);

        for (int i = 0; i < spaceData.size(); i++) {
            LocalDate currentDate = start.plusDays(i);
            String dateLabel = currentDate.format(formatter);

            dataset.addValue(spaceData.get(i), "Spaces", dateLabel);
            dataset.addValue(serviceData.get(i), "Services", dateLabel);
            dataset.addValue(accommodationData.get(i), "Accommodations", dateLabel);
        }

        JFreeChart lineChart = ChartFactory.createLineChart(
                "Daily Booking Amounts (" + startDate + " to " + endDate + ")",
                "Date",
                "Amount",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        return lineChart.createBufferedImage(800, 600);
    }
}
