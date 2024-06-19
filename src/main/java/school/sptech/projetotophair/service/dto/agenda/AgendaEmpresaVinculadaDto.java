package school.sptech.projetotophair.service.dto.agenda;

import lombok.Data;
import school.sptech.projetotophair.service.dto.empresa.EmpresaDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AgendaEmpresaVinculadaDto {
    private Long idAgenda;
    private LocalDateTime start;
    private LocalDateTime end;
    private String title;
    private EmpresaDto empresa;
}
