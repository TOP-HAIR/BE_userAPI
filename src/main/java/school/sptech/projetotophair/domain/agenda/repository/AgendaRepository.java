package school.sptech.projetotophair.domain.agenda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import school.sptech.projetotophair.domain.agenda.Agenda;

import java.util.List;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {
    List<Agenda> findAllByUsuarioIdUsuario(Long idUsuario);


    List<Agenda> findAllByEmpresaIdEmpresa(Long idEmpresa);

    List<Agenda> findTop10ByEmpresaIdEmpresaOrderByIdAgendaDesc(Long idEmpresa);


//    @Query("SELECT a FROM Agenda a WHERE a.empresa.idEmpresa = :idEmpresa")
//    List<Agenda> findAllByEmpresa_IdEmpresa(Long idEmpresa);


//    @Query(value = "SELECT "
//            + "YEAR(a.data_Inicio) ano, "
//            + "MONTH(a.data_Inicio) mes, "
//            + "MIN(a.data_Inicio) dataInicio, "
//            + "MAX(a.data_Fim) dataFinal, "
//            + "SUM(s.preco) precoTotal "
//            + "FROM agenda a "
//            + "JOIN servico s ON a.id_Agenda = s.fk_agenda "
//            + "WHERE a.fk_empresa = :idEmpresa "
//            + "GROUP BY YEAR(a.data_Inicio), MONTH(a.data_Inicio) "
//            + "ORDER BY YEAR(a.data_Inicio), MONTH(a.data_Inicio)",
//            nativeQuery = true)
//    List<Object[]> buscarPeriodosPorEmpresa(@Param("idEmpresa") Long idEmpresa);

}
