// Customer Orders JavaScript

const API_BASE_URL = 'http://localhost:8080/api';

document.addEventListener('DOMContentLoaded', () => {
    // Initialize navbar and footer (requires authentication)
    initNavbar('orders', true);
    initFooter();
    
    if (checkAuth()) {
        updateCartBadge();
        loadMyOrders();
    }
});

function checkAuth() {
    const userId = sessionStorage.getItem('userId');
    if (!userId) {
        window.location.href = 'login.html';
        return false;
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
        showToast('Lỗi kết nối server', 'error');
    }
}

let allOrders = []; // Store all orders for detail view

function displayOrders(orders) {
    const container = document.getElementById('ordersContainer');
    const emptyState = document.getElementById('emptyState');
    
    console.log('Orders data:', orders);
    
    // Sort by newest first (by orderId descending)
    const sortedOrders = [...orders].sort((a, b) => {
        const idA = a.orderId || a.maDonHang || 0;
        const idB = b.orderId || b.maDonHang || 0;
        return idB - idA;
    });
    
    // Store orders for later use
    allOrders = sortedOrders;
    
    container.classList.remove('hidden');
    emptyState.classList.add('hidden');
    
    container.innerHTML = sortedOrders.map(order => {
        console.log('Order:', order);
        
        // Handle different possible field names
        const orderId = order.orderId || order.id || order.maDonHang || 'undefined';
        const status = order.orderStatus || order.status || order.trangThai || 'PENDING';
        const customerName = order.customerName || order.tenNguoiNhan || order.receiverName || 'N/A';
        const customerPhone = order.customerPhone || order.soDienThoai || order.phoneNumber || 'N/A';
        const shippingAddress = order.shippingAddress || order.diaChiGiaoHang || order.address || 'N/A';
        const orderDate = order.formattedOrderDate || order.orderDate || order.ngayDat || 'N/A';
        const rawAmount = order.totalAmount || order.tongTien || 0;
        const totalAmount = typeof rawAmount === 'number' ? rawAmount.toLocaleString('vi-VN') + ' đ' : (order.formattedTotalAmount || 'N/A');
        const totalItems = order.soMatHang || 0;
        
        const statusClass = getStatusClass(status);
        const canCancel = status === 'CHO_XAC_NHAN' || status === 'PENDING';
        
        return `
        <div class="order-item">
            <div class="order-header-row">
                <div class="order-id-section">
                    <span class="order-label">Đơn hàng</span>
                    <span class="order-number">#${orderId}</span>
                </div>
                <div class="order-status ${statusClass}">${translateStatus(status)}</div>
            </div>
            
            <div class="order-details-grid">
                <div class="detail-item">
                    <div class="detail-label">Người nhận</div>
                    <div class="detail-value">${customerName}</div>
                </div>
                <div class="detail-item">
                    <div class="detail-label">Số điện thoại</div>
                    <div class="detail-value">${customerPhone}</div>
                </div>
                <div class="detail-item">
                    <div class="detail-label">Ngày đặt</div>
                    <div class="detail-value">${orderDate}</div>
                </div>
                <div class="detail-item">
                    <div class="detail-label">Số lượng</div>
                    <div class="detail-value">${totalItems} mặt hàng</div>
                </div>
                <div class="detail-item">
                    <div class="detail-label">Thanh toán</div>
                    <div class="detail-value">${translatePaymentMethod(order.paymentMethod || order.paymentMethodDisplay)}</div>
                </div>
            </div>
            
            <div class="detail-item full-width">
                <div class="detail-label">Địa chỉ giao hàng</div>
                <div class="detail-value">${shippingAddress}</div>
            </div>
            
            <div class="order-footer">
                <div class="order-total-section">
                    <span class="total-label">Tổng tiền:</span>
                    <span class="total-amount">${totalAmount}</span>
                </div>
                <div class="order-actions">
                    <button class="btn-view-details" onclick="viewOrderDetail(${orderId})">
                        Xem chi tiết
                    </button>
                    <button class="btn-cancel-order" 
                        ${canCancel ? '' : 'disabled'} 
                        onclick="cancelOrder(${orderId})">
                        ${canCancel ? 'Hủy đơn hàng' : 'Không thể hủy'}
                    </button>
                </div>
            </div>
        </div>
        `;
    }).join('');
}

function getStatusClass(status) {
    switch(status) {
        case 'PENDING':
        case 'CHO_XAC_NHAN': return 'status-pending';
        case 'CONFIRMED':
        case 'DA_XAC_NHAN': return 'status-confirmed';
        case 'CANCELLED':
        case 'DA_HUY': return 'status-cancelled';
        default: return 'status-pending';
    }
}

function translateStatus(status) {
    switch(status) {
        case 'PENDING':
        case 'CHO_XAC_NHAN': return 'Chờ xác nhận';
        case 'CONFIRMED':
        case 'DA_XAC_NHAN': return 'Đã xác nhận';
        case 'CANCELLED':
        case 'DA_HUY': return 'Đã hủy';
        default: return status;
    }
}

function translatePaymentMethod(method) {
    switch(method) {
        case 'CHUYEN_KHOAN': return 'Chuyển khoản ngân hàng';
        case 'THANH_TOAN_TRUC_TIEP': return 'Thanh toán khi nhận hàng (COD)';
        default: return method || 'COD';
    }
}

