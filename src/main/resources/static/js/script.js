console.log("Script loaded: script.js");

// ========== THEME FUNCTIONALITY ==========

// Function to update theme icons and logo
function updateThemeElements() {
    const isDark = document.documentElement.classList.contains('dark');
    const sunIcon = document.getElementById('theme-toggle-sun-icon');
    const moonIcon = document.getElementById('theme-toggle-moon-icon');
    const logo = document.getElementById('navbar-logo');

    // Update theme toggle icons
    if (sunIcon && moonIcon) {
        if (isDark) {
            // Dark mode: Show SUN icon (to switch to light)
            sunIcon.classList.add('hidden');
            moonIcon.classList.remove('hidden');
        } else {
            // Light mode: Show MOON icon (to switch to dark)
            sunIcon.classList.remove('hidden');
            moonIcon.classList.add('hidden');
        }
    }

    // Update logo
    if (logo) {
        if (isDark) {
            // Dark mode: Use logo3_png.png
            logo.src = '/images/logo3_png.png';
        } else {
            // Light mode: Use logo_png.png
            logo.src = '/images/logo_png.png';
        }
    }
}

// Theme toggle function
function toggleTheme() {
    const html = document.documentElement;
    
    if (html.classList.contains('dark')) {
        html.classList.remove('dark');
        localStorage.setItem('theme', 'light');
    } else {
        html.classList.add('dark');
        localStorage.setItem('theme', 'dark');
    }

    // Update icons and logo after theme change
    updateThemeElements();
}

// ========== SIDEBAR FUNCTIONALITY ==========

// Sidebar functionality
function initSidebar() {
    const sidebar = document.getElementById('sidebar');
    const sidebarToggle = document.getElementById('sidebar-toggle');
    const sidebarClose = document.getElementById('sidebar-close');
    const sidebarOverlay = document.getElementById('sidebar-overlay');

    // Toggle sidebar on mobile
    if (sidebarToggle) {
        sidebarToggle.addEventListener('click', function() {
            sidebar.style.transform = 'translateX(0)';
            sidebarOverlay.classList.add('active');
            document.body.style.overflow = 'hidden';
        });
    }

    // Close sidebar
    if (sidebarClose) {
        sidebarClose.addEventListener('click', function() {
            sidebar.style.transform = 'translateX(-100%)';
            sidebarOverlay.classList.remove('active');
            document.body.style.overflow = '';
        });
    }

    // Close sidebar when clicking on overlay
    if (sidebarOverlay) {
        sidebarOverlay.addEventListener('click', function() {
            sidebar.style.transform = 'translateX(-100%)';
            sidebarOverlay.classList.remove('active');
            document.body.style.overflow = '';
        });
    }
}

// ========== NAVBAR MOBILE MENU FUNCTIONALITY ==========

