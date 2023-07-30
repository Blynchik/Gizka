package project.gizka.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.gizka.dto.commonDto.EnemyCommonDto;
import project.gizka.dto.createDto.CreateEnemyDto;
import project.gizka.exception.notFound.EnemyNotFoundException;
import project.gizka.exception.validation.EnemyValidationException;
import project.gizka.model.Enemy;
import project.gizka.service.impl.EnemyService;
import project.gizka.util.Converter;

import java.util.List;

@RestController
@RequestMapping("/api/enemy")
public class EnemyController {

    private final EnemyService enemyService;

    @Autowired
    public EnemyController(EnemyService enemyService){
        this.enemyService = enemyService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<EnemyCommonDto>> getAll(){
        List<Enemy> enemies = enemyService.getAll();
        List<EnemyCommonDto> enemiesDto = enemies.stream()
                .map(Converter::getEnemyDtoFrom)
                .toList();
        return ResponseEntity.ok(enemiesDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnemyCommonDto> getById(@PathVariable Long id){
        checkEnemyExistence(id);
        var optionalEnemy = enemyService.getById(id);
        EnemyCommonDto enemyDto = Converter.getEnemyDtoFrom(optionalEnemy.get());
        return ResponseEntity.ok(enemyDto);
    }

    @GetMapping("/random")
    public ResponseEntity<EnemyCommonDto> getRandom(){
        Enemy enemy = enemyService.findRandom();
        EnemyCommonDto enemyDto = Converter.getEnemyDtoFrom(enemy);
        return ResponseEntity.ok(enemyDto);
    }

    @PostMapping("/create")
    public ResponseEntity<EnemyCommonDto> create(@RequestBody @Valid CreateEnemyDto enemyDto,
                                                 BindingResult bindingResult){
        //TODO unique description and name validation
        if(bindingResult.hasErrors()){
            List<String> errorMessages = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            throw new EnemyValidationException(errorMessages);
        }

        Enemy enemy = Converter.getEnemyFrom(enemyDto);
        Enemy createdEnemy = enemyService.create(enemy);
        EnemyCommonDto createdEnemyDto = Converter.getEnemyDtoFrom(createdEnemy);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEnemyDto);
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<EnemyCommonDto> edit(@PathVariable Long id,
                                               @RequestBody @Valid CreateEnemyDto enemyDto,
                                               BindingResult bindingResult){
        //TODO unique description and name validation
        if(bindingResult.hasErrors()){
            List<String> errorMessages = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            throw new EnemyValidationException(errorMessages);
        }

        Enemy enemy = Converter.getEnemyFrom(enemyDto);
        Enemy updatedEnemy = enemyService.update(id, enemy);
        EnemyCommonDto updatedEnemyDto = Converter.getEnemyDtoFrom(updatedEnemy);
        return ResponseEntity.ok(updatedEnemyDto);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id){
        checkEnemyExistence(id);
        enemyService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private void checkEnemyExistence(Long id){
        if(!enemyService.checkExistence(id)){
            throw new EnemyNotFoundException("Enemy " + id + " not found");
        }
    }
}
