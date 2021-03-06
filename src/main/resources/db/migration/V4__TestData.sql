
--Description: Adding Data for Testing DB

insert into users
    (id, is_moderator, reg_time, name, email, password, code, photo)
    values
    (1, 1, '2019-1-01 17:55', 'Дмитрий Петров', 'petrov@mail.ru', '$2y$12$4dDb4txcNUTOtkQKsXh7z.3lOdtIQhg9h1ILUIc5gCSZrhSPykYUi', NULL, NULL),
    (2, 0, '2018-5-20 07:02', 'Евгений Кучерявый', 'kucher@mail.ru', '$2y$12$SmQmThigpSW2nxNewW6J1.QH3uCFJmPu.UxTbOF1y2t73o8AMn3mK', '156k', '5e04c21a52a39.jpg'),
    (3, 0, '2019-10-20 17:02', 'Евгения', 'evgenia@mail.ru', '$2y$12$SmQmThigpSW2nxNewW6J1.QH3uCFJmPu.UxTbOF1y2t73o8AMn3mK', NULL, NULL),
    (4, 0, '2019-12-17 00:00', 'Антон Иванов', 'anton@mail.ru', '$2y$12$SmQmThigpSW2nxNewW6J1.QH3uCFJmPu.UxTbOF1y2t73o8AMn3mK', NULL, NULL);


