package project.gizka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.gizka.model.Enemy;

@Repository
public interface EnemyRepo extends JpaRepository<Enemy, Long> {

    @Query(value = "SELECT * FROM enemy ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Enemy findRandomEnemy();
}
