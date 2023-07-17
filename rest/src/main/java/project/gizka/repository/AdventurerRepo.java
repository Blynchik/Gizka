package project.gizka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.gizka.model.Adventurer;

@Repository
public interface AdventurerRepo extends JpaRepository<Adventurer, Long> {
}
