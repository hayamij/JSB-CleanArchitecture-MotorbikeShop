// Cart Page JavaScript

let currentCart = null;

async function loadCart() {
    const userId = sessionStorage.getItem('userId');
    const userName = sessionStorage.getItem('userName');
    const role = sessionStorage.getItem('userRole');
    
    if (!userId) {
        showAlert('Vui lòng đăng nhập để xem giỏ hàng', 'warning');
        setTimeout(() => {
            window.location.href = 'login.html';
        }, 2000);
        return;
    }

    // Update user info in navbar
    if (userName) document.getElementById('userName').textContent = userName;
    if (role === 'ADMIN') {
        const adminLink = document.getElementById('adminLink');
        if (adminLink) {
            adminLink.style.display = 'block';
            adminLink.href = 'admin.html';
        }
    }

    showLoading(true);

    try {
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
    } catch (error) {
        console.error('Error loading cart:', error);
        showAlert('Lỗi kết nối đến server', 'error');
        renderEmptyCart();
    } finally {
        showLoading(false);
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

async function updateQuantity(productId, newQuantity) {
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
    window.location.href = 'checkout.html';
}

window.onload = function() {
    loadCart();
};
