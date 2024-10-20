package co.edu.unbosque.complejidad.service;

import co.edu.unbosque.complejidad.exceptions.ArchivoVacioException;
import co.edu.unbosque.complejidad.exceptions.BusquedaException;
import co.edu.unbosque.complejidad.exceptions.ProcesarArchivoException;
import co.edu.unbosque.complejidad.model.dto.BusquedaDTO;
import co.edu.unbosque.complejidad.model.dto.ResultadoDTO;
import co.edu.unbosque.complejidad.model.entity.Resultado;
import co.edu.unbosque.complejidad.repository.BusquedaResultadoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BusquedaResultadoService {
    private final ModelMapper modelMapper;
    private final BusquedaResultadoRepository busquedaResultadoRepository;

    @Autowired
    public BusquedaResultadoService(ModelMapper modelMapper, BusquedaResultadoRepository busquedaResultadoRepository) {
        this.modelMapper = modelMapper;
        this.busquedaResultadoRepository = busquedaResultadoRepository;
    }

    public ResultadoDTO buscarPatronEnArchivo(BusquedaDTO busquedaDTO) {
        try {
            // Procesa el archivo de texto
            String texto = procesarArchivo(busquedaDTO.getRutaArchivo());

            // Realiza la búsqueda segun el algoritmo seleccionado
            List<Integer> coincidencias;
            if ("kmp".equalsIgnoreCase(busquedaDTO.getAlgoritmo())) {
                coincidencias = buscarKMP(texto, busquedaDTO.getPatron());
            } else {
                coincidencias = buscarBM(texto, busquedaDTO.getPatron());
            }

            int cantidad = coincidencias.size();

            // Crea y guarda el resultado en la base de datos
            Resultado resultado = new Resultado(texto, coincidencias, cantidad);
            Resultado resultadoGuardado = busquedaResultadoRepository.save(resultado);

            // Convierte el resultado a DTO
            ResultadoDTO resultadoDTO = modelMapper.map(resultadoGuardado, ResultadoDTO.class);
            resultadoDTO.setTextoProcesado(texto);
            return resultadoDTO;

        } catch (IOException e) {
            throw new ProcesarArchivoException("Error al procesar el archivo. ");
        } catch (Exception e) {
            throw new BusquedaException("Error en la busqueda del archivo. ");
        }
    }

    private String procesarArchivo(String rutaArchivo) throws IOException {
        StringBuilder contenido = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                contenido.append(linea).append("\n");
            }
        }


        if (contenido.toString().trim().isEmpty()) {
            throw new ArchivoVacioException("El archivo esta vacio.");
        }

        return contenido.toString().trim();
    }

    private List<Integer> buscarKMP(String texto, String patron) {
        List<Integer> coincidencias = new ArrayList<>();
        int[] lps = construirLPS(patron);
        int j = 0;

        for (int i = 0; i < texto.length(); i++) {
            while (j > 0 && Character.toLowerCase(texto.charAt(i)) != Character.toLowerCase(patron.charAt(j))) {
                j = lps[j - 1];
            }
            if (Character.toLowerCase(texto.charAt(i)) == Character.toLowerCase(patron.charAt(j))) {
                j++;
            }
            if (j == patron.length()) {
                coincidencias.add(i - j + 1);
                j = lps[j - 1];
            }
        }
        return coincidencias;
    }

    private int[] construirLPS(String patron) {
        int[] lps = new int[patron.length()];
        int j = 0;

        for (int i = 1; i < patron.length(); i++) {
            while (j > 0 && patron.charAt(i) != patron.charAt(j)) {
                j = lps[j - 1];
            }
            if (patron.charAt(i) == patron.charAt(j)) {
                j++;
                lps[i] = j;
            }
        }
        return lps;
    }

    private List<Integer> buscarBM(String texto, String patron) {
        List<Integer> coincidencias = new ArrayList<>();
        int[] malosCaracteres = heuristicaMalosCaracteres(patron);
        int longitudPatron = patron.length();
        int longitudTexto = texto.length();
        int i = 0;

        while (i <= longitudTexto - longitudPatron) {
            int j = longitudPatron - 1;

            while (j >= 0 && Character.toLowerCase(patron.charAt(j)) == Character.toLowerCase(texto.charAt(i + j))) {
                j--;
            }

            if (j < 0) {
                coincidencias.add(i);
                i += (i + longitudPatron < longitudTexto) ? longitudPatron - malosCaracteres[texto.charAt(i + longitudPatron)] : 1;
            } else {
                i += Math.max(1, j - malosCaracteres[texto.charAt(i + j)]);
            }
        }
        return coincidencias;
    }

    private int[] heuristicaMalosCaracteres(String patron) {
        int[] malosCaracteres = new int[256];
        for (int i = 0; i < 256; i++) {
            malosCaracteres[i] = -1;
        }
        for (int i = 0; i < patron.length(); i++) {
            malosCaracteres[patron.charAt(i)] = i;
        }
        return malosCaracteres;
    }
}
