// Admin Users Management JavaScript

const API_BASE_URL = 'http://localhost:8080/api';

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    checkAdminAuth();
    loadAllUsers();
});

function checkAdminAuth() {
    const role = sessionStorage.getItem('userRole');
    if (role !== 'ADMIN') {
        alert('Bạn không có quyền truy cập Admin Panel!');
        window.location.href = 'home.html';
        return false;
    }
    
    const userName = sessionStorage.getItem('userName');
    if (userName) {
        document.getElementById('userName').textContent = userName;
    }
    
    return true;
}

// Load all users - Use Case: GetAllUsers
async function loadAllUsers() {
    showLoading(true);
    try {
        const response = await fetch(`${API_BASE_URL}/admin/users/all`);
        const data = await response.json();

        showLoading(false);

        if (data.success && data.users) {
            displayUsers(data.users);
        } else {
            showEmptyState();
            showAlert(data.errorMessage || 'Không thể tải danh sách users', 'error');
        }
    } catch (error) {
        showLoading(false);
        console.error('Error loading users:', error);
        showAlert('Lỗi kết nối server', 'error');
    }
}

// Search users - Use Case: SearchUsers
async function searchUsers(event) {
    event.preventDefault();
    showLoading(true);

    const keyword = document.getElementById('searchKeyword').value;
    const role = document.getElementById('searchRole').value;

    const params = new URLSearchParams();
    if (keyword) params.append('keyword', keyword);
    if (role) params.append('role', role);

    try {
        const response = await fetch(`${API_BASE_URL}/admin/users/search?${params.toString()}`);
        const data = await response.json();

        showLoading(false);

        if (data.success && data.users) {
            if (data.users.length === 0) {
                showEmptyState();
            } else {
                displayUsers(data.users);
            }
        } else {
            showEmptyState();
            showAlert(data.errorMessage || 'Không tìm thấy user', 'error');
        }
    } catch (error) {
        showLoading(false);
        console.error('Error searching users:', error);
        showAlert('Lỗi kết nối server', 'error');
    }
}

// Display users in table
function displayUsers(users) {
    const tbody = document.getElementById('usersTableBody');
    const container = document.getElementById('usersContainer');
    const emptyState = document.getElementById('emptyState');

    container.classList.remove('hidden');
    emptyState.classList.add('hidden');

    tbody.innerHTML = users.map(user => `
        <tr>
            <td>${user.userId}</td>
            <td>${user.userName || 'N/A'}</td>
            <td>${user.email || 'N/A'}</td>
            <td><span class="status-badge ${user.role === 'ADMIN' ? 'status-confirmed' : 'status-pending'}">${user.role || 'N/A'}</span></td>
            <td>${user.phone || 'N/A'}</td>
            <td>${user.address || 'N/A'}</td>
            <td>
                <div class="action-buttons">
                    <button class="btn-action btn-edit" onclick="editUser(${user.userId})">Sửa</button>
                    <button class="btn-action btn-delete" onclick="deleteUser(${user.userId}, '${user.userName}')">Xóa</button>
                </div>
            </td>
        </tr>
    `).join('');
}

// Show empty state
function showEmptyState() {
    document.getElementById('usersContainer').classList.add('hidden');
    document.getElementById('emptyState').classList.remove('hidden');
}

// Show loading
function showLoading(show) {
    const loading = document.getElementById('loadingIndicator');
    const container = document.getElementById('usersContainer');
    const emptyState = document.getElementById('emptyState');

    if (show) {
        loading.classList.remove('hidden');
        container.classList.add('hidden');
        emptyState.classList.add('hidden');
    } else {
        loading.classList.add('hidden');
    }
}

// Show add user modal
function showAddUserModal() {
    document.getElementById('modalTitle').textContent = 'Thêm User';
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
            const user = data.users.find(u => u.userId === userId);
            if (user) {
                document.getElementById('modalTitle').textContent = 'Sửa User';
                document.getElementById('userId').value = user.userId;
                document.getElementById('userName').value = user.userName || '';
                document.getElementById('userEmail').value = user.email || '';
                document.getElementById('userPassword').value = ''; // Don't show password
                document.getElementById('userPhone').value = user.phone || '';
                document.getElementById('userAddress').value = user.address || '';
                document.getElementById('userRole').value = user.role || 'CUSTOMER';
                
                document.getElementById('userModal').classList.add('show');
            }
        }
    } catch (error) {
        console.error('Error loading user:', error);
        showAlert('Không thể tải thông tin user', 'error');
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
