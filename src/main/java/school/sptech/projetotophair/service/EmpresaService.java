package school.sptech.projetotophair.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import school.sptech.projetotophair.domain.empresa.Empresa;
import school.sptech.projetotophair.domain.empresa.MetricaEmpresa;
import school.sptech.projetotophair.domain.empresa.repository.EmpresaRepository;
import school.sptech.projetotophair.domain.empresa.repository.MetricaEmpresaRepository;
import school.sptech.projetotophair.domain.endereco.Endereco;
import school.sptech.projetotophair.domain.endereco.repository.EnderecoRepository;
import school.sptech.projetotophair.domain.usuario.Usuario;
import school.sptech.projetotophair.domain.usuario.repository.UsuarioRepository;
import school.sptech.projetotophair.service.dto.empresa.EmpresaAvaliacaoDto;
import school.sptech.projetotophair.service.dto.empresa.mapper.EmpresaMapper;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;
    @Autowired
    private MetricaEmpresaRepository metricaEmpresaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    private final JdbcTemplate jdbcTemplate;

    public EmpresaService(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Empresa cadastrarEmpresa(Empresa empresa) {
        if (empresa == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A empresa não pode ser nula.");
        }
        return empresaRepository.save(empresa);
    }

    @Transactional
    public Empresa vincularEndereco(Long idEmpresa, Long idEndereco){
        Optional<Empresa> empresaById = empresaRepository.findById(idEmpresa);
        Optional<Endereco> enderecoById = enderecoRepository.findById(idEndereco);

        if (enderecoById.isPresent() && empresaById.isPresent()) {
            empresaById.get().setEndereco(enderecoById.get());
            return empresaRepository.save(empresaById.get());
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "empresa ou endereço não encontrados");
    }

    public Optional<Empresa> buscarEmpresaPorId(Long id) {
        Optional<Empresa> empresaOptional = empresaRepository.findById(id);
        if (empresaOptional.isPresent()) {
            return empresaOptional;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada com o ID: " + id);
        }
    }
    public List<Empresa> listarEmpresasPorEstado(String estado) {
        List<Empresa> empresas = empresaRepository.findByEnderecoEstado(estado);
        if (empresas.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhuma empresa encontrada no estado: " + estado);
        }
        return empresas;
    }

//    public List<Empresa> listarEmpresasTop5AvaliacoesPorEstado(String estado) {
//        List<Empresa> empresas = empresaRepository.findTop5EmpresasMelhorAvaliadasPorEstado(estado);
//        if (empresas.isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Nenhuma avaliação de empresa encontrada no estado: " + estado);
//        }
//        return empresas;
//    }

    public List<EmpresaAvaliacaoDto> listarEmpresasTop5AvaliacoesPorFiltros(Long usuarioId) {

        String estado = "";
        if(estado != null && estado.isBlank() && usuarioRepository.existsById(usuarioId)) {
            Optional<Endereco> enderecoByUsuarioId = usuarioRepository.findEnderecoByUsuarioId(usuarioId);
            estado = enderecoByUsuarioId.get().getEstado();
        }

        estado = (estado != null) ? "%" + estado + "%" : null;

        List<Object[]> results = empresaRepository.findEmpresasTop5ByFiltros(estado);
        if (results.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Nenhuma empresa encontrada com os filtros fornecidos.");
        }


        return results.stream()
                .map(result -> {
                    Empresa empresa = (Empresa) result[0];
                    Double avgNivel = (Double) result[1];
                    EmpresaAvaliacaoDto dto = EmpresaMapper.toEmpresaAvaliacaoDto(empresa);
                    dto.setMediaNivelAvaliacoes(avgNivel);
                    return dto;
                })
                .limit(5)
                .collect(Collectors.toList());
    }


    public List<EmpresaAvaliacaoDto> listarEmpresasFiltros(String estado, String nomeEmpresa, Long usuarioId) {
        if(estado == null) {
            estado = "";
        }
        if(estado.isBlank() && usuarioRepository.existsById(usuarioId)) {
            Optional<Endereco> enderecoByUsuarioId = usuarioRepository.findEnderecoByUsuarioId(usuarioId);
            estado = enderecoByUsuarioId.get().getEstado();
        }

        estado = (estado != null) ? "%" + estado + "%" : null;
        nomeEmpresa = (nomeEmpresa != null) ? "%" + nomeEmpresa + "%" : null;


        List<Object[]> results = empresaRepository.findEmpresasByFiltros(estado, nomeEmpresa);
        if (results.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Nenhuma empresa encontrada com os filtros fornecidos.");
        }

        return results.stream()
                .map(result -> {
                    Empresa empresa = (Empresa) result[0];
                    Double avgNivel = (Double) result[1];
                    EmpresaAvaliacaoDto dto = EmpresaMapper.toEmpresaAvaliacaoDto(empresa);
                    dto.setMediaNivelAvaliacoes(avgNivel);
                    return dto;
                })
                .limit(5)
                .collect(Collectors.toList());
    }


    public Optional<Empresa> findEmpresaByUsuarioId(Long idUsuario) {
        if (usuarioRepository.existsById(idUsuario)) {
            Optional<Empresa> empresaByUsuarioId = empresaRepository.findEmpresaByUsuarioId(idUsuario);
            if (empresaByUsuarioId.isPresent()) {
                return empresaByUsuarioId;
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Este usuário não tem uma empresa");
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
    }

    public Optional<Empresa> atualizarEmpresa(Long id, Empresa empresa) {
        if (!empresaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada");
        }
        empresa.setIdEmpresa(id);
        Empresa empresaAtualizada = empresaRepository.save(empresa);
        return Optional.of(empresaAtualizada);
    }

    public void deletarEmpresa(Long id) {
        if (!empresaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada com o ID: " + id);
        }
        empresaRepository.deleteById(id);
    }



    @Transactional
    public MetricaEmpresa calcularMetricas(LocalDate dataInicio, LocalDate dataFim, Long empresaId) {
        return metricaEmpresaRepository.callMetricas(dataInicio, dataFim, empresaId);
    }

    public MetricaEmpresa calcularMetricas1(LocalDate dataInicio, LocalDate dataFim, Long empresaId) {
        // Chamar a procedure calcularInformacoes
        String procedureCall = "EXEC calcularInformacoes ?, ?, ?";

        // Define os parâmetros da procedure
        Object[] parameters = {dataInicio, dataFim, empresaId};
        int[] parameterTypes = {Types.TIMESTAMP, Types.TIMESTAMP, Types.INTEGER};

        // Executa a procedure e obtém o resultado
        Map<String, Object> resultMap = jdbcTemplate.queryForMap(procedureCall, parameters, parameterTypes);

        // Extrai os resultados da consulta
        BigDecimal totalSemanal = (BigDecimal) resultMap.get("total_semanal");
        int qtdAgendamentos = (int) resultMap.get("qtd_agendamentos");
        String servicoMaisPedido = (String) resultMap.get("servico_mais_pedidos");
        String servicoMenosPedido = (String) resultMap.get("servico_menos_pedidos");
        String faturamentoMinimo = (String) resultMap.get("faturamento_minimo");
        String faturamentoMaximo = (String) resultMap.get("faturamento_maximo");
        int qtdAgendamentoDomingo = (int) resultMap.get("qtd_agendamento_domingo");
        int qtdAgendamentoSegunda = (int) resultMap.get("qtd_agendamento_segunda");
        int qtdAgendamentoTerca = (int) resultMap.get("qtd_agendamento_terca");
        int qtdAgendamentoQuarta = (int) resultMap.get("qtd_agendamento_quarta");
        int qtdAgendamentoQuinta = (int) resultMap.get("qtd_agendamento_quinta");
        int qtdAgendamentoSexta = (int) resultMap.get("qtd_agendamento_sexta");
        int qtdAgendamentoSabado = (int) resultMap.get("qtd_agendamento_sabado");

        // Preenche o objeto MetricaEmpresa com os resultados
        //MetricaEmpresa metricas = new MetricaEmpresa();
        //metricas.setTotalSemanal(totalSemanal);
        //metricas.setQuantidadeAgendamentos(qtdAgendamentos);
        //metricas.setServicoMaisPedido(servicoMaisPedido);
        //metricas.setServicoMenosPedido(servicoMenosPedido);
        //metricas.setFaturamentoMinimo(faturamentoMinimo);
        //metricas.setFaturamentoMaximo(faturamentoMaximo);
        //metricas.setQtdAgendaDomingo(qtdAgendamentoDomingo);
        //metricas.setQtdAgendaSegunda(qtdAgendamentoSegunda);
        //metricas.setQtdAgendaTerca(qtdAgendamentoTerca);
        //metricas.setQtdAgendaQuarta(qtdAgendamentoQuarta);
        //metricas.setQtdAgendaQuinta(qtdAgendamentoQuinta);
        //metricas.setQtdAgendaSexta(qtdAgendamentoSexta);
        //metricas.setQtdAgendaSabado(qtdAgendamentoSabado);

        return null;
    }
}
