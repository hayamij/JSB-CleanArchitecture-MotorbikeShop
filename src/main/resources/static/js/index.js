// Landing Page JavaScript - Motorbike Shop

// Auto redirect if already logged in
window.onload = function() {
    const userId = sessionStorage.getItem('userId');
    if (userId) {
        window.location.href = 'home.html';
    }
};
