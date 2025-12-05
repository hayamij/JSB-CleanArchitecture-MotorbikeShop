// Cart Page JavaScript

let currentCart = null;

async function loadCart() {
    const userId = sessionStorage.getItem('userId');
    
    showLoading(true);

    try {
        if (userId) {
            // Load cart from server for logged-in users
            const response = await fetch(`/api/cart/${userId}`);
            const data = await response.json();

            if (data.success) {
                currentCart = data;
                renderCart(data);
                if (data.hasStockWarnings) {
                    showAlert('Một số sản phẩm trong giỏ hàng có cảnh báo về tồn kho', 'warning');
                }
            } else {
                showAlert(data.errorMessage || 'Không thể tải giỏ hàng', 'error');
                renderEmptyCart();
            }
        } else {
            // Load guest cart from localStorage
            await loadGuestCart();
        }
    } catch (error) {
        console.error('Error loading cart:', error);
        showAlert('Lỗi kết nối đến server', 'error');
        renderEmptyCart();
    } finally {
        showLoading(false);
    }
}

async function loadGuestCart() {
    const guestCart = getGuestCart();
    
    if (!guestCart || guestCart.length === 0) {
        renderEmptyCart();
        return;
    }
    
    // Fetch all products from server
    try {
        const response = await fetch('/api/products');
        const allProducts = await response.json();
        
        // Create a map for quick lookup
        const productMap = {};
        allProducts.forEach(product => {
            productMap[product.id] = product;
        });
        
        // Build cart object similar to server response
        const cartItems = guestCart.map(item => {
            const product = productMap[item.productId];
            if (!product) return null;
            
            const subtotal = product.price * item.quantity;
            const hasStockIssue = item.quantity > product.stock;
            
            return {
                productId: item.productId,
                productName: product.name,
                price: product.price,
                quantity: item.quantity,
                subtotal: subtotal,
                availableStock: product.stock,
                imageUrl: product.imageUrl,
                hasStockIssue: hasStockIssue
            };
        }).filter(item => item !== null);
        
        const totalAmount = cartItems.reduce((sum, item) => sum + item.subtotal, 0);
        const totalQuantity = cartItems.reduce((sum, item) => sum + item.quantity, 0);
        
        currentCart = {
            items: cartItems,
            totalItems: cartItems.length,
            totalQuantity: totalQuantity,
            totalAmount: totalAmount,
            isEmpty: cartItems.length === 0,
            isGuest: true
        };
        
        renderCart(currentCart);
    } catch (error) {
        console.error('Error loading guest cart:', error);
        showAlert('Không thể tải thông tin sản phẩm', 'error');
        renderEmptyCart();
    }
}

function renderCart(cart) {
    const cartItemsList = document.getElementById('cartItemsList');
    const cartContainer = document.getElementById('cartContainer');

    if (cart.isEmpty || !cart.items || cart.items.length === 0) {
        renderEmptyCart();
        return;
    }

    let html = '<div class="cart-items">';

    cart.items.forEach(item => {
        const hasStockIssue = item.hasStockIssue;
        const stockClass = hasStockIssue ? 'stock-warning' : '';

        const hasImage = item.imageUrl && item.imageUrl.trim() !== '' && item.imageUrl !== '/images/no-image.jpg' && item.imageUrl !== 'null';
        const imageUrl = hasImage ? item.imageUrl : '';
        
        html += `
            <div class="cart-item ${stockClass}">
                <div class="product-image">
                    ${hasImage ? `<img src="${imageUrl}" alt="${item.productName}">` : ''}
                </div>
                
                <div class="product-info">
                    <div class="product-name">${item.productName}</div>
                    <div class="product-price">${formatCurrency(item.price)} / sản phẩm</div>
                    <div class="product-stock">
                        Còn lại: ${item.availableStock} sản phẩm
                        ${hasStockIssue ? '<span class="stock-badge badge-warning">Vượt tồn kho</span>' : ''}
                    </div>
                </div>
                
                <div class="quantity-controls">
                    <button class="quantity-btn" onclick="decreaseQuantity(${item.productId}, ${item.quantity})">−</button>
                    <input type="number" class="quantity-input" id="qty-${item.productId}" value="${item.quantity}" min="0" onchange="applyQuantityChange(${item.productId})">
                    <button class="quantity-btn" onclick="increaseQuantity(${item.productId}, ${item.quantity})">+</button>
                </div>
                
                <div class="item-actions">
                    <div class="item-subtotal">${formatCurrency(item.subtotal)}</div>
                    <button class="btn-remove" onclick="removeItem(${item.productId})">Xóa</button>
                </div>
            </div>
        `;
    });

    html += '</div>';
    cartItemsList.innerHTML = html;

    document.getElementById('summaryTotalItems').textContent = cart.totalItems;
    document.getElementById('summaryTotalQuantity').textContent = cart.totalQuantity;
    document.getElementById('summaryTotalAmount').textContent = formatCurrency(cart.totalAmount);

    cartContainer.classList.remove('hidden');
}

