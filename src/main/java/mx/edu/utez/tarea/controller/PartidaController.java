package mx.edu.utez.tarea.controller;


import jakarta.validation.Valid;
import mx.edu.utez.tarea.dto.IniciarPartidaDto;
import mx.edu.utez.tarea.model.Partida;
import mx.edu.utez.tarea.model.Tiro;
import mx.edu.utez.tarea.service.JuegoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/partidas")
@CrossOrigin("*")
public class PartidaController {

    private final JuegoService juegoService;

    public PartidaController(JuegoService juegoService) {
        this.juegoService = juegoService;
    }

    @PostMapping("/iniciar")
    public ResponseEntity<Partida> iniciarPartida(@RequestBody @Valid IniciarPartidaDto dto) {
        return ResponseEntity.status(201).body(juegoService.iniciarPartida(dto));
    }

    @PostMapping("/{id}/tiro")
    public ResponseEntity<Tiro> realizarTiro(@PathVariable Long id) {
        return ResponseEntity.status(200).body(juegoService.realizarTiro(id));
    }

    @PutMapping("/{id}/finalizar")
    public ResponseEntity<Partida> finalizarPartidaManual(@PathVariable Long id) {
        return ResponseEntity.status(200).body(juegoService.finalizarPartidaManual(id));
    }
}