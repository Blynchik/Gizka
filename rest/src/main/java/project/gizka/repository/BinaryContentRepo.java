package project.gizka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.gizka.model.BinaryContent;

@Repository
public interface BinaryContentRepo extends JpaRepository<BinaryContent, Long> {
}
