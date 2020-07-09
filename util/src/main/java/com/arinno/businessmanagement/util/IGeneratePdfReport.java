package com.arinno.businessmanagement.util;

import com.arinno.businessmanagement.model.Invoice;
import com.itextpdf.text.DocumentException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

public interface IGeneratePdfReport {

    public void generateInvoice(Invoice invoice, OutputStream outputStream) throws Exception;

    public void writePdf(OutputStream outputStream) throws Exception;
}
