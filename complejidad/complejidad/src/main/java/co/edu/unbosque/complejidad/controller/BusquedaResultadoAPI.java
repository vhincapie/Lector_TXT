package co.edu.unbosque.complejidad.controller;

import co.edu.unbosque.complejidad.model.dto.ResultadoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequestMapping("/api/busqueda")
public interface BusquedaResultadoAPI {

    @PostMapping("/buscar")
    ResponseEntity<ResultadoDTO> buscarPatron(@RequestParam("archivo") MultipartFile archivo,
                                              @RequestParam("patron") String patron,
                                              @RequestParam("algoritmo") String algoritmo) throws IOException;

}
