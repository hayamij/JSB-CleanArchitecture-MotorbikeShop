// Admin Dashboard JavaScript

const API_BASE_URL = 'http://localhost:8080/api';

// Check admin authentication
function checkAdminAuth() {
    const role = sessionStorage.getItem('role');
    if (role !== 'ADMIN') {
        alert('Bạn không có quyền truy cập Admin Panel!');
        window.location.href = 'home.html';
        return false;
    }
    
    // Display admin name
    const username = sessionStorage.getItem('username');
    if (username) {
        const adminNameEl = document.getElementById('sidebarAdminName');
        if (adminNameEl) {
            adminNameEl.textContent = username;
        }
    }
    
    return true;
}

// Format price
function formatPrice(price) {
    return price.toLocaleString('vi-VN') + ' đ';
}

// Load dashboard statistics
async function loadDashboardStats() {
    try {
        // Get all orders for revenue calculation
        const ordersResponse = await fetch(`${API_BASE_URL}/admin/orders/all`);
        if (ordersResponse.ok) {
            const ordersData = await ordersResponse.json();
            const orders = ordersData.orders || [];
            
            calculateRevenueStats(orders);
            document.getElementById('totalOrders').textContent = orders.length;
        }

        // Get all users count
        const usersResponse = await fetch(`${API_BASE_URL}/admin/users/all`);
        if (usersResponse.ok) {
            const usersData = await usersResponse.json();
            document.getElementById('totalUsers').textContent = usersData.users ? usersData.users.length : 0;
        }

        // Get all motorbikes count
        const motorbikesResponse = await fetch(`${API_BASE_URL}/motorbikes`);
        if (motorbikesResponse.ok) {
            const motorbikesData = await motorbikesResponse.json();
            document.getElementById('totalMotorbikes').textContent = motorbikesData ? motorbikesData.length : 0;
        }

        // Get all accessories count
        const accessoriesResponse = await fetch(`${API_BASE_URL}/accessories`);
        if (accessoriesResponse.ok) {
            const accessoriesData = await accessoriesResponse.json();
            document.getElementById('totalAccessories').textContent = accessoriesData ? accessoriesData.length : 0;
        }

    } catch (error) {
        console.error('Error loading dashboard stats:', error);
        showAlert('Không thể tải thống kê', 'error');
    }
}

// Calculate revenue statistics
function calculateRevenueStats(orders) {
    const now = new Date();
    const currentMonth = now.getMonth();
    const currentYear = now.getFullYear();
    
    let revenueMonth = 0;
    let revenueYear = 0;
    let revenueAllTime = 0;
    let ordersMonth = 0;
    
    let revenuePrevMonth = 0;
    let revenuePrevYear = 0;
    let ordersPrevMonth = 0;
    
    orders.forEach(order => {
        // Check status - backend returns formatted status like "Đã xác nhận"
        const isConfirmed = order.orderStatus && (
            order.orderStatus.includes('xác nhận') || 
            order.orderStatus.includes('giao') ||
            order.orderStatus === 'DA_XAC_NHAN' ||
            order.orderStatus === 'DANG_GIAO' ||
            order.orderStatus === 'DA_GIAO'
        );
        
        if (isConfirmed) {
            const orderDate = new Date(order.orderDate);
            const orderMonth = orderDate.getMonth();
            const orderYear = orderDate.getFullYear();
            const totalAmount = order.totalAmount || 0;
            
            revenueAllTime += totalAmount;
            
            if (orderYear === currentYear) {
                revenueYear += totalAmount;
                
                if (orderMonth === currentMonth) {
                    revenueMonth += totalAmount;
                    ordersMonth++;
                }
                
                if (orderMonth === currentMonth - 1) {
                    revenuePrevMonth += totalAmount;
                    ordersPrevMonth++;
                }
            }
            
            if (orderYear === currentYear - 1) {
                revenuePrevYear += totalAmount;
            }
        }
    });
    
    // Calculate growth percentages
    const revenueMonthGrowth = revenuePrevMonth > 0 
        ? ((revenueMonth - revenuePrevMonth) / revenuePrevMonth * 100).toFixed(1)
        : 0;
    
    const revenueYearGrowth = revenuePrevYear > 0
        ? ((revenueYear - revenuePrevYear) / revenuePrevYear * 100).toFixed(1)
        : 0;
    
    const ordersMonthGrowth = ordersPrevMonth > 0
        ? ((ordersMonth - ordersPrevMonth) / ordersPrevMonth * 100).toFixed(1)
        : 0;
    
    // Update UI
    document.getElementById('revenueMonth').textContent = formatPrice(revenueMonth);
    document.getElementById('revenueYear').textContent = formatPrice(revenueYear);
    document.getElementById('revenueAllTime').textContent = formatPrice(revenueAllTime);
    document.getElementById('ordersMonth').textContent = ordersMonth;
    
    // Update growth indicators
    updateGrowthIndicator('revenueMonthChange', revenueMonthGrowth);
    updateGrowthIndicator('revenueYearChange', revenueYearGrowth);
    updateGrowthIndicator('ordersMonthChange', ordersMonthGrowth);
}

