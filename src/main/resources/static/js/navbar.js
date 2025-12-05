// Navbar Component - Shared navigation bar functionality

/**
 * Initialize and render the navbar
 * @param {string} activePage - The current active page (e.g., 'home', 'cart', 'orders')
 * @param {boolean} requireAuth - Whether this page requires authentication
 */
function initNavbar(activePage = '', requireAuth = true) {
    const navbarContainer = document.getElementById('navbarContainer');
    if (!navbarContainer) return;

    const userId = sessionStorage.getItem('userId');
    const username = sessionStorage.getItem('username');
    const role = sessionStorage.getItem('role');
    const isLoggedIn = !!userId;

    // If page requires auth and user is not logged in, redirect to login
    if (requireAuth && !isLoggedIn) {
        window.location.href = 'login.html';
        return;
    }

    // Render navbar HTML
    navbarContainer.innerHTML = `
        <nav class="navbar">
            <div class="nav-container">
                <a href="${isLoggedIn ? 'home.html' : 'index.html'}" class="logo">
                    <span>Motorbike Shop</span>
                </a>

                <ul class="nav-menu">
                    <li><a href="home.html" class="${activePage === 'home' ? 'active' : ''}">Trang chủ</a></li>
                    <li>
                        <a href="cart.html" class="${activePage === 'cart' ? 'active' : ''}">
                            Giỏ hàng <span id="cartItems" class="cart-badge"></span>
                        </a>
                    </li>
                    ${isLoggedIn ? `
                        <li><a href="my-orders.html" class="${activePage === 'orders' ? 'active' : ''}">Đơn hàng của tôi</a></li>
                        ${role === 'ADMIN' ? `<li><a href="admin.html" class="${activePage === 'admin' ? 'active' : ''}">Admin Panel</a></li>` : ''}
                    ` : ''}
                </ul>

                <div class="user-info">
                    ${isLoggedIn ? `
                        <span class="user-greeting">Xin chào, <span class="user-name">${username || 'User'}</span></span>
                        <button class="btn-logout" onclick="logout()">Đăng xuất</button>
                    ` : `
                        <a href="login.html" class="btn-login">Đăng nhập</a>
                        <a href="register.html" class="btn-register">Đăng ký</a>
                    `}
                </div>
            </div>
        </nav>
    `;
}

/**
 * Initialize and render the footer
 */
function initFooter() {
    const footerContainer = document.getElementById('footerContainer');
    if (!footerContainer) return;

    footerContainer.innerHTML = `
        <footer>
            <p>&copy; 2025 Motorbike Shop. All rights reserved.</p>
            <p>Hệ thống quản lý cửa hàng xe máy - Clean Architecture</p>
        </footer>
    `;
}

/**
 * Update navbar for user role (legacy support)
 */
function updateNavForRole() {
    // This function is kept for backward compatibility
    // The new initNavbar() handles role-based rendering automatically
    const role = sessionStorage.getItem('role');
    const adminLink = document.getElementById('adminLink');
    if (adminLink && role === 'ADMIN') {
        adminLink.style.display = 'block';
        adminLink.href = 'admin.html';
    }
}
