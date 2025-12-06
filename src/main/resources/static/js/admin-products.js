// Admin Products JavaScript

const API_BASE_URL = 'http://localhost:8080/api';
let currentEditingMotorbikeId = null;
let currentEditingAccessoryId = null;

// Check admin authentication
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

// Format price
function formatPrice(price) {
    return price.toLocaleString('vi-VN') + ' đ';
}

// Tab switching
function switchTab(tab) {
    // Update tab buttons
    document.querySelectorAll('.tab-btn').forEach(btn => btn.classList.remove('active'));
    event.target.classList.add('active');
    
    // Update tab content
    document.querySelectorAll('.tab-content').forEach(content => content.classList.remove('active'));
    
    if (tab === 'motorbikes') {
        document.getElementById('motorbikesTab').classList.add('active');
    } else {
        document.getElementById('accessoriesTab').classList.add('active');
    }
}

// ==================== MOTORBIKES ====================

// Load all motorbikes
async function loadAllMotorbikes() {
    try {
        const response = await fetch(`${API_BASE_URL}/motorbikes`);
        if (!response.ok) throw new Error('Failed to load motorbikes');
        
        const motorbikes = await response.json();
        renderMotorbikes(motorbikes);
        
    } catch (error) {
        console.error('Error loading motorbikes:', error);
        showAlert('Không thể tải danh sách xe máy', 'error');
    }
}

// Search motorbikes
async function searchMotorbikes(event) {
    event.preventDefault();
    
    const name = document.getElementById('searchMotorbikeName').value.trim();
    const brand = document.getElementById('searchMotorbikeBrand').value.trim();
    
    if (!name && !brand) {
        loadAllMotorbikes();
        return;
    }
    
    try {
        let url = `${API_BASE_URL}/motorbikes/search?`;
        if (name) url += `name=${encodeURIComponent(name)}&`;
        if (brand) url += `brand=${encodeURIComponent(brand)}&`;
        
        const response = await fetch(url);
        if (!response.ok) throw new Error('Failed to search motorbikes');
        
        const motorbikes = await response.json();
        renderMotorbikes(motorbikes);
        
    } catch (error) {
        console.error('Error searching motorbikes:', error);
        showAlert('Không thể tìm kiếm xe máy', 'error');
    }
}

// Render motorbikes table
function renderMotorbikes(motorbikes) {
    const tbody = document.getElementById('motorbikesTableBody');
    
    if (!motorbikes || motorbikes.length === 0) {
        tbody.innerHTML = '<tr><td colspan="9" style="text-align: center; padding: 40px; color: var(--color-gray);">Không tìm thấy xe máy nào</td></tr>';
        return;
    }
    
    tbody.innerHTML = motorbikes.map(bike => `
        <tr>
            <td>${bike.motorbikeId}</td>
            <td>
                ${bike.imageUrl ? `<img src="${bike.imageUrl}" class="product-thumbnail" alt="${bike.name}">` : 'N/A'}
            </td>
            <td><strong>${bike.name}</strong></td>
            <td>${bike.brand || 'N/A'}</td>
            <td>${bike.color || 'N/A'}</td>
            <td><strong>${formatPrice(bike.price)}</strong></td>
            <td>${bike.stockQuantity || 0}</td>
            <td>
                <span class="visibility-badge ${bike.isVisible ? 'visibility-visible' : 'visibility-hidden'}">
                    ${bike.isVisible ? 'Hiển thị' : 'Ẩn'}
                </span>
            </td>
            <td>
                <div class="action-buttons">
                    <button class="btn-action btn-edit" onclick="editMotorbike(${bike.motorbikeId})">Sửa</button>
                    <button class="btn-action" onclick="toggleMotorbikeVisibility(${bike.motorbikeId}, ${bike.isVisible})">${bike.isVisible ? 'Ẩn' : 'Hiện'}</button>
                    <button class="btn-action btn-delete" onclick="deleteMotorbike(${bike.motorbikeId})">Xóa</button>
                </div>
            </td>
        </tr>
    `).join('');
}

