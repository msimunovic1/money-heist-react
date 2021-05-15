package hr.msimunovic.moneyheist.email.service;

public interface EmailService {

    void sendEmail(String to, String subject, String text);
}
