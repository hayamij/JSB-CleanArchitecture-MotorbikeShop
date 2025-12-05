// Admin Orders Management JavaScript

const API_BASE_URL = 'http://localhost:8080/api';

document.addEventListener('DOMContentLoaded', () => {
    checkAdminAuth();
    loadAllOrders();
});

function checkAdminAuth() {
    const role = sessionStorage.getItem('userRole');
    if (role !== 'ADMIN') {
        alert('Bạn không có quyền truy cập Admin Panel!');
        window.location.href = 'home.html';
        return false;
    }
    const userName = sessionStorage.getItem('userName');
    if (userName) document.getElementById('userName').textContent = userName;
    return true;
}

// Use Case: ListAllOrders
async function loadAllOrders() {
    showLoading(true);
    try {
        const response = await fetch(`${API_BASE_URL}/admin/orders/all`);
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

// Use Case: SearchOrders
async function searchOrders(event) {
    event.preventDefault();
    showLoading(true);
    const params = new URLSearchParams();
    const keyword = document.getElementById('searchKeyword').value;
    const status = document.getElementById('searchStatus').value;
    
    if (keyword) params.append('keyword', keyword);
    if (status) params.append('status', status);

    try {
        const response = await fetch(`${API_BASE_URL}/admin/orders/search?${params.toString()}`);
        const data = await response.json();
        showLoading(false);
        if (data.success && data.orders && data.orders.length > 0) {
            displayOrders(data.orders);
        } else {
            showEmptyState();
        }
    } catch (error) {
        showLoading(false);
        console.error('Error searching orders:', error);
        showAlert('Lỗi kết nối server', 'error');
    }
}

function displayOrders(orders) {
    const tbody = document.getElementById('ordersTableBody');
    const container = document.getElementById('ordersContainer');
    const emptyState = document.getElementById('emptyState');
    container.classList.remove('hidden');
    emptyState.classList.add('hidden');

    tbody.innerHTML = orders.map(order => {
        const statusClass = getStatusClass(order.orderStatus);
        return `
        <tr>
            <td>#${order.orderId}</td>
            <td>${order.customerName || 'N/A'}</td>
            <td>${order.customerPhone || 'N/A'}</td>
            <td>${order.shippingAddress || 'N/A'}</td>
            <td>${order.formattedTotalAmount || 'N/A'}</td>
            <td>${order.totalItems || 0} (${order.totalQuantity || 0} sản phẩm)</td>
            <td><span class="status-badge ${statusClass}">${translateStatus(order.orderStatus)}</span></td>
            <td>${order.formattedOrderDate || 'N/A'}</td>
            <td>
                <div class="action-buttons">
                    <button class="btn-action btn-edit" onclick='editOrder(${JSON.stringify(order)})'>Cập nhật</button>
                </div>
            </td>
        </tr>
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

// Use Case: UpdateOrder
function editOrder(order) {
    document.getElementById('orderId').value = order.orderId;
    document.getElementById('orderStatus').value = order.orderStatus || 'PENDING';
    document.getElementById('orderNote').value = '';
    document.getElementById('orderModal').classList.add('show');
}

function closeOrderModal() {
    document.getElementById('orderModal').classList.remove('show');
}

async function submitOrderForm() {
    const orderId = document.getElementById('orderId').value;
    const status = document.getElementById('orderStatus').value;
    const note = document.getElementById('orderNote').value;

    try {
        const response = await fetch(`${API_BASE_URL}/admin/orders/${orderId}/update`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ status, note })
        });
        const data = await response.json();
        if (data.success || response.ok) {
            showAlert('Cập nhật đơn hàng thành công!', 'success');
            closeOrderModal();
            loadAllOrders();
        } else {
            showAlert(data.errorMessage || 'Có lỗi xảy ra', 'error');
        }
    } catch (error) {
        console.error('Error updating order:', error);
        showAlert('Lỗi kết nối server', 'error');
    }
}

function showAlert(message, type) {
    const alertContainer = document.getElementById('alertContainer');
    alertContainer.innerHTML = `<div class="alert alert-${type} show"><span>${message}</span></div>`;
    setTimeout(() => { alertContainer.innerHTML = ''; }, 3000);
}
