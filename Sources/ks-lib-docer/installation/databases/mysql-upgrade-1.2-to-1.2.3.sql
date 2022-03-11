USE `docersystem`;

ALTER TABLE `authorizations` 
  CHANGE COLUMN `systemrule` `systemrule` VARCHAR(5) NULL DEFAULT 'false', 
  CHANGE COLUMN `enabled` `enabled` VARCHAR(5) NULL DEFAULT 'false';

update `authorizations` set `systemrule`='false' where `systemrule`='0';
update `authorizations` set `systemrule`='true' where `systemrule`='1';
update `authorizations` set `enabled`='false' where `enabled`='0';
update `authorizations` set `enabled`='true' where `enabled`='1';

ALTER TABLE `serviceinstance`
  CHANGE COLUMN `enabled` `enabled` VARCHAR(5) NULL DEFAULT 'false';

update `serviceinstance` set `enabled`='false' where `enabled`='0';
update `serviceinstance` set `enabled`='true' where `enabled`='1';

ALTER TABLE `servicemethods`
  CHANGE COLUMN `visible` `visible` VARCHAR(5) NULL DEFAULT 'false';

update `servicemethods` set `visible`='false' where `visible`='0';
update `servicemethods` set `visible`='true' where `visible`='1';

ALTER TABLE `services` 
  CHANGE COLUMN `enabled` `enabled` VARCHAR(5) NULL DEFAULT 'false', 
  CHANGE COLUMN `haslogin` `haslogin` VARCHAR(5) NULL DEFAULT 'false';

update `services` set `haslogin`='false' where `haslogin`='0';
update `services` set `haslogin`='true' where `haslogin`='1';
update `services` set `enabled`='false' where `enabled`='0';
update `services` set `enabled`='true' where `enabled`='1';
