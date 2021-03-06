DROP TABLE IF EXISTS chats;
DROP TABLE IF EXISTS notifications;
DROP TABLE IF EXISTS visited_recs;
DROP TABLE IF EXISTS chatroom_invites;
DROP TABLE IF EXISTS chatroom_members;
DROP TABLE IF EXISTS chatrooms;
DROP TABLE IF EXISTS friends;
DROP TABLE IF EXISTS friend_requests;
DROP TABLE IF EXISTS internal_credentials;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id CHAR(36) PRIMARY KEY NOT NULL,
    profile TEXT,
    preferences TEXT
);

CREATE TABLE notifications (
    id CHAR(36) NOT NULL,
    userId CHAR(36) NOT NULL,
    title TEXT NOT NULL,
    at BIGINT NOT NULL,
    data TEXT NOT NULL,
    canAccept BOOLEAN,
    canReject BOOLEAN,
    PRIMARY KEY (id, userId)
);

CREATE TABLE internal_credentials (
    username VARCHAR(256) NOT NULL PRIMARY KEY,
    userId CHAR(36) NOT NULL,
    password_hash CHAR(64) NOT NULL,
    FOREIGN KEY (userId) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE friends (
    userA CHAR(36) NOT NULL,
    userB CHAR(36) NOT NULL,
    PRIMARY KEY (userA, userB),
    FOREIGN KEY (userA) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (userB) REFERENCES users(id) ON DELETE CASCADE,
    CHECK (userA < userB)
);

CREATE TABLE friend_requests (
    source CHAR(36) NOT NULL,
    target CHAR(36) NOT NULL,
    PRIMARY KEY(source, target),
    FOREIGN KEY (source) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (target) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE chatrooms (
    id CHAR(36) PRIMARY KEY NOT NULL,
    updated BIGINT NOT NULL,
    isDirectMessage BOOLEAN,
    info TEXT,
    INDEX updated_index (updated)
);

CREATE TABLE chatroom_invites (
    room CHAR(36) NOT NULL,
    source CHAR(36) NOT NULL,
    target CHAR(36) NOT NULL,
    FOREIGN KEY (room) REFERENCES chatrooms(id) ON DELETE CASCADE,
    FOREIGN KEY (source) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (target) REFERENCES users(id) ON DELETE CASCADE,
    PRIMARY KEY (room, target)
);

CREATE TABLE chatroom_members (
    room CHAR(36) NOT NULL,
    user CHAR(36) NOT NULL,
    PRIMARY KEY (room, user),
    FOREIGN KEY (room) REFERENCES chatrooms(id) ON DELETE CASCADE
);

CREATE TABLE chats (
    id INT AUTO_INCREMENT PRIMARY KEY,
    room CHAR(36) NOT NULL,
    user CHAR(36),
    at BIGINT NOT NULL,
    message TEXT,
    FOREIGN KEY (room) REFERENCES chatrooms(id) ON DELETE CASCADE,
    INDEX at_index (at)
);

CREATE TABLE visited_recs (
    userId CHAR(36) NOT NULL,
    targetId CHAR(36) NOT NULL,
    PRIMARY KEY (userId, targetId)
);