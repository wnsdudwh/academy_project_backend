package com.wnsdudwh.Academy_Project.service.impl;

import com.wnsdudwh.Academy_Project.dto.CartAddRequestDTO;
import com.wnsdudwh.Academy_Project.dto.CartItemResponseDTO;
import com.wnsdudwh.Academy_Project.dto.CartResponseDTO;
import com.wnsdudwh.Academy_Project.entity.*;
import com.wnsdudwh.Academy_Project.exception.ResourceNotFoundException;
import com.wnsdudwh.Academy_Project.repository.*;
import com.wnsdudwh.Academy_Project.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        if (requestDTO.getOptionId() != null)
        {
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
        // 1. 회원 ID로 Member 객체 먼저 찾기
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 회원을 찾을 수 없습니다." + memberId));

        // 2. 찾아온 Member 객체로 장바구니(Cart) 찾기
        Cart cart = cartRepository.findByMember(member)
                .orElse(null);  // 장바구니가 없을 수 있으니 orElse(null)로 처리

        if (cart == null || cart.getCartItems().isEmpty())  //  장바구니가 없거나, 비어 있을 경우
        {
            return new CartResponseDTO();   // 비어있는 CartResponseDTO 반환
        }

        // 3. CartItem 엔티티 목록을 CartItemResponseDTO 목록으로 변환
        List<CartItemResponseDTO> itemDTOs = cart.getCartItems().stream()
                .map(CartItemResponseDTO::fromEntity)
                .collect(Collectors.toList());

        // 4. 최종 CartResponseDTO 조립
        return CartResponseDTO.builder()
                .cartId(cart.getId())
                .items(itemDTOs)
                .totalAmount(calculateTotalAmount(itemDTOs))
                .build();
    }        // (getCartByMemberId)

    // 총액을 계산하는 private 헬퍼 메서드
    private int calculateTotalAmount(List<CartItemResponseDTO> items)
    {
        return items.stream()
                .mapToInt(CartItemResponseDTO::getTotalPrice)
                .sum();
    }

    @Override
    @Transactional
    public void updateItemQuantity(Long memberId, Long cartItemId, int quantity)
    {
        // 1. 장바구니 상품(CartItem) 조회
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 장바구니 상품을 찾을 수 없습니다. ID: " + cartItemId));

        // 2. ⭐ 소유권 확인: 이 장바구니 상품이 현재 로그인한 회원의 것인지 확인
        if (!cartItem.getCart().getMember().getIdx().equals(memberId)) {
            throw new SecurityException("수정 권한이 없습니다."); // 혹은 다른 적절한 예외
        }

        // 3. 수량 업데이트
        cartItem.setQuantity(quantity);
        // @Transactional에 의해 메서드 종료 시 자동으로 DB에 업데이트
    }

    @Override
    @Transactional
    public void deleteCartItem(Long memberId, Long cartItemId)
    {
        // 1. 장바구니 상품(CartItem) 조회
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 장바구니 상품을 찾을 수 없습니다. ID: " + cartItemId));

        // 2. ⭐ 소유권 확인
        if (!cartItem.getCart().getMember().getIdx().equals(memberId))
        {
            throw new SecurityException("삭제 권한이 없습니다.");
        }

        // 3. 상품 삭제
        cartItemRepository.delete(cartItem);
    }


}
