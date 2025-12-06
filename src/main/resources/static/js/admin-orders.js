// Admin Orders Management JavaScript

const API_BASE_URL = 'http://localhost:8080/api';

document.addEventListener('DOMContentLoaded', () => {
    checkAdminAuth();
    loadAllOrders();
});

function checkAdminAuth() {
    const role = sessionStorage.getItem('role');
    if (role !== 'ADMIN') {
        alert('Bạn không có quyền truy cập Admin Panel!');
        window.location.href = 'home.html';
        return false;
    }
    const username = sessionStorage.getItem('username');
    if (username) {
        const adminNameEl = document.getElementById('sidebarAdminName');
        if (adminNameEl) {
            adminNameEl.textContent = username;
        }
    }
    return true;
}

function formatPrice(price) {
    return price.toLocaleString('vi-VN') + ' đ';
}

// Use Case: ListAllOrders
async function loadAllOrders() {
    const tbody = document.getElementById('ordersTableBody');
    tbody.innerHTML = '<tr><td colspan="10" style="text-align: center; padding: 40px;"><div class="loading-spinner"></div></td></tr>';
    
    try {
        const response = await fetch(`${API_BASE_URL}/admin/orders/all`);
        const data = await response.json();
        
        if (data.success && data.orders && data.orders.length > 0) {
            displayOrders(data.orders);
        } else {
            tbody.innerHTML = '<tr><td colspan="10" style="text-align: center; padding: 40px; color: var(--color-gray);">Không có đơn hàng nào</td></tr>';
        }
    } catch (error) {
        tbody.innerHTML = '<tr><td colspan="10" style="text-align: center; padding: 40px; color: var(--color-gray);">Lỗi kết nối server</td></tr>';
        console.error('Error loading orders:', error);
        showAlert('Lỗi kết nối server', 'error');
    }
}

// Use Case: SearchOrders
async function searchOrders(event) {
    event.preventDefault();
    const tbody = document.getElementById('ordersTableBody');
    tbody.innerHTML = '<tr><td colspan="10" style="text-align: center; padding: 40px;"><div class="loading-spinner"></div></td></tr>';
    
    const params = new URLSearchParams();
    const keyword = document.getElementById('searchKeyword').value;
    const status = document.getElementById('searchStatus').value;
    
    if (keyword) params.append('keyword', keyword);
    if (status) params.append('status', status);

    try {
        const response = await fetch(`${API_BASE_URL}/admin/orders/search?${params.toString()}`);
        const data = await response.json();
        
        if (data.success && data.orders && data.orders.length > 0) {
            displayOrders(data.orders);
        } else {
            tbody.innerHTML = '<tr><td colspan="10" style="text-align: center; padding: 40px; color: var(--color-gray);">Không tìm thấy đơn hàng</td></tr>';
        }
    } catch (error) {
        tbody.innerHTML = '<tr><td colspan="10" style="text-align: center; padding: 40px; color: var(--color-gray);">Lỗi kết nối server</td></tr>';
        console.error('Error searching orders:', error);
        showAlert('Lỗi kết nối server', 'error');
    }
}

