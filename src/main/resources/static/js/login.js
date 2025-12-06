// Login Page JavaScript

function togglePassword() {
    const passwordInput = document.getElementById('password');
    const toggleBtn = document.querySelector('.password-toggle');
    
    if (passwordInput.type === 'password') {
        passwordInput.type = 'text';
        toggleBtn.textContent = 'Ẩn';
    } else {
        passwordInput.type = 'password';
        toggleBtn.textContent = 'Hiện';
    }
}

function setLoading(loading) {
    const loginBtn = document.getElementById('loginBtn');
    const form = document.getElementById('loginForm');
    
    if (loading) {
        loginBtn.disabled = true;
        loginBtn.textContent = 'Đang đăng nhập...';
        form.querySelectorAll('input, button').forEach(el => el.disabled = true);
    } else {
        loginBtn.disabled = false;
        loginBtn.textContent = 'Đăng nhập';
        form.querySelectorAll('input, button').forEach(el => el.disabled = false);
    }
}

document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value;
    const rememberMe = document.getElementById('rememberMe').checked;

    if (!email || !password) {
        showAlert('Vui lòng điền đầy đủ thông tin', 'error');
        return;
    }

    setLoading(true);

    try {
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                email: email,
                password: password
            })
        });

        const data = await response.json();

        if (data.success) {
            // Store user information in sessionStorage
            sessionStorage.setItem('userId', data.userId);
            sessionStorage.setItem('email', data.email);
            sessionStorage.setItem('username', data.username);
            sessionStorage.setItem('role', data.role);
            sessionStorage.setItem('phone', data.phone || '');
            sessionStorage.setItem('address', data.address || '');
            sessionStorage.setItem('cartId', data.cartId);

            if (rememberMe) {
                localStorage.setItem('userId', data.userId);
                localStorage.setItem('email', data.email);
                localStorage.setItem('username', data.username);
                localStorage.setItem('role', data.role);
                localStorage.setItem('phone', data.phone || '');
                localStorage.setItem('address', data.address || '');
            }

            // Merge guest cart into user cart
            const mergeResult = await mergeGuestCartToUser(data.userId);
            
            showAlert('Đăng nhập thành công! Đang chuyển hướng...', 'success');

            if (mergeResult.success && mergeResult.itemsMerged > 0) {
                showAlert(`Đã hợp nhất ${mergeResult.itemsMerged} sản phẩm vào giỏ hàng`, 'success');
            } else if (data.cartMerged) {
                showAlert(`Đã hợp nhất ${data.mergedItemsCount} sản phẩm vào giỏ hàng`, 'success');
            }

            // Check if user was trying to checkout
            const returnToCheckout = sessionStorage.getItem('returnToCheckout');
            sessionStorage.removeItem('returnToCheckout');
            
            setTimeout(() => {
                if (returnToCheckout === 'true') {
                    window.location.href = 'checkout.html';
                } else {
                    window.location.href = 'home.html';
                }
            }, 1500);
        } else {
            showAlert(data.errorMessage || 'Đăng nhập thất bại', 'error');
        }
    } catch (error) {
        console.error('Login error:', error);
        showAlert('Lỗi kết nối đến server', 'error');
    } finally {
        setLoading(false);
    }
});

window.onload = function() {
    const savedEmail = localStorage.getItem('email');
    if (savedEmail) {
        document.getElementById('email').value = savedEmail;
        document.getElementById('rememberMe').checked = true;
    }
};
