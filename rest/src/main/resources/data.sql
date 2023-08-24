INSERT INTO app_user(name, chat)
VALUES ('my name', 'telegram chat id'),
       ('person', 'another chat id');
INSERT INTO adventurer(first_name, last_name, user_id, strength, dexterity, constitution)
VALUES ('Герой', 'Героич', 1, 5, 5, 5);

INSERT INTO enemy(name, description, strength, dexterity, constitution)
VALUES ('Змей Горыныч',
        'Горыныч — огненный дракон, который извергает пламя, когда нападает. ' ||
        'Иногда Горыныча связывают и со стихией воды. Тот предстает спящим на ' ||
        'камне в море или живущим в воде, выходит из вод навстречу герою. Змей ' ||
        'Горыныч часто составляет в сказках пару с Кощеем Бессмертным, еще одним ' ||
        'отрицательным фольклорным персонажем.',
        5, 5, 5),
       ('Кощей Бессмертный',
        'Обычно его описывают как тощего героя, старого, седого, слепого, с длинной ' ||
        'бородой. В сказке сказано, что этого «старика» возможно одолеть только настоящему' ||
        ' герою, коим и является Иван-Царевич. Его роль в сюжете – это зеркальное отражение ' ||
        'положительно героя Ивана-Царевича: храброго и доброго.',
        5, 5, 5),
        ('Минотавр Павлик',
        'No description',
        5, 6, 5),
        ('Кобольд Валера',
        'No description',
        3, 3, 3),
        ('Гоблин Саша',
        'No description',
        4, 5, 6),
        ('Тролль Серега',
        'No description',
        7, 7, 7),
        ('Орк Иосиф',
        'No description',
        5, 6, 3),
        ('Кабан Петрович',
        'No description',
        7, 2, 2),
        ('Медведь Потапыч',
        'No description',
        4, 4, 8),
        ('Крыса Шнырь',
         'No description',
        2, 2, 4),
        ('Волк Вован',
          'No description',
        4, 4, 4),
        ('Хоббит Федор Сумкин',
           'No description',
        4, 4, 5);

INSERT INTO role(app_user_id, role)
VALUES (1, 'ADMIN'),
       (1, 'USER');