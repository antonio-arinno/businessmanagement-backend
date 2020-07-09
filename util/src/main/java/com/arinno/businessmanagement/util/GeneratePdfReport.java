package com.arinno.businessmanagement.util;

import com.arinno.businessmanagement.model.Invoice;
import com.itextpdf.text.*;

import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class GeneratePdfReport implements IGeneratePdfReport {


    private static final Logger logger = LoggerFactory.getLogger(GeneratePdfReport.class);

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



    public void writePdf(OutputStream outputStream) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();
        Paragraph paragraph = new Paragraph();
        paragraph.add(new Chunk("hello!"));
        document.add(paragraph);
        document.close();
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
