
--Description: Adding Global Settings

insert into global_settings (id, code, name, value)
    values
    (LAST_INSERT_ID(), 'MULTIUSER_MODE', 'Многопользовательский режим', 'YES'),
    (LAST_INSERT_ID(), 'POST_PREMODERATION', 'Премодерация постов', 'YES'),
    (LAST_INSERT_ID(), 'STATISTICS_IS_PUBLIC', 'Показывать всем статистику блога', 'YES')
