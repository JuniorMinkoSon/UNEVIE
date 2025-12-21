document.addEventListener('DOMContentLoaded', function () {
    // DOM Elements
    const sidebar = document.getElementById('adminSidebar');
    const sidebarToggle = document.getElementById('sidebarToggle');
    const sidebarBackdrop = document.getElementById('sidebarBackdrop');
    const userDropdownBtn = document.getElementById('userDropdownBtn');
    const userDropdown = document.querySelector('.user-dropdown');

    // Toggle Sidebar
    if (sidebarToggle) {
        sidebarToggle.addEventListener('click', function (e) {
            e.stopPropagation();

            // Check if Desktop (> 1024px)
            if (window.innerWidth > 1024) {
                document.body.classList.toggle('sidebar-collapsed');
            } else {
                // Mobile behavior
                sidebar.classList.toggle('active');
                if (sidebarBackdrop) {
                    sidebarBackdrop.classList.toggle('active');
                }
            }
        });
    }

    // Close Sidebar when clicking backdrop
    if (sidebarBackdrop) {
        sidebarBackdrop.addEventListener('click', function () {
            sidebar.classList.remove('active');
            sidebarBackdrop.classList.remove('active');
        });
    }

    // Toggle User Dropdown
    if (userDropdownBtn) {
        userDropdownBtn.addEventListener('click', function (e) {
            e.stopPropagation();
            if (userDropdown) {
                userDropdown.classList.toggle('active');
            }
        });
    }

    // Close Dropdown and Sidebar when clicking outside
    document.addEventListener('click', function (e) {
        // Close user dropdown
        if (userDropdown && userDropdown.classList.contains('active') && !userDropdown.contains(e.target)) {
            userDropdown.classList.remove('active');
        }

        // Close sidebar on mobile if clicked outside (and not on toggle)
        if (window.innerWidth <= 768) {
            if (sidebar && sidebar.classList.contains('active') &&
                !sidebar.contains(e.target) &&
                sidebarToggle && !sidebarToggle.contains(e.target)) {
                sidebar.classList.remove('active');
                if (sidebarBackdrop) {
                    sidebarBackdrop.classList.remove('active');
                }
            }
        }
    });

    // Active Menu Highlight
    const currentPath = window.location.pathname;
    const navItems = document.querySelectorAll('.nav-item');

    navItems.forEach(item => {
        const href = item.getAttribute('href');
        // Exact match or sub-path match (but not for dashboard to avoid matching everything)
        if (href === currentPath || (href !== '/admin/dashboard' && currentPath.startsWith(href))) {
            item.classList.add('active');
        }
    });

    // Smooth Animation for Stats Cards (if present)
    const cards = document.querySelectorAll('.stat-card');
    cards.forEach((card, index) => {
        card.style.opacity = '0';
        card.style.transform = 'translateY(20px)';
        setTimeout(() => {
            card.style.transition = 'all 0.5s ease';
            card.style.opacity = '1';
            card.style.transform = 'translateY(0)';
        }, 100 * index);
    });
});
