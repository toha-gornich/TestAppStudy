-- Users table
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       first_name VARCHAR(255) NOT NULL,
                       last_name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       role VARCHAR(50) NOT NULL CHECK (role IN ('TUTOR', 'STUDENT'))
);

-- Tests table
CREATE TABLE IF NOT EXISTS tests (
                                     id BIGSERIAL PRIMARY KEY,
                                     subject VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    tutor_id BIGINT NOT NULL,
    is_active BOOLEAN DEFAULT false,
    FOREIGN KEY (tutor_id) REFERENCES users(id)
    );

-- Questions table
CREATE TABLE IF NOT EXISTS questions (
    id BIGSERIAL PRIMARY KEY,
    text TEXT NOT NULL,
    type VARCHAR(50) NOT NULL CHECK (type IN ('SINGLE', 'MULTIPLE')),
    test_id BIGINT NOT NULL,
    FOREIGN KEY (test_id) REFERENCES tests(id) ON DELETE CASCADE
    );

-- Answers table
CREATE TABLE IF NOT EXISTS answers (
    id BIGSERIAL PRIMARY KEY,
    text TEXT NOT NULL,
    is_correct BOOLEAN NOT NULL,
    question_id BIGINT NOT NULL,
    FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE
    );

-- Test Results table
CREATE TABLE IF NOT EXISTS test_results (
    id BIGSERIAL PRIMARY KEY,
    test_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    score INTEGER NOT NULL,
    completion_date TIMESTAMP NOT NULL,
    FOREIGN KEY (test_id) REFERENCES tests(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
    );

-- User Answers table
CREATE TABLE IF NOT EXISTS user_answers (
    test_result_id BIGINT NOT NULL,
    question_id BIGINT NOT NULL,
                                            answer_id BIGINT NOT NULL,
                                            PRIMARY KEY (test_result_id, question_id, answer_id),
    FOREIGN KEY (test_result_id) REFERENCES test_results(id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES questions(id),
    FOREIGN KEY (answer_id) REFERENCES answers(id)
    );

-- Indexes
CREATE INDEX IF NOT EXISTS idx_tests_tutor ON tests(tutor_id);
CREATE INDEX IF NOT EXISTS idx_questions_test ON questions(test_id);
CREATE INDEX IF NOT EXISTS idx_answers_question ON answers(question_id);
CREATE INDEX IF NOT EXISTS idx_test_results_test ON test_results(test_id);
CREATE INDEX IF NOT EXISTS idx_test_results_user ON test_results(user_id);