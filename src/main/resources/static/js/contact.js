console.log("Contacts.js");
const baseURL = "http://localhost:8080";
// const baseURL = "https://www.linko.site";
const viewContactModal = document.getElementById("view_contact_modal");

// options with default values
// const options = {
//     placement: "bottom-right",
//     backdrop: "dynamic",
//     backdropClasses: "bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40",
//     closable: true,
//     onHide: () => {
//         console.log("modal is hidden");
//     },
//     onShow: () => {
//         setTimeout(() => {
//             contactModal.classList.add("scale-100");
//         }, 50);
//     },
//     onToggle: () => {
//         console.log("modal has been toggled");
//     },
// };

// // instance options object
// const instanceOptions = {
//     id: "view_contact_mdoal",
//     override: true,
// };

// const contactModal = new Modal(viewContactModal, options, instanceOptions);

// open contact
function openContactModal(id) {
    // contactModal.show();
    window.location.replace(baseURL + "/user/contacts/view-contact/" + id);

}

// call contact
function callContact(phoneNumber) {
    window.location.href = `tel:${phoneNumber}`;
}


// delete contact

async function deleteContact(id) {
    const result = await Swal.fire({
        title: "Do you want to delete the contact?",
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Delete",
    });

    if (result.isConfirmed) {
        const url = `${baseURL}/user/contacts/delete/${id}`;

        const response = await fetch(url, {
            method: "DELETE",

        });

        if (response.ok) {
            Swal.fire("Deleted!", "Contact deleted successfully", "success")
                .then(() => window.location.reload());
        } else {
            Swal.fire("Error!", "Failed to delete contact", "error");
        }
    }
}
















function exportData() {
    TableToExcel.convert(document.querySelector(".contacts-table"), {
        name: "contact.xlsx",
        sheet: {
            name: "Sheet 1",
        },
    });
}

// Adjust main content padding based on sidebar
document.addEventListener('DOMContentLoaded', function () {
    function adjustLayout() {
        const mainContent = document.querySelector('.main-content');
        const sidebar = document.querySelector('.sidebar');

        if (window.innerWidth >= 768) {
            if (sidebar) {
                mainContent.style.paddingLeft = '250px';
            } else {
                mainContent.style.paddingLeft = '0';
            }
        } else {
            mainContent.style.paddingLeft = '0';
        }
    }

    // Initial adjustment
    adjustLayout();

    // Adjust on window resize
    window.addEventListener('resize', adjustLayout);

    // Adjust when sidebar is toggled
    const sidebarToggle = document.getElementById('sidebar-toggle');
    if (sidebarToggle) {
        sidebarToggle.addEventListener('click', function () {
            setTimeout(adjustLayout, 300);
        });
    }
});