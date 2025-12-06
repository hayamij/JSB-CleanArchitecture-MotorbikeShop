// Admin Users Management JavaScript

const API_BASE_URL = 'http://localhost:8080/api';

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    checkAdminAuth();
    loadAllUsers();
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

// Load all users - Use Case: GetAllUsers
async function loadAllUsers() {
    const tbody = document.getElementById('usersTableBody');
    tbody.innerHTML = '<tr><td colspan="8" style="text-align: center; padding: 40px;"><div class="loading-spinner"></div></td></tr>';
    
    try {
        const response = await fetch(`${API_BASE_URL}/admin/users/all`);
        const data = await response.json();

        if (data.success && data.users) {
            displayUsers(data.users);
        } else {
            tbody.innerHTML = '<tr><td colspan="8" style="text-align: center; padding: 40px; color: var(--color-gray);">Không thể tải danh sách users</td></tr>';
            showAlert(data.errorMessage || 'Không thể tải danh sách users', 'error');
        }
    } catch (error) {
        tbody.innerHTML = '<tr><td colspan="8" style="text-align: center; padding: 40px; color: var(--color-gray);">Lỗi kết nối server</td></tr>';
        console.error('Error loading users:', error);
        showAlert('Lỗi kết nối server', 'error');
    }
}

// Search users - Use Case: SearchUsers
async function searchUsers(event) {
    event.preventDefault();
    const tbody = document.getElementById('usersTableBody');
    tbody.innerHTML = '<tr><td colspan="8" style="text-align: center; padding: 40px;"><div class="loading-spinner"></div></td></tr>';

    const keyword = document.getElementById('searchKeyword').value;
    const role = document.getElementById('searchRole').value;

    const params = new URLSearchParams();
    if (keyword) params.append('keyword', keyword);
    if (role) params.append('role', role);

    try {
        const response = await fetch(`${API_BASE_URL}/admin/users/search?${params.toString()}`);
        const data = await response.json();

        if (data.success && data.users) {
            displayUsers(data.users);
        } else {
            tbody.innerHTML = '<tr><td colspan="8" style="text-align: center; padding: 40px; color: var(--color-gray);">Không tìm thấy user</td></tr>';
            showAlert(data.errorMessage || 'Không tìm thấy user', 'error');
        }
    } catch (error) {
        tbody.innerHTML = '<tr><td colspan="8" style="text-align: center; padding: 40px; color: var(--color-gray);">Lỗi kết nối server</td></tr>';
        console.error('Error searching users:', error);
        showAlert('Lỗi kết nối server', 'error');
    }
}

// Display users in table
function displayUsers(users) {
    const tbody = document.getElementById('usersTableBody');
    
    if (!users || users.length === 0) {
        tbody.innerHTML = '<tr><td colspan="8" style="text-align: center; padding: 40px; color: var(--color-gray);">Không tìm thấy user nào</td></tr>';
        return;
    }

    tbody.innerHTML = users.map(user => `
        <tr>
            <td><strong>${user.maTaiKhoan}</strong></td>
            <td><strong>${user.tenDangNhap || 'N/A'}</strong></td>
            <td>${user.email || 'N/A'}</td>
            <td><span class="status-badge ${user.vaiTro === 'ADMIN' ? 'status-confirmed' : 'status-pending'}">${user.vaiTro || 'N/A'}</span></td>
            <td>${user.soDienThoai || 'N/A'}</td>
            <td>${user.diaChi || 'N/A'}</td>
            <td>
                <span class="visibility-badge ${user.hoatDong ? 'visibility-visible' : 'visibility-hidden'}">
                    ${user.hoatDong ? 'Hoạt động' : 'Bị khóa'}
                </span>
            </td>
            <td>
                <div class="action-buttons">
                    <button class="btn-action btn-edit" onclick="editUser(${user.maTaiKhoan})">Sửa</button>
                    <button class="btn-action" onclick="toggleUserStatus(${user.maTaiKhoan}, ${user.hoatDong})">${user.hoatDong ? 'Khóa' : 'Mở'}</button>
                    <button class="btn-action btn-delete" onclick="deleteUser(${user.maTaiKhoan}, '${user.tenDangNhap}')">Xóa</button>
                </div>
            </td>
        </tr>
    `).join('');
}