// Navbar mobile menu functionality
function initNavbarMobileMenu() {
    const mobileMenuBtn = document.querySelector('.mobile-menu-btn');
    
    if (mobileMenuBtn) {
        console.log('Mobile menu button found, adding click event...');
        
        mobileMenuBtn.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            
            // Find the nav menu container
            const navMenuContainer = document.querySelector('.nav-menu-container');
            
            if (navMenuContainer) {
                // Toggle the active class
                const isActive = navMenuContainer.classList.contains('active');
                navMenuContainer.classList.toggle('active');
                const isNowActive = navMenuContainer.classList.contains('active');
                
                // Toggle body class to prevent scrolling
                if (isNowActive) {
                    document.body.classList.add('mobile-menu-open');
                } else {
                    document.body.classList.remove('mobile-menu-open');
                }
                
                // Update aria attributes
                mobileMenuBtn.setAttribute('aria-expanded', isNowActive);
                
                // Toggle icon between hamburger and X
                const menuIcon = mobileMenuBtn.querySelector('.menu-icon');
                if (menuIcon) {
                    if (isNowActive) {
                        // Change to X icon
                        menuIcon.setAttribute('viewBox', '0 0 24 24');
                        menuIcon.innerHTML = `
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                        `;
                    } else {
                        // Change back to hamburger icon
                        menuIcon.setAttribute('viewBox', '0 0 24 24');
                        menuIcon.innerHTML = `
                            <path stroke-linecap="round" stroke-width="2" d="M5 7h14M5 12h14M5 17h14" />
                        `;
                    }
                }
            }
        });

        // Close mobile menu when clicking on a nav link
        const navLinks = document.querySelectorAll('.nav-link');
        navLinks.forEach(link => {
            link.addEventListener('click', function() {
                const navMenuContainer = document.querySelector('.nav-menu-container');
                const mobileMenuBtn = document.querySelector('.mobile-menu-btn');
                
                if (navMenuContainer && navMenuContainer.classList.contains('active')) {
                    navMenuContainer.classList.remove('active');
                    document.body.classList.remove('mobile-menu-open');
                    mobileMenuBtn.setAttribute('aria-expanded', 'false');
                    
                    // Change icon back to hamburger
                    const menuIcon = mobileMenuBtn.querySelector('.menu-icon');
                    if (menuIcon) {
                        menuIcon.setAttribute('viewBox', '0 0 24 24');
                        menuIcon.innerHTML = `
                            <path stroke-linecap="round" stroke-width="2" d="M5 7h14M5 12h14M5 17h14" />
                        `;
                    }
                }
            });
        });

        // Close mobile menu when clicking outside
        document.addEventListener('click', function(event) {
            const navMenuContainer = document.querySelector('.nav-menu-container');
            const mobileMenuBtn = document.querySelector('.mobile-menu-btn');
            
            if (navMenuContainer && navMenuContainer.classList.contains('active') && 
                !navMenuContainer.contains(event.target) && 
                !mobileMenuBtn.contains(event.target)) {
                
                navMenuContainer.classList.remove('active');
                document.body.classList.remove('mobile-menu-open');
                mobileMenuBtn.setAttribute('aria-expanded', 'false');
                
                // Change icon back to hamburger
                const menuIcon = mobileMenuBtn.querySelector('.menu-icon');
                if (menuIcon) {
                    menuIcon.setAttribute('viewBox', '0 0 24 24');
                    menuIcon.innerHTML = `
                        <path stroke-linecap="round" stroke-width="2" d="M5 7h14M5 12h14M5 17h14" />
                    `;
                }
            }
        });

        // Close mobile menu on window resize (if resized to desktop)
        window.addEventListener('resize', function() {
            const navMenuContainer = document.querySelector('.nav-menu-container');
            const mobileMenuBtn = document.querySelector('.mobile-menu-btn');
            
            if (window.innerWidth > 768 && navMenuContainer && navMenuContainer.classList.contains('active')) {
                navMenuContainer.classList.remove('active');
                document.body.classList.remove('mobile-menu-open');
                mobileMenuBtn.setAttribute('aria-expanded', 'false');
                
                // Change icon back to hamburger
                const menuIcon = mobileMenuBtn.querySelector('.menu-icon');
                if (menuIcon) {
                    menuIcon.setAttribute('viewBox', '0 0 24 24');
                    menuIcon.innerHTML = `
                        <path stroke-linecap="round" stroke-width="2" d="M5 7h14M5 12h14M5 17h14" />
                    `;
                }
            }
        });
    } else {
        console.error('Mobile menu button not found!');
    }
}

// ========== PROFILE DROPDOWN FUNCTIONALITY ==========