function renderEmptyCart() {
    const cartContainer = document.getElementById('cartContainer');
    cartContainer.innerHTML = `
        <div class="cart-items-section">
            <div class="cart-empty">
                <h2>Giỏ hàng trống</h2>
                <p>Bạn chưa có sản phẩm nào trong giỏ hàng</p>
                <button class="btn-checkout" onclick="window.location.href='home.html'">
                    Bắt đầu mua sắm
                </button>
            </div>
        </div>
    `;
    cartContainer.classList.remove('hidden');
}

async function increaseQuantity(productId, currentQuantity) {
    const newQuantity = currentQuantity + 1;
    await updateQuantity(productId, newQuantity);
}

async function decreaseQuantity(productId, currentQuantity) {
    const newQuantity = currentQuantity - 1;
    await updateQuantity(productId, newQuantity);
}

async function applyQuantityChange(productId) {
    const input = document.getElementById(`qty-${productId}`);
    if (!input) return;

    const newQuantity = parseInt(input.value);
    await updateQuantity(productId, newQuantity);
}

function updateGuestCartQuantity(productId, newQuantity) {
    const guestCart = getGuestCart();
    const itemIndex = guestCart.findIndex(item => item.productId === productId);
    
    if (itemIndex !== -1) {
        if (newQuantity <= 0) {
            guestCart.splice(itemIndex, 1);
        } else {
            guestCart[itemIndex].quantity = newQuantity;
        }
        saveGuestCart(guestCart);
    }
    
    loadGuestCart();
    updateCartBadge();
}

async function updateQuantity(productId, newQuantity) {
    const userId = sessionStorage.getItem('userId');
    
    if (!userId) {
        // Update guest cart
        updateGuestCartQuantity(productId, newQuantity);
        showAlert(newQuantity === 0 ? 'Đã xóa sản phẩm' : 'Đã cập nhật', 'success');
        return;
    }
    
    // Update server cart for logged-in users
    if (!currentCart || !currentCart.cartId) {
        showAlert('Không tìm thấy thông tin giỏ hàng', 'error');
        return;
    }

    try {
        const response = await fetch('/api/cart/update', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                cartId: parseInt(currentCart.cartId),
                productId: productId,
                newQuantity: newQuantity
            })
        });

        const data = await response.json();

        if (data.success) {
            showAlert(data.message || 'Đã cập nhật', 'success');
            loadCart();
            updateCartBadge();
        } else {
            showAlert(data.errorMessage || 'Không thể cập nhật', 'error');
        }
    } catch (error) {
        console.error('Error updating quantity:', error);
        showAlert('Lỗi kết nối đến server', 'error');
    }
}

async function removeItem(productId) {
    if (!confirm('Bạn có chắc muốn xóa sản phẩm này khỏi giỏ hàng?')) {
        return;
    }

    await updateQuantity(productId, 0);
}

async function checkout() {
    const userId = sessionStorage.getItem('userId');
    
    if (!userId) {
        // Guest user - redirect to login and set flag to return to checkout
        sessionStorage.setItem('returnToCheckout', 'true');
        showAlert('Vui lòng đăng nhập để tiếp tục thanh toán', 'info');
        setTimeout(() => {
            window.location.href = 'login.html';
        }, 1500);
        return;
    }
    
    // Logged-in user - proceed to checkout
    window.location.href = 'checkout.html';
}

window.onload = function() {
    // Initialize navbar and footer (no authentication required for cart view)
    initNavbar('cart', false);
    initFooter();
    loadCart();
    updateCartBadge();
};
