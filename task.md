# Linko UI/UX Redesign

## Planning
- [/] Explore existing project structure and understand codebase
- [/] Write implementation plan for UI redesign
- [ ] Get user approval on implementation plan

## Execution — Foundation
- [x] Update [tailwind.config.js](file:///F:/code/NewProjects/spring-boot/Linko/tailwind.config.js) with design tokens (colors, fonts, spacing, shadows)
- [x] Update [base.html](file:///F:/code/NewProjects/spring-boot/Linko/src/main/resources/templates/base.html) with Inter font, new CDN links, and improved structure
- [ ] Create new theme CSS files ([light.css](file:///F:/code/NewProjects/spring-boot/Linko/src/main/resources/static/css/theme/light.css), [dark.css](file:///F:/code/NewProjects/spring-boot/Linko/src/main/resources/static/css/theme/dark.css)) with design system variables
- [ ] Create [base.css](file:///F:/code/NewProjects/spring-boot/Linko/src/main/resources/static/css/base.css) with global resets and utilities

## Execution — Layout Components
- [x] Redesign [sidebar.html](file:///F:/code/NewProjects/spring-boot/Linko/src/main/resources/templates/user/sidebar.html) — modern collapsible sidebar with icons, active states, sections
- [x] Redesign [user_navbar.html](file:///F:/code/NewProjects/spring-boot/Linko/src/main/resources/templates/user/user_navbar.html) — top bar with global search, add contact, notifications, profile dropdown
- [ ] Update [main.css](file:///F:/code/NewProjects/spring-boot/Linko/src/main/resources/static/css/main.css) with new component imports

## Execution — Dashboard Page
- [x] Redesign [dashboard.html](file:///F:/code/NewProjects/spring-boot/Linko/src/main/resources/templates/user/dashboard.html) — stats cards, recent contacts, activity feed, quick actions

## Execution — Contacts Page  
- [/] Redesign [contacts.html](file:///F:/code/NewProjects/spring-boot/Linko/src/main/resources/templates/user/contacts.html) — modern card/table toggle view, search, filters, pagination
- [x] Fix Thymeleaf `onerror` safe pattern in [contacts.html](file:///F:/code/NewProjects/spring-boot/Linko/src/main/resources/templates/user/contacts.html)
- [ ] Redesign [contact_modals.html](file:///F:/code/NewProjects/spring-boot/Linko/src/main/resources/templates/user/contact_modals.html) — glassmorphism modal for quick view

## Execution — Contact Detail & Forms
- [x] Redesign [view_contact.html](file:///F:/code/NewProjects/spring-boot/Linko/src/main/resources/templates/user/view_contact.html) — modern detail page with tabs, quick actions
- [x] Redesign [add_contact.html](file:///F:/code/NewProjects/spring-boot/Linko/src/main/resources/templates/user/add_contact.html) — clean form with proper validation states
- [x] Redesign [update_contact_view.html](file:///F:/code/NewProjects/spring-boot/Linko/src/main/resources/templates/user/update_contact_view.html) — consistent with add contact form

## Execution — Profile & Settings
- [x] Redesign [profile.html](file:///F:/code/NewProjects/spring-boot/Linko/src/main/resources/templates/user/profile.html) — modern profile card with edit capability
- [x] Redesign [update_profile_view.html](file:///F:/code/NewProjects/spring-boot/Linko/src/main/resources/templates/user/update_profile_view.html) — consistent profile edit form

## Execution — Email Pages
- [ ] Redesign [send_email.html](file:///F:/code/NewProjects/spring-boot/Linko/src/main/resources/templates/user/send_email.html) / [send_email_view.html](file:///F:/code/NewProjects/spring-boot/Linko/src/main/resources/templates/user/send_email_view.html) — clean email compose
- [ ] Redesign [outbox_email.html](file:///F:/code/NewProjects/spring-boot/Linko/src/main/resources/templates/user/outbox_email.html) — email list with status badges
- [ ] Redesign [view_email.html](file:///F:/code/NewProjects/spring-boot/Linko/src/main/resources/templates/user/view_email.html) — email detail view

## Execution — Other Pages
- [ ] Redesign [direct.html](file:///F:/code/NewProjects/spring-boot/Linko/src/main/resources/templates/user/direct.html) / [direct_search.html](file:///F:/code/NewProjects/spring-boot/Linko/src/main/resources/templates/user/direct_search.html) — direct contacts interface
- [ ] Redesign [search.html](file:///F:/code/NewProjects/spring-boot/Linko/src/main/resources/templates/user/search.html) — enhanced search results page

## Execution — JavaScript
- [ ] Update [script.js](file:///F:/code/NewProjects/spring-boot/Linko/src/main/resources/static/js/script.js) — new sidebar, theme, interactions, keyboard shortcuts
- [ ] Update [contact.js](file:///F:/code/NewProjects/spring-boot/Linko/src/main/resources/static/js/contact.js) — modernized contact interactions
- [ ] Update [admin.js](file:///F:/code/NewProjects/spring-boot/Linko/src/main/resources/static/js/admin.js) — admin functionality

## Verification
- [ ] Visual review via browser — all pages in light and dark mode
- [ ] Mobile responsive testing
- [ ] User sign-off
