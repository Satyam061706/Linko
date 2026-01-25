
// delete email 
async function deleteEmail(id) {
    const result = await Swal.fire({
        title: "Do you want to delete the email?",
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Delete",
    });

    if (result.isConfirmed) {
        const url = `${baseURL}/user/contacts/email/delete/${id}`;

        const response = await fetch(url, {
            method: "DELETE",

        });

        if (response.ok) {
            Swal.fire("Deleted!", "Email deleted successfully", "success")
                .then(() => window.location.reload());
        } else {
            Swal.fire("Error!", "Failed to delete email", "error");
        }
    }
}

// open email
function openEmailModal(id) {
    // contactModal.show();
    window.location.replace(baseURL + "/user/contacts/view-email/" + id);

}













// email-view.js
document.addEventListener('DOMContentLoaded', function () {
    // Download attachment functionality
    const downloadButtons = document.querySelectorAll('.attachment-download-btn');
    downloadButtons.forEach(button => {
        button.addEventListener('click', function (e) {
            e.preventDefault();
            const attachmentName = this.closest('.attachment-item').querySelector('.attachment-name').textContent;

            // Show success message (you can replace this with actual download logic)
            Swal.fire({
                title: 'Download Started',
                text: `Downloading ${attachmentName}`,
                icon: 'success',
                timer: 1500,
                showConfirmButton: false
            });
        });
    });

    // Email action buttons functionality
    const actionButtons = document.querySelectorAll('.email-action-btn, .email-reply-btn, .email-reply-all-btn, .email-forward-btn');
    actionButtons.forEach(button => {
        button.addEventListener('click', function (e) {
            const action = this.title || this.textContent.trim();

            if (action.includes('Delete')) {
                Swal.fire({
                    title: 'Delete Email?',
                    text: 'Are you sure you want to delete this email?',
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonText: 'Yes, delete it',
                    cancelButtonText: 'Cancel'
                }).then((result) => {
                    if (result.isConfirmed) {
                        // Add your delete logic here
                        Swal.fire('Deleted!', 'Email has been deleted.', 'success');
                    }
                });
            } else if (action.includes('Print')) {
                window.print();
            } else {
                // For Reply, Forward, etc.
                Swal.fire({
                    title: `${action} Email`,
                    text: `${action} functionality would open here.`,
                    icon: 'info',
                    timer: 1500,
                    showConfirmButton: false
                });
            }
        });
    });
});