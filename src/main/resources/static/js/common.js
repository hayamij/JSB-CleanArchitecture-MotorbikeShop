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
    window.location.href = 'login.html';
}
