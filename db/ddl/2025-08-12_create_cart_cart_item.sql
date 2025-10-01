-- ======================================
-- 장바구니 테이블 생성
-- 각 회원당 하나의 장바구니 (1:1)
-- ======================================
CREATE TABLE cart (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,       -- 장바구니 고유 ID
    member_id INT NOT NULL,                     -- FK: 회원 ID
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,  -- 생성일
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 수정일

    CONSTRAINT fk_cart_member FOREIGN KEY (member_id)
        REFERENCES member(idx)                  -- member 엔티티의 PK
        ON DELETE CASCADE                        -- 회원 삭제 시 장바구니 삭제
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ======================================
-- 장바구니 아이템 테이블 생성
-- 장바구니에 담긴 개별 상품 정보
-- ======================================
CREATE TABLE cart_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,       -- 장바구니 아이템 고유 ID
    cart_id BIGINT NOT NULL,                    -- FK: cart.id
    product_id BIGINT NOT NULL,                  -- FK: product.id
    product_option_id BIGINT DEFAULT NULL,       -- FK: product_option.id (nullable)
    quantity INT NOT NULL DEFAULT 1,            -- 수량
    selected BOOLEAN NOT NULL DEFAULT TRUE,     -- 결제 대상 여부
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,  -- 생성일
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 수정일

    -- FK 설정
    CONSTRAINT fk_cart_item_cart FOREIGN KEY (cart_id)
        REFERENCES cart(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_cart_item_product FOREIGN KEY (product_id)
        REFERENCES product(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_cart_item_option FOREIGN KEY (product_option_id)
        REFERENCES product_option(id)
        ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;