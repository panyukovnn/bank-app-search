UPDATE page p
SET icon = tmp.icon
FROM (VALUES
    ('a3ce3281-f643-4f16-8211-3772149bb73b'::uuid, 'ic_mobile_1'),
    ('a9172e34-b371-4621-9516-786ef55b7772'::uuid, 'ic_mobile_2'),
    ('20afe515-7f4a-44b0-aab9-f717ddd8d1e6'::uuid, 'ic_mobile_3'),
    ('e5f48475-dd60-4fb9-9eac-77c45429d120'::uuid, 'ic_mobile_4'),
    ('a48526a0-931b-41cf-9e89-055360a4dcd0'::uuid, 'ic_mobile_5'),
    ('40070193-09d6-4cbb-a3d2-91f191197866'::uuid, 'ic_mobile_6'),
    ('189ca830-6ee7-4a6b-b97e-0f0c83d888f2'::uuid, 'ic_mobile_7'),
    ('417b8ff8-43d1-4195-9989-1862426ec170'::uuid, 'ic_mobile_8'),
    ('ac78558d-f5fb-45ab-a5c2-3381daaf57a0'::uuid, 'ic_mobile_9'),
    ('fd1ddddd-c1ed-4fab-a331-78c5d02737ad'::uuid, 'ic_mobile_10'),
    ('e51a68b3-5c04-433e-ad3a-2ad1cd446c3d'::uuid, 'ic_mobile_11'),
    ('cc7a8bd1-27ad-46e7-b1b3-a31e044cfe96'::uuid, 'ic_mobile_12'),
    ('9d4c588b-4690-4123-bfbe-4cbbdbb939fd'::uuid, 'ic_mobile_13'),
    ('a5098b02-919c-4ce9-90a6-95bddb53968e'::uuid, 'ic_mobile_14'),
    ('e8e4eb9c-ab59-49b2-a9be-e705677ee890'::uuid, 'ic_mobile_15')
) AS tmp (id, icon)
WHERE p.id = tmp.id;