// Show add user modal
function showAddUserModal() {
    document.getElementById('modalTitle').textContent = 'THÊM USER';
    document.getElementById('userForm').reset();
    document.getElementById('userId').value = '';
    document.getElementById('userModal').classList.add('show');
}

// Close modal
function closeUserModal() {
    document.getElementById('userModal').classList.remove('show');
}

// Submit user form - Use Case: CreateUser or AddUser
async function submitUserForm() {
    const userId = document.getElementById('userId').value;
    const isEdit = userId !== '';

    const userData = {
        name: document.getElementById('userName').value,
        email: document.getElementById('userEmail').value,
        password: document.getElementById('userPassword').value,
        phone: document.getElementById('userPhone').value,
        address: document.getElementById('userAddress').value,
        role: document.getElementById('userRole').value
    };

    try {
        const url = isEdit 
            ? `${API_BASE_URL}/admin/users/${userId}/update`
            : `${API_BASE_URL}/admin/users/create`;
        
        const response = await fetch(url, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(userData)
        });

        const data = await response.json();

        if (data.success || response.ok) {
            showAlert(isEdit ? 'Cập nhật user thành công!' : 'Thêm user thành công!', 'success');
            closeUserModal();
            loadAllUsers();
        } else {
            showAlert(data.errorMessage || 'Có lỗi xảy ra', 'error');
        }
    } catch (error) {
        console.error('Error submitting user:', error);
        showAlert('Lỗi kết nối server', 'error');
    }
}

// Edit user - Use Case: UpdateUser
async function editUser(userId) {
    try {
        const response = await fetch(`${API_BASE_URL}/admin/users/all`);
        const data = await response.json();
        
        if (data.success && data.users) {
            const user = data.users.find(u => u.maTaiKhoan === userId);
            if (user) {
                document.getElementById('modalTitle').textContent = 'SỬA USER';
                document.getElementById('userId').value = user.maTaiKhoan;
                document.getElementById('userName').value = user.tenDangNhap || '';
                document.getElementById('userEmail').value = user.email || '';
                document.getElementById('userPassword').value = ''; // Don't show password
                document.getElementById('userPhone').value = user.soDienThoai || '';
                document.getElementById('userAddress').value = user.diaChi || '';
                document.getElementById('userRole').value = user.vaiTro || 'CUSTOMER';
                
                document.getElementById('userModal').classList.add('show');
            }
        }
    } catch (error) {
        console.error('Error loading user:', error);
        showAlert('Không thể tải thông tin user', 'error');
    }
}

// Toggle user status (Lock/Unlock)
async function toggleUserStatus(userId, currentStatus) {
    const action = currentStatus ? 'khóa' : 'mở khóa';
    if (!confirm(`Bạn có chắc muốn ${action} user này?`)) {
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/admin/users/${userId}/status`, {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ isActive: !currentStatus })
        });

        if (response.ok) {
            showAlert(`${action.charAt(0).toUpperCase() + action.slice(1)} user thành công!`, 'success');
            loadAllUsers();
        } else {
            showAlert(`Không thể ${action} user`, 'error');
        }
    } catch (error) {
        console.error('Error toggling user status:', error);
        showAlert('Lỗi kết nối server', 'error');
    }
}

// Delete user - Use Case: DeleteUser
async function deleteUser(userId, userName) {
    if (!confirm(`Bạn có chắc muốn xóa user "${userName}"?`)) {
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/admin/users/${userId}/delete`, {
            method: 'DELETE'
        });

        const data = await response.json();

        if (data.success || response.ok) {
            showAlert('Xóa user thành công!', 'success');
            loadAllUsers();
        } else {
            showAlert(data.errorMessage || 'Không thể xóa user', 'error');
        }
    } catch (error) {
        console.error('Error deleting user:', error);
        showAlert('Lỗi kết nối server', 'error');
    }
}

function showAlert(message, type) {
    const alertContainer = document.getElementById('alertContainer');
    alertContainer.innerHTML = `
        <div class="alert alert-${type} show">
            <span>${message}</span>
        </div>
    `;
    
    setTimeout(() => {
        alertContainer.innerHTML = '';
    }, 3000);
}
