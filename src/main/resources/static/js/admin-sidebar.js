// Admin Sidebar Component - Reusable across all admin pages

function loadAdminSidebar(activePage) {
    const sidebarHTML = `
        <aside class="admin-sidebar">
            <div class="sidebar-header">
                <h2>ADMIN PANEL</h2>
                <p class="admin-name" id="sidebarAdminName">Admin</p>
            </div>
            
            <nav class="sidebar-nav">
                <a href="admin.html" class="nav-item ${activePage === 'dashboard' ? 'active' : ''}">
                    <span class="nav-icon">📊</span>
                    <span class="nav-text">Dashboard</span>
                </a>
                <a href="admin-users.html" class="nav-item ${activePage === 'users' ? 'active' : ''}">
                    <span class="nav-icon">👥</span>
                    <span class="nav-text">Quản lý Users</span>
                </a>
                <a href="admin-products.html" class="nav-item ${activePage === 'products' ? 'active' : ''}">
                    <span class="nav-icon">📦</span>
                    <span class="nav-text">Quản lý Sản phẩm</span>
                </a>
                <a href="admin-orders.html" class="nav-item ${activePage === 'orders' ? 'active' : ''}">
                    <span class="nav-icon">🛒</span>
                    <span class="nav-text">Quản lý Đơn hàng</span>
                </a>
            </nav>

            <div class="sidebar-footer">
                <a href="home.html" class="nav-item">
                    <span class="nav-icon">🏠</span>
                    <span class="nav-text">Trang chủ</span>
                </a>
                <button class="nav-item btn-logout-sidebar" onclick="logout()">
                    <span class="nav-icon">🚪</span>
                    <span class="nav-text">Đăng xuất</span>
                </button>
            </div>
        </aside>
    `;
    
    // Insert sidebar at the beginning of body
    document.body.insertAdjacentHTML('afterbegin', sidebarHTML);
    
    // Update admin name from session
    const username = sessionStorage.getItem('username');
    if (username) {
        document.getElementById('sidebarAdminName').textContent = username;
    }
}

// Auto-load sidebar on DOMContentLoaded if not already present
document.addEventListener('DOMContentLoaded', () => {
    // Check if sidebar doesn't exist yet
    if (!document.querySelector('.admin-sidebar')) {
        // Detect current page from URL or data attribute
        const currentPage = document.body.getAttribute('data-page') || 'dashboard';
        loadAdminSidebar(currentPage);
    }
});
