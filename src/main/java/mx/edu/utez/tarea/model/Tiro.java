package mx.edu.utez.tarea.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tiros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tiro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "partida_id", nullable = false)
    private Partida partida;

    private int valorDado1;
    private int valorDado2;
    private int suma;
    private boolean esGanador;
}