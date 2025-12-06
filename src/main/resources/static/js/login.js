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
            sessionStorage.setItem('userId', data.userId);
            sessionStorage.setItem('email', data.email);
            sessionStorage.setItem('username', data.username);
            sessionStorage.setItem('role', data.role);
            sessionStorage.setItem('cartId', data.cartId);

            if (rememberMe) {
                localStorage.setItem('userId', data.userId);
                localStorage.setItem('email', data.email);
                localStorage.setItem('username', data.username);
            }

            console.log('✅ Đăng nhập thành công');
            console.log('Role từ API:', data.role);
            console.log('Role lưu trong sessionStorage:', sessionStorage.getItem('role'));

            showAlert('Đăng nhập thành công! Đang chuyển hướng...', 'success');

            if (data.cartMerged) {
                showAlert(`Đã hợp nhất ${data.mergedItemsCount} sản phẩm vào giỏ hàng`, 'success');
            }

            // Chuyển hướng dựa trên vai trò
            const role = data.role ? String(data.role).trim().toUpperCase() : '';
            console.log('Role đã format:', role);
            
            const isAdmin = role.includes('ADMIN') || 
                           role.includes('QUẢN TRỊ') ||
                           role.includes('ADMIN_ROLE') ||
                           role.includes('ROLE_ADMIN');
            const redirectUrl = isAdmin ? 'home-admin.html' : 'home.html';
            
            console.log('Is Admin?', isAdmin);
            console.log('Chuyển hướng đến:', redirectUrl);

            setTimeout(() => {
                window.location.href = redirectUrl;
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
