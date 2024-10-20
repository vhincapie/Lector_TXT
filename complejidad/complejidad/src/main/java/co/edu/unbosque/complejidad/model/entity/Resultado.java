package co.edu.unbosque.complejidad.model.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "resultado")
public class Resultado {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Lob
    private String textoProcesado;

    @ElementCollection
    @CollectionTable(name = "coincidencias", joinColumns = @JoinColumn(name = "resultado_id"))
    @Column(name = "coincidencia")
    private List<Integer> coincidencias;

    @Column
    private int cantidad;


    public Resultado() {}


    public Resultado(String textoProcesado, List<Integer> coincidencias, int cantidad) {
        this.textoProcesado = textoProcesado;
        this.coincidencias = coincidencias;
        this.cantidad = cantidad;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTextoProcesado() {
        return textoProcesado;
    }

    public void setTextoProcesado(String textoProcesado) {
        this.textoProcesado = textoProcesado;
    }

    public List<Integer> getCoincidencias() {
        return coincidencias;
    }

    public void setCoincidencias(List<Integer> coincidencias) {
        this.coincidencias = coincidencias;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
