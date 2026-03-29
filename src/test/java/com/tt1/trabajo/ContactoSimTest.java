package com.tt1.trabajo;

import com.tt1.trabajo.utilidades.ApiClient;
import com.tt1.trabajo.utilidades.api.ResultadosApi;
import com.tt1.trabajo.utilidades.api.SolicitudApi;
import com.tt1.trabajo.utilidades.model.ResultsResponse;
import com.tt1.trabajo.utilidades.model.SolicitudResponse;
import modelo.DatosSimulation;
import modelo.DatosSolicitud;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import servicios.ContactoSim;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class ContactoSimTest {
    //Test unitario
    private ContactoSim servicio;
    private SolicitudApi solicitudApiMock;
    private ResultadosApi resultadosApiMock;
    @BeforeEach
    void setUp() throws Exception {
        servicio = new ContactoSim();

        solicitudApiMock = Mockito.mock(SolicitudApi.class);
        resultadosApiMock = Mockito.mock(ResultadosApi.class);

        ApiClient mockApiClient = Mockito.mock(ApiClient.class);
        when(solicitudApiMock.getApiClient()).thenReturn(mockApiClient);
        when(resultadosApiMock.getApiClient()).thenReturn(mockApiClient);

        servicio.setSolicitudApi(solicitudApiMock);
        servicio.setResultadosApi(resultadosApiMock);
    }
    @Test
    void solicitarSimulacionTest() throws Exception {
        SolicitudResponse mockResponse = new SolicitudResponse();
        mockResponse.setTokenSolicitud(42);

        when(solicitudApiMock.solicitudSolicitarPost(any(), any()))
                .thenReturn(mockResponse);

        Map<Integer, Integer> entradas = new HashMap<>();
        entradas.put(1, 10);
        DatosSolicitud datos = new DatosSolicitud(entradas);

        int token = servicio.solicitarSimulation(datos);
        assertTrue(token >= 0);
    }
    @Test
    void descargarDatosTest() throws Exception {
        ResultsResponse mockResponse = new ResultsResponse();
        mockResponse.setData("10\n1,2,3,red\n");

        when(resultadosApiMock.resultadosPost(any(), anyInt()))
                .thenReturn(mockResponse);

        DatosSimulation resultado = servicio.descargarDatos(1);
        assertNotNull(resultado);
    }
}
