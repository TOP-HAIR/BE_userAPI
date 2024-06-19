package school.sptech.projetotophair.domain.agendaservico;

import jakarta.persistence.*;
import school.sptech.projetotophair.domain.agenda.Agenda;
import school.sptech.projetotophair.domain.servico.Servico;

@Entity
public class AgendaServico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAgendaServico;

    @ManyToOne
    @JoinColumn(name = "fkAgenda", referencedColumnName = "idAgenda")
    private Agenda agenda;

    @ManyToOne
    @JoinColumn(name = "fkServico", referencedColumnName = "idServico")
    private Servico servico;

    public AgendaServico() {
    }

    public AgendaServico(Long idAgendaServico, Agenda agenda, Servico servico) {
        this.idAgendaServico = idAgendaServico;
        this.agenda = agenda;
        this.servico = servico;
    }

    public Long getIdAgendaServico() {
        return idAgendaServico;
    }

    public void setIdAgendaServico(Long idAgendaServico) {
        this.idAgendaServico = idAgendaServico;
    }

    public Agenda getAgenda() {
        return agenda;
    }

    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }
}
