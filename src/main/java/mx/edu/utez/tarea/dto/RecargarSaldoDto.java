package mx.edu.utez.tarea.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecargarSaldoDto {
    @NotNull(message = "El monto a recargar es obligatorio")
    @Positive(message = "El monto a recargar debe ser mayor a 0")
    private Double monto;
}