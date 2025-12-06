// Admin Products JavaScript

const API_BASE_URL = 'http://localhost:8080/api';
let currentEditingMotorbikeId = null;
let currentEditingAccessoryId = null;
let allMotorbikesData = [];
let allAccessoriesData = [];

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
        const response = await fetch(`${API_BASE_URL}/products`);
        if (!response.ok) throw new Error('Failed to load products');
        
        const products = await response.json();
        allMotorbikesData = products.filter(p => p.category === 'XE_MAY');
        
        // Populate filter dropdowns
        populateMotorbikeFilters();
        applyMotorbikeFilters();
        
    } catch (error) {
        console.error('Error loading motorbikes:', error);
        showAlert('Không thể tải danh sách xe máy', 'error');
    }
}

// Populate filter dropdowns
function populateMotorbikeFilters() {
    const brands = [...new Set(allMotorbikesData.map(m => m.brand))].filter(Boolean).sort();
    const colors = [...new Set(allMotorbikesData.map(m => m.color))].filter(Boolean).sort();
    
    const brandSelect = document.getElementById('filterMotorbikeBrand');
    const colorSelect = document.getElementById('filterMotorbikeColor');
    
    brandSelect.innerHTML = '<option value="">Tất cả</option>' + 
        brands.map(b => `<option value="${b}">${b}</option>`).join('');
    
    colorSelect.innerHTML = '<option value="">Tất cả</option>' + 
        colors.map(c => `<option value="${c}">${c}</option>`).join('');
}

// Apply filters and sorting
function applyMotorbikeFilters() {
    if (allMotorbikesData.length === 0) return;
    
    const keyword = document.getElementById('searchMotorbikeName').value.toLowerCase();
    const brandFilter = document.getElementById('filterMotorbikeBrand').value;
    const colorFilter = document.getElementById('filterMotorbikeColor').value;
    const availabilityFilter = document.getElementById('filterMotorbikeAvailability').value;
    const sortOrder = document.getElementById('sortMotorbikeOrder').value;
    
    let filtered = [...allMotorbikesData];
    
    // Filter by keyword
    if (keyword) {
        filtered = filtered.filter(m => 
            (m.name || '').toLowerCase().includes(keyword) ||
            (m.brand || '').toLowerCase().includes(keyword) ||
            (m.model || '').toLowerCase().includes(keyword)
        );
    }
    
    // Filter by brand
    if (brandFilter) {
        filtered = filtered.filter(m => m.brand === brandFilter);
    }
    
    // Filter by color
    if (colorFilter) {
        filtered = filtered.filter(m => m.color === colorFilter);
    }
    
    // Filter by availability
    if (availabilityFilter) {
        if (availabilityFilter === 'available') {
            filtered = filtered.filter(m => m.available === true);
        } else if (availabilityFilter === 'hidden') {
            filtered = filtered.filter(m => m.available === false);
        }
    }
    
    // Sort
    filtered.sort((a, b) => {
        switch(sortOrder) {
            case 'newest':
            case 'oldest':
                const dateA = new Date(a.createdDate);
                const dateB = new Date(b.createdDate);
                return sortOrder === 'newest' ? dateB - dateA : dateA - dateB;
            case 'price-high':
            case 'price-low':
                return sortOrder === 'price-high' ? b.price - a.price : a.price - b.price;
            case 'name-asc':
                return (a.name || '').localeCompare(b.name || '', 'vi');
            case 'name-desc':
                return (b.name || '').localeCompare(a.name || '', 'vi');
            default:
                return 0;
        }
    });
    
    renderMotorbikes(filtered);
}

// Search motorbikes
function searchMotorbikes(event) {
    event.preventDefault();
    applyMotorbikeFilters();
}

