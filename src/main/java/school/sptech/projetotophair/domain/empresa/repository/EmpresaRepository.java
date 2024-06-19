package school.sptech.projetotophair.domain.empresa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import school.sptech.projetotophair.domain.empresa.Empresa;
import school.sptech.projetotophair.domain.empresa.MetricaEmpresa;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long>{

    @Query("SELECT e FROM Empresa e JOIN e.usuarios u WHERE u.idUsuario = :idUsuario")
    Optional<Empresa> findEmpresaByUsuarioId(@Param("idUsuario") Long idUsuario);

    List<Empresa> findByEnderecoEstado(String estado);

    @Query("SELECT e FROM Empresa e " +
            "JOIN e.avaliacoes a " +
            "WHERE e.endereco.estado = :estado " +
            "GROUP BY e " +
            "ORDER BY AVG(a.nivel) DESC")
    List<Empresa> findTopEmpresasMelhorAvaliadasPorEstado(@Param("estado") String estado);


//    @Query("SELECT DISTINCT s.empresa, AVG(a.nivel) as avgNivel FROM Servico s " +
//            "LEFT JOIN s.empresa e " +
//            "LEFT JOIN e.avaliacoes a " +
//            "LEFT JOIN e.endereco endereco " +
//            "WHERE (:estado IS NULL OR endereco.estado LIKE %:estado%) " +
//            "AND (:nomeServico IS NULL OR s.nomeServico LIKE %:nomeServico%) " +
//            "AND (:nomeEmpresa IS NULL OR e.razaoSocial LIKE %:nomeEmpresa%) " +
//            "AND (:usuarioId IS NULL OR EXISTS (SELECT u FROM e.usuarios u WHERE u.id = :usuarioId)) " +
//            "GROUP BY e " +
//            "ORDER BY avgNivel DESC")
//    List<Object[]> findEmpresasTop5ByFiltros(
//            @Param("estado") String estado,
//            @Param("nomeServico") String nomeServico,
//            @Param("nomeEmpresa") String nomeEmpresa,
//            @Param("usuarioId") Long usuarioId);


@Query("SELECT DISTINCT e, AVG(a.nivel) AS avgNivel " +
        "FROM Empresa e " +
        "INNER JOIN e.endereco en " +
        "LEFT JOIN e.avaliacoes a " +
        "WHERE (:estado IS NULL OR en.estado LIKE %:estado%) " +
        "AND (:nomeEmpresa IS NULL OR e.razaoSocial LIKE %:nomeEmpresa%) " +
        "GROUP BY e " +
        "ORDER BY avgNivel DESC")
List<Object[]> findEmpresasByFiltros(
        @Param("estado") String estado,
        @Param("nomeEmpresa") String nomeEmpresa);




    @Query("SELECT DISTINCT e, AVG(a.nivel) AS avgNivel " +
            "FROM Empresa e " +
            "INNER JOIN e.endereco en " +
            "LEFT JOIN e.avaliacoes a " +
            "WHERE (:estado IS NULL OR en.estado LIKE %:estado%) " +
            "GROUP BY e " +
            "ORDER BY avgNivel DESC"
    )
    List<Object[]> findEmpresasTop5ByFiltros(
            @Param("estado") String estado);



    @Procedure(name = "calcularInformacoes")
    MetricaEmpresa callMetricas(
            @Param("data_inicio") LocalDate dataInicio,
            @Param("data_fim") LocalDate dataFim,
            @Param("empresa_id") Long empresaId);
}
