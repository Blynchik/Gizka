package project.gizka.service;

import project.gizka.model.AppUser;

import java.util.List;
import java.util.Optional;

public interface CRUDService {

    List<AppUser> getAll();

    Optional<AppUser> getById(Long id);

    AppUser create(AppUser appUser);

    AppUser update(Long id, AppUser updatedUser);

    void delete(Long id);
}
