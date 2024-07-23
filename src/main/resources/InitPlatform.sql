# INSERT INTO User (username, password, email)
# VALUES ('testuser', 'password123', 'testuser@example.com');

# CREATE TABLE global (
#     id int PRIMARY KEY,
#     section varchar(255),
#     data varchar(255)
# );
# INSERT INTO global (id, section, data)
#  VALUES (1, 'current', 'testuser@example.com');

CREATE TABLE user_info_register(
    id int PRIMARY KEY ,
    username varchar(255),
    data varchar(255)
)