// Show add motorbike modal
function showAddMotorbikeModal() {
    currentEditingMotorbikeId = null;
    document.getElementById('motorbikeModalTitle').textContent = 'THÊM XE MÁY';
    document.getElementById('motorbikeForm').reset();
    document.getElementById('motorbikeId').value = '';
    document.getElementById('motorbikeModal').classList.add('show');
}

// Edit motorbike
async function editMotorbike(id) {
    try {
        const response = await fetch(`${API_BASE_URL}/motorbikes/${id}`);
        if (!response.ok) throw new Error('Failed to load motorbike');
        
        const bike = await response.json();
        currentEditingMotorbikeId = id;
        
        document.getElementById('motorbikeModalTitle').textContent = 'SỬA XE MÁY';
        document.getElementById('motorbikeId').value = bike.motorbikeId;
        document.getElementById('motorbikeName').value = bike.name || '';
        document.getElementById('motorbikeBrand').value = bike.brand || '';
        document.getElementById('motorbikeColor').value = bike.color || '';
        document.getElementById('motorbikePrice').value = bike.price || 0;
        document.getElementById('motorbikeStock').value = bike.stockQuantity || 0;
        document.getElementById('motorbikeImage').value = bike.imageUrl || '';
        document.getElementById('motorbikeDescription').value = bike.description || '';
        
        document.getElementById('motorbikeModal').classList.add('show');
        
    } catch (error) {
        console.error('Error loading motorbike:', error);
        showAlert('Không thể tải thông tin xe máy', 'error');
    }
}

