package school.sptech.projetotophair.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.projetotophair.service.integraveis.fila.Fila;
import school.sptech.projetotophair.service.integraveis.pilha.PilhaObj;
import school.sptech.projetotophair.domain.agenda.Agenda;
import school.sptech.projetotophair.domain.agendaservico.AgendaServico;
import school.sptech.projetotophair.domain.usuario.Usuario;
import school.sptech.projetotophair.domain.usuario.repository.UsuarioRepository;
import school.sptech.projetotophair.service.AgendaService;
import school.sptech.projetotophair.service.dto.agenda.AgendaDto;
import school.sptech.projetotophair.service.dto.agenda.AgendaEmpresaVinculadaDto;
import school.sptech.projetotophair.service.dto.agenda.UltimosAgendamentosDto;
import school.sptech.projetotophair.service.dto.agenda.mapper.AgendaMapper;
import school.sptech.projetotophair.service.dto.agendaservico.AgendaServicoDto;
import school.sptech.projetotophair.service.dto.agendaservico.mapper.AgendaServicoMapper;
import school.sptech.projetotophair.service.dto.usuario.UsuarioAgendaResponseDto;
import school.sptech.projetotophair.service.dto.usuario.mapper.UsuarioMapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/agendas")
public class AgendaController {

    @Autowired
    private UsuarioRepository u;

    @Autowired
    private AgendaService agendaService;

    @PostMapping
    public ResponseEntity<AgendaDto> cadastrar(@RequestBody Agenda agenda) {
        agenda.setData(LocalDate.now());
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("HH:mm");
        agenda.setHora(LocalTime.now().format(formato));
        Agenda agendaCadastrada = agendaService.cadastrarAgenda(agenda);
        AgendaDto agendaDto = AgendaMapper.toAgendaDto(agendaCadastrada);
        return ResponseEntity.ok(agendaDto);
    }

    @PutMapping("/vincular-empresa/{idAgenda}/{idEmpresa}")
    public ResponseEntity<AgendaEmpresaVinculadaDto> vincularEmpresa(@PathVariable Long idAgenda, @PathVariable Long idEmpresa){
        Agenda agenda = agendaService.vincularEmpresa(idAgenda, idEmpresa);
        AgendaEmpresaVinculadaDto agendaEmpresaVinculadaDto = AgendaMapper.toAgendaEmpresaVinculadaDto(agenda);
        return ResponseEntity.ok(agendaEmpresaVinculadaDto);
    }

    @PutMapping("/vincular-usuario/{idAgenda}/{idUsuario}")
    public ResponseEntity<UsuarioAgendaResponseDto> vincularUsuario(@PathVariable Long idAgenda, @PathVariable Long idUsuario){
        Usuario usuario = agendaService.vincularUsuario(idAgenda, idUsuario);
        UsuarioAgendaResponseDto usuarioAgendaResponseDto = UsuarioMapper.toUsuarioAgendaResponseDto(usuario);
        return ResponseEntity.ok(usuarioAgendaResponseDto);
    }

    @PutMapping("/vincular-servico/{idAgenda}/{idServico}")
    public ResponseEntity<AgendaServicoDto> vincularServico(@PathVariable Long idAgenda, @PathVariable Long idServico){
        AgendaServico agendaServico = agendaService.vincularServico(idAgenda, idServico);
        AgendaServicoDto agendaServicoDto = AgendaServicoMapper.toAgendaServicoDto(agendaServico);
        return ResponseEntity.ok(agendaServicoDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Agenda>> listar(@PathVariable Long id) {
        Optional<Agenda> agenda = agendaService.buscarAgendaPorId(id);
        return ResponseEntity.ok(agenda);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<AgendaDto>> listarPorUsuario(@PathVariable Long idUsuario){
        List<Agenda> agendas = agendaService.listarAgendasPorUsuario(idUsuario);
        List<AgendaDto> dtos = new ArrayList<>();
        for (Agenda agendaDaVez: agendas) {
            dtos.add(AgendaMapper.toAgendaDto(agendaDaVez));
        }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/empresa/{idEmpresa}")
    public ResponseEntity<List<AgendaDto>> listarPorEmpresa(@PathVariable Long idEmpresa){
        List<Agenda> agendas = agendaService.listarAgendasPorEmpresa(idEmpresa);
        List<AgendaDto> dtos = new ArrayList<>();
        for (Agenda agendaDaVez: agendas) {
            dtos.add(AgendaMapper.toAgendaDto(agendaDaVez));
        }
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Agenda> atualizar(
            @PathVariable Long id,
            @RequestBody Agenda agenda
    ) {
        Optional<Agenda> agendaAtualizada = agendaService.atualizarAgenda(id, agenda);
            return ResponseEntity.status(200).body(agendaAtualizada.get());
    }

//    @GetMapping("/ultimos-agendamentos/{id}")
//    public ResponseEntity<UltimosAgendamentosDto> ultimosAgendamentos(@PathVariable Long id){
//        Optional<Usuario> all = u.findById(id);
//
//        if (all.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//
//        UltimosAgendamentosDto dto = UltimosAgendamentosMapper.toDto(all.get());
//
//        return ResponseEntity.ok(dto);
//    }

//    @GetMapping("/ultimos-agendamentos/{idEmpresa}")
//    public ResponseEntity<List<UltimosAgendamentosDto>> ultimosAgendamentos(@PathVariable Long idEmpresa) {
//        // Assuming you have a service instance called agendaService
//        PilhaObj<Agenda> ultimosAgendamentos = agendaService.getUltimosAgendamentos(idEmpresa);
//
//        // Convert Agenda objects to UltimosAgendamentosDto objects using the mapper
//        List<UltimosAgendamentosDto> dtos = new ArrayList<>();
//        while (!ultimosAgendamentos.isEmpty()) {
//            Agenda agenda = ultimosAgendamentos.pop();
//            UltimosAgendamentosDto dto = AgendaMapper.toDto(agenda);
//            dtos.add(dto);
//        }
//
//        return ResponseEntity.ok(dtos);
//    }
// No seu controlador
@GetMapping("/ultimos-agendamentos/{idEmpresa}")
public ResponseEntity<List<UltimosAgendamentosDto>> ultimosAgendamentos(@PathVariable Long idEmpresa) {
    List<UltimosAgendamentosDto> dtos = agendaService.getUltimosAgendamentosDto(idEmpresa);
    return ResponseEntity.ok(dtos);
}


    @GetMapping("/meses-ordenados/{idEmpresa}")
    public ResponseEntity<List<String>> obterMesesOrdenados(@PathVariable Long idEmpresa) {
        Fila mesesOrdenados = agendaService.mesesOrdenados();

        // Verifica se a fila não está vazia antes de retornar o conteúdo
        if (!mesesOrdenados.isEmpty()) {
            List<String> meses = new ArrayList<>();

            // Obtém os elementos da fila e adiciona à lista
            while (!mesesOrdenados.isEmpty()) {
                meses.add(mesesOrdenados.poll());
            }

            return ResponseEntity.ok(meses);
        } else {
            // Caso a fila esteja vazia, pode retornar uma resposta indicando isso
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAgenda(@PathVariable Long id) {
        agendaService.deletarAgenda(id);
        return ResponseEntity.noContent().build();
    }
}
