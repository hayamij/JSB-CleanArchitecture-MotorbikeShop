// Admin Orders Management JavaScript

const API_BASE_URL = 'http://localhost:8080/api';

// Store original orders data for filtering
let allOrdersData = [];

// Format price helper
function formatPrice(price) {
    return price.toLocaleString('vi-VN') + ' đ';
}

document.addEventListener('DOMContentLoaded', () => {
    checkAdminAuth();
    loadAllOrders();
    
    // Add real-time search on keyword input
    const searchKeyword = document.getElementById('searchKeyword');
    if (searchKeyword) {
        searchKeyword.addEventListener('input', applyFilters);
    }
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

// Apply filters and sorting
function applyFilters() {
    if (allOrdersData.length === 0) return;
    
    const keyword = document.getElementById('searchKeyword').value.toLowerCase();
    const statusFilter = document.getElementById('searchStatus').value;
    const paymentFilter = document.getElementById('searchPaymentMethod').value;
    const sortOrder = document.getElementById('sortOrder').value;
    
    let filtered = [...allOrdersData];
    
    // Filter by customer name
    if (keyword) {
        filtered = filtered.filter(o => 
            (o.customerName || '').toLowerCase().includes(keyword) ||
            (o.customerPhone || '').toLowerCase().includes(keyword)
        );
    }
    
    // Filter by status
    if (statusFilter) {
        filtered = filtered.filter(o => o.orderStatus === statusFilter);
    }
    
    // Filter by payment method
    if (paymentFilter) {
        filtered = filtered.filter(o => o.paymentMethodText === paymentFilter);
    }
    
    // Sort
    filtered.sort((a, b) => {
        switch(sortOrder) {
            case 'newest':
            case 'oldest':
                // Parse date strings for comparison
                const dateA = a.formattedOrderDate || '';
                const dateB = b.formattedOrderDate || '';
                return sortOrder === 'newest' ? dateB.localeCompare(dateA) : dateA.localeCompare(dateB);
            case 'amount-high':
            case 'amount-low':
                // Extract numeric values from formatted amounts
                const amountA = parseFloat((a.formattedTotalAmount || '0').replace(/[^\d]/g, ''));
                const amountB = parseFloat((b.formattedTotalAmount || '0').replace(/[^\d]/g, ''));
                return sortOrder === 'amount-high' ? amountB - amountA : amountA - amountB;
            case 'customer-asc':
                return (a.customerName || '').localeCompare(b.customerName || '', 'vi');
            case 'customer-desc':
                return (b.customerName || '').localeCompare(a.customerName || '', 'vi');
            default:
                return 0;
        }
    });
    
    displayOrders(filtered);
}

// Use Case: ListAllOrders
async function loadAllOrders() {
    const tbody = document.getElementById('ordersTableBody');
    tbody.innerHTML = '<tr><td colspan="10" style="text-align: center; padding: 40px;"><div class="loading-spinner"></div></td></tr>';
    
    try {
        const response = await fetch(`${API_BASE_URL}/admin/orders/all`);
        const data = await response.json();
        
        if (data.success && data.orders && data.orders.length > 0) {
            allOrdersData = data.orders;
            console.log('Sample order data:', data.orders[0]); // Debug: check what fields are available
            applyFilters();
        } else {
            tbody.innerHTML = '<tr><td colspan="10" style="text-align: center; padding: 40px; color: var(--color-gray);">Không có đơn hàng nào</td></tr>';
        }
    } catch (error) {
        tbody.innerHTML = '<tr><td colspan="10" style="text-align: center; padding: 40px; color: var(--color-gray);">Lỗi kết nối server</td></tr>';
        console.error('Error loading orders:', error);
        showAlert('Lỗi kết nối server', 'error');
    }
}

// Use Case: SearchOrders - use client-side filtering instead
function searchOrders(event) {
    event.preventDefault();
    applyFilters();
}

function displayOrders(orders) {
    const tbody = document.getElementById('ordersTableBody');
    
    if (!orders || orders.length === 0) {
        tbody.innerHTML = '<tr><td colspan="10" style="text-align: center; padding: 40px; color: var(--color-gray);">Không có đơn hàng nào</td></tr>';
        return;
    }

    tbody.innerHTML = orders.map(order => {
        const statusClass = getStatusClassFromText(order.orderStatus);
        const totalAmount = order.formattedTotalAmount || '0 đ';
        const orderDate = order.formattedOrderDate || 'N/A';
        const paymentText = order.paymentMethodText || 'COD';
        return `
        <tr>
            <td><strong>#${order.orderId}</strong></td>
            <td>${order.customerName || 'N/A'}</td>
            <td>${order.customerPhone || 'N/A'}</td>
            <td style="max-width: 200px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">${order.shippingAddress || 'N/A'}</td>
            <td><strong>${totalAmount}</strong></td>
            <td>${order.totalItems || 0} mặt hàng</td>
            <td>${paymentText}</td>
            <td><span class="status-badge ${statusClass}">${order.orderStatus}</span></td>
            <td>${orderDate}</td>
            <td>
                <div class="action-buttons">
                    <button class="btn-action btn-edit" onclick='viewOrder(${order.orderId})'>Xem</button>
                    <button class="btn-action btn-edit" onclick='editOrder(${JSON.stringify(order).replace(/'/g, "&apos;")})'>Cập nhật</button>
                </div>
            </td>
        </tr>
        `;
    }).join('');
}

// Get CSS class from formatted status text (from presenter)
function getStatusClassFromText(statusText) {
    switch(statusText) {
        case 'Chờ xác nhận': return 'status-pending';
        case 'Đã xác nhận': return 'status-confirmed';
        case 'Đang giao hàng': return 'status-shipping';
        case 'Đã giao hàng': return 'status-delivered';
        case 'Đã hủy': return 'status-cancelled';
        default: return 'status-pending';
    }
}

function translatePaymentMethod(method) {
    switch(method) {
        case 'CHUYEN_KHOAN': return 'Chuyển khoản';
        case 'BANK_TRANSFER': return 'Chuyển khoản';
        case 'THANH_TOAN_TRUC_TIEP': return 'COD';
        case 'COD': return 'COD';
        default: return method || 'COD';
    }
}

// Translate status code to Vietnamese text
function translateStatusCode(statusCode) {
    switch(statusCode) {
        case 'CHO_XAC_NHAN': return 'Chờ xác nhận';
        case 'DA_XAC_NHAN': return 'Đã xác nhận';
        case 'DANG_GIAO': return 'Đang giao hàng';
        case 'DA_GIAO': return 'Đã giao hàng';
        case 'DA_HUY': return 'Đã hủy';
        default: return statusCode;
    }
}

// Fetch valid next statuses from backend (business logic moved to backend)
async function getValidNextStatuses(orderId) {
    try {
        const response = await fetch(`${API_BASE_URL}/admin/orders/${orderId}/valid-statuses`);
        if (!response.ok) throw new Error('Failed to fetch valid statuses');
        
        const data = await response.json();
        if (!data.success) {
            console.error('Failed to get valid statuses:', data.message);
            return [];
        }
        
        return data.validStatuses || [];
    } catch (error) {
        console.error('Error fetching valid statuses:', error);
        return [];
    }
}

// View order details
async function viewOrder(orderId) {
    try {
        const response = await fetch(`${API_BASE_URL}/admin/orders/${orderId}`);
        if (!response.ok) throw new Error('Failed to load order');
        
        const data = await response.json();
        if (!data.success || !data.order) {
            showAlert('Không thể tải chi tiết đơn hàng', 'error');
            return;
        }
        
        const order = data.order;
        
        // Set modal title
        document.getElementById('modalTitle').textContent = `CHI TIẾT ĐƠN HÀNG #${order.orderId}`;
        
        // Hide form, show only view
        document.getElementById('orderForm').style.display = 'none';
        document.getElementById('modalFooter').innerHTML = '<button class="btn-cancel" onclick="closeOrderModal()">ĐÓNG</button>';
        
        // Create detailed modal content
        const statusText = order.orderStatus || 'N/A';
        const modalHtml = `
            <div style="background: var(--color-light-gray); padding: 20px; margin-bottom: 20px;">
                <h4 style="margin: 0 0 15px 0;">Thông tin đơn hàng</h4>
                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 10px;">
                    <div><strong>Trạng thái:</strong> <span class="status-badge ${getStatusClassFromText(statusText)}">${statusText}</span></div>
                    <div><strong>Ngày đặt:</strong> ${order.formattedOrderDate}</div>
                    <div><strong>Tổng số lượng:</strong> ${order.totalItems} sản phẩm</div>
                    <div><strong>Phương thức:</strong> ${translatePaymentMethod(order.paymentMethod)}</div>
                </div>
            </div>
            
            <div style="background: var(--color-light-gray); padding: 20px; margin-bottom: 20px;">
                <h4 style="margin: 0 0 15px 0;">Thông tin người nhận</h4>
                <div style="margin-bottom: 8px;"><strong>Họ tên:</strong> ${order.customerName}</div>
                <div style="margin-bottom: 8px;"><strong>Số điện thoại:</strong> ${order.customerPhone}</div>
                <div><strong>Địa chỉ:</strong> ${order.shippingAddress}</div>
            </div>
            
            <div style="background: var(--color-light-gray); padding: 20px; margin-bottom: 20px;">
                <h4 style="margin: 0 0 15px 0;">Danh sách sản phẩm</h4>
                ${order.items && order.items.length > 0 ? 
                    order.items.map(item => `
                        <div style="padding: 12px; background: white; margin-bottom: 8px; border-left: 3px solid var(--color-black); display: flex; justify-content: space-between; align-items: center;">
                            <div>
                                <strong>${item.productName}</strong><br>
                                <span style="color: var(--color-gray);">Số lượng: ${item.quantity} x ${formatPrice(item.price)}</span>
                            </div>
                            <div style="font-weight: bold;">${formatPrice(item.subtotal)}</div>
                        </div>
                    `).join('') : 
                    '<p style="text-align: center;">Không có sản phẩm</p>'
                }
            </div>
            
            <div style="text-align: right; padding: 20px; background: var(--color-black); color: white; font-size: 1.2em;">
                <strong>Tổng cộng: ${formatPrice(order.totalAmount)}</strong>
            </div>
        `;
        
        document.getElementById('orderDetails').innerHTML = modalHtml;
        document.getElementById('orderModal').classList.add('show');
        
    } catch (error) {
        console.error('Error loading order:', error);
        showAlert('Không thể tải chi tiết đơn hàng', 'error');
    }
}

// Use Case: UpdateOrder
async function editOrder(order) {
    try {
        // Load full order details first
        const response = await fetch(`${API_BASE_URL}/admin/orders/${order.orderId}`);
        if (!response.ok) throw new Error('Failed to load order');
        
        const data = await response.json();
        if (!data.success || !data.order) {
            showAlert('Không thể tải chi tiết đơn hàng', 'error');
            return;
        }
        
        const fullOrder = data.order;
        
        // Set modal title
        document.getElementById('modalTitle').textContent = `CẬP NHẬT ĐƠN HÀNG #${fullOrder.orderId}`;
        
        // Show form
        document.getElementById('orderForm').style.display = 'block';
        document.getElementById('orderId').value = fullOrder.orderId;
        document.getElementById('orderNote').value = fullOrder.note || '';
        
        // Populate status dropdown based on business rules from backend
        const statusSelect = document.getElementById('orderStatus');
        const currentStatusCode = fullOrder.orderStatusCode; // Backend now sends both code and text
        const currentStatusText = fullOrder.orderStatus;
        statusSelect.innerHTML = ''; // Clear existing options
        
        // Add current status as first option
        const currentOption = document.createElement('option');
        currentOption.value = currentStatusCode;
        currentOption.textContent = currentStatusText + ' (hiện tại)';
        currentOption.selected = true;
        statusSelect.appendChild(currentOption);
        
        // Fetch and add valid next statuses from backend (business logic on server)
        const validNextStatuses = await getValidNextStatuses(fullOrder.orderId);
        validNextStatuses.forEach(statusOption => {
            const option = document.createElement('option');
            option.value = statusOption.code;
            option.textContent = statusOption.display;
            statusSelect.appendChild(option);
        });
        
        // Update modal footer
        document.getElementById('modalFooter').innerHTML = `
            <button class="btn-cancel" onclick="closeOrderModal()">HỦY</button>
            <button class="btn-submit" onclick="submitOrderForm()">CẬP NHẬT</button>
        `;
        
        // Load order details into modal
        const detailsDiv = document.getElementById('orderDetails');
        detailsDiv.innerHTML = `
            <div style="background: var(--color-light-gray); padding: 20px; margin-bottom: 20px;">
                <h4 style="margin-bottom: 15px;">THÔNG TIN ĐƠN HÀNG</h4>
                <div style="margin-bottom: 10px;"><strong>Khách hàng:</strong> ${fullOrder.customerName}</div>
                <div style="margin-bottom: 10px;"><strong>SĐT:</strong> ${fullOrder.customerPhone}</div>
                <div style="margin-bottom: 10px;"><strong>Địa chỉ:</strong> ${fullOrder.shippingAddress}</div>
                <div style="margin-bottom: 10px;"><strong>Ngày đặt:</strong> ${fullOrder.formattedOrderDate}</div>
                <div style="margin-bottom: 10px;"><strong>Tổng tiền:</strong> <strong>${formatPrice(fullOrder.totalAmount)}</strong></div>
            </div>
            
            <div style="background: var(--color-light-gray); padding: 20px;">
                <h4 style="margin: 0 0 10px 0;">SẢN PHẨM (${fullOrder.totalItems} mặt hàng)</h4>
                ${fullOrder.items && fullOrder.items.length > 0 ? 
                    fullOrder.items.map(item => `
                        <div style="padding: 10px; background: white; margin-bottom: 5px; border-left: 3px solid var(--color-black);">
                            <div style="display: flex; justify-content: space-between; align-items: center;">
                                <div>
                                    <strong>${item.productName}</strong><br>
                                    <span style="color: var(--color-gray); font-size: 0.9em;">
                                        Số lượng: ${item.quantity} x ${formatPrice(item.price)}
                                    </span>
                                </div>
                                <strong>${formatPrice(item.subtotal)}</strong>
                            </div>
                        </div>
                    `).join('') :
                    '<p>Không có sản phẩm</p>'
                }
            </div>
        `;
        
        document.getElementById('orderModal').classList.add('show');
    } catch (error) {
        console.error('Error loading order:', error);
        showAlert('Không thể tải thông tin đơn hàng', 'error');
    }
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
