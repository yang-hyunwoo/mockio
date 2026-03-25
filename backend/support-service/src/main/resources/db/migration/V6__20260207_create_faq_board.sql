CREATE TABLE faq_board (
       id BIGSERIAL PRIMARY KEY,
       user_id VARCHAR(255),
       question VARCHAR(200) NOT NULL,
       answer TEXT NOT NULL,
       faq_type VARCHAR(20),
       sort INTEGER NOT NULL,
       visible BOOLEAN NOT NULL,
       deleted BOOLEAN NOT NULL,
       deleted_at TIMESTAMPTZ,
       created_by VARCHAR(255),
       updated_by VARCHAR(255),
       created_at TIMESTAMPTZ NOT NULL,
       updated_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_faq_board_question ON faq_board (question);

CREATE INDEX idx_faq_board_deleted ON faq_board (deleted);

CREATE INDEX idx_faq_board_visible_deleted
    ON faq_board (visible, deleted);

INSERT INTO FAQ_BOARD
(user_id, question, answer, faq_type, sort, visible, deleted, deleted_at, created_by, updated_by, created_at, updated_at)
VALUES
-- ACCOUNT
('admin', '비밀번호를 잊어버렸어요.', '로그인 화면에서 "비밀번호 찾기"를 선택하고 이메일 인증을 진행하면 비밀번호를 재설정할 수 있습니다.', 'ACCOUNT', 1, true, false, NULL, 'admin', 'admin', NOW(), NOW()),
('admin', '이메일을 변경할 수 있나요?', '마이페이지 > 계정 설정에서 이메일 변경을 진행할 수 있습니다.', 'ACCOUNT', 2, true, false, NULL, 'admin', 'admin', NOW(), NOW()),
-- INTERVIEW
('admin', '면접은 몇 번까지 연습할 수 있나요?', '플랜에 따라 연습 가능한 면접 횟수가 다릅니다. 무료 사용자는 제한된 횟수만 이용할 수 있습니다.', 'INTERVIEW', 1, true, false, NULL, 'admin', 'admin', NOW(), NOW()),
('admin', '면접 질문은 어떻게 생성되나요?', '면접 질문은 AI가 지원 직무와 기술 스택을 기반으로 자동 생성합니다.', 'INTERVIEW', 2, true, false, NULL, 'admin', 'admin', NOW(), NOW()),
-- FEEDBACK
('admin', 'AI 피드백은 언제 받을 수 있나요?', '면접 답변을 제출하면 AI가 분석 후 몇 초 내에 피드백을 제공합니다.', 'FEEDBACK', 1, true, false, NULL, 'admin', 'admin', NOW(), NOW()),
('admin', 'AI 피드백 점수는 어떻게 계산되나요?', '답변의 구조, 기술 정확도, 전달력 등을 종합적으로 분석하여 점수를 계산합니다.', 'FEEDBACK', 2, true, false, NULL, 'admin', 'admin', NOW(), NOW()),
-- PAYMENT
('admin', '결제는 어떤 방식으로 진행되나요?', '신용카드 및 간편결제를 통해 결제할 수 있습니다.', 'PAYMENT', 1, true, false, NULL, 'admin', 'admin', NOW(), NOW()),
('admin', '결제 후 환불이 가능한가요?', '결제 후 사용하지 않은 경우 고객센터를 통해 환불 요청이 가능합니다.', 'PAYMENT', 2, true, false, NULL, 'admin', 'admin', NOW(), NOW()),
-- TECHNICAL
('admin', '카메라가 작동하지 않습니다.', '브라우저에서 카메라 권한이 허용되어 있는지 확인해 주세요.', 'TECHNICAL', 1, true, false, NULL, 'admin', 'admin', NOW(), NOW()),
('admin', '마이크가 인식되지 않습니다.', '브라우저 설정에서 마이크 접근 권한을 허용해야 정상적으로 작동합니다.', 'TECHNICAL', 2, true, false, NULL, 'admin', 'admin', NOW(), NOW()),
-- ETC
('admin', '서비스 이용 중 오류가 발생했습니다.', '문제가 지속되면 고객센터로 문의해 주세요.', 'ETC', 1, true, false, NULL, 'admin', 'admin', NOW(), NOW()),
('admin', '문의는 어디에서 할 수 있나요?', '마이페이지 > 고객센터 메뉴에서 문의를 등록할 수 있습니다.', 'ETC', 2, true, false, NULL, 'admin', 'admin', NOW(), NOW());
