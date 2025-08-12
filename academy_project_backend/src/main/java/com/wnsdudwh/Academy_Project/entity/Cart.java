package com.wnsdudwh.Academy_Project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Cart
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 회원과 1:1 관계. 한 회원은 하나의 장바구니만 가집니다.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // Cart와 CartItem은 1:N 관계입니다. 한 장바구니에 여러 상품 항목이 들어갑니다.
    @Builder.Default    //  NPE 방지
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 장바구니 항목 추가/삭제를 위한 편의 메서드
    public void addItem(CartItem item)
    {
        cartItems.add(item);
        item.setCart(this);
    }

    public void removeItem(CartItem item)
    {
        cartItems.remove(item);
        item.setCart(null);
    }
}