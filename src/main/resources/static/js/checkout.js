// Checkout Page JavaScript

/**
 * WARNING: CLEAN ARCHITECTURE VIOLATION
 * 
 * This file contains form validation business logic that should be on backend:
 * - Phone number format validation
 * - Address length validation
 * - Required field validation
 * 
 * NOTE: Frontend validation is acceptable for UX (instant feedback),
 * but backend MUST validate again. Never trust client data.
 * 
 * TODO: Ensure backend Use Cases validate all fields independently
 */

let currentCart = null;
let currentUser = null;

function showValidationError(fieldId, message) {
    const errorElement = document.getElementById(fieldId + 'Error');
    if (message) {
        errorElement.textContent = message;
        errorElement.classList.add('show');
    } else {
        errorElement.classList.remove('show');
    }
}

async function loadCartForCheckout() {
    const userId = sessionStorage.getItem('userId');
    const userEmail = sessionStorage.getItem('email');
    const username = sessionStorage.getItem('username');
    
    if (!userId) {
        showAlert('Vui lòng đăng nhập để thanh toán', 'warning');
        setTimeout(() => {
            window.location.href = 'login.html';
        }, 2000);
        return;
    }

    currentUser = {
        userId: userId,
        email: userEmail,
        username: username
    };

    document.getElementById('userEmailDisplay').textContent = userEmail || username || 'User';
    
    // Autofill shipping information from session storage or last order
    autofillShippingInfo();

    showLoading(true);

    try {
        const response = await fetch(`/api/cart/${userId}`);
        const data = await response.json();

        if (data.success) {
            if (data.isEmpty || !data.items || data.items.length === 0) {
                showAlert('Giỏ hàng trống. Vui lòng thêm sản phẩm trước khi thanh toán', 'warning');
                setTimeout(() => {
                    window.location.href = 'home.html';
                }, 2000);
                return;
            }

            currentCart = data;
            renderOrderSummary(data);
            
            if (data.hasStockWarnings) {
                showAlert('Một số sản phẩm trong giỏ hàng có cảnh báo về tồn kho. Vui lòng kiểm tra lại', 'warning');
            }
        } else {
            showAlert(data.errorMessage || 'Không thể tải giỏ hàng', 'error');
            setTimeout(() => {
                window.location.href = 'cart.html';
            }, 2000);
        }
    } catch (error) {
        console.error('Error loading cart:', error);
        showAlert('Lỗi kết nối đến server', 'error');
        setTimeout(() => {
            window.location.href = 'cart.html';
        }, 2000);
    } finally {
        showLoading(false);
    }
}

function autofillShippingInfo() {
    const username = sessionStorage.getItem('username');
    const phone = sessionStorage.getItem('phone');
    const address = sessionStorage.getItem('address');
    
    // Try to get saved shipping info from localStorage (from previous orders)
    const savedShipping = localStorage.getItem('lastShippingInfo');
    
    if (savedShipping) {
        try {
            const shippingData = JSON.parse(savedShipping);
            // Use saved info, fallback to session info, then empty
            document.getElementById('receiverName').value = shippingData.receiverName || username || '';
            document.getElementById('phoneNumber').value = shippingData.phoneNumber || phone || '';
            document.getElementById('shippingAddress').value = shippingData.shippingAddress || address || '';
        } catch (error) {
            console.error('Error parsing saved shipping info:', error);
            // Fallback to session info
            document.getElementById('receiverName').value = username || '';
            document.getElementById('phoneNumber').value = phone || '';
            document.getElementById('shippingAddress').value = address || '';
        }
    } else {
        // No saved info, use session storage (from user account)
        document.getElementById('receiverName').value = username || '';
        document.getElementById('phoneNumber').value = phone || '';
        document.getElementById('shippingAddress').value = address || '';
    }
}

function saveShippingInfo() {
    const shippingInfo = {
        receiverName: document.getElementById('receiverName').value.trim(),
        phoneNumber: document.getElementById('phoneNumber').value.trim(),
        shippingAddress: document.getElementById('shippingAddress').value.trim()
    };
    
    localStorage.setItem('lastShippingInfo', JSON.stringify(shippingInfo));
}

