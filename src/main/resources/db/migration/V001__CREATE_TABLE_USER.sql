DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
 `id`                   VARCHAR(255) CHARACTER SET latin1 NOT NULL    ,
 `username`             VARCHAR(255) CHARACTER SET latin1 DEFAULT NULL,
 `email`                VARCHAR(255) CHARACTER SET latin1 DEFAULT NULL,
 `password`             VARCHAR(255) CHARACTER SET latin1 DEFAULT NULL,
 `enabled`              BIT(1)                            DEFAULT 0   ,
 `locked`               BIT(1)                            DEFAULT 0   ,
 `credentials_expired`  datetime                          DEFAULT NULL,
 `account_expired`      datetime                          DEFAULT NULL,

 PRIMARY KEY (`id`)                                                   ,
 UNIQUE KEY `idx_u_users_username` (`username`) USING BTREE           ,
 UNIQUE KEY `idx_u_users_email`    (`email`   ) USING BTREE           
 
) ENGINE=InnoDB;