function displayOrders(orders) {
    const tbody = document.getElementById('ordersTableBody');
    
    if (!orders || orders.length === 0) {
        tbody.innerHTML = '<tr><td colspan="10" style="text-align: center; padding: 40px; color: var(--color-gray);">Không có đơn hàng nào</td></tr>';
        return;
    }

    tbody.innerHTML = orders.map(order => {
        const statusClass = getStatusClass(order.trangThai);
        const totalAmount = order.tongTien || 0;
        const orderDate = order.ngayDat ? new Date(order.ngayDat).toLocaleDateString('vi-VN') : 'N/A';
        return `
        <tr>
            <td><strong>#${order.maDonHang}</strong></td>
            <td>${order.tenNguoiNhan || 'N/A'}</td>
            <td>${order.soDienThoai || 'N/A'}</td>
            <td style="max-width: 200px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">${order.diaChiGiaoHang || 'N/A'}</td>
            <td><strong>${formatPrice(totalAmount)}</strong></td>
            <td>${order.soMatHang || 0} mặt hàng</td>
            <td>COD</td>
            <td><span class="status-badge ${statusClass}">${translateStatus(order.trangThai)}</span></td>
            <td>${orderDate}</td>
            <td>
                <div class="action-buttons">
                    <button class="btn-action btn-edit" onclick='viewOrder(${order.maDonHang})'>Xem</button>
                    <button class="btn-action btn-edit" onclick='editOrder(${JSON.stringify(order).replace(/'/g, "&apos;")})'>Cập nhật</button>
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

function translatePaymentMethod(method) {
    switch(method) {
        case 'CHUYEN_KHOAN': return 'Chuyển khoản';
        case 'THANH_TOAN_TRUC_TIEP': return 'COD';
        default: return method || 'COD';
    }
}

// View order details
async function viewOrder(orderId) {
    try {
        const response = await fetch(`${API_BASE_URL}/admin/orders/${orderId}`);
        if (!response.ok) throw new Error('Failed to load order');
        
        const data = await response.json();
        const order = data.order || data;
        
        // Show order details in modal
        const detailsHtml = `
            <h3 style="margin-bottom: 15px;">CHI TIẾT ĐƠN HÀNG #${order.maDonHang}</h3>
            <div style="margin-bottom: 10px;"><strong>Khách hàng:</strong> ${order.tenNguoiNhan || 'N/A'}</div>
            <div style="margin-bottom: 10px;"><strong>SĐT:</strong> ${order.soDienThoai || 'N/A'}</div>
            <div style="margin-bottom: 10px;"><strong>Địa chỉ:</strong> ${order.diaChiGiaoHang || 'N/A'}</div>
            <div style="margin-bottom: 10px;"><strong>Trạng thái:</strong> ${translateStatus(order.trangThai)}</div>
            <div style="margin-bottom: 10px;"><strong>Tổng tiền:</strong> ${formatPrice(order.tongTien || 0)}</div>
            <h4 style="margin: 15px 0 10px 0;">Sản phẩm:</h4>
            ${order.sanPham && order.sanPham.length > 0 ? 
                order.sanPham.map(item => `
                    <div style="padding: 8px; background: white; margin-bottom: 5px;">
                        ${item.tenSanPham} - ${item.soLuong}x - ${formatPrice(item.giaBan || 0)}
                    </div>
                `).join('') : 
                '<p>Không có sản phẩm</p>'
            }
        `;
        
        alert(detailsHtml.replace(/<[^>]*>/g, '\n'));
        
    } catch (error) {
        console.error('Error loading order:', error);
        showAlert('Không thể tải chi tiết đơn hàng', 'error');
    }
}

// Use Case: UpdateOrder
function editOrder(order) {
    document.getElementById('orderId').value = order.maDonHang;
    document.getElementById('orderStatus').value = order.trangThai || 'PENDING';
    document.getElementById('orderNote').value = '';
    
    // Load order details into modal
    const detailsDiv = document.getElementById('orderDetails');
    if (detailsDiv && order.sanPham) {
        detailsDiv.innerHTML = `
            <h4 style="margin-bottom: 10px;">CHI TIẾT ĐƠN HÀNG #${order.maDonHang}</h4>
            <p><strong>Khách hàng:</strong> ${order.tenNguoiNhan || 'N/A'}</p>
            <p><strong>SĐT:</strong> ${order.soDienThoai || 'N/A'}</p>
            <p><strong>Tổng tiền:</strong> ${formatPrice(order.tongTien || 0)}</p>
            <h5 style="margin-top: 10px;">Sản phẩm:</h5>
            ${order.sanPham.map(item => `
                <div style="padding: 8px; background: white; margin-bottom: 5px; border-left: 3px solid black;">
                    ${item.tenSanPham} - SL: ${item.soLuong} - ${formatPrice(item.giaBan || 0)}
                </div>
            `).join('')}
        `;
    }
    
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