// Render motorbikes table
function renderMotorbikes(motorbikes) {
    const tbody = document.getElementById('motorbikesTableBody');
    
    if (!motorbikes || motorbikes.length === 0) {
        tbody.innerHTML = '<tr><td colspan="12" style="text-align: center; padding: 40px; color: var(--color-gray);">Không tìm thấy xe máy nào</td></tr>';
        return;
    }
    
    tbody.innerHTML = motorbikes.map(bike => {
        const createdDate = bike.createdDate ? new Date(bike.createdDate).toLocaleDateString('vi-VN') : 'N/A';
        return `
        <tr>
            <td><strong>#${bike.id}</strong></td>
            <td>
                ${bike.imageUrl ? `<img src="${bike.imageUrl}" class="product-thumbnail" alt="${bike.name}">` : '<div class="no-image">Chưa có ảnh</div>'}
            </td>
            <td><strong>${bike.name}</strong></td>
            <td>${bike.brand || 'N/A'}</td>
            <td>${bike.model || 'N/A'}</td>
            <td>${bike.color || 'N/A'}</td>
            <td>${bike.year || 'N/A'}</td>
            <td>${bike.engineCapacity ? bike.engineCapacity + ' cc' : 'N/A'}</td>
            <td><strong>${formatPrice(bike.price)}</strong></td>
            <td>${bike.stock || 0}</td>
            <td><span class="badge ${bike.available ? 'badge-success' : 'badge-secondary'}">${bike.available ? 'Hiển thị' : 'Đã ẩn'}</span></td>
            <td>${createdDate}</td>
            <td>
                <div class="action-buttons">
                    <button class="btn-action btn-edit" onclick="editMotorbike(${bike.id})">Sửa</button>
                    <button class="btn-action ${!bike.available ? 'btn-secondary' : ''}" onclick="toggleMotorbikeVisibility(${bike.id}, ${bike.available})">${bike.available ? 'Ẩn' : 'Hiện'}</button>
                    <button class="btn-action btn-delete" onclick="deleteMotorbike(${bike.id})">Xóa</button>
                </div>
            </td>
        </tr>
        `;
    }).join('');
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
function editMotorbike(id) {
    const bike = allMotorbikesData.find(m => m.id === id);
    if (!bike) {
        showAlert('Không tìm thấy xe máy', 'error');
        return;
    }
    
    currentEditingMotorbikeId = id;
    
    document.getElementById('motorbikeModalTitle').textContent = 'SỬA XE MÁY';
    document.getElementById('motorbikeId').value = bike.id;
    document.getElementById('motorbikeName').value = bike.name || '';
    document.getElementById('motorbikeBrand').value = bike.brand || '';
    document.getElementById('motorbikeModel').value = bike.model || '';
    document.getElementById('motorbikeColor').value = bike.color || '';
    document.getElementById('motorbikeYear').value = bike.year || '';
    document.getElementById('motorbikeDisplacement').value = bike.engineCapacity || '';
    document.getElementById('motorbikePrice').value = bike.price || 0;
    document.getElementById('motorbikeStock').value = bike.stock || 0;
    document.getElementById('motorbikeImage').value = bike.imageUrl || '';
    document.getElementById('motorbikeDescription').value = bike.description || '';
    
    document.getElementById('motorbikeModal').classList.add('show');
}

// Save motorbike
async function saveMotorbike() {
    const name = document.getElementById('motorbikeName').value.trim();
    const brand = document.getElementById('motorbikeBrand').value.trim();
    const model = document.getElementById('motorbikeModel').value.trim();
    const color = document.getElementById('motorbikeColor').value.trim();
    const year = parseInt(document.getElementById('motorbikeYear').value);
    const engineCapacity = parseInt(document.getElementById('motorbikeDisplacement').value);
    const price = parseFloat(document.getElementById('motorbikePrice').value);
    const stock = parseInt(document.getElementById('motorbikeStock').value);
    const image = document.getElementById('motorbikeImage').value.trim();
    const description = document.getElementById('motorbikeDescription').value.trim();
    
    if (!name || !brand || !model || !color || isNaN(year) || isNaN(engineCapacity) || isNaN(price) || isNaN(stock)) {
        showAlert('Vui lòng điền đầy đủ thông tin bắt buộc', 'error');
        return;
    }
    
    const motorbikeData = {
        name,
        description,
        price,
        imageUrl: image || null,
        stock,
        brand,
        model,
        color,
        year,
        displacement: engineCapacity
    };
    
    console.log('Saving motorbike data:', JSON.stringify(motorbikeData, null, 2));
    console.log('Current editing ID:', currentEditingMotorbikeId);
    
    try {
        let response;
        if (currentEditingMotorbikeId) {
            // Update
            response = await fetch(`${API_BASE_URL}/admin/motorbikes/${currentEditingMotorbikeId}/update`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(motorbikeData)
            });
        } else {
            // Create
            response = await fetch(`${API_BASE_URL}/motorbikes/add`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(motorbikeData)
            });
        }
        
        if (!response.ok) {
            const errorData = await response.json();
            console.error('Server error:', errorData);
            throw new Error(errorData.errorMessage || 'Failed to save motorbike');
        }
        
        showAlert(currentEditingMotorbikeId ? 'Cập nhật xe máy thành công' : 'Thêm xe máy thành công', 'success');
        closeMotorbikeModal();
        loadAllMotorbikes();
        
    } catch (error) {
        console.error('Error saving motorbike:', error);
        showAlert('Không thể lưu xe máy: ' + error.message, 'error');
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
        const response = await fetch(`${API_BASE_URL}/admin/motorbikes/${id}/delete`, {
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
        const response = await fetch(`${API_BASE_URL}/products`);
        if (!response.ok) throw new Error('Failed to load products');
        
        const products = await response.json();
        allAccessoriesData = products.filter(p => p.category === 'PHU_KIEN');
        
        // Populate filter dropdowns
        populateAccessoryFilters();
        applyAccessoryFilters();
        
    } catch (error) {
        console.error('Error loading accessories:', error);
        showAlert('Không thể tải danh sách phụ kiện', 'error');
    }
}

// Populate filter dropdowns
function populateAccessoryFilters() {
    const types = [...new Set(allAccessoriesData.map(a => a.type))].filter(Boolean).sort();
    const brands = [...new Set(allAccessoriesData.map(a => a.brand))].filter(Boolean).sort();
    
    const typeSelect = document.getElementById('filterAccessoryType');
    const brandSelect = document.getElementById('filterAccessoryBrand');
    
    typeSelect.innerHTML = '<option value="">Tất cả</option>' + 
        types.map(t => `<option value="${t}">${t}</option>`).join('');
    
    brandSelect.innerHTML = '<option value="">Tất cả</option>' + 
        brands.map(b => `<option value="${b}">${b}</option>`).join('');
}

// Apply filters and sorting
function applyAccessoryFilters() {
    if (allAccessoriesData.length === 0) return;
    
    const keyword = document.getElementById('searchAccessoryName').value.toLowerCase();
    const typeFilter = document.getElementById('filterAccessoryType').value;
    const brandFilter = document.getElementById('filterAccessoryBrand').value;
    const availabilityFilter = document.getElementById('filterAccessoryAvailability').value;
    const sortOrder = document.getElementById('sortAccessoryOrder').value;
    
    let filtered = [...allAccessoriesData];
    
    // Filter by keyword
    if (keyword) {
        filtered = filtered.filter(a => 
            (a.name || '').toLowerCase().includes(keyword) ||
            (a.type || '').toLowerCase().includes(keyword) ||
            (a.brand || '').toLowerCase().includes(keyword)
        );
    }
    
    // Filter by type
    if (typeFilter) {
        filtered = filtered.filter(a => a.type === typeFilter);
    }
    
    // Filter by brand
    if (brandFilter) {
        filtered = filtered.filter(a => a.brand === brandFilter);
    }
    
    // Filter by availability
    if (availabilityFilter) {
        if (availabilityFilter === 'available') {
            filtered = filtered.filter(a => a.available === true);
        } else if (availabilityFilter === 'hidden') {
            filtered = filtered.filter(a => a.available === false);
        }
    }
    
    // Sort
    filtered.sort((a, b) => {
        switch(sortOrder) {
            case 'newest':
            case 'oldest':
                const dateA = new Date(a.createdDate);
                const dateB = new Date(b.createdDate);
                return sortOrder === 'newest' ? dateB - dateA : dateA - dateB;
            case 'price-high':
            case 'price-low':
                return sortOrder === 'price-high' ? b.price - a.price : a.price - b.price;
            case 'name-asc':
                return (a.name || '').localeCompare(b.name || '', 'vi');
            case 'name-desc':
                return (b.name || '').localeCompare(a.name || '', 'vi');
            default:
                return 0;
        }
    });
    
    renderAccessories(filtered);
}

// Search accessories
function searchAccessories(event) {
    event.preventDefault();
    applyAccessoryFilters();
}

// Render accessories table
function renderAccessories(accessories) {
    const tbody = document.getElementById('accessoriesTableBody');
    
    if (!accessories || accessories.length === 0) {
        tbody.innerHTML = '<tr><td colspan="11" style="text-align: center; padding: 40px; color: var(--color-gray);">Không tìm thấy phụ kiện nào</td></tr>';
        return;
    }
    
    tbody.innerHTML = accessories.map(acc => {
        const createdDate = acc.createdDate ? new Date(acc.createdDate).toLocaleDateString('vi-VN') : 'N/A';
        return `
        <tr>
            <td><strong>#${acc.id}</strong></td>
            <td>
                ${acc.imageUrl ? `<img src="${acc.imageUrl}" class="product-thumbnail" alt="${acc.name}">` : '<div class="no-image">Chưa có ảnh</div>'}
            </td>
            <td><strong>${acc.name}</strong></td>
            <td>${acc.type || 'N/A'}</td>
            <td>${acc.brand || 'N/A'}</td>
            <td>${acc.material || 'N/A'}</td>
            <td>${acc.size || 'N/A'}</td>
            <td><strong>${formatPrice(acc.price)}</strong></td>
            <td>${acc.stock || 0}</td>
            <td><span class="badge ${acc.available ? 'badge-success' : 'badge-secondary'}">${acc.available ? 'Hiển thị' : 'Đã ẩn'}</span></td>
            <td>${createdDate}</td>
            <td>
                <div class="action-buttons">
                    <button class="btn-action btn-edit" onclick="editAccessory(${acc.id})">Sửa</button>
                    <button class="btn-action ${!acc.available ? 'btn-secondary' : ''}" onclick="toggleAccessoryVisibility(${acc.id}, ${acc.available})">${acc.available ? 'Ẩn' : 'Hiện'}</button>
                    <button class="btn-action btn-delete" onclick="deleteAccessory(${acc.id})">Xóa</button>
                </div>
            </td>
        </tr>
        `;
    }).join('');
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
function editAccessory(id) {
    const acc = allAccessoriesData.find(a => a.id === id);
    if (!acc) {
        showAlert('Không tìm thấy phụ kiện', 'error');
        return;
    }
    
    currentEditingAccessoryId = id;
    
    document.getElementById('accessoryModalTitle').textContent = 'SỬA PHỤ KIỆN';
    document.getElementById('accessoryId').value = acc.id;
    document.getElementById('accessoryName').value = acc.name || '';
    document.getElementById('accessoryType').value = acc.type || '';
    document.getElementById('accessoryBrand').value = acc.brand || '';
    document.getElementById('accessoryMaterial').value = acc.material || '';
    document.getElementById('accessorySize').value = acc.size || '';
    document.getElementById('accessoryPrice').value = acc.price || 0;
    document.getElementById('accessoryStock').value = acc.stock || 0;
    document.getElementById('accessoryImage').value = acc.imageUrl || '';
    document.getElementById('accessoryDescription').value = acc.description || '';
    
    document.getElementById('accessoryModal').classList.add('show');
}

// Save accessory
async function saveAccessory() {
    const name = document.getElementById('accessoryName').value.trim();
    const type = document.getElementById('accessoryType').value.trim();
    const brand = document.getElementById('accessoryBrand').value.trim();
    const material = document.getElementById('accessoryMaterial').value.trim();
    const size = document.getElementById('accessorySize').value.trim();
    const price = parseFloat(document.getElementById('accessoryPrice').value);
    const stock = parseInt(document.getElementById('accessoryStock').value);
    const image = document.getElementById('accessoryImage').value.trim();
    const description = document.getElementById('accessoryDescription').value.trim();
    
    if (!name || !type || !brand || !material || !size || isNaN(price) || isNaN(stock)) {
        showAlert('Vui lòng điền đầy đủ thông tin bắt buộc', 'error');
        return;
    }
    
    const accessoryData = {
        name,
        description,
        price,
        imageUrl: image || null,
        stock,
        loaiPhuKien: type,
        thuongHieu: brand,
        chatLieu: material,
        kichThuoc: size
    };
    
    try {
        let response;
        if (currentEditingAccessoryId) {
            // Update
            response = await fetch(`${API_BASE_URL}/admin/accessories/${currentEditingAccessoryId}/update`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(accessoryData)
            });
        } else {
            // Create
            response = await fetch(`${API_BASE_URL}/admin/accessories/add`, {
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
        const response = await fetch(`${API_BASE_URL}/admin/accessories/${id}/delete`, {
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
        
        // Add real-time search for motorbikes
        const motorbikeSearch = document.getElementById('searchMotorbikeName');
        if (motorbikeSearch) {
            motorbikeSearch.addEventListener('input', applyMotorbikeFilters);
        }
        
        // Add real-time search for accessories
        const accessorySearch = document.getElementById('searchAccessoryName');
        if (accessorySearch) {
            accessorySearch.addEventListener('input', applyAccessoryFilters);
        }
    }
});
