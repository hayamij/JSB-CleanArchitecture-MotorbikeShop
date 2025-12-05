// Admin Accessories Management JavaScript

const API_BASE_URL = 'http://localhost:8080/api';

document.addEventListener('DOMContentLoaded', () => {
    checkAdminAuth();
    loadAllAccessories();
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

// Use Case: GetAllAccessories
async function loadAllAccessories() {
    showLoading(true);
    try {
        const response = await fetch(`${API_BASE_URL}/accessories`);
        const data = await response.json();
        showLoading(false);
        if (data && Array.isArray(data)) {
            displayAccessories(data);
        } else {
            showEmptyState();
        }
    } catch (error) {
        showLoading(false);
        console.error('Error loading accessories:', error);
        showAlert('Lỗi kết nối server', 'error');
    }
}

// Use Case: SearchAccessories
async function searchAccessories(event) {
    event.preventDefault();
    showLoading(true);
    const params = new URLSearchParams();
    const keyword = document.getElementById('searchKeyword').value;
    const type = document.getElementById('searchType').value;
    const brand = document.getElementById('searchBrand').value;
    const material = document.getElementById('searchMaterial').value;
    
    if (keyword) params.append('keyword', keyword);
    if (type) params.append('type', type);
    if (brand) params.append('brand', brand);
    if (material) params.append('material', material);

    try {
        const response = await fetch(`${API_BASE_URL}/accessories/search?${params.toString()}`);
        const data = await response.json();
        showLoading(false);
        if (data && Array.isArray(data) && data.length > 0) {
            displayAccessories(data);
        } else {
            showEmptyState();
        }
    } catch (error) {
        showLoading(false);
        console.error('Error searching accessories:', error);
        showAlert('Lỗi kết nối server', 'error');
    }
}

function displayAccessories(accessories) {
    const tbody = document.getElementById('accessoriesTableBody');
    const container = document.getElementById('accessoriesContainer');
    const emptyState = document.getElementById('emptyState');
    container.classList.remove('hidden');
    emptyState.classList.add('hidden');

    tbody.innerHTML = accessories.map(acc => `
        <tr>
            <td><img src="${acc.imageUrl || 'https://via.placeholder.com/60'}" alt="${acc.name}" class="product-thumbnail"></td>
            <td>${acc.name}</td>
            <td>${acc.loaiPhuKien || acc.type || 'N/A'}</td>
            <td>${acc.thuongHieu || acc.brand || 'N/A'}</td>
            <td>${acc.chatLieu || acc.material || 'N/A'}</td>
            <td>${formatCurrency(acc.price)}</td>
            <td>${acc.stock || 0}</td>
            <td>
                <div class="action-buttons">
                    <button class="btn-action btn-edit" onclick='editAccessory(${JSON.stringify(acc)})'>Sửa</button>
                    <button class="btn-action btn-delete" onclick="deleteAccessory(${acc.id}, '${acc.name}')">Xóa</button>
                </div>
            </td>
        </tr>
    `).join('');
}

function showEmptyState() {
    document.getElementById('accessoriesContainer').classList.add('hidden');
    document.getElementById('emptyState').classList.remove('hidden');
}

function showLoading(show) {
    const loading = document.getElementById('loadingIndicator');
    const container = document.getElementById('accessoriesContainer');
    const emptyState = document.getElementById('emptyState');
    if (show) {
        loading.classList.remove('hidden');
        container.classList.add('hidden');
        emptyState.classList.add('hidden');
    } else {
        loading.classList.add('hidden');
    }
}

function showAddAccessoryModal() {
    document.getElementById('modalTitle').textContent = 'Thêm Phụ kiện';
    document.getElementById('accessoryForm').reset();
    document.getElementById('accessoryId').value = '';
    document.getElementById('accessoryModal').classList.add('show');
}

function closeAccessoryModal() {
    document.getElementById('accessoryModal').classList.remove('show');
}

// Use Case: AddAccessory
async function submitAccessoryForm() {
    const accessoryData = {
        name: document.getElementById('accessoryName').value,
        description: document.getElementById('accessoryDescription').value,
        price: document.getElementById('accessoryPrice').value,
        imageUrl: document.getElementById('accessoryImage').value,
        stock: document.getElementById('accessoryStock').value,
        loaiPhuKien: document.getElementById('accessoryType').value,
        thuongHieu: document.getElementById('accessoryBrand').value,
        chatLieu: document.getElementById('accessoryMaterial').value
    };

    try {
        const response = await fetch(`${API_BASE_URL}/admin/accessories/add`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(accessoryData)
        });
        const data = await response.json();
        if (data.productId || response.ok) {
            showAlert('Thêm phụ kiện thành công!', 'success');
            closeAccessoryModal();
            loadAllAccessories();
        } else {
            showAlert(data.errorMessage || 'Có lỗi xảy ra', 'error');
        }
    } catch (error) {
        console.error('Error adding accessory:', error);
        showAlert('Lỗi kết nối server', 'error');
    }
}

// Use Case: UpdateAccessory
function editAccessory(accessory) {
    document.getElementById('modalTitle').textContent = 'Sửa Phụ kiện';
    document.getElementById('accessoryId').value = accessory.id;
    document.getElementById('accessoryName').value = accessory.name || '';
    document.getElementById('accessoryDescription').value = accessory.description || '';
    document.getElementById('accessoryPrice').value = accessory.price || 0;
    document.getElementById('accessoryImage').value = accessory.imageUrl || '';
    document.getElementById('accessoryStock').value = accessory.stock || 0;
    document.getElementById('accessoryType').value = accessory.loaiPhuKien || accessory.type || '';
    document.getElementById('accessoryBrand').value = accessory.thuongHieu || accessory.brand || '';
    document.getElementById('accessoryMaterial').value = accessory.chatLieu || accessory.material || '';
    document.getElementById('accessoryModal').classList.add('show');
}

// Use Case: DeleteAccessory
async function deleteAccessory(id, name) {
    if (!confirm(`Bạn có chắc muốn xóa phụ kiện "${name}"?`)) return;
    try {
        const response = await fetch(`${API_BASE_URL}/admin/accessories/${id}/delete`, {
            method: 'DELETE'
        });
        if (response.ok) {
            showAlert('Xóa phụ kiện thành công!', 'success');
            loadAllAccessories();
        } else {
            showAlert('Không thể xóa phụ kiện', 'error');
        }
    } catch (error) {
        console.error('Error deleting accessory:', error);
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
