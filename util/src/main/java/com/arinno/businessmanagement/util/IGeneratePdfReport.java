package com.arinno.businessmanagement.util;

import com.arinno.businessmanagement.model.Invoice;
import com.arinno.businessmanagement.model.Order;
import com.itextpdf.text.DocumentException;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface IGeneratePdfReport {

    public void generateInvoice(Invoice invoice, OutputStream outputStream) throws Exception;

    public ByteArrayInputStream generateOrder(Order order) throws DocumentException;

    public ByteArrayInputStream ordersPDFReport(List<Order> orders);

}
