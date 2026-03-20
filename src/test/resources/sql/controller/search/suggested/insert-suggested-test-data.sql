TRUNCATE TABLE page;
TRUNCATE TABLE latest_result;

INSERT INTO page (id, name, version, platform, link, dictionary, top_result, create_time, create_user, last_update_time, last_update_user)
VALUES
    ('f59b2248-3b9a-412c-b2a1-dabdff0d7839', 'Переводы', NULL, 'ios', '/transfers', 'переводы перевод', true, NOW(), 'system', NOW(), 'system'),
    ('7cc96730-2a16-4ee5-a8b8-0545a3e1dcd3', 'Платежи', '2.0.0', 'ios', '/payments', 'платежи оплата', true, NOW(), 'system', NOW(), 'system'),
    ('449a5319-92ec-43d2-b884-5d1a80dd5f18', 'Курсы валют', NULL, 'android', '/currency', 'курсы валюты', true, NOW(), 'system', NOW(), 'system');

INSERT INTO latest_result (id, client_id, search_string, create_time, create_user, last_update_time, last_update_user)
VALUES
    ('3a59e199-e1d7-47d6-b7c2-cc0ac5714451', 'client1', 'переводы', NOW(), 'system', NOW(), 'system'),
    ('3aab5ef3-ac43-4aed-80a6-2a4ad8114d37', 'client1', 'платежи', NOW(), 'system', NOW(), 'system');