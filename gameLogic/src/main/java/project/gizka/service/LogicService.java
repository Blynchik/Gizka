package project.gizka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.gizka.client.RestClient;
import project.gizka.dto.commonDto.AdventurerCommonDto;
import project.gizka.dto.commonDto.EnemyCommonDto;
import project.gizka.dto.commonDto.FighterDto;
import project.gizka.util.FightLog;
import project.gizka.util.FightTurn;

import java.util.Random;

@Service
@Transactional(readOnly = true)
public class LogicService {

    private final RestClient restClient;

    @Autowired
    public LogicService(RestClient restClient) {
        this.restClient = restClient;
    }

    public FightLog fight(Long adventurerId) throws Exception{
        Random random = new Random();
        FightLog fightLog = new FightLog();

        AdventurerCommonDto adventurer = (AdventurerCommonDto) restClient.getAdventurerById(adventurerId.toString());
        EnemyCommonDto enemy = (EnemyCommonDto) restClient.getRandomEnemy();


        while (adventurer.getHealthPoint() > 0 && enemy.getHealthPoint() > 0) {
            // Определение очередности хода
            FighterDto firstAttacker, secondAttacker;
            if (random.nextBoolean()) {
                firstAttacker = adventurer;
                secondAttacker = enemy;
            } else {
                firstAttacker = enemy;
                secondAttacker = adventurer;
            }

            // Вычисление атаки первого атакующего
            int attack = random.nextInt(firstAttacker.getHighAttack() - firstAttacker.getLowAttack() + 1)
                    + firstAttacker.getLowAttack();

            // Вычисление уклонения второго атакующего
            int evasion = random.nextInt(secondAttacker.getHighEvasion() - secondAttacker.getLowEvasion() + 1)
                    + secondAttacker.getLowEvasion();

            // Вычисление урона
            int damage = Math.max(attack - evasion, 0);

            // Вычитаем урон из здоровья второго атакующего
            int healthPoint = secondAttacker.getHealthPoint() - damage;
            secondAttacker.setHealthPoint(healthPoint);

            // Создание записи о ходе
            FightTurn fightTurn = new FightTurn(firstAttacker.getName(), secondAttacker.getName(), attack, evasion, damage, healthPoint);
            fightLog.addTurn(fightTurn);

            // Проверка на окончание сражения
            if (adventurer.getHealthPoint() <=0 || enemy.getHealthPoint() <= 0) {
                break;
            }

            // Вычисление атаки второго атакующего
            attack = random.nextInt(secondAttacker.getHighAttack() - secondAttacker.getLowAttack() + 1)
                    + secondAttacker.getLowAttack();

            // Вычисление уклонения первого атакующего
            evasion = random.nextInt(firstAttacker.getHighEvasion() - firstAttacker.getLowEvasion() + 1)
                    + firstAttacker.getLowEvasion();

            // Вычисление урона
            damage = Math.max(attack - evasion, 0);

            // Вычитаем урон из здоровья первого атакующего
            healthPoint = firstAttacker.getHealthPoint() - damage;
            firstAttacker.setHealthPoint(healthPoint);

            // Создание записи о ходе
            fightTurn = new FightTurn(secondAttacker.getName(), firstAttacker.getName(), attack, evasion, damage, healthPoint);
            fightLog.addTurn(fightTurn);

            if(adventurer.getHealthPoint()<=0){
                fightLog.setWinner(enemy.getName());
            } else {
                fightLog.setWinner(adventurer.getName());
            }
        }
        return fightLog;
    }
}
