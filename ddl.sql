CREATE TABLE `expense` (
  `id` varchar(36) NOT NULL,
  `amount` double NOT NULL,
  `comment` varchar(200) DEFAULT NULL,
  `date_time` datetime NOT NULL,
  `description` varchar(40) NOT NULL,
  `user_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE `user` (
  `id` varchar(36) NOT NULL,
  `active` bit(1) NOT NULL,
  `password` varchar(60) NOT NULL,
  `username` varchar(40) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `user_role` (
  `role` varchar(255) NOT NULL,
  `user_id` varchar(36) NOT NULL,
  PRIMARY KEY (`role`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
