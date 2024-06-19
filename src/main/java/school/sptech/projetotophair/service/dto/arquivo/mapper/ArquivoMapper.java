package school.sptech.projetotophair.service.dto.arquivo.mapper;

import school.sptech.projetotophair.domain.arquivo.Arquivo;
import school.sptech.projetotophair.service.dto.arquivo.ArquivoDto;
import school.sptech.projetotophair.service.dto.arquivo.ArquivoEmpresaVinculadaDto;
import school.sptech.projetotophair.service.dto.arquivo.ArquivoUsuarioVinculadoDto;
import school.sptech.projetotophair.service.dto.empresa.mapper.EmpresaMapper;
import school.sptech.projetotophair.service.dto.usuario.mapper.UsuarioMapper;

public class ArquivoMapper {
    public static ArquivoDto toArquivoDto(Arquivo entity){
        ArquivoDto dto = new ArquivoDto();
        dto.setId(entity.getIdArquivo());
        dto.setNomeArquivoOriginal(entity.getNomeArquivoOriginal());
        dto.setNomeArquivoOriginal(entity.getNomeArquivoOriginal());
        dto.setDataUpload(entity.getDataUpload());

        return dto;
    }

    public static ArquivoEmpresaVinculadaDto toArquivoEmpresaVinculadaDto(Arquivo entity){
        ArquivoEmpresaVinculadaDto dto = new ArquivoEmpresaVinculadaDto();
        dto.setId(entity.getIdArquivo());
        dto.setNomeArquivoOriginal(entity.getNomeArquivoOriginal());
        dto.setNomeArquivoOriginal(entity.getNomeArquivoOriginal());
        dto.setDataUpload(entity.getDataUpload());
        dto.setEmpresaDto(EmpresaMapper.toEmpresaDto(entity.getEmpresa()));

        return dto;
    }

    public static ArquivoUsuarioVinculadoDto toArquivoUsuarioVinculadoDto (Arquivo entity){
        ArquivoUsuarioVinculadoDto dto = new ArquivoUsuarioVinculadoDto();
        dto.setUsuarioDto(UsuarioMapper.toUsuarioDto(entity.getUsuario()));
        dto.setNomeArquivoOriginal(entity.getNomeArquivoOriginal());
        dto.setNomeArquivoSalvo(entity.getNomeArquivoSalvo());
        dto.setId(entity.getIdArquivo());
        dto.setDataUpload(entity.getDataUpload());

        return dto;
    }
}
