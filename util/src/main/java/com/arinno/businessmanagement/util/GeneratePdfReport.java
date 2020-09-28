package com.arinno.businessmanagement.util;

import com.arinno.businessmanagement.model.Invoice;
import com.arinno.businessmanagement.model.Order;
import com.arinno.businessmanagement.model.OrderItem;
import com.itextpdf.text.*;

import com.itextpdf.text.pdf.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Stream;

@Service
public class GeneratePdfReport implements IGeneratePdfReport {


    private static final Logger logger = LoggerFactory.getLogger(GeneratePdfReport.class);

    private BaseFont bfBold;
    private BaseFont bf;
    private int pageNumber = 0;

    @Override
    public void generateInvoice(Invoice invoice, OutputStream outputStream ) throws Exception {

        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();

        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase("LEFT--------L"), 50, 800, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase("RIGHT------R"), 300, 800,0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase("LEFT--------1"), 50, 750, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase("RIGHT------2"), 300, 750,0);
        document.close();


    }

    @Override
    public void generateOrder(Order order, OutputStream outputStream) throws Exception {
        System.out.println("generateOrder");
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();

        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase("LEFT--------L"), 50, 800, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase("RIGHT------R"), 300, 800,0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase("LEFT--------1"), 50, 750, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase("RIGHT------2"), 300, 750,0);
        document.close();
    }

    @Override
    public ByteArrayInputStream generateOrder(Order order) throws DocumentException {
        Document doc = new Document();
        PdfWriter docWriter = null;
        initializeFonts();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        docWriter = PdfWriter.getInstance(doc, out);
        doc.open();
        PdfContentByte cb = docWriter.getDirectContent();

        boolean beginPage = true;
        int y = 0;

        for (OrderItem orderItem : order.getItems()){
            if(beginPage){
                beginPage = false;
                generateLayout(doc, cb);
                generateHeader(order, cb);
                y = 515;
            }
            generateDetail(orderItem, cb, y);
            y = y - 15;
            if(y < 145){
                printPageNumber(cb);
                doc.newPage();
                beginPage = true;
            }
        }

        generateFooter(cb);

        printPageNumber(cb);
        doc.close();

        return new ByteArrayInputStream(out.toByteArray());

    }

    private void generateLayout(Document doc, PdfContentByte cb) {
        try {

            cb.setLineWidth(1f);

            // Invoice Header box layout

            cb.rectangle(420,600,150,60);
            cb.moveTo(420,620);
            cb.lineTo(570,620);
            cb.moveTo(420,640);
            cb.lineTo(570,640);
            cb.moveTo(480,600);
            cb.lineTo(480,660);
            cb.stroke();

            // Invoice Header box Text Headings
            createHeadings(cb,422,643,"Account No.");
            createHeadings(cb,422,623,"Order No.");
            createHeadings(cb,422,603,"Order Date");

            // Invoice Detail box layout
            cb.rectangle(20,150,550,400);
            cb.moveTo(20,530);
            cb.lineTo(570,530);

            cb.moveTo(90,150);
            cb.lineTo(90,550);

            cb.moveTo(138,150);//150
            cb.lineTo(138,550);//150

            cb.moveTo(380,150);
            cb.lineTo(380,550);

            cb.moveTo(445,150);
            cb.lineTo(445,550);

            cb.moveTo(500,150);
            cb.lineTo(500,550);
            cb.stroke();

            // Invoice Detail box Text Headings
            createHeadings(cb,22,533,"Ref.");
            createHeadings(cb,92,533,"Lote");
            createHeadings(cb,142,533,"Concept");
            createHeadings(cb,380,533,"Price");
            createHeadings(cb,446,533,"Qty");
            createHeadings(cb,502,533,"Total");


            //add the images
            Image companyLogo = Image.getInstance("logo.png");
            companyLogo.setAbsolutePosition(25,700);
            companyLogo.scalePercent(15);
            doc.add(companyLogo);

        }

        catch (DocumentException dex){
            dex.printStackTrace();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void generateHeader(Order order, PdfContentByte cb)  {

        try {

            createHeadings(cb,400,750, order.getCompany().getName());
            createHeadings(cb,400,735,"Address Line 1");
            createHeadings(cb,400,720,"Address Line 2");
            createHeadings(cb,400,705,"City, State - ZipCode");
            createHeadings(cb,400,690,"Country");

            createHeadings(cb,50,650, order.getCustomer().getName());
            createHeadings(cb,50,635, order.getCustomer().getAddress());
            createHeadings(cb,50,620, order.getCustomer().getTown() +", "+
                                                    order.getCustomer().getStateProvince() +" - "+
                                                    order.getCustomer().getPostalCode());
            createHeadings(cb,50,605,"Country");

            createHeadings(cb,482,643, order.getCustomer().getCode());
            createHeadings(cb,482,623, String.valueOf(order.getNumber()));
            createHeadings(cb,482,603, String.valueOf(order.getCreateAt()));

        }

        catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private void generateDetail(OrderItem orderItem, PdfContentByte cb, int y)  {
        DecimalFormat df = new DecimalFormat("0.00");

        createContent(cb,83,y, orderItem.getProduct().getCode(),PdfContentByte.ALIGN_RIGHT);
//        createContent(cb,87,y, "ITEM" + String.valueOf(1),PdfContentByte.ALIGN_LEFT);
        createContent(cb,142,y, orderItem.getProduct().getDescription(),PdfContentByte.ALIGN_LEFT);
        createContent(cb,440,y, df.format(orderItem.getPrice()),PdfContentByte.ALIGN_RIGHT);
        createContent(cb,498,y, String.valueOf(orderItem.getQuantity()),PdfContentByte.ALIGN_RIGHT);
        createContent(cb,568,y, df.format(orderItem.getAmount()),PdfContentByte.ALIGN_RIGHT);

    }

    private void createHeadings(PdfContentByte cb, float x, float y, String text){

        cb.beginText();
        cb.setFontAndSize(bfBold, 8);
        cb.setTextMatrix(x,y);
        cb.showText(text.trim());
        cb.endText();

    }

    private void generateFooter(PdfContentByte cb){
        createContent(cb,150,100, "Total Neto EUR",PdfContentByte.ALIGN_RIGHT);
        createContent(cb,225,100, "IVA",PdfContentByte.ALIGN_LEFT);
        createHeadings(cb,350,100, "Total EUR");

        cb.moveTo(20,90);
        cb.lineTo(570,90);
        cb.stroke();

        createContent(cb,150,80, "XX.XX",PdfContentByte.ALIGN_RIGHT);
        createContent(cb,225,80, "XX.XX",PdfContentByte.ALIGN_LEFT);
        createHeadings(cb,350,80, "XX.XX");

    }

    private void printPageNumber(PdfContentByte cb){


        cb.beginText();
        cb.setFontAndSize(bfBold, 8);
        cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Page No. " + (pageNumber+1), 570 , 25, 0);
        cb.endText();

        pageNumber++;

    }

    private void createContent(PdfContentByte cb, float x, float y, String text, int align){
        cb.beginText();
        cb.setFontAndSize(bf, 8);
        cb.showTextAligned(align, text.trim(), x , y, 0);
        cb.endText();

    }

    private void initializeFonts(){


        try {
            bfBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



/*
    @Override
    public ByteArrayInputStream generateOrder2(Order order) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try{
            PdfWriter.getInstance(document, out);
            document.open();

            com.itextpdf.text.Font font = FontFactory.getFont(FontFactory.COURIER, 14, BaseColor.BLACK);
            Paragraph para = new Paragraph(order.getCompany().getName(), font);
            para.setAlignment(Element.ALIGN_LEFT);
            document.add(para);
            para = new Paragraph(order.getCompany().getAddress(), font);
            para.setAlignment(Element.ALIGN_LEFT);
            document.add(para);
            para = new Paragraph(order.getCompany().getPhone(), font);
            para.setAlignment(Element.ALIGN_LEFT);
            document.add(para);
            para = new Paragraph(order.getCompany().getFax(), font);
            para.setAlignment(Element.ALIGN_LEFT);
            document.add(para);
            para = new Paragraph(order.getCompany().getWeb(), font);
            para.setAlignment(Element.ALIGN_LEFT);
            document.add(para);


            document.add(Chunk.NEWLINE);
            PdfPTable table = new PdfPTable(2);
            Stream.of("Title", "Description").forEach(headerTitle -> {
                PdfPCell header = new PdfPCell();
                com.itextpdf.text.Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
                header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                header.setHorizontalAlignment(Element.ALIGN_CENTER);
                header.setBorderWidth(1);
                header.setPhrase(new Phrase(headerTitle, headFont));
                table.addCell(header);
            });


            document.add(table);
            document.close();

        } catch (DocumentException e){
            e.printStackTrace();
        }
        return new ByteArrayInputStream(out.toByteArray());

    }
*/

    public void writePdf(OutputStream outputStream) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();
        Paragraph paragraph = new Paragraph();
        paragraph.add(new Chunk("hello!"));
        document.add(paragraph);
        document.close();
    }

    @Override
    public ByteArrayInputStream ordersPDFReport(List<Order> orders) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try{
            PdfWriter.getInstance(document, out);
            document.open();;

            com.itextpdf.text.Font font = FontFactory.getFont(FontFactory.COURIER, 14, BaseColor.BLACK);
            Paragraph para = new Paragraph("Orders", font);
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);
            document.add(Chunk.NEWLINE);
            PdfPTable table = new PdfPTable(2);
            Stream.of("Title", "Description").forEach(headerTitle -> {
                PdfPCell header = new PdfPCell();
                com.itextpdf.text.Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
                header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                header.setHorizontalAlignment(Element.ALIGN_CENTER);
                header.setBorderWidth(1);
                header.setPhrase(new Phrase(headerTitle, headFont));
                table.addCell(header);
            });

            for(Order ord: orders){
                PdfPCell descCell = new PdfPCell(new Phrase(ord.getObservation()));
                descCell.setPadding(1);
                descCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                descCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(descCell);
            }
            document.add(table);
            document.close();

        } catch (DocumentException e){
            e.printStackTrace();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    private void pdfCabeceras() throws IOException, DocumentException{

        Document document = new Document();

        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("iTextHelloWorld.pdf"));

        document.open();

        Rectangle rect = new Rectangle(36, 36, 290, 806);

        float center = (rect.getLeft() + rect.getRight()) / 2;

        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase("LEFT--------L"), 50, 800, 0);

        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase("RIGHT------R"), 300, 800,0);

        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase("LEFT--------1"), 50, 750, 0);

        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase("RIGHT------2"), 300, 750,0);

        document.close();

    }

    private void pdfColumn() throws IOException, DocumentException {

        Document document = new Document();

        PdfWriter write = PdfWriter.getInstance(document, new FileOutputStream("iTextHelloWorld.pdf"));

        document.open();

        ColumnText ct = new ColumnText(write.getDirectContent());
        BufferedReader br = new BufferedReader(new FileReader("text.txt"));
        String line;
        Paragraph p;
        Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 12);
        Font bold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
        boolean title = true;
        while ((line = br.readLine()) != null) {
            p = new Paragraph(line, title ? bold : normal);
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            title = line.isEmpty();
            ct.addElement(p);
        }
        Rectangle[] columns = {
                new Rectangle(36, 36, 290, 806), new Rectangle(305, 36, 559, 806)
        };

        int c = 0;
        int status = ColumnText.START_COLUMN;
        while (ColumnText.hasMoreText(status)) {
            ct.setSimpleColumn(columns[c]);
            status = ct.go();
            if (++c == 2) {
                document.newPage();
                c = 0;
            }
        }

        document.close();


    }


}