function updateGrowthIndicator(elementId, growth) {
    const el = document.getElementById(elementId);
    if (el) {
        const isPositive = growth >= 0;
        el.textContent = (isPositive ? '+' : '') + growth + '%';
        el.className = 'stat-change ' + (isPositive ? 'positive' : 'negative');
    }
}

// Load top products
async function loadTopProducts() {
    try {
        const response = await fetch(`${API_BASE_URL}/admin/orders/stats/top-products`);
        if (!response.ok) return;
        
        const data = await response.json();
        if (data.success && data.products) {
            const topProducts = data.products.map(p => [p.name, p.sold]);
            renderTopProducts(topProducts);
        }
    } catch (error) {
        console.error('Error loading top products:', error);
    }
}

function renderTopProducts(products) {
    const container = document.getElementById('topProductsList');
    
    if (products.length === 0) {
        container.innerHTML = '<p style="text-align: center; color: var(--color-gray); padding: 20px;">Chưa có dữ liệu</p>';
        return;
    }
    
    container.innerHTML = products.map((item, index) => `
        <div class="top-product-item">
            <div class="product-rank">${index + 1}</div>
            <div class="product-name">${item[0]}</div>
            <div class="product-sold">${item[1]} đã bán</div>
        </div>
    `).join('');
}

// Load recent activities
async function loadRecentActivities() {
    try {
        const ordersResponse = await fetch(`${API_BASE_URL}/admin/orders/all`);
        if (!ordersResponse.ok) return;
        
        const ordersData = await ordersResponse.json();
        const orders = ordersData.orders || [];
        
        // Sort by date and get recent 10
        const recentOrders = orders
            .sort((a, b) => new Date(b.orderDate) - new Date(a.orderDate))
            .slice(0, 10);
        
        renderActivities(recentOrders);
        
    } catch (error) {
        console.error('Error loading activities:', error);
    }
}

function renderActivities(orders) {
    const container = document.getElementById('activitiesList');
    
    if (orders.length === 0) {
        container.innerHTML = '<p style="text-align: center; color: var(--color-gray); padding: 20px;">Chưa có hoạt động nào</p>';
        return;
    }
    
    container.innerHTML = orders.map(order => {
        const date = new Date(order.orderDate);
        const timeStr = date.toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit' });
        const dateStr = date.toLocaleDateString('vi-VN');
        
        return `
            <div class="activity-item">
                <div class="activity-time">${timeStr}<br>${dateStr}</div>
                <div class="activity-desc">
                    Đơn hàng #${order.orderId} - ${order.orderStatus || 'N/A'} - ${order.formattedTotalAmount || formatPrice(order.totalAmount || 0)}
                </div>
                <div class="activity-user">${order.customerName || 'N/A'}</div>
            </div>
        `;
    }).join('');
}

// Load revenue chart
async function loadRevenueChart() {
    try {
        const ordersResponse = await fetch(`${API_BASE_URL}/admin/orders/all`);
        if (!ordersResponse.ok) return;
        
        const ordersData = await ordersResponse.json();
        const orders = ordersData.orders || [];
        
        // Calculate monthly revenue for last 12 months
        const monthlyRevenue = new Array(12).fill(0);
        const currentDate = new Date();
        
        orders.forEach(order => {
            // Check if order is confirmed (same logic as statistics)
            const isConfirmed = order.orderStatus && (
                order.orderStatus.includes('xác nhận') || 
                order.orderStatus.includes('giao') ||
                order.orderStatus === 'DA_XAC_NHAN' ||
                order.orderStatus === 'DANG_GIAO' ||
                order.orderStatus === 'DA_GIAO'
            );
            
            if (isConfirmed && order.orderDate) {
                const orderDate = new Date(order.orderDate);
                const monthDiff = (currentDate.getFullYear() - orderDate.getFullYear()) * 12 
                    + (currentDate.getMonth() - orderDate.getMonth());
                
                if (monthDiff >= 0 && monthDiff < 12) {
                    monthlyRevenue[11 - monthDiff] += order.totalAmount || 0;
                }
            }
        });
        
        renderRevenueChart(monthlyRevenue);
        
    } catch (error) {
        console.error('Error loading revenue chart:', error);
    }
}

function renderRevenueChart(data) {
    const ctx = document.getElementById('revenueChart');
    if (!ctx) return;
    
    // Generate month labels
    const labels = [];
    const currentDate = new Date();
    for (let i = 11; i >= 0; i--) {
        const date = new Date(currentDate.getFullYear(), currentDate.getMonth() - i, 1);
        labels.push(date.toLocaleDateString('vi-VN', { month: 'short', year: 'numeric' }));
    }
    
    new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: 'Doanh thu (đ)',
                data: data,
                borderColor: '#000000',
                backgroundColor: 'rgba(0, 0, 0, 0.1)',
                borderWidth: 3,
                tension: 0.4,
                fill: true
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: false
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: function(value) {
                            return (value / 1000000).toFixed(0) + 'M';
                        }
                    }
                }
            }
        }
    });
}

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    if (checkAdminAuth()) {
        loadDashboardStats();
        loadTopProducts();
        loadRecentActivities();
        loadRevenueChart();
    }
});
