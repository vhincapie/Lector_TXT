package co.edu.unbosque.complejidad.repository;

import co.edu.unbosque.complejidad.model.entity.Resultado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusquedaResultadoRepository extends JpaRepository<Resultado, Long> {
}
