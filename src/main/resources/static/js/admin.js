// Admin Dashboard JavaScript

const API_BASE_URL = 'http://localhost:8080/api';

// Check admin authentication
function checkAdminAuth() {
    const role = sessionStorage.getItem('userRole');
    if (role !== 'ADMIN') {
        alert('Bạn không có quyền truy cập Admin Panel!');
        window.location.href = 'home.html';
        return false;
    }
    
    // Display admin name
    const userName = sessionStorage.getItem('userName');
    if (userName) {
        document.getElementById('userName').textContent = userName;
    }
    
    return true;
}

// Load dashboard statistics
async function loadDashboardStats() {
    try {
        // Get all users count
        const usersResponse = await fetch(`${API_BASE_URL}/admin/users/all`);
        if (usersResponse.ok) {
            const usersData = await usersResponse.json();
            document.getElementById('totalUsers').textContent = usersData.users ? usersData.users.length : 0;
        }

        // Get all motorbikes count
        const motorbikesResponse = await fetch(`${API_BASE_URL}/motorbikes`);
        if (motorbikesResponse.ok) {
            const motorbikesData = await motorbikesResponse.json();
            document.getElementById('totalMotorbikes').textContent = motorbikesData ? motorbikesData.length : 0;
        }

        // Get all accessories count
        const accessoriesResponse = await fetch(`${API_BASE_URL}/accessories`);
        if (accessoriesResponse.ok) {
            const accessoriesData = await accessoriesResponse.json();
            document.getElementById('totalAccessories').textContent = accessoriesData ? accessoriesData.length : 0;
        }

        // Get all orders count
        const ordersResponse = await fetch(`${API_BASE_URL}/admin/orders/all`);
        if (ordersResponse.ok) {
            const ordersData = await ordersResponse.json();
            document.getElementById('totalOrders').textContent = ordersData.orders ? ordersData.orders.length : 0;
        }

    } catch (error) {
        console.error('Error loading dashboard stats:', error);
        showAlert('Không thể tải thống kê', 'error');
    }
}

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    if (checkAdminAuth()) {
        loadDashboardStats();
    }
});
