// Admin Motorbikes Management JavaScript

const API_BASE_URL = 'http://localhost:8080/api';

document.addEventListener('DOMContentLoaded', () => {
    checkAdminAuth();
    loadAllMotorbikes();
});

function checkAdminAuth() {
    const role = sessionStorage.getItem('userRole');
    if (role !== 'ADMIN') {
        alert('Bạn không có quyền truy cập Admin Panel!');
        window.location.href = 'home.html';
        return false;
    }
    const username = sessionStorage.getItem('username');
    if (username) document.getElementById('userName').textContent = username;
    return true;
}

// Use Case: GetAllMotorbikes
async function loadAllMotorbikes() {
    showLoading(true);
    try {
        const response = await fetch(`${API_BASE_URL}/motorbikes`);
        const data = await response.json();
        showLoading(false);
        if (data && Array.isArray(data)) {
            displayMotorbikes(data);
        } else {
            showEmptyState();
        }
    } catch (error) {
        showLoading(false);
        console.error('Error loading motorbikes:', error);
        showAlert('Lỗi kết nối server', 'error');
    }
}

// Use Case: SearchMotorbikes
async function searchMotorbikes(event) {
    event.preventDefault();
    showLoading(true);
    const params = new URLSearchParams();
    const keyword = document.getElementById('searchKeyword').value;
    const brand = document.getElementById('searchBrand').value;
    const model = document.getElementById('searchModel').value;
    const color = document.getElementById('searchColor').value;
    
    if (keyword) params.append('keyword', keyword);
    if (brand) params.append('brand', brand);
    if (model) params.append('model', model);
    if (color) params.append('color', color);

    try {
        const response = await fetch(`${API_BASE_URL}/motorbikes/search?${params.toString()}`);
        const data = await response.json();
        showLoading(false);
        if (data && Array.isArray(data) && data.length > 0) {
            displayMotorbikes(data);
        } else {
            showEmptyState();
        }
    } catch (error) {
        showLoading(false);
        console.error('Error searching motorbikes:', error);
        showAlert('Lỗi kết nối server', 'error');
    }
}

function displayMotorbikes(motorbikes) {
    const tbody = document.getElementById('motorbikesTableBody');
    const container = document.getElementById('motorbikesContainer');
    const emptyState = document.getElementById('emptyState');
    container.classList.remove('hidden');
    emptyState.classList.add('hidden');

    tbody.innerHTML = motorbikes.map(mb => `
        <tr>
            <td><img src="${mb.imageUrl || 'https://via.placeholder.com/60'}" alt="${mb.name}" class="product-thumbnail"></td>
            <td>${mb.name}</td>
            <td>${mb.brand || 'N/A'}</td>
            <td>${mb.model || 'N/A'}</td>
            <td>${mb.color || 'N/A'}</td>
            <td>${mb.displacement || 'N/A'} cc</td>
            <td>${mb.year || 'N/A'}</td>
            <td>${formatCurrency(mb.price)}</td>
            <td>${mb.stock || 0}</td>
            <td>
                <div class="action-buttons">
                    <button class="btn-action btn-edit" onclick='editMotorbike(${JSON.stringify(mb)})'>Sửa</button>
                    <button class="btn-action btn-delete" onclick="deleteMotorbike(${mb.id}, '${mb.name}')">Xóa</button>
                </div>
            </td>
        </tr>
    `).join('');
}

function showEmptyState() {
    document.getElementById('motorbikesContainer').classList.add('hidden');
    document.getElementById('emptyState').classList.remove('hidden');
}

function showLoading(show) {
    const loading = document.getElementById('loadingIndicator');
    const container = document.getElementById('motorbikesContainer');
    const emptyState = document.getElementById('emptyState');
    if (show) {
        loading.classList.remove('hidden');
        container.classList.add('hidden');
        emptyState.classList.add('hidden');
    } else {
        loading.classList.add('hidden');
    }
}

function showAddMotorbikeModal() {
    document.getElementById('modalTitle').textContent = 'Thêm Xe máy';
    document.getElementById('motorbikeForm').reset();
    document.getElementById('motorbikeId').value = '';
    document.getElementById('motorbikeModal').classList.add('show');
}

function closeMotorbikeModal() {
    document.getElementById('motorbikeModal').classList.remove('show');
}

// Use Case: AddMotorbike
async function submitMotorbikeForm() {
    const motorbikeData = {
        name: document.getElementById('motorbikeName').value,
        description: document.getElementById('motorbikeDescription').value,
        price: document.getElementById('motorbikePrice').value,
        imageUrl: document.getElementById('motorbikeImage').value,
        stock: document.getElementById('motorbikeStock').value,
        brand: document.getElementById('motorbikeBrand').value,
        model: document.getElementById('motorbikeModel').value,
        color: document.getElementById('motorbikeColor').value,
        year: document.getElementById('motorbikeYear').value,
        displacement: document.getElementById('motorbikeDisplacement').value
    };

    try {
        const response = await fetch(`${API_BASE_URL}/motorbikes/add`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(motorbikeData)
        });
        const data = await response.json();
        if (data.productId || response.ok) {
            showAlert('Thêm xe máy thành công!', 'success');
            closeMotorbikeModal();
            loadAllMotorbikes();
        } else {
            showAlert(data.errorMessage || 'Có lỗi xảy ra', 'error');
        }
    } catch (error) {
        console.error('Error adding motorbike:', error);
        showAlert('Lỗi kết nối server', 'error');
    }
}

// Use Case: UpdateMotorbike
function editMotorbike(motorbike) {
    document.getElementById('modalTitle').textContent = 'Sửa Xe máy';
    document.getElementById('motorbikeId').value = motorbike.id;
    document.getElementById('motorbikeName').value = motorbike.name || '';
    document.getElementById('motorbikeDescription').value = motorbike.description || '';
    document.getElementById('motorbikePrice').value = motorbike.price || 0;
    document.getElementById('motorbikeImage').value = motorbike.imageUrl || '';
    document.getElementById('motorbikeStock').value = motorbike.stock || 0;
    document.getElementById('motorbikeBrand').value = motorbike.brand || '';
    document.getElementById('motorbikeModel').value = motorbike.model || '';
    document.getElementById('motorbikeColor').value = motorbike.color || '';
    document.getElementById('motorbikeYear').value = motorbike.year || '';
    document.getElementById('motorbikeDisplacement').value = motorbike.displacement || '';
    document.getElementById('motorbikeModal').classList.add('show');
}

// Use Case: DeleteMotorbike
async function deleteMotorbike(id, name) {
    if (!confirm(`Bạn có chắc muốn xóa xe "${name}"?`)) return;
    try {
        const response = await fetch(`${API_BASE_URL}/admin/motorbikes/${id}/delete`, {
            method: 'DELETE'
        });
        if (response.ok) {
            showAlert('Xóa xe máy thành công!', 'success');
            loadAllMotorbikes();
        } else {
            showAlert('Không thể xóa xe máy', 'error');
        }
    } catch (error) {
        console.error('Error deleting motorbike:', error);
        showAlert('Lỗi kết nối server', 'error');
    }
}

function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
}

function showAlert(message, type) {
    const alertContainer = document.getElementById('alertContainer');
    alertContainer.innerHTML = `<div class="alert alert-${type} show"><span>${message}</span></div>`;
    setTimeout(() => { alertContainer.innerHTML = ''; }, 3000);
}
