package school.sptech.projetotophair.api.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.projetotophair.domain.empresa.Empresa;
import school.sptech.projetotophair.service.EmpresaService;
import school.sptech.projetotophair.service.dto.empresa.*;
import school.sptech.projetotophair.service.dto.empresa.mapper.EmpresaMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @PostMapping("/cadastrar")
    public ResponseEntity<Empresa> cadastrar(@RequestBody @Valid Empresa empresa) {
        Empresa empresaCadastrada = empresaService.cadastrarEmpresa(empresa);
        return ResponseEntity.status(201).body(empresaCadastrada);
    }

    @PutMapping("/vincular-endereco/{idEmpresa}/{idEndereco}")
    public ResponseEntity<EmpresaEnderecoVinculadoDto> vincularEndereco(@PathVariable Long idEmpresa, @PathVariable Long idEndereco){
        Empresa empresa = empresaService.vincularEndereco(idEmpresa, idEndereco);

        EmpresaEnderecoVinculadoDto empresaEnderecoVinculadoDto = EmpresaMapper.toEmpresaEnderecoVinculadoDto(empresa);
        return ResponseEntity.ok(empresaEnderecoVinculadoDto);
    }

    @GetMapping("/listar-por-id/{id}")
    public ResponseEntity<EmpresaComImagensDto> listar(@PathVariable Long id) {
        Optional<Empresa> empresa = empresaService.buscarEmpresaPorId(id);
        EmpresaComImagensDto empresaComImagensDto = EmpresaMapper.toEmpresaComImagensDto(empresa.get());
        return ResponseEntity.ok(empresaComImagensDto);
    }

    @GetMapping("/estado")
    public ResponseEntity<List<EmpresaPorEstadoDto>> listarPorEstado(@RequestParam String estado) {
        List<Empresa> empresasPorEstado = empresaService.listarEmpresasPorEstado(estado);
        List<EmpresaPorEstadoDto> dtos = new ArrayList<>();
        for (Empresa empresaDaVez: empresasPorEstado) {
            dtos.add(EmpresaMapper.toEmpresaPorEstadoDto(empresaDaVez));
        }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<EmpresaDto> findEmpresaByUsuarioId(@PathVariable Long id) {

        Optional<Empresa> empresaByUsuarioId = this.empresaService.findEmpresaByUsuarioId(id);

        if (empresaByUsuarioId.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        EmpresaDto empresaDto = EmpresaMapper.toEmpresaDto(empresaByUsuarioId.get());

        return ResponseEntity.ok(empresaDto);
    }

    @GetMapping("/top5-avaliadas-por-estado")
    public ResponseEntity<List<EmpresaAvaliacaoDto>> findTop5EmpresasMelhorAvaliadasPorEstado(@RequestParam String estado) {
        List<Empresa> empresas = empresaService.listarEmpresasTop5AvaliacoesPorEstado(estado);
        if (empresas.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<EmpresaAvaliacaoDto> dtos = new ArrayList<>();

        for (Empresa empresaDaVez: empresas) {
            dtos.add(EmpresaMapper.toEmpresaAvaliacaoDto(empresaDaVez));
        }

        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpresaDto> atualizar(@PathVariable Long id, @RequestBody @Valid Empresa empresa
    ) {
        Optional<Empresa> empresaAtualizada = empresaService.atualizarEmpresa(id, empresa);
        if (empresaAtualizada.isPresent()) {
            EmpresaDto empresaDto = EmpresaMapper.toEmpresaDto(empresaAtualizada.get());
            return ResponseEntity.ok(empresaDto);
        }
        return ResponseEntity.internalServerError().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEmpresa(@PathVariable Long id) {
        empresaService.deletarEmpresa(id);
        return ResponseEntity.noContent().build();
    }
}