insert into posts
    (id, is_active, moderation_status, moderator_id, user_id, time, title, text, view_count)
    values
        (1, 0, 'NEW', NULL, 1,
            now(),
            'Ужасы чужого кода: как найти смысл и не умереть',
            'В жизни каждого разработчика наступает момент, когда нужно взяться за код, написанный другим человеком. Это может быть связано с поддержкой старого проекта, оптимизацией legacy-кода, переделыванием приложения, которое не доделал другой программист.',
            0),
        (2, 1, 'ACCEPTED', 1, 1,
            '2021-01-10 17:32',
            'Устраняем уязвимости: как защитить сайт от SQL-инъекции',
            'Заводишь сайт, наполняешь его контентом, запускаешь рекламную кампанию — трафик стабильно растет, пользователи активно комментируют и делятся статьями. Всё хорошо до того момента, пока в один не предвещающий беды день на сайте не оказывается ни одной статьи. Заходишь в лог запросов и видишь, что кто-то сделал DROP.',
            102),
        (3, 0, 'DECLINED', 1, 2,
                '2019-07-21 12:20',
                'Что такое класс в Java',
                'Объектно-ориентированное программирование — это подход к разработке приложений с помощью классов и объектов. Он позволяет писать значительно меньше кода и при этом реализовывать больше возможностей, чем при функциональном проектировании программ.',
                170),
        (4, 1, 'ACCEPTED', 1, 1,
                '2021-01-08 19:20',
                'Ужасы чужого кода: как найти смысл и не умереть',
                'Нельзя стать хорошим разработчиком, если не создавать проекты. Но никто не должен решать за вас, какие проекты вам подходят. Чтобы стать хорошим разработчиком, нужно пройти определенный путь и написать несколько проектов. В этой статье мы поразмышляем о том, какой путь правильный, какие проекты точно нужно выполнить и как не стать жертвой чужого влияния.<br>Инструкция, которая подойдёт каждому<br>Часто можно наткнуться на публикации, в которых новичкам предлагают выполнить конкретные проекты: TODO-листы, блоги, методы сортировки и их визуализаторы и прочее. Выполнишь — станешь профессионалом.',
                23),
        (5, 1, 'ACCEPTED', 1, 1,
                '2019-11-18 17:32',
                'Устраняем уязвимости: как защитить сайт от SQL-инъекции',
                'Заводишь сайт, наполняешь его контентом, запускаешь рекламную кампанию — трафик стабильно растет, пользователи активно комментируют и делятся статьями. Всё хорошо до того момента, пока в один не предвещающий беды день на сайте не оказывается ни одной статьи. Заходишь в лог запросов и видишь, что кто-то сделал DROP.',
                102),
        (6, 1, 'ACCEPTED', 1, 2,
                '2021-01-20 12:20',
                'Что такое класс в Java',
                'Объектно-ориентированное программирование — это подход к разработке приложений с помощью классов и объектов. Он позволяет писать значительно меньше кода и при этом реализовывать больше возможностей, чем при функциональном проектировании программ.',
                170),
        (7, 1, 'ACCEPTED', 1, 1,
                '2021-01-20 19:20',
                'Ужасы чужого кода: как найти смысл и не умереть',
                'В жизни каждого разработчика наступает момент, когда нужно взяться за код, написанный другим человеком. Это может быть связано с поддержкой старого проекта, оптимизацией legacy-кода, переделыванием приложения, которое не доделал другой программист.',
                23),
        (8, 1, 'ACCEPTED', 1, 1,
                        '2019-06-07 10:36',
                        'Ужасы чужого кода: как найти смысл и не умереть',
                        'В жизни каждого разработчика наступает момент, когда нужно взяться за код, написанный другим человеком. Это может быть связано с поддержкой старого проекта, оптимизацией legacy-кода, переделыванием приложения, которое не доделал другой программист.',
                        23),
        (9, 1, 'ACCEPTED', 1, 1,
                        '2020-12-08 12:23',
                        'Что такое класс в Java',
                        'Объектно-ориентированное программирование — это подход к разработке приложений с помощью классов и объектов. Он позволяет писать значительно меньше кода и при этом реализовывать больше возможностей, чем при функциональном проектировании программ.',
                        170),
        (10, 1, 'ACCEPTED', 1, 1,
                        '2019-07-21 12:20',
                        'Что такое класс в Java',
                        'Объектно-ориентированное программирование — это подход к разработке приложений с помощью классов и объектов. Он позволяет писать значительно меньше кода и при этом реализовывать больше возможностей, чем при функциональном проектировании программ.',
                        102),
        (11, 1, 'ACCEPTED', 1, 2,
                        '2019-07-21 12:21',
                        'Что такое класс в Java',
                        'Объектно-ориентированное программирование — это подход к разработке приложений с помощью классов и объектов. Он позволяет писать значительно меньше кода и при этом реализовывать больше возможностей, чем при функциональном проектировании программ.',
                         188),
        (12, 1, 'ACCEPTED', 1, 2,
                        '2020-12-08 12:25',
                        'Что такое класс в Java',
                        'Объектно-ориентированное программирование — это подход к разработке приложений с помощью классов и объектов. Он позволяет писать значительно меньше кода и при этом реализовывать больше возможностей, чем при функциональном проектировании программ.',
                        100),
        (13, 1, 'ACCEPTED', 1, 1,
                       '2020-12-08 13:25',
                       'Что такое класс в Java',
                       'Объектно-ориентированное программирование — это подход к разработке приложений с помощью классов и объектов. Он позволяет писать значительно меньше кода и при этом реализовывать больше возможностей, чем при функциональном проектировании программ.',
                        2),
        (14, 1, 'ACCEPTED', 1, 1,
                        '2021-01-25 10:36',
                        'Ужасы чужого кода: как найти смысл и не умереть',
                        'В жизни каждого разработчика наступает момент, когда нужно взяться за код, написанный другим человеком. Это может быть связано с поддержкой старого проекта, оптимизацией legacy-кода, переделыванием приложения, которое не доделал другой программист.',
                        55);

insert into post_votes (id, user_id, post_id, time, value) values
    (1, 1, 2, '2019-12-15 17:55', -1),
    (2, 1, 3, '2019-07-23 12:50', -1),
    (3, 2, 2, now(), 1),
    (4, 2, 3, now(), 1);

insert into tags(id, name) values
    (1, 'JAVA'),
    (2, 'СТАТЬИ'),
    (3, 'РУКОВОДСТВА'),
    (4, 'HTML');

insert into tag2post(id, post_id, tag_id) values
    (1, 1, 2),
    (2, 2, 2),
    (3, 3, 1),
    (4, 3, 3),
    (5, 1, 3),
    (6, 8, 3),
    (7, 9, 3),
    (8, 10, 3),
    (9, 7, 2),
    (10, 7, 4),
    (11, 11, 1),
    (12, 11, 4);

insert into post_comments(id, parent_id, post_id, user_id, time, text) values
    (1, NULL, 2, 3, '2019-12-17 00:38', '- вот это просто отличные слова!'),
    (2, 1, 2, 4, '2019-12-17 00:32', 'тоже задумываюсь'),
    (3, NULL, 3, 3, '2021-1-07 17:32', 'Теперь количество хороших программистов значительно увеличится.'),
    (4, 2, 3, 2, now(), 'Возьму на заметку!');
