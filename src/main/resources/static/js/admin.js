//   <!-- JavaScript for image preview -->

    document.addEventListener('DOMContentLoaded', function () {
      // Image Preview Functionality
      const imageInput = document.getElementById('linko-contact-image');
      const imagePreview = document.getElementById('linko-upload-image-preview');
      const imagePlaceholder = document.getElementById('linko-image-placeholder');
      const fileNameDisplay = document.getElementById('linko-file-name');

      if (imageInput && imagePreview && imagePlaceholder && fileNameDisplay) {
        // Handle file input change
        imageInput.addEventListener('change', function () {
          if (this.files && this.files[0]) {
            const fileName = this.files[0].name;
            fileNameDisplay.textContent = fileName;
            
            const reader = new FileReader();

            reader.onload = function (e) {
              imagePreview.src = e.target.result;
              imagePreview.style.display = 'block';
              imagePlaceholder.style.display = 'none';
            }

            reader.readAsDataURL(this.files[0]);
          } else {
            fileNameDisplay.textContent = 'No file chosen';
            imagePreview.src = '';
            imagePreview.style.display = 'none';
            imagePlaceholder.style.display = 'flex';
          }
        });
      }

      // Fix for icon positioning
      function fixIconPositions() {
        const inputWithIcons = document.querySelectorAll('.linko-input-with-icon');

        inputWithIcons.forEach(container => {
          const input = container.querySelector('input, textarea');
          const icon = container.querySelector('.linko-input-icon');

          if (input && icon) {
            // Ensure proper padding for all inputs
            if (!input.style.paddingLeft || input.style.paddingLeft === '') {
              input.style.paddingLeft = '2.5rem';
            }

            // Special handling for textarea
            if (input.tagName === 'TEXTAREA') {
              // Add specific class for textarea with icon
              input.classList.add('linko-textarea-with-icon');
              // Adjust icon position for textarea
              icon.style.top = '1rem';
              icon.style.transform = 'none';
            } else {
              // Center icon for regular inputs
              icon.style.top = '50%';
              icon.style.transform = 'translateY(-50%)';
            }
          }
        });
      }

      // Apply fixes on page load
      fixIconPositions();

      // Re-apply on window resize
      window.addEventListener('resize', fixIconPositions);

      // Re-apply on form reset
      const resetButton = document.querySelector('button[type="reset"]');
      if (resetButton) {
        resetButton.addEventListener('click', function () {
          setTimeout(fixIconPositions, 10);
          
          // Reset image preview
          if (imagePreview && imagePlaceholder && fileNameDisplay) {
            imagePreview.src = '';
            imagePreview.style.display = 'none';
            imagePlaceholder.style.display = 'flex';
            fileNameDisplay.textContent = 'No file chosen';
          }
        });
      }

      console.log('Add Contact page loaded successfully');
    });