// Save motorbike
async function saveMotorbike() {
    const name = document.getElementById('motorbikeName').value.trim();
    const brand = document.getElementById('motorbikeBrand').value.trim();
    const color = document.getElementById('motorbikeColor').value.trim();
    const price = parseFloat(document.getElementById('motorbikePrice').value);
    const stock = parseInt(document.getElementById('motorbikeStock').value);
    const image = document.getElementById('motorbikeImage').value.trim();
    const description = document.getElementById('motorbikeDescription').value.trim();
    
    if (!name || !brand || !color || isNaN(price) || isNaN(stock)) {
        showAlert('Vui lòng điền đầy đủ thông tin bắt buộc', 'error');
        return;
    }
    
    const motorbikeData = {
        name,
        brand,
        color,
        price,
        stockQuantity: stock,
        imageUrl: image || null,
        description: description || null
    };
    
    try {
        let response;
        if (currentEditingMotorbikeId) {
            // Update
            response = await fetch(`${API_BASE_URL}/admin/motorbikes/${currentEditingMotorbikeId}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(motorbikeData)
            });
        } else {
            // Create
            response = await fetch(`${API_BASE_URL}/admin/motorbikes`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(motorbikeData)
            });
        }
        
        if (!response.ok) throw new Error('Failed to save motorbike');
        
        showAlert(currentEditingMotorbikeId ? 'Cập nhật xe máy thành công' : 'Thêm xe máy thành công', 'success');
        closeMotorbikeModal();
        loadAllMotorbikes();
        
    } catch (error) {
        console.error('Error saving motorbike:', error);
        showAlert('Không thể lưu xe máy', 'error');
    }
}

// Toggle motorbike visibility
async function toggleMotorbikeVisibility(id, currentVisibility) {
    if (!confirm(`Bạn có chắc muốn ${currentVisibility ? 'ẩn' : 'hiện'} xe máy này?`)) return;
    
    try {
        const response = await fetch(`${API_BASE_URL}/admin/motorbikes/${id}/visibility`, {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ isVisible: !currentVisibility })
        });
        
        if (!response.ok) throw new Error('Failed to toggle visibility');
        
        showAlert(`${currentVisibility ? 'Ẩn' : 'Hiện'} xe máy thành công`, 'success');
        loadAllMotorbikes();
        
    } catch (error) {
        console.error('Error toggling visibility:', error);
        showAlert('Không thể thay đổi trạng thái hiển thị', 'error');
    }
}

// Delete motorbike
async function deleteMotorbike(id) {
    if (!confirm('Bạn có chắc chắn muốn xóa xe máy này?')) return;
    
    try {
        const response = await fetch(`${API_BASE_URL}/admin/motorbikes/${id}`, {
            method: 'DELETE'
        });
        
        if (!response.ok) throw new Error('Failed to delete motorbike');
        
        showAlert('Xóa xe máy thành công', 'success');
        loadAllMotorbikes();
        
    } catch (error) {
        console.error('Error deleting motorbike:', error);
        showAlert('Không thể xóa xe máy', 'error');
    }
}

// Close motorbike modal
function closeMotorbikeModal() {
    document.getElementById('motorbikeModal').classList.remove('show');
    document.getElementById('motorbikeForm').reset();
    currentEditingMotorbikeId = null;
}

// ==================== ACCESSORIES ====================

// Load all accessories
async function loadAllAccessories() {
    try {
        const response = await fetch(`${API_BASE_URL}/accessories`);
        if (!response.ok) throw new Error('Failed to load accessories');
        
        const accessories = await response.json();
        renderAccessories(accessories);
        
    } catch (error) {
        console.error('Error loading accessories:', error);
        showAlert('Không thể tải danh sách phụ kiện', 'error');
    }
}

// Search accessories
async function searchAccessories(event) {
    event.preventDefault();
    
    const name = document.getElementById('searchAccessoryName').value.trim();
    const category = document.getElementById('searchAccessoryCategory').value.trim();
    
    if (!name && !category) {
        loadAllAccessories();
        return;
    }
    
    try {
        let url = `${API_BASE_URL}/accessories/search?`;
        if (name) url += `name=${encodeURIComponent(name)}&`;
        if (category) url += `category=${encodeURIComponent(category)}&`;
        
        const response = await fetch(url);
        if (!response.ok) throw new Error('Failed to search accessories');
        
        const accessories = await response.json();
        renderAccessories(accessories);
        
    } catch (error) {
        console.error('Error searching accessories:', error);
        showAlert('Không thể tìm kiếm phụ kiện', 'error');
    }
}

// Render accessories table
function renderAccessories(accessories) {
    const tbody = document.getElementById('accessoriesTableBody');
    
    if (!accessories || accessories.length === 0) {
        tbody.innerHTML = '<tr><td colspan="8" style="text-align: center; padding: 40px; color: var(--color-gray);">Không tìm thấy phụ kiện nào</td></tr>';
        return;
    }
    
    tbody.innerHTML = accessories.map(acc => `
        <tr>
            <td>${acc.accessoryId}</td>
            <td>
                ${acc.imageUrl ? `<img src="${acc.imageUrl}" class="product-thumbnail" alt="${acc.name}">` : 'N/A'}
            </td>
            <td><strong>${acc.name}</strong></td>
            <td>${acc.category || 'N/A'}</td>
            <td><strong>${formatPrice(acc.price)}</strong></td>
            <td>${acc.stockQuantity || 0}</td>
            <td>
                <span class="visibility-badge ${acc.isVisible ? 'visibility-visible' : 'visibility-hidden'}">
                    ${acc.isVisible ? 'Hiển thị' : 'Ẩn'}
                </span>
            </td>
            <td>
                <div class="action-buttons">
                    <button class="btn-action btn-edit" onclick="editAccessory(${acc.accessoryId})">Sửa</button>
                    <button class="btn-action" onclick="toggleAccessoryVisibility(${acc.accessoryId}, ${acc.isVisible})">${acc.isVisible ? 'Ẩn' : 'Hiện'}</button>
                    <button class="btn-action btn-delete" onclick="deleteAccessory(${acc.accessoryId})">Xóa</button>
                </div>
            </td>
        </tr>
    `).join('');
}

// Show add accessory modal
function showAddAccessoryModal() {
    currentEditingAccessoryId = null;
    document.getElementById('accessoryModalTitle').textContent = 'THÊM PHỤ KIỆN';
    document.getElementById('accessoryForm').reset();
    document.getElementById('accessoryId').value = '';
    document.getElementById('accessoryModal').classList.add('show');
}

// Edit accessory
async function editAccessory(id) {
    try {
        const response = await fetch(`${API_BASE_URL}/accessories/${id}`);
        if (!response.ok) throw new Error('Failed to load accessory');
        
        const acc = await response.json();
        currentEditingAccessoryId = id;
        
        document.getElementById('accessoryModalTitle').textContent = 'SỬA PHỤ KIỆN';
        document.getElementById('accessoryId').value = acc.accessoryId;
        document.getElementById('accessoryName').value = acc.name || '';
        document.getElementById('accessoryCategory').value = acc.category || '';
        document.getElementById('accessoryPrice').value = acc.price || 0;
        document.getElementById('accessoryStock').value = acc.stockQuantity || 0;
        document.getElementById('accessoryImage').value = acc.imageUrl || '';
        document.getElementById('accessoryDescription').value = acc.description || '';
        
        document.getElementById('accessoryModal').classList.add('show');
        
    } catch (error) {
        console.error('Error loading accessory:', error);
        showAlert('Không thể tải thông tin phụ kiện', 'error');
    }
}

// Save accessory
async function saveAccessory() {
    const name = document.getElementById('accessoryName').value.trim();
    const category = document.getElementById('accessoryCategory').value.trim();
    const price = parseFloat(document.getElementById('accessoryPrice').value);
    const stock = parseInt(document.getElementById('accessoryStock').value);
    const image = document.getElementById('accessoryImage').value.trim();
    const description = document.getElementById('accessoryDescription').value.trim();
    
    if (!name || !category || isNaN(price) || isNaN(stock)) {
        showAlert('Vui lòng điền đầy đủ thông tin bắt buộc', 'error');
        return;
    }
    
    const accessoryData = {
        name,
        category,
        price,
        stockQuantity: stock,
        imageUrl: image || null,
        description: description || null
    };
    
    try {
        let response;
        if (currentEditingAccessoryId) {
            // Update
            response = await fetch(`${API_BASE_URL}/admin/accessories/${currentEditingAccessoryId}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(accessoryData)
            });
        } else {
            // Create
            response = await fetch(`${API_BASE_URL}/admin/accessories`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(accessoryData)
            });
        }
        
        if (!response.ok) throw new Error('Failed to save accessory');
        
        showAlert(currentEditingAccessoryId ? 'Cập nhật phụ kiện thành công' : 'Thêm phụ kiện thành công', 'success');
        closeAccessoryModal();
        loadAllAccessories();
        
    } catch (error) {
        console.error('Error saving accessory:', error);
        showAlert('Không thể lưu phụ kiện', 'error');
    }
}

