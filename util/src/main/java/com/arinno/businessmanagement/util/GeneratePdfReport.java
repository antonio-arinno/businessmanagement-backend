package com.arinno.businessmanagement.util;

import com.arinno.businessmanagement.model.*;
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
    public ByteArrayInputStream generateInvoice(Invoice invoice) throws DocumentException {

        Document doc = new Document();
        PdfWriter docWriter = null;
        initializeFonts();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        docWriter = PdfWriter.getInstance(doc, out);
        doc.open();
        PdfContentByte cb = docWriter.getDirectContent();

        boolean beginPage = true;
        int y = 0;

        for (Order invoiceItem : invoice.getItems()){
            if(beginPage){
                beginPage = false;
                generateLayout(doc, cb);
                generateHeader(invoice, cb);
                y = 515;
            }
            generateDetail(invoiceItem, cb, y);
            y = y - 20;
            for (OrderItem orderItem : invoiceItem.getItems()){
                if(beginPage){
                    beginPage = false;
                    generateLayout(doc, cb);
                    generateHeader(invoice, cb);
                    y = 515;
                }
                generateDetail(orderItem, cb, y);
                y = y - 15;
                if(y < 178){
                    printPageNumber(cb);
                    doc.newPage();
                    beginPage = true;
                }
            }
            y = y - 5;

        }

        generateFooter(cb, invoice);

        printPageNumber(cb);
        doc.close();

        return new ByteArrayInputStream(out.toByteArray());

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

        generateFooter(cb, order);

        printPageNumber(cb);
        doc.close();

        return new ByteArrayInputStream(out.toByteArray());

    }

    private void generateHeader(Order order, PdfContentByte cb)  {

        Customer customer = order.getCustomer();
        Company company = order.getCompany();

        try {

            createHeadings(cb,400,750, company.getName(), PdfContentByte.ALIGN_RIGHT);
            createHeadings(cb,400,735, company.getAddress().getAddressLine_1(), PdfContentByte.ALIGN_RIGHT);
            createHeadings(cb,400,720, company.getAddress().getAddressLine_2(), PdfContentByte.ALIGN_RIGHT);
            createHeadings(cb,400,705, company.getAddress().getCountry(), PdfContentByte.ALIGN_RIGHT);
            createHeadings(cb,400,690, company.getAddress().getAddressLine_3(), PdfContentByte.ALIGN_RIGHT);

            createHeadings(cb,50,650, customer.getName(), PdfContentByte.ALIGN_RIGHT);
//            createHeadings(cb,50,635, order.getCustomer().getFullAddress(), PdfContentByte.ALIGN_RIGHT);

            createHeadings(cb,50,635, customer.getAddress().getAddressLine_1(), PdfContentByte.ALIGN_RIGHT);
            createHeadings(cb,50,620, customer.getAddress().getAddressLine_2(), PdfContentByte.ALIGN_RIGHT);
            createHeadings(cb,50,605, customer.getAddress().getCountry(), PdfContentByte.ALIGN_RIGHT);
            createHeadings(cb,50,590, customer.getAddress().getAddressLine_3(), PdfContentByte.ALIGN_RIGHT);


            createHeadings(cb,482,643, order.getCustomer().getCode(), PdfContentByte.ALIGN_RIGHT);
            createHeadings(cb,482,623, String.valueOf(order.getNumber()), PdfContentByte.ALIGN_RIGHT);
            createHeadings(cb,482,603, String.valueOf(order.getCreateAt()), PdfContentByte.ALIGN_RIGHT);

        }

        catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private void generateHeader(Invoice invoice, PdfContentByte cb)  {

        Customer customer = invoice.getCustomer();
        Company company = invoice.getCompany();

        try {

            createHeadings(cb,400,750, company.getName(), PdfContentByte.ALIGN_RIGHT);
            createHeadings(cb,400,735, company.getAddress().getAddressLine_1(), PdfContentByte.ALIGN_RIGHT);
            createHeadings(cb,400,720, company.getAddress().getAddressLine_2(), PdfContentByte.ALIGN_RIGHT);
            createHeadings(cb,400,705, company.getAddress().getCountry(), PdfContentByte.ALIGN_RIGHT);
            createHeadings(cb,400,690, company.getAddress().getAddressLine_3(), PdfContentByte.ALIGN_RIGHT);

            createHeadings(cb,50,650, customer.getName(), PdfContentByte.ALIGN_RIGHT);
//            createHeadings(cb,50,635, order.getCustomer().getFullAddress(), PdfContentByte.ALIGN_RIGHT);

            createHeadings(cb,50,635, customer.getAddress().getAddressLine_1(), PdfContentByte.ALIGN_RIGHT);
            createHeadings(cb,50,620, customer.getAddress().getAddressLine_2(), PdfContentByte.ALIGN_RIGHT);
            createHeadings(cb,50,605, customer.getAddress().getCountry(), PdfContentByte.ALIGN_RIGHT);
            createHeadings(cb,50,590, customer.getAddress().getAddressLine_3(), PdfContentByte.ALIGN_RIGHT);


            createHeadings(cb,482,643, invoice.getCustomer().getCode(), PdfContentByte.ALIGN_RIGHT);
            createHeadings(cb,482,623, String.valueOf(invoice.getNumber()), PdfContentByte.ALIGN_RIGHT);
            createHeadings(cb,482,603, String.valueOf(invoice.getCreateAt()), PdfContentByte.ALIGN_RIGHT);

        }

        catch (Exception ex){
            ex.printStackTrace();
        }

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
            createHeadings(cb,422,643,"Account No.", PdfContentByte.ALIGN_RIGHT);
            createHeadings(cb,422,623,"Order No.", PdfContentByte.ALIGN_RIGHT);
            createHeadings(cb,422,603,"Order Date", PdfContentByte.ALIGN_RIGHT);

            // Invoice Detail box layout
            cb.rectangle(20,150,550,400);

            cb.moveTo(20,530);
            cb.lineTo(570,530);

            cb.moveTo(85,150);
            cb.lineTo(85,550);

            cb.moveTo(138,150);
            cb.lineTo(138,550);

            cb.moveTo(370,150);
            cb.lineTo(370,550);

            cb.moveTo(419,150);
            cb.lineTo(419,550);

            cb.moveTo(457,150);
            cb.lineTo(457,550);

            cb.moveTo(500,150);
            cb.lineTo(500,550);

            cb.stroke();

            // Invoice Detail box Text Headings
            createHeadings(cb,22,533,"Ref.", PdfContentByte.ALIGN_RIGHT);
            createHeadings(cb,87,533,"Lote", PdfContentByte.ALIGN_RIGHT);
            createHeadings(cb,142,533,"Concept", PdfContentByte.ALIGN_RIGHT);
            createHeadings(cb,395,533,"Price", PdfContentByte.ALIGN_RIGHT);
            createHeadings(cb,441,533,"Dto", PdfContentByte.ALIGN_RIGHT);
            createHeadings(cb,484,533,"Qty", PdfContentByte.ALIGN_RIGHT);
            createHeadings(cb,550,533,"Total", PdfContentByte.ALIGN_RIGHT);


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

    private void generateDetail(Order invoiceItem, PdfContentByte cb, int y)  {
        DecimalFormat df = new DecimalFormat("0.00");

        createContent(cb,142,y, "Albaran Nº " + String.valueOf(invoiceItem.getNumber()) +
                                    " Fecha " + invoiceItem.getCreateAt(),PdfContentByte.ALIGN_LEFT);

        y = y - 5;
        cb.moveTo(142, y );
        cb.lineTo(350, y );
        cb.stroke();

    }

    private void generateDetail(OrderItem orderItem, PdfContentByte cb, int y)  {
        DecimalFormat df = new DecimalFormat("0.00");

        createContent(cb,24,y, orderItem.getProduct().getCode(),PdfContentByte.ALIGN_LEFT);
        createContent(cb,87,y, orderItem.getLot(), PdfContentByte.ALIGN_LEFT);
        createContent(cb,142,y, orderItem.getProduct().getDescription(),PdfContentByte.ALIGN_LEFT);
        createContent(cb,415,y, df.format(orderItem.getPrice()),PdfContentByte.ALIGN_RIGHT);

        createContent(cb,455,y, df.format(orderItem.getDiscount()),PdfContentByte.ALIGN_RIGHT);

        createContent(cb,498,y, String.valueOf(orderItem.getQuantity()),PdfContentByte.ALIGN_RIGHT);
        createContent(cb,568,y, df.format(orderItem.getAmount()),PdfContentByte.ALIGN_RIGHT);

    }

    private void createHeadings(PdfContentByte cb, float x, float y, String text, int alignRight){

        cb.beginText();
        cb.setFontAndSize(bfBold, 8);
        cb.setTextMatrix(x,y);
        cb.showText(text.trim());
        cb.endText();

    }

    private void generateFooter(PdfContentByte cb, Invoice invoice){
        DecimalFormat df = new DecimalFormat("0.00");
        int y = 80;

        createContent(cb,150,100, "Total Neto EUR",PdfContentByte.ALIGN_RIGHT);
        createContent(cb,244,100, "%IVA",PdfContentByte.ALIGN_RIGHT);
        createContent(cb,350,100, "Total IVA",PdfContentByte.ALIGN_LEFT);
        createHeadings(cb,475,100, "Total EUR", PdfContentByte.ALIGN_RIGHT);

        cb.moveTo(20,90);
        cb.lineTo(570,90);
        cb.stroke();

        if(invoice.hasIvaType(IvaType.GENERAL)) {
            createContent(cb, 150, y, df.format(invoice.getTotal(IvaType.GENERAL)), PdfContentByte.ALIGN_RIGHT);
            createContent(cb, 225, y, df.format(IvaType.GENERAL().getIva()), PdfContentByte.ALIGN_LEFT);
            createContent(cb, 350, y, df.format(invoice.getTotalIva(IvaType.GENERAL)), PdfContentByte.ALIGN_LEFT);
            y -= 10;
        }

        if(invoice.hasIvaType(IvaType.REDUCED)) {
            createContent(cb, 150, y, df.format(invoice.getTotal(IvaType.REDUCED)), PdfContentByte.ALIGN_RIGHT);
            createContent(cb, 225, y, df.format(IvaType.REDUCED().getIva()), PdfContentByte.ALIGN_LEFT);
            createContent(cb, 350, y, df.format(invoice.getTotalIva(IvaType.REDUCED)), PdfContentByte.ALIGN_LEFT);
            y -= 10;
        }

        if(invoice.hasIvaType(IvaType.SUPER_REDUCED)) {
            createContent(cb, 150, y, df.format(invoice.getTotal(IvaType.SUPER_REDUCED)), PdfContentByte.ALIGN_RIGHT);
            createContent(cb, 225, y, df.format(IvaType.SUPER_REDUCED().getIva()), PdfContentByte.ALIGN_LEFT);
            createContent(cb, 350, y, df.format(invoice.getTotalIva(IvaType.SUPER_REDUCED)), PdfContentByte.ALIGN_LEFT);
        }

        createHeadings(cb,475,80, df.format(invoice.getTotalWithIva()), PdfContentByte.ALIGN_RIGHT);

    }

    private void generateFooter(PdfContentByte cb, Order order){
        DecimalFormat df = new DecimalFormat("0.00");
        int y = 80;

        createContent(cb,150,100, "Total Neto EUR",PdfContentByte.ALIGN_RIGHT);
        createContent(cb,225,100, "IVA",PdfContentByte.ALIGN_LEFT);
        createHeadings(cb,350,100, "Total EUR", PdfContentByte.ALIGN_RIGHT);

        cb.moveTo(20,90);
        cb.lineTo(570,90);
        cb.stroke();

        if(order.hasIvaType(IvaType.GENERAL)) {
            createContent(cb, 150, y, df.format(order.getTotal(IvaType.GENERAL)), PdfContentByte.ALIGN_RIGHT);
            createContent(cb, 225, y, df.format(order.getTotalIva(IvaType.GENERAL)), PdfContentByte.ALIGN_LEFT);
            y -= 10;
        }

        if(order.hasIvaType(IvaType.REDUCED)) {
            createContent(cb, 150, y, df.format(order.getTotal(IvaType.REDUCED)), PdfContentByte.ALIGN_RIGHT);
            createContent(cb, 225, y, df.format(order.getTotalIva(IvaType.REDUCED)), PdfContentByte.ALIGN_LEFT);
            y -= 10;
        }

        if(order.hasIvaType(IvaType.SUPER_REDUCED)) {
            createContent(cb, 150, y, df.format(order.getTotal(IvaType.SUPER_REDUCED)), PdfContentByte.ALIGN_RIGHT);
            createContent(cb, 225, y, df.format(order.getTotalIva(IvaType.SUPER_REDUCED)), PdfContentByte.ALIGN_LEFT);
        }

        createHeadings(cb,350,80, df.format(order.getTotalWithIva()), PdfContentByte.ALIGN_RIGHT);

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

}
