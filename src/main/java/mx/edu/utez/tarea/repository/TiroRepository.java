package mx.edu.utez.tarea.repository;


import mx.edu.utez.tarea.model.Tiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TiroRepository extends JpaRepository<Tiro, Long> {
}