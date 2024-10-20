package co.edu.unbosque.complejidad.model.dto;

import java.util.List;

public class ResultadoDTO {

    private String textoProcesado;
    private int cantidad;
    private List<Integer> coincidencias;


    public ResultadoDTO() {}


    public ResultadoDTO(String textoProcesado, int cantidad, List<Integer> coincidencias) {
        this.textoProcesado = textoProcesado;
        this.cantidad = cantidad;
        this.coincidencias = coincidencias;
    }


    public String getTextoProcesado() {
        return textoProcesado;
    }

    public void setTextoProcesado(String textoProcesado) {
        this.textoProcesado = textoProcesado;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public List<Integer> getCoincidencias() {
        return coincidencias;
    }

    public void setCoincidencias(List<Integer> coincidencias) {
        this.coincidencias = coincidencias;
    }
}
