package com.isa.arnhem.isarest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.isa.arnhem.isarest.models.Description;
import com.isa.arnhem.isarest.models.Event;
import com.isa.arnhem.isarest.models.User;
import com.isa.arnhem.isarest.models.UserType;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.config.TransportStrategy;

import static javax.mail.Message.RecipientType.BCC;

public class TestMongo {

    public static void main(String[] args) throws UnirestException, JsonProcessingException {



        Email email = EmailBuilder.startingBlank()
                .to("lollypop", "ryansusana@live.com")
                .from("jazz.holiday@hotmail.com").withPlainText("Some text")
                .buildEmail();

        Mailer mailer = MailerBuilder
                .withSMTPServer("smtp-relay.sendinblue.com", 587, "ryansusana@live.com", "xw8Rp7204CKFYEcm")
                .withTransportStrategy(TransportStrategy.SMTP)
                .buildMailer();

    }
}
