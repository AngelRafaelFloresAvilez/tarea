package mx.edu.utez.tarea.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrearJugadorDto {
    @NotBlank(message = "El nombre del jugador es obligatorio")
    private String nombre;

    @PositiveOrZero(message = "El saldo inicial no puede ser negativo")
    private double saldoInicial;
}