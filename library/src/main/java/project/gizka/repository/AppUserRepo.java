package project.gizka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.gizka.model.AppUser;

@Repository
public interface AppUserRepo extends JpaRepository<AppUser, Long> {
}
