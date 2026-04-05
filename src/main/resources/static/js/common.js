// Common JavaScript Functions - Shared utilities

/**
 * Format currency to Vietnamese Dong
 */
function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
    }).format(amount);
}

/**
 * Show alert message
 */
function showAlert(message, type = 'success') {
    const alertContainer = document.getElementById('alertContainer');
    if (!alertContainer) return;
    
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} show`;
    alertDiv.innerHTML = `
        <span>${message}</span>
    `;
    alertContainer.innerHTML = '';
    alertContainer.appendChild(alertDiv);
    
    if (type === 'success') {
        setTimeout(() => {
            alertDiv.remove();
        }, 3000);
    }
}

/**
 * Show loading state
 */
function showLoading(show) {
    const loadingIndicator = document.getElementById('loadingIndicator');
    const mainContainer = document.getElementById('mainContainer') || 
                         document.getElementById('cartContainer') || 
                         document.getElementById('checkoutContainer') ||
                         document.getElementById('productContainer');
    
    if (!loadingIndicator || !mainContainer) return;
    
    if (show) {
        loadingIndicator.classList.remove('hidden');
        mainContainer.classList.add('hidden');
    } else {
        loadingIndicator.classList.add('hidden');
        mainContainer.classList.remove('hidden');
    }
}

/**
 * Show toast notification
 */
function showToast(message, type = 'success') {
    const toast = document.createElement('div');
    toast.style.cssText = `
        position: fixed; 
        top: 20px; 
        right: 20px; 
        background: ${type === 'success' ? '#28a745' : type === 'error' ? '#dc3545' : '#ffc107'}; 
        color: white; 
        padding: 15px 25px; 
        border-radius: 8px; 
        box-shadow: 0 4px 12px rgba(0,0,0,0.15); 
        z-index: 10000; 
        animation: slideIn 0.3s ease;
    `;
    toast.innerHTML = message;
    document.body.appendChild(toast);
    
    setTimeout(() => {
        toast.style.animation = 'slideOut 0.3s ease';
        setTimeout(() => toast.remove(), 300);
    }, 2000);
}

/**
 * Check if user is authenticated
 */
function checkAuth() {
    const userId = sessionStorage.getItem('userId');
    if (!userId) {
        window.location.href = 'login.html';
        return false;
    }
    return true;
}

/**
 * Logout user
 */
function logout() {
    sessionStorage.clear();
    const rememberMe = localStorage.getItem('userId');
    if (!rememberMe) {
        localStorage.clear();
    }
    window.location.href = 'home.html';
}

/**
 * Show/Hide admin link based on user role
 */
function updateNavForRole() {
    const role = sessionStorage.getItem('userRole');
    const adminLink = document.getElementById('adminLink');
    if (adminLink && role === 'ADMIN') {
        adminLink.style.display = 'block';
        adminLink.href = 'admin.html';
    }
}

/**
 * Guest Cart Management - LocalStorage based
 */
function getGuestCart() {
    const cart = localStorage.getItem('guestCart');
    return cart ? JSON.parse(cart) : [];
}

function saveGuestCart(cart) {
    localStorage.setItem('guestCart', JSON.stringify(cart));
}

function addToGuestCart(productId, quantity = 1) {
    const cart = getGuestCart();
    const existingItem = cart.find(item => item.productId === productId);
    
    if (existingItem) {
        existingItem.quantity += quantity;
    } else {
        cart.push({ productId, quantity });
    }
    
    saveGuestCart(cart);
    return cart;
}

function getGuestCartCount() {
    const cart = getGuestCart();
    return cart.reduce((total, item) => total + item.quantity, 0);
}

function clearGuestCart() {
    localStorage.removeItem('guestCart');
}

/**
 * Update cart badge count on navbar
 */
async function updateCartBadge() {
    const cartBadge = document.getElementById('cartItems');
    if (!cartBadge) return;
    
    const userId = sessionStorage.getItem('userId');
    
    if (!userId) {
        // Guest user - show guest cart count
        cartBadge.textContent = getGuestCartCount();
        return;
    }

    // Logged-in user - fetch from server
    try {
        const response = await fetch(`/api/cart/${userId}`);
        const data = await response.json();

        if (data.success) {
            cartBadge.textContent = data.totalItems || 0;
        } else {
            cartBadge.textContent = '0';
        }
    } catch (error) {
        console.error('Error loading cart info:', error);
        // Fallback to 0
        cartBadge.textContent = '0';
    }
}

/**
 * Merge guest cart into user's cart after login/registration
 */
async function mergeGuestCartToUser(userId) {
    const guestCart = getGuestCart();
    
    if (!guestCart || guestCart.length === 0) {
        return { success: true, itemsMerged: 0 };
    }
    
    try {
        const mergePromises = guestCart.map(item =>
            fetch('/api/cart/add', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    userId: userId,
                    productId: item.productId,
                    quantity: item.quantity
                })
            }).then(response => response.json())
        );
        
        const results = await Promise.all(mergePromises);
        const successfulMerges = results.filter(r => r.success).length;
        
        // Clear guest cart after successful merge
        clearGuestCart();
        
        // Update cart badge
        await updateCartBadge();
        
        return {
            success: true,
            itemsMerged: successfulMerges,
            totalItems: guestCart.length
        };
    } catch (error) {
        console.error('Error merging guest cart:', error);
        return { success: false, error: error.message };
    }
}


