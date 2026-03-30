package servicios;

import com.tt1.trabajo.utilidades.ApiClient;
import com.tt1.trabajo.utilidades.ApiException;
import com.tt1.trabajo.utilidades.api.SolicitudApi;
import com.tt1.trabajo.utilidades.model.ResultsResponse;
import com.tt1.trabajo.utilidades.model.Solicitud;
import com.tt1.trabajo.utilidades.model.SolicitudResponse;
import interfaces.InterfazContactoSim;
import modelo.DatosSimulation;
import modelo.DatosSolicitud;
import modelo.Entidad;
import modelo.Punto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.tt1.trabajo.utilidades.api.ResultadosApi;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class ContactoSim implements InterfazContactoSim {
    private Map<Integer,DatosSolicitud> solicitudes;
    private static Random RANDOM;
    private List<Entidad> entidades;
    @Value("${servicio.url}")
    private String url;
    private SolicitudApi solicitudApi;
    private ResultadosApi resultadosApi;

    public ContactoSim() {
        this("http://localhost:8080");
    }
    @Autowired
    public ContactoSim(@Value("${servicio.url}")String servicioUrl){
        this.solicitudes = new HashMap<>();
        this.RANDOM = new Random();
        this.entidades = new ArrayList<>();
        this.entidades.add( new Entidad(1,"Entidad 1","Descripción 1"));
        this.entidades.add( new Entidad(2,"Entidad 2","Descripción 2"));
        this.entidades.add( new Entidad(3,"Entidad 3","Descripción 3"));
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(servicioUrl);
        this.solicitudApi = new SolicitudApi(apiClient);
        this.resultadosApi = new ResultadosApi(apiClient);
    }
    @Override
    public int solicitarSimulation(DatosSolicitud datosSolicitud) {
        if(datosSolicitud==null){
            return -1;
        }
        try {
            Solicitud solicitud = new Solicitud();
            List<Integer> cantidades = new ArrayList<>();
            List<String> nombres = new ArrayList<>();

            for (Entidad e : entidades) {
                Integer cantidad = datosSolicitud.getNums().get(e.getId());
                if (cantidad != null) {
                    nombres.add(e.getName());
                    cantidades.add(cantidad);
                }
            }

            solicitud.setCantidadesIniciales(cantidades);
            solicitud.setNombreEntidades(nombres);

            SolicitudResponse respuesta = this.solicitudApi.solicitudSolicitarPost("user", solicitud);
            if(respuesta.getTokenSolicitud()!=null){
                return respuesta.getTokenSolicitud();
            }
            return -1;
        }catch(ApiException e){
            return -1;
        }
    }
    @Override
    public DatosSimulation descargarDatos(int token) {
        try{
            ResultsResponse resultsResponse = this.resultadosApi.resultadosPost("user",token);

            String [] data = resultsResponse.getData().split("\n");
            int anchoTablero = Integer.parseInt(data[0].trim());
            int maxSegundos = 0;
            Map<Integer,List<Punto>> puntos = new HashMap<>();

            for(int i=1; i < data.length; i++){
                if(!data[i].trim().isEmpty()){
                   String [] lexemes = data[i].trim().split(",");
                   int time = Integer.parseInt(lexemes[0]);
                   int y = Integer.parseInt(lexemes[1]);
                   int x = Integer.parseInt(lexemes[2]);
                   String color = lexemes[3];

                   Punto point = new Punto();
                   point.setX(x);
                   point.setY(y);
                   point.setColor(color);
                    puntos.computeIfAbsent(time, k -> new ArrayList<>()).add(point);
                    if (time > maxSegundos){
                        maxSegundos = time;
                    }
                }
            }
            DatosSimulation datosSimulation = new DatosSimulation();
            datosSimulation.setAnchoTablero(anchoTablero);
            datosSimulation.setMaxSegundos(maxSegundos+1);
            datosSimulation.setPuntos(puntos);
            return datosSimulation;
        }catch(ApiException e){
            return new DatosSimulation();
        }
    }
    @Override
    public List<Entidad> getEntities() {
        return this.entidades;
    }

    @Override
    public boolean isValidEntityId(int id) {
        return this.entidades.stream().anyMatch(e -> e.getId() == id);
    }
    public void setUrl(String url){
        this.url = url;
    }
    public void setSolicitudApi(SolicitudApi solicitudApi) {
        this.solicitudApi = solicitudApi;
    }
    public void setResultadosApi(ResultadosApi resultadosApi) {
        this.resultadosApi = resultadosApi;
    }
}
