CREATE TABLE IF NOT EXISTS users(
    id TEXT PRIMARY KEY UNIQUE NOT NULL,
    login TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    full_name TEXT NOT NULL,
    role TEXT NOT NULL,
    photo BYTEA
);

CREATE TABLE IF NOT EXISTS tags (
    id TEXT PRIMARY KEY UNIQUE NOT NULL,
    name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS articles (
      id TEXT PRIMARY KEY UNIQUE NOT NULL,
      title TEXT,
      content TEXT,
      created_date TIMESTAMP,
      date_modified TIMESTAMP,
      user_id TEXT,
      tag_id TEXT,
      external_references TEXT,
      image_description TEXT,
      image BYTEA,
      FOREIGN KEY (user_id) REFERENCES users(id),
      FOREIGN KEY (tag_id ) REFERENCES tags(id)
);

CREATE TABLE IF NOT EXISTS article_tags (
      article_id TEXT,
      tag_id TEXT,
      FOREIGN KEY (article_id) REFERENCES articles(id) ON DELETE CASCADE,
      FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE,
      PRIMARY KEY (article_id, tag_id)
);
CREATE TABLE IF NOT EXISTS attachments (
     id TEXT PRIMARY KEY UNIQUE NOT NULL,
     article_id TEXT,
     name_file VARCHAR(255),
     content BYTEA NOT NULL,
     type_file VARCHAR(255) NOT NULL,
     FOREIGN KEY (article_id) REFERENCES articles(id)
);

CREATE TABLE IF NOT EXISTS attachment_articles (
     attachment_id TEXT,
     article_id TEXT,
     FOREIGN KEY (attachment_id) REFERENCES attachments(id) ON DELETE CASCADE,
     FOREIGN KEY (article_id) REFERENCES articles(id) ON DELETE CASCADE,
     PRIMARY KEY (attachment_id, article_id)
);

CREATE TABLE IF NOT EXISTS
favorites_user (
	user_id TEXT NOT NULL,
	article_id TEXT NOT NULL,
	PRIMARY KEY (user_id,article_id),
	FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
	FOREIGN KEY (article_id) REFERENCES articles(id) ON DELETE CASCADE
	
);