function initProfileDropdown() {
    const profileBtn = document.getElementById('profile-dropdown-btn');
    const profileMenu = document.getElementById('profile-dropdown-menu');
    
    if (profileBtn && profileMenu) {
        console.log('Profile dropdown button found, adding click event...');
        
        // Toggle dropdown on button click
        profileBtn.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            
            const isActive = profileMenu.classList.contains('active');
            
            // Close other open dropdowns (mobile menu, etc.)
            document.querySelectorAll('.nav-menu-container.active').forEach(menu => {
                menu.classList.remove('active');
            });
            
            // Close mobile menu if open
            const mobileMenuBtn = document.querySelector('.mobile-menu-btn');
            const navMenuContainer = document.querySelector('.nav-menu-container');
            if (navMenuContainer && navMenuContainer.classList.contains('active')) {
                navMenuContainer.classList.remove('active');
                document.body.classList.remove('mobile-menu-open');
                if (mobileMenuBtn) {
                    mobileMenuBtn.setAttribute('aria-expanded', 'false');
                    // Change icon back to hamburger
                    const menuIcon = mobileMenuBtn.querySelector('.menu-icon');
                    if (menuIcon) {
                        menuIcon.setAttribute('viewBox', '0 0 24 24');
                        menuIcon.innerHTML = `
                            <path stroke-linecap="round" stroke-width="2" d="M5 7h14M5 12h14M5 17h14" />
                        `;
                    }
                }
            }
            
            // Toggle current dropdown
            profileMenu.classList.toggle('active');
            
            // Update aria attribute
            profileBtn.setAttribute('aria-expanded', !isActive);
            
            // Prevent body scroll when dropdown is open on mobile
            if (window.innerWidth <= 768 && !isActive) {
                document.body.classList.add('mobile-menu-open');
            } else if (window.innerWidth <= 768) {
                document.body.classList.remove('mobile-menu-open');
            }
        });
        
        // Close dropdown when clicking outside
        document.addEventListener('click', function(event) {
            if (profileMenu.classList.contains('active') && 
                !profileMenu.contains(event.target) && 
                !profileBtn.contains(event.target)) {
                
                profileMenu.classList.remove('active');
                profileBtn.setAttribute('aria-expanded', 'false');
                
                // Remove body scroll lock
                if (window.innerWidth <= 768) {
                    document.body.classList.remove('mobile-menu-open');
                }
            }
        });
        
        // Close dropdown when pressing Escape key
        document.addEventListener('keydown', function(event) {
            if (event.key === 'Escape' && profileMenu.classList.contains('active')) {
                profileMenu.classList.remove('active');
                profileBtn.setAttribute('aria-expanded', 'false');
                
                // Remove body scroll lock
                if (window.innerWidth <= 768) {
                    document.body.classList.remove('mobile-menu-open');
                }
            }
        });
        
        // Close dropdown when clicking on a dropdown item
        const dropdownItems = profileMenu.querySelectorAll('.dropdown-item');
        dropdownItems.forEach(item => {
            item.addEventListener('click', function() {
                profileMenu.classList.remove('active');
                profileBtn.setAttribute('aria-expanded', 'false');
                
                // Remove body scroll lock
                if (window.innerWidth <= 768) {
                    document.body.classList.remove('mobile-menu-open');
                }
            });
        });
        
        // Close dropdown on window resize (for mobile/desktop transitions)
        window.addEventListener('resize', function() {
            if (window.innerWidth > 768) {
                // Keep open on desktop if already open
                // Remove body scroll lock
                document.body.classList.remove('mobile-menu-open');
            } else {
                // On mobile, close dropdown if open
                if (profileMenu.classList.contains('active')) {
                    // Keep it open on mobile if user opened it
                    document.body.classList.add('mobile-menu-open');
                }
            }
        });
    } else {
        console.log('Profile dropdown elements not found, skipping initialization.');
    }
}

// ========== INITIALIZATION ==========

// Initialize when DOM is loaded
document.addEventListener('DOMContentLoaded', function () {
    console.log('DOM loaded - initializing components...');

    // Check for saved theme preference
    const savedTheme = localStorage.getItem('theme');
    if (savedTheme === 'dark' || (!savedTheme && window.matchMedia('(prefers-color-scheme: dark)').matches)) {
        document.documentElement.classList.add('dark');
    } else {
        document.documentElement.classList.remove('dark');
    }

    // Set initial icons and logo
    updateThemeElements();
    
    // Initialize sidebar
    initSidebar();
    
    // Initialize navbar mobile menu
    initNavbarMobileMenu();
    
    // Initialize profile dropdown
    initProfileDropdown();

    // Theme button
    const themeButton = document.getElementById('theme-toggle');
    if (themeButton) {
        themeButton.addEventListener('click', toggleTheme);
        console.log('Theme button event listener attached');
    } else {
        console.error('Theme button not found!');
    }
    
    console.log('All components initialized successfully.');
});

// Also update when page fully loads
window.addEventListener('load', function() {
    console.log('Page fully loaded - final theme check');
    updateThemeElements();
});