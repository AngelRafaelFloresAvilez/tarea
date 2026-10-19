package mx.edu.utez.tarea.controller;


import jakarta.validation.Valid;
import mx.edu.utez.tarea.dto.CrearJugadorDto;
import mx.edu.utez.tarea.dto.RecargarSaldoDto;
import mx.edu.utez.tarea.model.Jugador;
import mx.edu.utez.tarea.model.Partida;
import mx.edu.utez.tarea.service.JuegoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jugadores")
@CrossOrigin("*")
public class JugadorController {

    private final JuegoService juegoService;

    public JugadorController(JuegoService juegoService) {
        this.juegoService = juegoService;
    }

    @PostMapping
    public ResponseEntity<Jugador> crearJugador(@RequestBody @Valid CrearJugadorDto dto) {
        return ResponseEntity.status(201).body(juegoService.crearJugador(dto));
    }

    @PostMapping("/{id}/recargar")
    public ResponseEntity<Jugador> recargarSaldo(@PathVariable Long id, @RequestBody @Valid RecargarSaldoDto dto) {
        return ResponseEntity.status(200).body(juegoService.recargarSaldo(id, dto));
    }

    @GetMapping("/{id}/partidas")
    public ResponseEntity<List<Partida>> obtenerHistorialPartida(@PathVariable Long id) {
        return ResponseEntity.status(200).body(juegoService.obtenerHistorialPartida(id));
    }
}