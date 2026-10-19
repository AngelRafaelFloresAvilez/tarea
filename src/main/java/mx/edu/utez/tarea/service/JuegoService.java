package mx.edu.utez.tarea.service;


import mx.edu.utez.tarea.dto.CrearJugadorDto;
import mx.edu.utez.tarea.dto.IniciarPartidaDto;
import mx.edu.utez.tarea.dto.RecargarSaldoDto;
import mx.edu.utez.tarea.exception.CustomBadRequestException;
import mx.edu.utez.tarea.exception.CustomNotFoundException;
import mx.edu.utez.tarea.model.*;
import mx.edu.utez.tarea.repository.JugadorRepository;
import mx.edu.utez.tarea.repository.PartidaRepository;
import mx.edu.utez.tarea.repository.TiroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class JuegoService {

    private final JugadorRepository jugadorRepository;
    private final PartidaRepository partidaRepository;
    private final TiroRepository tiroRepository;
    private final Random random = new Random();

    public JuegoService(JugadorRepository jugadorRepository, PartidaRepository partidaRepository, TiroRepository tiroRepository) {
        this.jugadorRepository = jugadorRepository;
        this.partidaRepository = partidaRepository;
        this.tiroRepository = tiroRepository;
    }

    public Jugador crearJugador(CrearJugadorDto dto) {
        Jugador jugador = new Jugador();
        jugador.setNombre(dto.getNombre());
        jugador.setSaldo(dto.getSaldoInicial());
        jugador.setActivo(true);
        return jugadorRepository.save(jugador);
    }

    @Transactional
    public Jugador recargarSaldo(Long jugadorId, RecargarSaldoDto dto) {
        Jugador jugador = jugadorRepository.findById(jugadorId)
                .orElseThrow(() -> new CustomNotFoundException("El jugador con ID " + jugadorId + " no existe"));

        jugador.setSaldo(jugador.getSaldo() + dto.getMonto());
        return jugadorRepository.save(jugador);
    }

    public List<Partida> obtenerHistorialPartida(Long jugadorId) {
        if (!jugadorRepository.existsById(jugadorId)) {
            throw new CustomNotFoundException("El jugador con ID " + jugadorId + " no existe");
        }
        return partidaRepository.findByJugadorId(jugadorId);
    }

    @Transactional
    public Partida iniciarPartida(IniciarPartidaDto dto) {
        Jugador jugador = jugadorRepository.findById(dto.getJugadorId())
                .orElseThrow(() -> new CustomNotFoundException("El jugador con ID " + dto.getJugadorId() + " no existe"));

        if (!jugador.isActivo()) {
            throw new CustomBadRequestException("El jugador no está activo");
        }

        if (jugador.getSaldo() < dto.getApuesta()) {
            throw new CustomBadRequestException("Saldo insuficiente para realizar la apuesta");
        }

        // Descontar apuesta del saldo
        jugador.setSaldo(jugador.getSaldo() - dto.getApuesta());
        jugadorRepository.save(jugador);

        // Crear partida en estado EN_JUEGO
        Partida partida = new Partida();
        partida.setFecha(LocalDateTime.now());
        partida.setEstado(EstadoPartida.EN_JUEGO);
        partida.setJugador(jugador);
        partida.setApuesta(dto.getApuesta());

        return partidaRepository.save(partida);
    }

    @Transactional
    public Tiro realizarTiro(Long partidaId) {
        Partida partida = partidaRepository.findById(partidaId)
                .orElseThrow(() -> new CustomNotFoundException("La partida con ID " + partidaId + " no existe"));

        if (partida.getEstado() != EstadoPartida.EN_JUEGO) {
            throw new CustomBadRequestException("La partida ya no está en juego o fue finalizada");
        }

        // Lanzamiento de dados
        int dado1 = random.nextInt(6) + 1;
        int dado2 = random.nextInt(6) + 1;
        int suma = dado1 + dado2;
        boolean esGanador = false;

        // Reglas de negocio del tiro
        if (suma == 7 || suma == 11) {
            // Gana: duplica la apuesta
            esGanador = true;
            partida.setEstado(EstadoPartida.FINALIZADA);

            Jugador jugador = partida.getJugador();
            jugador.setSaldo(jugador.getSaldo() + (partida.getApuesta() * 2));
            jugadorRepository.save(jugador);

        } else if (suma == 2 || suma == 3 || suma == 12) {
            // Pierde: la partida se cierra
            esGanador = false;
            partida.setEstado(EstadoPartida.FINALIZADA);
        }

        partidaRepository.save(partida);

        // Guardar el tiro como último paso
        Tiro tiro = new Tiro();
        tiro.setPartida(partida);
        tiro.setValorDado1(dado1);
        tiro.setValorDado2(dado2);
        tiro.setSuma(suma);
        tiro.setEsGanador(esGanador);

        return tiroRepository.save(tiro);
    }

    @Transactional
    public Partida finalizarPartidaManual(Long partidaId) {
        Partida partida = partidaRepository.findById(partidaId)
                .orElseThrow(() -> new CustomNotFoundException("La partida con ID " + partidaId + " no existe"));

        if (partida.getEstado() == EstadoPartida.FINALIZADA) {
            throw new CustomBadRequestException("La partida ya se encuentra finalizada");
        }

        partida.setEstado(EstadoPartida.FINALIZADA);
        return partidaRepository.save(partida);
    }
}