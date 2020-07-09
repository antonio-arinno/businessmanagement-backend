package com.arinno.businessmanagement.util;

import com.arinno.businessmanagement.model.Invoice;

import javax.mail.MessagingException;

public interface IEmailPort {

    public boolean sendEmail(EmailBody emailBody) throws MessagingException;

    public void sendEmail(EmailBody emailBody, Invoice invoice) throws Exception;


}