function renderOrderSummary(cart) {
    const orderItemsList = document.getElementById('orderItemsList');
    
    let html = '';
    cart.items.forEach(item => {
        const hasImage = item.imageUrl && item.imageUrl.trim() !== '' && item.imageUrl !== '/images/no-image.jpg' && item.imageUrl !== 'null';
        const imageHtml = hasImage 
            ? `<img src="${item.imageUrl}" alt="${item.productName}">`
            : '';

        html += `
            <div class="order-item">
                <div class="item-image">${imageHtml}</div>
                <div class="item-details">
                    <div class="item-name">${item.productName}</div>
                    <div class="item-quantity">Số lượng: ${item.quantity}</div>
                </div>
                <div class="item-price">${formatCurrency(item.subtotal)}</div>
            </div>
        `;
    });
    
    orderItemsList.innerHTML = html;
    
    document.getElementById('summaryTotalItems').textContent = cart.totalItems;
    document.getElementById('summaryTotalQuantity').textContent = cart.totalQuantity;
    document.getElementById('summaryTotalAmount').textContent = formatCurrency(cart.totalAmount);
}

function validateForm() {
    let isValid = true;

    const receiverName = document.getElementById('receiverName').value.trim();
    if (!receiverName || receiverName.length < 2) {
        showValidationError('receiverName', 'Tên người nhận phải có ít nhất 2 ký tự');
        isValid = false;
    } else {
        showValidationError('receiverName', '');
    }

    const phoneNumber = document.getElementById('phoneNumber').value.trim();
    const phoneRegex = /^[0-9]{10,11}$/;
    if (!phoneRegex.test(phoneNumber)) {
        showValidationError('phoneNumber', 'Số điện thoại phải có 10-11 chữ số');
        isValid = false;
    } else {
        showValidationError('phoneNumber', '');
    }

    const shippingAddress = document.getElementById('shippingAddress').value.trim();
    if (!shippingAddress || shippingAddress.length < 10) {
        showValidationError('shippingAddress', 'Địa chỉ phải có ít nhất 10 ký tự');
        isValid = false;
    } else {
        showValidationError('shippingAddress', '');
    }

    return isValid;
}

async function placeOrder() {
    if (!validateForm()) {
        showAlert('Vui lòng điền đầy đủ thông tin giao hàng', 'warning');
        return;
    }

    if (!currentUser || !currentUser.userId) {
        showAlert('Không tìm thấy thông tin người dùng', 'error');
        return;
    }

    if (!currentCart || currentCart.isEmpty) {
        showAlert('Giỏ hàng trống', 'error');
        return;
    }

    const paymentMethod = document.getElementById('paymentMethod').value;
    
    // If bank transfer, show QR modal
    if (paymentMethod === 'CHUYEN_KHOAN') {
        showQRModal();
        return;
    }
    
    // For COD, proceed directly
    submitOrder();
}

function showQRModal() {
    const modal = document.getElementById('qrModal');
    modal.classList.add('active');
}

function cancelPayment() {
    const modal = document.getElementById('qrModal');
    modal.classList.remove('active');
}

function confirmPayment() {
    const modal = document.getElementById('qrModal');
    modal.classList.remove('active');
    submitOrder();
}

async function submitOrder() {
    const formData = {
        userId: parseInt(currentUser.userId),
        receiverName: document.getElementById('receiverName').value.trim(),
        phoneNumber: document.getElementById('phoneNumber').value.trim(),
        shippingAddress: document.getElementById('shippingAddress').value.trim(),
        note: document.getElementById('note').value.trim() || null,
        paymentMethod: document.getElementById('paymentMethod').value
    };

    const placeOrderBtn = document.querySelector('.btn-place-order');
    placeOrderBtn.disabled = true;
    placeOrderBtn.textContent = 'Đang xử lý...';

    try {
        const response = await fetch('/api/orders/checkout', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        });

        const data = await response.json();

        if (data.success) {
            // Save shipping info for next time
            saveShippingInfo();
            
            showAlert(data.message || 'Đặt hàng thành công!', 'success');
            
            setTimeout(() => {
                alert(`Đơn hàng đã được đặt thành công!\n\n` +
                      `Mã đơn hàng: #${data.orderId}\n` +
                      `Người nhận: ${data.customerName}\n` +
                      `SĐT: ${data.customerPhone}\n` +
                      `Địa chỉ: ${data.shippingAddress}\n` +
                      `Trạng thái: ${data.orderStatus}\n\n` +
                      `Cảm ơn bạn đã mua hàng!`);
                
                window.location.href = 'home.html';
            }, 1000);
        } else {
            showAlert(data.errorMessage || 'Không thể đặt hàng', 'error');
            placeOrderBtn.disabled = false;
            placeOrderBtn.textContent = 'Đặt hàng';
        }
    } catch (error) {
        console.error('Error placing order:', error);
        showAlert('Lỗi kết nối đến server', 'error');
        placeOrderBtn.disabled = false;
        placeOrderBtn.textContent = 'Đặt hàng';
    }
}

window.onload = function() {
    // Initialize navbar and footer (requires authentication)
    initNavbar('cart', true);
    initFooter();
    updateCartBadge();
    loadCartForCheckout();
};
