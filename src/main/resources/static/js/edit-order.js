const API_BASE_URL = 'http://localhost:8080/api';

const params = new URLSearchParams(window.location.search);
const orderId = parseInt(params.get('orderId'), 10);

const receiverNameEl = document.getElementById('receiverName');
const phoneNumberEl = document.getElementById('phoneNumber');
const shippingAddressEl = document.getElementById('shippingAddress');
const noteEl = document.getElementById('note');
const orderMetaEl = document.getElementById('orderMeta');
const breadcrumbEl = document.getElementById('breadcrumb');
const alertContainer = document.getElementById('alertContainer');
const form = document.getElementById('editOrderForm');

function showAlert(message, type = 'error') {
    const alert = document.createElement('div');
    alert.className = `alert alert-${type} show`;
    alert.textContent = message;
    alertContainer.innerHTML = '';
    alertContainer.appendChild(alert);
    setTimeout(() => {
        alert.classList.remove('show');
        setTimeout(() => alert.remove(), 300);
    }, 3000);
}

function requireAuth() {
    const userId = sessionStorage.getItem('userId');
    if (!userId) {
        window.location.href = 'login.html';
        return null;
    }
    return parseInt(userId, 10);
}

async function loadOrder() {
    const userId = requireAuth();
    if (!userId || !orderId) {
        showAlert('Thiếu thông tin đơn hàng', 'error');
        return;
    }

    try {
        const res = await fetch(`${API_BASE_URL}/user/orders/${userId}`);
        const data = await res.json();

        if (!data.success || !data.orders) {
            showAlert(data.errorMessage || data.message || 'Không tải được đơn hàng', 'error');
            return;
        }

        const order = data.orders.find(o => o.orderId === orderId);
        if (!order) {
            showAlert('Không tìm thấy đơn hàng này', 'error');
            return;
        }

        if (order.orderStatus !== 'Chờ xác nhận') {
            showAlert('Chỉ sửa được đơn hàng ở trạng thái Chờ xác nhận', 'error');
            return;
        }

        receiverNameEl.value = order.customerName || '';
        phoneNumberEl.value = order.customerPhone || '';
        shippingAddressEl.value = order.shippingAddress || '';
        noteEl.value = order.note || '';

        orderMetaEl.textContent = `Đơn hàng #${order.orderId} • ${order.orderStatus} • Tổng ${order.formattedTotalAmount}`;
        breadcrumbEl.textContent = `Đơn hàng / #${order.orderId} / Sửa thông tin giao hàng`;
    } catch (err) {
        console.error(err);
        showAlert('Lỗi khi tải đơn hàng', 'error');
    }
}

if (form) {
    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        const userId = requireAuth();
        if (!userId) return;

        const payload = {
            userId,
            receiverName: receiverNameEl.value.trim(),
            phoneNumber: phoneNumberEl.value.trim(),
            shippingAddress: shippingAddressEl.value.trim(),
            note: noteEl.value.trim()
        };

        if (!payload.receiverName || !payload.phoneNumber || !payload.shippingAddress) {
            showAlert('Vui lòng nhập đủ thông tin bắt buộc', 'error');
            return;
        }

        try {
            const res = await fetch(`${API_BASE_URL}/user/orders/${orderId}/shipping`, {
                method: 'PUT',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(payload)
            });
            const data = await res.json();

            if (data.success) {
                showAlert('Cập nhật thông tin giao hàng thành công', 'success');
                setTimeout(() => window.location.href = 'my-orders.html', 800);
            } else {
                showAlert(data.errorMessage || data.message || 'Cập nhật thất bại', 'error');
            }
        } catch (err) {
            console.error(err);
            showAlert('Lỗi khi cập nhật đơn hàng', 'error');
        }
    });
}

document.addEventListener('DOMContentLoaded', loadOrder);

function logout() {
    sessionStorage.clear();
    window.location.href = 'login.html';
}
