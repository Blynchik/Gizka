package project.gizka.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.gizka.model.AppUser;
import project.gizka.repository.AppUserRepo;
import project.gizka.service.CRUDService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AppUserService implements CRUDService {

    private final AppUserRepo appUserRepo;

    @Autowired
    public AppUserService(AppUserRepo appUserRepo){
        this.appUserRepo = appUserRepo;
    }

    @Override
    public List<AppUser> getAll() {
        return appUserRepo.findAll();
    }

    @Override
    public Optional<AppUser> getById(Long id) {
        return appUserRepo.findById(id);
    }

    @Transactional
    @Override
    public AppUser create(AppUser appUser) {
        return appUserRepo.save(appUser);
    }

    @Transactional
    @Override
    public void update(Long id, AppUser updatedUser) {
        AppUser appUser = appUserRepo.getReferenceById(id);
        updatedUser.setId(appUser.getId());
        updatedUser.setRegisteredAt(appUser.getRegisteredAt());
        updatedUser.setUpdatedAt(LocalDateTime.now());
        appUserRepo.save(updatedUser);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        appUserRepo.deleteById(id);
    }
}
