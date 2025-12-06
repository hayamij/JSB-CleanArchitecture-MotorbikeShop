const API_BASE_URL = 'http://localhost:8080/api';
let ordersCache = [];

// On load
document.addEventListener('DOMContentLoaded', () => {
    checkAdminAccess();
    updateAdminGreeting();
    bindEvents();
    loadAllOrders();
});

function bindEvents() {
    const refreshBtn = document.getElementById('refreshBtn');

    if (refreshBtn) refreshBtn.addEventListener('click', loadAllOrders);
}

function updateAdminGreeting() {
    const username = sessionStorage.getItem('username') || 'Admin';
    const adminNameEl = document.getElementById('adminName');
    if (adminNameEl) adminNameEl.textContent = username;
}

function checkAdminAccess() {
    const role = sessionStorage.getItem('role');
    const userId = sessionStorage.getItem('userId');
    if (!userId) {
        window.location.href = 'login.html';
        return;
    }
    const normalizedRole = role ? String(role).trim().toUpperCase() : '';
    const isAdmin = normalizedRole.includes('ADMIN') || normalizedRole.includes('QUẢN TRỊ');
    if (!isAdmin) {
        document.body.innerHTML = '<div style="padding:80px;text-align:center;">Bạn không có quyền truy cập trang này.</div>';
    }
}

async function loadAllOrders() {
    setLoading(true);
    try {
        const res = await fetch(`${API_BASE_URL}/admin/orders/all`);
        const data = await res.json();

        if (!data.success) {
            showError(data.errorMessage || data.message || 'Không tải được đơn hàng');
            return;
        }

        ordersCache = data.orders || [];
        renderOrders();
    } catch (err) {
        console.error(err);
        showError('Lỗi khi tải đơn hàng');
    } finally {
        setLoading(false);
    }
}

function renderOrders() {
    const tbody = document.getElementById('ordersTableBody');
    if (!tbody) return;

    if (!ordersCache || ordersCache.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="8" style="text-align:center; padding: 30px; color:#999;">
                    Không có đơn hàng
                </td>
            </tr>
        `;
        return;
    }

    tbody.innerHTML = ordersCache.map(order => `
        <tr>
            <td><strong>#${order.orderId || 'N/A'}</strong></td>
            <td>${order.customerName || 'N/A'}</td>
            <td>${order.customerPhone || ''}</td>
            <td>${order.shippingAddress || ''}</td>
            <td><span class="status-badge status-${(order.statusColor || 'gray').toLowerCase()}">${order.orderStatus || 'N/A'}</span></td>
            <td>${order.formattedTotalAmount || formatCurrency(order.totalAmount || 0)}</td>
            <td>${order.formattedOrderDate || formatDate(order.orderDate || '')}</td>
            <td><button class="btn-secondary" style="padding:6px 12px; font-size:0.85em;" onclick="viewOrder(${order.orderId})">Xem</button></td>
        </tr>
    `).join('');
}

function showError(msg) {
    const tbody = document.getElementById('ordersTableBody');
    if (tbody) {
        tbody.innerHTML = `<tr><td colspan="8" style="text-align:center; color:#e74c3c; padding:20px;">${msg}</td></tr>`;
    }
}

function setLoading(isLoading) {
    const tbody = document.getElementById('ordersTableBody');
    if (tbody && isLoading) {
        tbody.innerHTML = '<tr><td colspan="8" style="text-align:center; padding:30px; color:#999;">Đang tải...</td></tr>';
    }
}

// Helpers
function formatCurrency(value) {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
}

function formatDate(dateString) {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleDateString('vi-VN');
}

function viewOrder(orderId) {
    alert(`Xem đơn hàng #${orderId}`);
    // TODO: điều hướng tới trang chi tiết nếu có
}

function logout() {
    sessionStorage.clear();
    localStorage.removeItem('userId');
    localStorage.removeItem('email');
    localStorage.removeItem('username');
    window.location.href = 'login.html';
}