// Toggle accessory visibility
async function toggleAccessoryVisibility(id, currentVisibility) {
    if (!confirm(`Bạn có chắc muốn ${currentVisibility ? 'ẩn' : 'hiện'} phụ kiện này?`)) return;
    
    try {
        const response = await fetch(`${API_BASE_URL}/admin/accessories/${id}/visibility`, {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ isVisible: !currentVisibility })
        });
        
        if (!response.ok) throw new Error('Failed to toggle visibility');
        
        showAlert(`${currentVisibility ? 'Ẩn' : 'Hiện'} phụ kiện thành công`, 'success');
        loadAllAccessories();
        
    } catch (error) {
        console.error('Error toggling visibility:', error);
        showAlert('Không thể thay đổi trạng thái hiển thị', 'error');
    }
}

// Delete accessory
async function deleteAccessory(id) {
    if (!confirm('Bạn có chắc chắn muốn xóa phụ kiện này?')) return;
    
    try {
        const response = await fetch(`${API_BASE_URL}/admin/accessories/${id}`, {
            method: 'DELETE'
        });
        
        if (!response.ok) throw new Error('Failed to delete accessory');
        
        showAlert('Xóa phụ kiện thành công', 'success');
        loadAllAccessories();
        
    } catch (error) {
        console.error('Error deleting accessory:', error);
        showAlert('Không thể xóa phụ kiện', 'error');
    }
}

// Close accessory modal
function closeAccessoryModal() {
    document.getElementById('accessoryModal').classList.remove('show');
    document.getElementById('accessoryForm').reset();
    currentEditingAccessoryId = null;
}

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    if (checkAdminAuth()) {
        loadAllMotorbikes();
        loadAllAccessories();
    }
});
