DROP TABLE IF EXISTS pop_balloon_event_invitations;
DROP TABLE IF EXISTS pop_balloon_event_participation;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       ab_group ENUM('A', 'B') NOT NULL,
                       level INT NOT NULL DEFAULT 1,
                       coins INT NOT NULL DEFAULT 2000,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       version INT NOT NULL DEFAULT 0,
                       INDEX idx_level (level),
                       INDEX idx_ab_group (ab_group)
);

CREATE TABLE pop_balloon_event_participation (
                                                 id INT AUTO_INCREMENT PRIMARY KEY,
                                                 user_id INT NOT NULL,
                                                 partner_user_id INT,
                                                 has_partner BOOLEAN DEFAULT FALSE,
                                                 helium_collected INT DEFAULT 0,
                                                 balloon_inflated_amount INT DEFAULT 0,
                                                 has_popped BOOLEAN DEFAULT FALSE,
                                                 reward_claimed BOOLEAN DEFAULT FALSE,
                                                 event_date DATE NOT NULL,
                                                 FOREIGN KEY (user_id) REFERENCES users(id),
                                                 FOREIGN KEY (partner_user_id) REFERENCES users(id)
);

CREATE TABLE pop_balloon_event_invitations (
                                               id INT AUTO_INCREMENT PRIMARY KEY,
                                               inviter_user_id INT NOT NULL,
                                               invitee_user_id INT NOT NULL,
                                               status ENUM('PENDING','ACCEPTED','REJECTED') DEFAULT 'PENDING',
                                               event_date DATE NOT NULL,
                                               FOREIGN KEY (inviter_user_id) REFERENCES users(id),
                                               FOREIGN KEY (invitee_user_id) REFERENCES users(id)
);

-- Removed idx_users_level to avoid duplication, my bad!
CREATE INDEX idx_invitations_invitee ON pop_balloon_event_invitations(invitee_user_id);
CREATE INDEX idx_participation_user ON pop_balloon_event_participation(user_id);
