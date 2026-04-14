TRUNCATE TABLE page;
TRUNCATE TABLE latest_result;

INSERT INTO page(id, name, version, platform, link, dictionary, top_result, create_time, create_user, last_update_time, last_update_user, icon)
VALUES
    ('a3ce3281-f643-4f16-8211-3772149bb73b', 'Перевод по номеру телефона', NULL, 'ios', 'transfer/phone', 'перевод телефон номер мобильный отправить', true, NOW(), 'system', NOW(), 'system', 'ic_mobile_1'),
    ('a9172e34-b371-4621-9516-786ef55b7772', 'Перевод по номеру карты', NULL, 'ios', 'transfer/card', 'перевод карта номер отправить деньги', false, NOW(), 'system', NOW(), 'system', 'ic_mobile_2'),
    ('20afe515-7f4a-44b0-aab9-f717ddd8d1e6', 'Перевод между счетами', NULL, 'ios', 'transfer/accounts', 'перевод счет между своими', false, NOW(), 'system', NOW(), 'system', 'ic_mobile_3'),
    ('e5f48475-dd60-4fb9-9eac-77c45429d120', 'Оплата мобильной связи', NULL, 'android', 'payments/mobile', 'оплата мобильная связь телефон пополнить', true, NOW(), 'system', NOW(), 'system', 'ic_mobile_4'),
    ('a48526a0-931b-41cf-9e89-055360a4dcd0', 'Оплата ЖКХ', NULL, 'android', 'payments/housing', 'оплата жкх коммунальные услуги квартира', true, NOW(), 'system', NOW(), 'system', 'ic_mobile_5');

INSERT INTO latest_result(id, client_id, search_string, create_time, create_user, last_update_time, last_update_user)
VALUES
    ('29c6292f-606f-49dc-8f29-e16eed0a9288', '9fb5188b-35d5-4a80-994f-0d158bd260bd', 'перевод на карту', '1900-01-01 00:00:01', 'system', '1900-01-01 00:00:01', 'system'),
    ('f6e730a3-f191-41dc-b1c3-8878ebcd25a3', '9fb5188b-35d5-4a80-994f-0d158bd260bd', 'перевод на карту', '1900-01-01 00:00:00', 'system', '1900-01-01 00:00:00', 'system');