package com.tt1.trabajo;

import modelo.DatosSimulation;
import modelo.DatosSolicitud;
import modelo.Entidad;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servicios.ContactoSim;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ContactoSimTest {
    private ContactoSim servicio;
    @BeforeEach
    void setUp(){
        servicio = new ContactoSim();
    }
    @AfterEach
    void tearDown(){
        servicio = null;
    }
    @Test
    void solicitarSimulacionTest(){
        Map<Integer, Integer> entradas = new HashMap<>();
        entradas.put(1,10);
        DatosSolicitud datos = new DatosSolicitud(entradas);

        int token = servicio.solicitarSimulation(datos);
        assertTrue(token>=0);
    }
    @Test
    void descargarDatosTest(){
        int token = 1;
        DatosSimulation resultado = servicio.descargarDatos(token);
        assertNotNull(resultado,"El resultado no debería ser nulo");
    }
    @Test
    void getEntitiesTest(){
        List<Entidad> entities = servicio.getEntities();
        assertEquals(3,entities.size());
    }
    @Test
    void entityValidTest(){
        assertTrue(servicio.isValidEntityId(1));
    }
}
