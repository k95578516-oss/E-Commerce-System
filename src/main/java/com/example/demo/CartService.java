package com.example.demo;

import com.example.demo.Exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository,
                       UserRepository userRepository,
                       CartItemRepository cartItemRepository,
                       ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    // ================= GET CART =================
    public CartDTO getCartByUserId(int userId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        return convertToDTO(cart);
    }

    // ================= ADD ITEM =================
    public CartDTO addItemToCart(int userId, CartItemDTO dto) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                    Cart newCart = new Cart();
                    newCart.setUser(user);

                    return cartRepository.save(newCart); // ✅ FIX: must save first
                });

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        CartItem item = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), product.getId())
                .orElse(null);

        if (item != null) {
            item.setQuantity(item.getQuantity() + dto.getQuantity());
        } else {
            item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(dto.getQuantity());
        }

        cartItemRepository.save(item);

        return convertToDTO(getFreshCart(cart.getId())); // ✅ FIX
    }

    // ================= UPDATE ITEM =================
    public CartDTO updateCartItem(int userId, int productId, int quantity) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        CartItem item = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

        item.setQuantity(quantity);
        cartItemRepository.save(item);

        return convertToDTO(getFreshCart(cart.getId())); // ✅ FIX
    }

    // ================= REMOVE ITEM =================
    public CartDTO removeItem(int userId, int productId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        CartItem item = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

        cartItemRepository.delete(item);

        return convertToDTO(getFreshCart(cart.getId())); // ✅ FIX
    }

    // ================= CLEAR CART =================
    public void clearCart(int userId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        cartItemRepository.deleteAll(
                cartItemRepository.findByCartId(cart.getId())
        );
    }

    // ================= SAFE REFRESH =================
    private Cart getFreshCart(int cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
    }

    // ================= MAPPER =================
    private CartDTO convertToDTO(Cart cart) {

        List<CartItemDTO> items = cartItemRepository.findByCartId(cart.getId())
                .stream()
                .map(this::mapItem)
                .toList();

        CartDTO dto = new CartDTO();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUser().getId());
        dto.setItems(items);

        return dto;
    }

    private CartItemDTO mapItem(CartItem item) {

        return new CartItemDTO(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getProduct().getPrice(),
                item.getQuantity()
        );
    }
}