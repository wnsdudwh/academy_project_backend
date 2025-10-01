-- alter_member_table__V1.sql

-- [OAuth2.0] 소셜 로그인을 통해 가입하는 사용자의 긴 이메일 주소를 저장하기 위해 userid 컬럼의 크기를 확장합니다.
ALTER TABLE member MODIFY COLUMN userid VARCHAR(255);

-- [OAuth2.0] 소셜 로그인 사용자는 usernumber가 없으므로, 해당 컬럼이 NULL 값을 허용하도록 수정합니다.
ALTER TABLE member MODIFY COLUMN usernumber VARCHAR(255) NULL;