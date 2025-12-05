// Customer Orders JavaScript

const API_BASE_URL = 'http://localhost:8080/api';

document.addEventListener('DOMContentLoaded', () => {
    if (checkAuth()) {
        loadMyOrders();
    }
});

function checkAuth() {
    const userId = sessionStorage.getItem('userId');
    if (!userId) {
        window.location.href = 'login.html';
        return false;
    }
    const userName = sessionStorage.getItem('userName');
    if (userName) {
        document.getElementById('userName').textContent = userName;
    }
    return true;
}

// Use Case: SearchOrders (by userId - customer's orders only)
async function loadMyOrders() {
    showLoading(true);
    const userId = sessionStorage.getItem('userId');
    
    try {
        const response = await fetch(`${API_BASE_URL}/user/orders/${userId}`);
        const data = await response.json();
        
        showLoading(false);
        
        if (data.success && data.orders && data.orders.length > 0) {
            displayOrders(data.orders);
        } else {
            showEmptyState();
        }
    } catch (error) {
        showLoading(false);
        console.error('Error loading orders:', error);
        showAlert('Lỗi kết nối server', 'error');
    }
}

function displayOrders(orders) {
    const container = document.getElementById('ordersContainer');
    const emptyState = document.getElementById('emptyState');
    
    container.classList.remove('hidden');
    emptyState.classList.add('hidden');
    
    container.innerHTML = orders.map(order => {
        const statusClass = getStatusClass(order.orderStatus);
        const canCancel = order.orderStatus === 'PENDING';
        
        return `
        <div class="order-card">
            <div class="order-header">
                <div class="order-id">Đơn hàng #${order.orderId}</div>
                <div class="order-status ${statusClass}">${translateStatus(order.orderStatus)}</div>
            </div>
            
            <div class="order-info">
                <div class="info-item">
                    <span class="info-label">Người nhận</span>
                    <span class="info-value">${order.customerName || 'N/A'}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">Số điện thoại</span>
                    <span class="info-value">${order.customerPhone || 'N/A'}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">Địa chỉ giao hàng</span>
                    <span class="info-value">${order.shippingAddress || 'N/A'}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">Ngày đặt</span>
                    <span class="info-value">${order.formattedOrderDate || 'N/A'}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">Số lượng</span>
                    <span class="info-value">${order.totalItems || 0} mặt hàng (${order.totalQuantity || 0} sản phẩm)</span>
                </div>
            </div>
            
            <div class="order-total">
                Tổng tiền: ${order.formattedTotalAmount || 'N/A'}
            </div>
            
            <div class="order-actions">
                <button class="btn-cancel-order" 
                    ${canCancel ? '' : 'disabled'} 
                    onclick="cancelOrder(${order.orderId})">
                    ${canCancel ? 'Hủy đơn hàng' : 'Không thể hủy'}
                </button>
            </div>
        </div>
        `;
    }).join('');
}

function getStatusClass(status) {
    switch(status) {
        case 'PENDING': return 'status-pending';
        case 'CONFIRMED': return 'status-confirmed';
        case 'CANCELLED': return 'status-cancelled';
        default: return 'status-pending';
    }
}

function translateStatus(status) {
    switch(status) {
        case 'PENDING': return 'Chờ xác nhận';
        case 'CONFIRMED': return 'Đã xác nhận';
        case 'CANCELLED': return 'Đã hủy';
        default: return status;
    }
}

// Use Case: CancelOrder
async function cancelOrder(orderId) {
    if (!confirm('Bạn có chắc muốn hủy đơn hàng này?')) {
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE_URL}/user/orders/${orderId}/cancel`, {
            method: 'DELETE'
        });
        const data = await response.json();
        
        if (data.success || response.ok) {
            showAlert('Hủy đơn hàng thành công!', 'success');
            loadMyOrders();
        } else {
            showAlert(data.errorMessage || 'Không thể hủy đơn hàng', 'error');
        }
    } catch (error) {
        console.error('Error canceling order:', error);
        showAlert('Lỗi kết nối server', 'error');
    }
}

function showEmptyState() {
    document.getElementById('ordersContainer').classList.add('hidden');
    document.getElementById('emptyState').classList.remove('hidden');
}

function showLoading(show) {
    const loading = document.getElementById('loadingIndicator');
    const container = document.getElementById('ordersContainer');
    const emptyState = document.getElementById('emptyState');
    
    if (show) {
        loading.classList.remove('hidden');
        container.classList.add('hidden');
        emptyState.classList.add('hidden');
    } else {
        loading.classList.add('hidden');
    }
}

function showAlert(message, type) {
    const alertContainer = document.getElementById('alertContainer');
    alertContainer.innerHTML = `
        <div class="alert alert-${type} show">
            <span>${message}</span>
        </div>
    `;
    setTimeout(() => {
        alertContainer.innerHTML = '';
    }, 3000);
}

function logout() {
    sessionStorage.clear();
    window.location.href = 'login.html';
}
