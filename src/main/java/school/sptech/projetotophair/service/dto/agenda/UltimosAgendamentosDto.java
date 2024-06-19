package school.sptech.projetotophair.service.dto.agenda;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UltimosAgendamentosDto {
    private Long idAgenda;
    private Long idUsuario;
    private String nomeUsuario;
    private LocalDateTime start;
    private LocalDateTime end;
    private String title;
}
