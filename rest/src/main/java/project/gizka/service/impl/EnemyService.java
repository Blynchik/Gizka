package project.gizka.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.gizka.model.Enemy;
import project.gizka.repository.EnemyRepo;
import project.gizka.service.CRUDService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class EnemyService implements CRUDService<Enemy> {

    private final EnemyRepo enemyRepo;

    @Autowired
    public EnemyService(EnemyRepo enemyRepo){
        this.enemyRepo = enemyRepo;
    }


    @Override
    public List<Enemy> getAll() {
        return enemyRepo.findAll();
    }

    @Override
    public Optional<Enemy> getById(Long id) {
        var optionalEnemy = enemyRepo.findById(id);
        return optionalEnemy;
    }

    @Transactional
    @Override
    public Enemy create(Enemy entity) {
        entity.setStrength(5);
        entity.setDexterity(5);
        entity.setConstitution(5);
        return enemyRepo.save(entity);
    }

    @Transactional
    @Override
    public Enemy update(Long id, Enemy updatedEntity) {
        Enemy enemy = enemyRepo.getReferenceById(id);
        updatedEntity.setId(enemy.getId());
        updatedEntity.setCreatedAt(enemy.getCreatedAt());
        return enemyRepo.save(updatedEntity);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        enemyRepo.deleteById(id);
    }

    public Enemy findRandom(){
        return enemyRepo.findRandomEnemy();
    }

    public boolean checkExistence(Long id){
        return enemyRepo.existsById(id);
    }
}
