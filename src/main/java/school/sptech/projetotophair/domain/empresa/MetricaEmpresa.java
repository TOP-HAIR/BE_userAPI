package school.sptech.projetotophair.domain.empresa;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@NamedStoredProcedureQuery(name = "MetricaEmpresa.calcInfo", procedureName = "calcularInformacoes", parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "empresaId", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "data_inicio", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "data_fim", type = String.class)
})
@Getter
@Setter
public class MetricaEmpresa {

    @Id
    private Long idMetricaEmpresa;
    private BigDecimal totalSemanal;
    private int qtdAgendas;
    private String servicoMaisPedidos;
    private String servicoMenosPedidos;
    private String faturamentoMinimo;
    private String faturamentoMaximo;

    @Nullable
    private Integer qtdAgendaDomingo;
    @Nullable
    private Integer qtdAgendaSegunda;
    @Nullable
    private Integer qtdAgendaTerca;
    @Nullable
    private Integer qtdAgendaQuarta;
    @Nullable
    private Integer qtdAgendaQuinta;
    @Nullable
    private Integer qtdAgendaSexta;
    @Nullable
    private Integer qtdAgendaSabado;

    @Nullable
    private BigDecimal rendimentoDomingo;
    @Nullable
    private BigDecimal rendimentoSegunda;
    @Nullable
    private BigDecimal rendimentoTerca;
    @Nullable
    private BigDecimal rendimentoQuarta;
    @Nullable
    private BigDecimal rendimentoQuinta;
    @Nullable
    private BigDecimal rendimentoSexta;
    @Nullable
    private BigDecimal rendimentoSabado;

    @Nullable
    private String topServicoPrimeiro;
    @Nullable
    private String topServicoSegundo;
    @Nullable
    private String topServicoTerceiro;
    @Nullable
    private String topServicoQuarto;
    @Nullable
    private String topServicoQuinto;
}
