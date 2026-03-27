package com.tt1.trabajo;

import modelo.Destinatario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import servicios.EnviarEmails;

import static org.junit.jupiter.api.Assertions.*;

public class EnviarEmailsTest {
    private EnviarEmails enviarEmails;

    @BeforeEach
    void setUp(){
        Logger logger = LoggerFactory.getLogger(EnviarEmails.class);
        enviarEmails = new EnviarEmails(logger);
    }
    @AfterEach
    void tearDown(){
        enviarEmails = null;
    }
    @Test
    void enviarEmail(){
        String email = "If you don't get the better of yourself, someone else will.";
        Destinatario destinatario = new Destinatario();
        assertTrue(enviarEmails.enviarEmail(destinatario,email));
    }
}
