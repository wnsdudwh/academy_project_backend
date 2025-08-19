package com.wnsdudwh.Academy_Project.service.impl;

import com.wnsdudwh.Academy_Project.dto.CartAddRequestDTO;
import com.wnsdudwh.Academy_Project.dto.CartResponseDTO;
import com.wnsdudwh.Academy_Project.entity.*;
import com.wnsdudwh.Academy_Project.exception.ResourceNotFoundException;
import com.wnsdudwh.Academy_Project.repository.*;
import com.wnsdudwh.Academy_Project.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartServiceImpl implements CartService
{
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;

    @Override
    @Transactional
    public void addItemToCart(Long memberId, CartAddRequestDTO requestDTO)
    {
        // TODO: 실제 장바구니 추가 로직 구현
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 회원을 찾을 수 없습니다. ID : " + memberId));

        Product product = productRepository.findById(requestDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("해당 상품을 찾을 수 없습니다. ID : " + requestDTO.getProductId()));

        // 2. 회원의 장바구니 조회 또는 생성
        Cart cart = cartRepository.findByMember(member).orElseGet(() -> {
            Cart newCart = Cart.builder().member(member).build();
            return cartRepository.save(newCart);
        });

        // 3. 옵션 ID 유무에 따른 분기 처리
        if (requestDTO.getOptionId() != null) {
            // 3-1. 옵션이 있는 경우
            ProductOption option = productOptionRepository.findById(requestDTO.getOptionId())
                    .orElseThrow(() -> new ResourceNotFoundException("해당 옵션을 찾을 수 없습니다. ID: " + requestDTO.getOptionId()));

            Optional<CartItem> existingCartItem = cartItemRepository.findByCartAndProductAndProductOption(cart, product, option);

            if (existingCartItem.isPresent())
            {
                // 이미 상품이 있다면 수량만 증가
                CartItem item = existingCartItem.get();
                item.setQuantity(item.getQuantity() + requestDTO.getQuantity());
            }
            else
            {
                // 없는 상품이면 새로 추가
                CartItem newCartItem = CartItem.builder()
                        .cart(cart)
                        .product(product)
                        .productOption(option) // 옵션 추가
                        .quantity(requestDTO.getQuantity())
                        .selected(true)
                        .build();
                cartItemRepository.save(newCartItem);
            }
        }
        else
        {
            // 3-2. 옵션이 없는 경우
            Optional<CartItem> existingCartItem = cartItemRepository.findByCartAndProductAndProductOptionIsNull(cart, product);

            if (existingCartItem.isPresent())
            {
                // 이미 상품이 있다면 수량만 증가
                CartItem item = existingCartItem.get();
                item.setQuantity(item.getQuantity() + requestDTO.getQuantity());

            }
            else
            {
                // 없는 상품이면 새로 추가
                CartItem newCartItem = CartItem.builder()
                        .cart(cart)
                        .product(product)
                        // 옵션은 없음 (null)
                        .quantity(requestDTO.getQuantity())
                        .selected(true)
                        .build();
                cartItemRepository.save(newCartItem);
            }
        }
    }        // (addItemToCart)

    @Override
    public CartResponseDTO getCartByMemberId(Long memberId)
    {
        // TODO: 실제 장바구니 조회 로직 구현
        return null;
    }        // (getCartByMemberId)
}
