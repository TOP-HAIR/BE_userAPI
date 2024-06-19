package school.sptech.projetotophair.domain.agenda;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import school.sptech.projetotophair.domain.empresa.Empresa;
import school.sptech.projetotophair.domain.usuario.Usuario;

import java.time.LocalDateTime;

@Entity
public class Agenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAgenda;

    @Nullable
    private LocalDateTime startTime;

    @Nullable
    private LocalDateTime endTime;

    @Nullable
    private String background;

    @NotBlank
    private String title;

//    @OneToMany(mappedBy = "agenda")
//    private List<Usuario> usuarios;
//
//    @ManyToOne
//    @OnDelete(action = OnDeleteAction.SET_NULL)
//    @JoinColumn(name = "fkEmpresa", referencedColumnName = "idEmpresa")
//    private Empresa empresa;
@ManyToOne
@OnDelete(action = OnDeleteAction.SET_NULL)
@JoinColumn(name = "fkEmpresa", referencedColumnName = "idEmpresa")
private Empresa empresa;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "fkUsuario", referencedColumnName = "idUsuario")
    private Usuario usuario;

    public Agenda() {
    }

    public Agenda(Long idAgenda, @Nullable LocalDateTime startTime, @Nullable LocalDateTime endTime, @Nullable String background, String title, Empresa empresa, Usuario usuario) {
        this.idAgenda = idAgenda;
        this.startTime = startTime;
        this.endTime = endTime;
        this.background = background;
        this.title = title;
        this.empresa = empresa;
        this.usuario = usuario;
    }

    public Long getIdAgenda() {
        return idAgenda;
    }

    public void setIdAgenda(Long idAgenda) {
        this.idAgenda = idAgenda;
    }

    @Nullable
    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(@Nullable LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Nullable
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(@Nullable LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Nullable
    public String getBackground() {
        return background;
    }

    public void setBackground(@Nullable String background) {
        this.background = background;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}