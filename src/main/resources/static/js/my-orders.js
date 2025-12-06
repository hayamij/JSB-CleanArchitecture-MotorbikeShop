// My Orders Page Logic
const API_BASE_URL = 'http://localhost:8080/api';

// DOM Elements
const loadingIndicator = document.getElementById('loadingIndicator');
const ordersContainer = document.getElementById('ordersContainer');
const emptyState = document.getElementById('emptyState');
const ordersList = document.getElementById('ordersList');
const alertContainer = document.getElementById('alertContainer');

// Stats elements
const totalOrdersEl = document.getElementById('totalOrders');
const processingOrdersEl = document.getElementById('processingOrders');
const completedOrdersEl = document.getElementById('completedOrders');
const cancelledOrdersEl = document.getElementById('cancelledOrders');

// Initialize page
document.addEventListener('DOMContentLoaded', function() {
    checkAuthAndLoadOrders();
    updateUserGreeting();
});

// Check authentication and load orders
function checkAuthAndLoadOrders() {
    const userId = sessionStorage.getItem('userId');
    
    if (!userId) {
        window.location.href = 'login.html';
        return;
    }

    showCancelSuccessIfNeeded();

    loadMyOrders(userId);
}

// Load user's orders
async function loadMyOrders(userId) {
    showLoading();

    try {
        const response = await fetch(`${API_BASE_URL}/user/orders/${userId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        const data = await response.json();
        console.log('Orders response:', data); // Debug log

        if (data.success && data.orders && data.orders.length > 0) {
            displayOrders(data.orders);
            updateStats(data.orders);
        } else {
            console.log('No orders or error:', data.message); // Debug log
            updateStats([]); // Update stats với empty array
            showEmptyState();
        }

    } catch (error) {
        console.error('Error loading orders:', error);
        showAlert('Không thể tải đơn hàng. Vui lòng thử lại!', 'error');
        updateStats([]); // Update stats với empty array
        showEmptyState();
    } finally {
        hideLoading();
    }
}

// Custom confirm modal (reuse style across site)
let confirmModalOverlay = null;

function ensureConfirmModal() {
    if (confirmModalOverlay) return confirmModalOverlay;
    const overlay = document.createElement('div');
    overlay.className = 'modal-overlay';
    overlay.innerHTML = `
        <div class="modal" role="dialog" aria-modal="true">
            <h3 class="modal-title">Xác nhận hủy đơn</h3>
            <p class="modal-message" id="confirmMessage">Bạn có chắc chắn?</p>
            <div class="modal-actions">
                <button type="button" class="btn-secondary" id="confirmCancelBtn">Để sau</button>
                <button type="button" class="btn-danger" id="confirmOkBtn">Hủy đơn</button>
            </div>
        </div>
    `;
    document.body.appendChild(overlay);
    confirmModalOverlay = overlay;
    return overlay;
}

function showConfirmModal(message) {
    return new Promise(resolve => {
        const overlay = ensureConfirmModal();
        const msgEl = overlay.querySelector('#confirmMessage');
        const okBtn = overlay.querySelector('#confirmOkBtn');
        const cancelBtn = overlay.querySelector('#confirmCancelBtn');

        msgEl.textContent = message || 'Bạn có chắc chắn?';
        overlay.classList.add('show');

        const cleanup = (result) => {
            overlay.classList.remove('show');
            document.removeEventListener('keydown', onEsc);
            resolve(result);
        };

        const onEsc = (e) => {
            if (e.key === 'Escape') {
                cleanup(false);
            }
        };

        okBtn.onclick = () => cleanup(true);
        cancelBtn.onclick = () => cleanup(false);
        document.addEventListener('keydown', onEsc);
    });
}

// Display orders list
function displayOrders(orders) {
    ordersList.innerHTML = '';

    orders.forEach(order => {
        const orderCard = createOrderCard(order);
        ordersList.appendChild(orderCard);
    });

    ordersContainer.classList.remove('hidden');
    emptyState.classList.add('hidden');
}

// Create order card element
function createOrderCard(order) {
    const card = document.createElement('div');
    card.className = 'order-card';

    // Check if order can be cancelled (only if waiting for confirmation)
    const canCancelOrder = order.orderStatus === 'Chờ xác nhận';
    const cancelButtonHTML = canCancelOrder ? `
        <button class="btn-cancel" data-action="cancel-order" data-order-id="${order.orderId}">
            Hủy đơn hàng
        </button>
    ` : '';

    const editButtonHTML = canCancelOrder ? `
        <button class="btn-edit" data-action="edit-shipping">
            Sửa thông tin giao hàng
        </button>
    ` : '';

    card.innerHTML = `
        <div class="order-header">
            <div class="order-id">Đơn hàng #${order.orderId}</div>
            <div class="order-status status-${order.statusColor}">${order.orderStatus}</div>
        </div>

        <div class="order-body">
            <div class="order-info-item">
                <span class="info-label">Người nhận</span>
                <span class="info-value">${order.customerName}</span>
            </div>

            <div class="order-info-item">
                <span class="info-label">Số điện thoại</span>
                <span class="info-value">${order.customerPhone}</span>
            </div>

            <div class="order-info-item">
                <span class="info-label">Địa chỉ giao hàng</span>
                <span class="info-value">${order.shippingAddress}</span>
            </div>

            <div class="order-info-item">
                <span class="info-label">Ngày đặt</span>
                <span class="info-value">${order.formattedOrderDate}</span>
            </div>
        </div>

        <div class="order-items-summary">
            📦 ${order.totalItems} loại sản phẩm • Tổng ${order.totalQuantity} sản phẩm
        </div>

        <div class="order-footer">
            <div class="order-total">${order.formattedTotalAmount}</div>
            <div class="order-actions">
                ${editButtonHTML}
                ${cancelButtonHTML}
            </div>
        </div>
    `;

    card.addEventListener('click', (e) => {
        if (e.target.closest('[data-action]')) return;
        goToDetailPage(order.orderId);
    });
    card.style.cursor = 'pointer';

    if (canCancelOrder) {
        const editBtn = card.querySelector('[data-action="edit-shipping"]');
        const cancelBtn = card.querySelector('[data-action="cancel-order"]');

        if (editBtn) {
            editBtn.addEventListener('click', (e) => {
                e.stopPropagation();
                goToEditPage(order.orderId);
            });
        }
        if (cancelBtn) {
            cancelBtn.addEventListener('click', (e) => {
                e.stopPropagation();
                cancelOrder(order.orderId);
            });
        }
    }

    return card;
}

function goToEditPage(orderId) {
    const userId = sessionStorage.getItem('userId');
    if (!userId) {
        showAlert('Lỗi: Không tìm thấy thông tin người dùng!', 'error');
        return;
    }
    window.location.href = `edit-order.html?orderId=${orderId}`;
}

function goToDetailPage(orderId) {
    window.location.href = `order-detail.html?orderId=${orderId}&from=user`;
}

// Update statistics
function updateStats(orders) {
    const stats = {
        total: orders.length,
        processing: 0,
        completed: 0,
        cancelled: 0
    };

    orders.forEach(order => {
        const status = order.orderStatus;
        
        if (status === 'Chờ xác nhận' || status === 'Đã xác nhận' || status === 'Đang giao hàng') {
            stats.processing++;
        } else if (status === 'Đã giao hàng') {
            stats.completed++;
        } else if (status === 'Đã hủy') {
            stats.cancelled++;
        }
    });

    totalOrdersEl.textContent = stats.total;
    processingOrdersEl.textContent = stats.processing;
    completedOrdersEl.textContent = stats.completed;
    cancelledOrdersEl.textContent = stats.cancelled;
}

// Show empty state
function showEmptyState() {
    ordersContainer.classList.add('hidden');
    emptyState.classList.remove('hidden');
}

// Loading state
function showLoading() {
    loadingIndicator.classList.remove('hidden');
    ordersContainer.classList.add('hidden');
    emptyState.classList.add('hidden');
}

function hideLoading() {
    loadingIndicator.classList.add('hidden');
}

// Alert functions
function showAlert(message, type = 'success') {
    const alert = document.createElement('div');
    alert.className = `alert alert-${type} show`;
    alert.textContent = message;
    
    alertContainer.appendChild(alert);
    
    setTimeout(() => {
        alert.classList.remove('show');
        setTimeout(() => alert.remove(), 300);
    }, 3000);
}

// Hiển thị thông báo hủy thành công khi quay lại từ trang chi tiết
function showCancelSuccessIfNeeded() {
    const params = new URLSearchParams(window.location.search);
    if (params.get('cancelSuccess') === '1') {
        showAlert('Đơn hàng đã được hủy thành công!', 'success');
        // Xóa query để tránh hiện lại khi refresh
        params.delete('cancelSuccess');
        const newUrl = `${window.location.pathname}${params.toString() ? '?' + params.toString() : ''}`;
        window.history.replaceState({}, '', newUrl);
    }
}

// Cancel order function
async function cancelOrder(orderId) {
    const confirmed = await showConfirmModal(`Bạn có chắc chắn muốn hủy đơn hàng #${orderId}?`);
    if (!confirmed) return;

    const userId = sessionStorage.getItem('userId');
    if (!userId) {
        showAlert('Lỗi: Không tìm thấy thông tin người dùng!', 'error');
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/user/orders/${orderId}/cancel`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                userId: parseInt(userId),
                cancelReason: 'Hủy từ người dùng'
            })
        });

        if (!response.ok) {
            const errorText = await response.text();
            console.error('Cancel error response:', errorText);
            showAlert('Không thể hủy đơn hàng. Vui lòng thử lại!', 'error');
            return;
        }

        const data = await response.json();
        console.log('Cancel response:', data);

        if (data.success) {
            showAlert('Đơn hàng đã được hủy thành công!', 'success');
            // Reload orders after successful cancellation
            setTimeout(() => {
                const userId = sessionStorage.getItem('userId');
                loadMyOrders(userId);
            }, 1500);
        } else {
            showAlert('Không thể hủy đơn hàng: ' + (data.message || data.errorMessage || 'Lỗi không xác định'), 'error');
        }
    } catch (error) {
        console.error('Error cancelling order:', error);
        showAlert('Lỗi khi hủy đơn hàng. Vui lòng thử lại!', 'error');
    }
}

// Update user greeting
function updateUserGreeting() {
    const username = sessionStorage.getItem('username');
    const userNameEl = document.getElementById('userName');
    if (userNameEl) {
        userNameEl.textContent = username || 'User';
    }
}

// Logout function
function logout() {
    sessionStorage.clear();
    window.location.href = 'login.html';
}
