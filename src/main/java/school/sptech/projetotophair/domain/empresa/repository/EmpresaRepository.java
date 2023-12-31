package school.sptech.projetotophair.domain.empresa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import school.sptech.projetotophair.domain.empresa.Empresa;

import java.awt.print.Pageable;
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

}