// Use Case: CancelOrder
async function cancelOrder(orderId) {
    if (!confirm('Bạn có chắc muốn hủy đơn hàng này?')) {
        return;
    }
    
    const userId = sessionStorage.getItem('userId');
    
    try {
        const response = await fetch(`${API_BASE_URL}/user/orders/${orderId}/cancel?userId=${userId}`, {
            method: 'DELETE'
        });
        const data = await response.json();
        
        if (data.success || response.ok) {
            showToast('Hủy đơn hàng thành công!', 'success');
            loadMyOrders();
        } else {
            showToast(data.errorMessage || 'Không thể hủy đơn hàng', 'error');
        }
    } catch (error) {
        console.error('Error canceling order:', error);
        showToast('Lỗi kết nối server', 'error');
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



function logout() {
    sessionStorage.clear();
    window.location.href = 'login.html';
}

// View Order Detail
function viewOrderDetail(orderId) {
    console.log('Loading order detail:', orderId);
    
    // Find order from stored orders
    const order = allOrders.find(o => {
        const id = o.orderId || o.id || o.maDonHang;
        return id == orderId;
    });
    
    if (order) {
        renderOrderDetail(order);
        showOrderDetailSidebar();
    } else {
        showToast('Không tìm thấy đơn hàng', 'error');
    }
}

function renderOrderDetail(order) {
    console.log('Rendering order detail:', order);
    
    const orderId = order.orderId || order.id || order.maDonHang || 'N/A';
    const status = order.orderStatus || order.status || order.trangThai || 'PENDING';
    const customerName = order.customerName || order.tenNguoiNhan || order.receiverName || 'N/A';
    const customerPhone = order.customerPhone || order.soDienThoai || order.phoneNumber || 'N/A';
    const shippingAddress = order.shippingAddress || order.diaChiGiaoHang || order.address || 'N/A';
    const orderDate = order.formattedOrderDate || order.orderDate || order.ngayDat || 'N/A';
    const rawAmount = order.totalAmount || order.tongTien || 0;
    const totalAmount = typeof rawAmount === 'number' ? rawAmount.toLocaleString('vi-VN') : rawAmount.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '.');
    const items = order.sanPham || order.items || order.orderItems || order.chiTietDonHang || [];
    
    let productsHtml = '';
    if (items && items.length > 0) {
        productsHtml = items.map(item => {
            const productName = item.tenSanPham || item.productName || 'N/A';
            const price = item.giaBan || item.price || item.gia || 0;
            const quantity = item.soLuong || item.quantity || 0;
            const subtotal = item.thanhTien || item.subtotal || 0;
            
            const formattedSubtotal = subtotal.toLocaleString('vi-VN');
            
            return `
                <div class="detail-product-item">
                    <div class="detail-product-name">${productName}</div>
                    <div class="detail-product-quantity">x${quantity}</div>
                    <div class="detail-product-subtotal">${formattedSubtotal} đ</div>
                </div>
            `;
        }).join('');
    } else {
        productsHtml = '<p style="text-align: center; color: #666;">Không có sản phẩm</p>';
    }
    
    const content = `
        <div class="detail-section">
            <div class="detail-section-title">Thông tin đơn hàng</div>
            <div class="detail-info-grid">
                <div class="detail-info-item">
                    <div class="detail-info-label">Mã đơn hàng</div>
                    <div class="detail-info-value">#${orderId}</div>
                </div>
                <div class="detail-info-item">
                    <div class="detail-info-label">Trạng thái</div>
                    <div class="detail-info-value">${translateStatus(status)}</div>
                </div>
                <div class="detail-info-item">
                    <div class="detail-info-label">Ngày đặt</div>
                    <div class="detail-info-value">${orderDate}</div>
                </div>
                <div class="detail-info-item">
                    <div class="detail-info-label">Tổng số lượng</div>
                    <div class="detail-info-value">${items.length} sản phẩm</div>
                </div>
                <div class="detail-info-item">
                    <div class="detail-info-label">Phương thức thanh toán</div>
                    <div class="detail-info-value">${translatePaymentMethod(order.paymentMethod || order.paymentMethodDisplay)}</div>
                </div>
            </div>
        </div>
        
        <div class="detail-section">
            <div class="detail-section-title">Thông tin người nhận</div>
            <div class="detail-info-grid">
                <div class="detail-info-item">
                    <div class="detail-info-label">Họ tên</div>
                    <div class="detail-info-value">${customerName}</div>
                </div>
                <div class="detail-info-item">
                    <div class="detail-info-label">Số điện thoại</div>
                    <div class="detail-info-value">${customerPhone}</div>
                </div>
            </div>
            <div class="detail-info-item" style="margin-top: 15px;">
                <div class="detail-info-label">Địa chỉ giao hàng</div>
                <div class="detail-info-value">${shippingAddress}</div>
            </div>
        </div>
        
        <div class="detail-section">
            <div class="detail-section-title">Danh sách sản phẩm</div>
            <div class="detail-products-list">
                ${productsHtml}
            </div>
        </div>
        
        <div class="detail-total-box">
            <span class="detail-total-label">Tổng cộng</span>
            <span class="detail-total-amount">${totalAmount}</span>
        </div>
    `;
    
    document.getElementById('orderDetailContent').innerHTML = content;
}

function showOrderDetailSidebar() {
    document.getElementById('orderDetailOverlay').classList.add('show');
    document.getElementById('orderDetailSidebar').classList.add('show');
    document.body.style.overflow = 'hidden';
}

function closeOrderDetail() {
    document.getElementById('orderDetailOverlay').classList.remove('show');
    document.getElementById('orderDetailSidebar').classList.remove('show');
    document.body.style.overflow = '';
}
