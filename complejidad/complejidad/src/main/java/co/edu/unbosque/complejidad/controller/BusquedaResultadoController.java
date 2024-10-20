package co.edu.unbosque.complejidad.controller;

import co.edu.unbosque.complejidad.model.dto.ResultadoDTO;
import co.edu.unbosque.complejidad.model.dto.BusquedaDTO;
import co.edu.unbosque.complejidad.service.BusquedaResultadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
public class BusquedaResultadoController implements BusquedaResultadoAPI {

    private final BusquedaResultadoService busquedaResultadoService;

    @Autowired
    public BusquedaResultadoController(BusquedaResultadoService busquedaResultadoService) {
        this.busquedaResultadoService = busquedaResultadoService;
    }

    @Override
    public ResponseEntity<ResultadoDTO> buscarPatron(MultipartFile archivo, String patron, String algoritmo) throws IOException {

        // Guarda el archivo temporalmente en el servidor
        String rutaArchivo = guardarArchivoTemporal(archivo);

        // Crea el DTO para la busqueda
        BusquedaDTO busquedaDTO = new BusquedaDTO();
        busquedaDTO.setRutaArchivo(rutaArchivo);
        busquedaDTO.setPatron(patron);
        busquedaDTO.setAlgoritmo(algoritmo);

        // Llama al service para buscar el patron en el archivo
        ResultadoDTO resultadoDTO = busquedaResultadoService.buscarPatronEnArchivo(busquedaDTO);

        return ResponseEntity.status(HttpStatus.OK).body(resultadoDTO);
    }

    private String guardarArchivoTemporal(MultipartFile archivo) throws IOException {
        File tempFile = File.createTempFile("tempFile", ".txt");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(archivo.getBytes());
        }
        return tempFile.getAbsolutePath();
    }
}
