package project.gizka.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.gizka.model.Adventurer;
import project.gizka.repository.AdventurerRepo;
import project.gizka.service.CRUDService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AdventurerService implements CRUDService<Adventurer>{

    private final AdventurerRepo adventurerRepo;

    @Autowired
    public AdventurerService(AdventurerRepo adventurerRepo){
        this.adventurerRepo = adventurerRepo;
    }

    @Override
    public List<Adventurer> getAll() {
        return adventurerRepo.findAll();
    }

    @Override
    public Optional<Adventurer> getById(Long id) {
        var optionalAdventurer = adventurerRepo.findById(id);
        return optionalAdventurer;
    }

    @Transactional
    @Override
    public Adventurer create(Adventurer adventurer) {
        adventurer.setStrength(5);
        adventurer.setDexterity(5);
        adventurer.setConstitution(5);
        return adventurerRepo.save(adventurer);
    }

    @Transactional
    @Override
    public Adventurer update(Long id, Adventurer updatedAdventurer) {
        Adventurer adventurer = adventurerRepo.getReferenceById(id);
        updatedAdventurer.setId(adventurer.getId());
        updatedAdventurer.setCreatedAt(adventurer.getCreatedAt());
        updatedAdventurer.setAppUser(adventurer.getAppUser());
        return adventurerRepo.save(updatedAdventurer);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        adventurerRepo.deleteById(id);
    }

    public boolean checkExistence(Long id){
        return adventurerRepo.existsById(id);
    }
}
