const API_BASE_URL = 'http://localhost:8080/api';

const params = new URLSearchParams(window.location.search);
const orderId = params.get('orderId');
const from = params.get('from') || 'user';

const userNameEl = document.getElementById('userName');
const backLink = document.getElementById('backLink');
const statusPill = document.getElementById('statusPill');
const orderIdHeading = document.getElementById('orderIdHeading');
const orderDateEl = document.getElementById('orderDate');
const receiverNameEl = document.getElementById('receiverName');
const phoneNumberEl = document.getElementById('phoneNumber');
const shippingAddressEl = document.getElementById('shippingAddress');
const totalAmountEl = document.getElementById('totalAmount');
const totalItemsEl = document.getElementById('totalItems');
const totalQuantityEl = document.getElementById('totalQuantity');
const orderNoteEl = document.getElementById('orderNote');
const orderNoteSection = document.getElementById('orderNoteSection');
const itemsBody = document.getElementById('itemsBody');
const errorBox = document.getElementById('errorBox');
const orderActionsSection = document.getElementById('orderActionsSection');
const cancelOrderBtn = document.getElementById('cancelOrderBtn');

const STATUS_COLORS = {
    "ORANGE": "#fef3c7",
    "BLUE": "#dbeafe",
    "PURPLE": "#e9d5ff",
    "GREEN": "#d1fae5",
    "RED": "#fee2e2",
    "GRAY": "#e5e7eb"
};

const STATUS_TEXT_COLORS = {
    "ORANGE": "#92400e",
    "BLUE": "#1e3a8a",
    "PURPLE": "#6b21a8",
    "GREEN": "#065f46",
    "RED": "#7f1d1d",
    "GRAY": "#374151"
};

document.addEventListener('DOMContentLoaded', () => {
    if (!orderId) {
        showError('Thiếu mã đơn hàng.');
        return;
    }
    setupNav();
    loadOrderDetail();
});

function setupNav() {
    const username = sessionStorage.getItem('username') || 'User';
    if (userNameEl) userNameEl.textContent = username;

    const userId = sessionStorage.getItem('userId');
    const role = (sessionStorage.getItem('role') || '').toUpperCase();
    const isAdmin = role.includes('ADMIN') || role.includes('QUẢN TRỊ');

    if (from === 'admin' || isAdmin) {
        backLink.href = 'order.html';
    } else {
        backLink.href = 'my-orders.html';
    }

    if (!userId) {
        window.location.href = 'login.html';
    }
}

async function loadOrderDetail() {
    clearError();
    setLoading(true);
    try {
        const userId = sessionStorage.getItem('userId');
        const role = (sessionStorage.getItem('role') || '').toUpperCase();
        const isAdmin = role.includes('ADMIN') || role.includes('QUẢN TRỊ') || from === 'admin';

        const url = isAdmin
            ? `${API_BASE_URL}/admin/orders/${orderId}`
            : `${API_BASE_URL}/user/orders/${userId}/${orderId}`;

        const res = await fetch(url);
        const data = await res.json();

        if (!data.success) {
            showError(data.errorMessage || data.message || 'Không tải được chi tiết đơn hàng');
            return;
        }

        renderOrder(data);
    } catch (err) {
        console.error(err);
        showError('Lỗi khi tải chi tiết đơn hàng');
    } finally {
        setLoading(false);
    }
}

function renderOrder(data) {
    const statusColor = STATUS_COLORS[data.statusColor] || '#e5e7eb';
    const statusTextColor = STATUS_TEXT_COLORS[data.statusColor] || '#374151';
    orderIdHeading.textContent = `Đơn hàng #${data.orderId}`;
    orderDateEl.textContent = data.formattedOrderDate || '';
    statusPill.textContent = data.orderStatus || '---';
    statusPill.style.backgroundColor = statusColor;
    statusPill.style.color = statusTextColor;

    receiverNameEl.textContent = data.receiverName || '--';
    phoneNumberEl.textContent = data.phoneNumber || '--';
    shippingAddressEl.textContent = data.shippingAddress || '--';
    totalAmountEl.textContent = data.formattedTotalAmount || '--';
    totalItemsEl.textContent = data.totalItems ?? '--';
    totalQuantityEl.textContent = data.totalQuantity ?? '--';

    if (data.note) {
        orderNoteEl.textContent = data.note;
        orderNoteSection.classList.remove('hidden');
    } else {
        orderNoteSection.classList.add('hidden');
    }

    // Show cancel button only for users (not admin) and only for "Chờ xác nhận" status
    const role = (sessionStorage.getItem('role') || '').toUpperCase();
    const isAdmin = role.includes('ADMIN') || role.includes('QUẢN TRỊ') || from === 'admin';
    const canCancel = !isAdmin && data.orderStatus === 'Chờ xác nhận';
    
    if (canCancel) {
        orderActionsSection.style.display = 'block';
        cancelOrderBtn.onclick = () => cancelOrder(data.orderId);
    } else {
        orderActionsSection.style.display = 'none';
    }

    if (!data.items || data.items.length === 0) {
        itemsBody.innerHTML = '<tr><td colspan="5" style="text-align:center; padding:20px; color:#999;">Không có sản phẩm</td></tr>';
        return;
    }

    itemsBody.innerHTML = data.items.map(item => `
        <tr>
            <td>${item.productId ?? ''}</td>
            <td>${item.productName ?? ''}</td>
            <td>${item.formattedUnitPrice ?? ''}</td>
            <td>${item.quantity ?? 0}</td>
            <td>${item.formattedLineTotal ?? ''}</td>
        </tr>
    `).join('');
}

function showError(message) {
    errorBox.textContent = message;
    errorBox.classList.remove('hidden');
    itemsBody.innerHTML = '<tr><td colspan="5" style="text-align:center; padding:20px; color:#999;">Không có dữ liệu</td></tr>';
}

function clearError() {
    errorBox.textContent = '';
    errorBox.classList.add('hidden');
}

function setLoading(isLoading) {
    if (isLoading) {
        itemsBody.innerHTML = '<tr><td colspan="5" style="text-align:center; padding:20px; color:#999;">Đang tải...</td></tr>';
    }
}

async function cancelOrder(orderId) {
    const confirmed = await showConfirmModal(`Bạn có chắc chắn muốn hủy đơn hàng #${orderId}?`);
    if (!confirmed) return;

    const userId = sessionStorage.getItem('userId');
    if (!userId) {
        showError('Lỗi: Không tìm thấy thông tin người dùng!');
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
            showError('Không thể hủy đơn hàng. Vui lòng thử lại!');
            return;
        }

        const data = await response.json();

            if (data.success) {
                // Quay về trang danh sách đơn hàng kèm cờ thông báo thành công
                window.location.href = 'my-orders.html?cancelSuccess=1';
        } else {
            showError('Không thể hủy đơn hàng: ' + (data.message || data.errorMessage || 'Lỗi không xác định'));
        }
    } catch (error) {
        console.error('Error cancelling order:', error);
        showError('Lỗi khi hủy đơn hàng. Vui lòng thử lại!');
    }
}

// Custom confirm modal (styling aligned with project)
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

function logout() {
    sessionStorage.clear();
    localStorage.removeItem('userId');
    localStorage.removeItem('email');
    localStorage.removeItem('username');
    window.location.href = 'login.html';
}
