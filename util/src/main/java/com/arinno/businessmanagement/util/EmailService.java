package com.arinno.businessmanagement.util;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import com.arinno.businessmanagement.model.Invoice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;

@Service
public class EmailService implements IEmailPort {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private IGeneratePdfReport generatePdfReport;

    @Override
    public boolean sendEmail(EmailBody emailBody) throws MessagingException {
        LOGGER.info("EmailBody: {}", emailBody.toString());
        return sendEmailTool(emailBody.getContent(),emailBody.getEmail(), emailBody.getSubject());
    }

    @Override
    public void sendEmail(EmailBody emailBody, Invoice invoice) throws Exception {

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(emailBody.getEmail());
        helper.setText(emailBody.getContent(), true);
        helper.setSubject(emailBody.getSubject());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        generatePdfReport.generateInvoice(invoice, outputStream);
        byte[] bytes = outputStream.toByteArray();

        DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");

        helper.addAttachment("Invoice.pdf", dataSource);
        sender.send(message);


 /*
        try {
            helper.setTo(emailBody.getEmail());
            helper.setText(emailBody.getContent(), true);
            helper.setSubject(emailBody.getSubject());

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            generatePdfReport.generateInvoice(invoice, outputStream);
            byte[] bytes = outputStream.toByteArray();

            DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");

            helper.addAttachment("Invoice.pdf", dataSource);
            sender.send(message);
            send = true;
            LOGGER.info("Mail enviado!");
        } catch (MessagingException e) {
            System.out.println("catch 1");
            LOGGER.error("Hubo un error al enviar el mail: {}", e);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("catch 2");
        }


  */


    }

    private boolean sendEmailTool(String textMessage, String email, String subject) throws MessagingException {
        boolean send = false;
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        try {
            helper.setTo(email);
            helper.setText(textMessage, true);
            helper.setSubject(subject);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 //           generatePdfReport.generateInvoice(invoice, outputStream);
            byte[] bytes = outputStream.toByteArray();

            DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");

//           FileSystemResource file
//                   = new FileSystemResource(new File("iTextHelloWorld.pdf"));
//            helper.addAttachment("Invoice.pdf", file);

            helper.addAttachment("Invoice.pdf", dataSource);
            sender.send(message);
            send = true;
            LOGGER.info("Mail enviado!");
        } catch (MessagingException e) {
            LOGGER.error("Hubo un error al enviar el mail: {}", e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return send;
    }
}
