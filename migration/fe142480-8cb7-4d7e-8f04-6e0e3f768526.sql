CREATE TABLE user
(
    user_id    INT AUTO_INCREMENT NOT NULL,
    username   VARCHAR(50) NOT NULL COMMENT '用户名',
    nickname   VARCHAR(50) NULL COMMENT '用户昵称',
    user_type  INT NULL COMMENT '用户类型',
    password   VARCHAR(50) NOT NULL,
    email      VARCHAR(50) NULL,
    user_phone VARCHAR(50) NULL,
    created_at datetime NULL,
    updated_at datetime NULL,
    real_name  VARCHAR(50) NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (user_id)
);