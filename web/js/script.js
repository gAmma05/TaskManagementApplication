document.addEventListener('DOMContentLoaded', function () {
    // Editable cells logic for "My Info" tab
    const editableCells = document.querySelectorAll('.editable-cell');
    const updateBtn = document.getElementById('updateUserBtn');
    const fullNameInput = document.getElementById('fullNameInput');
    const emailInput = document.getElementById('emailInput');
    const phoneInput = document.getElementById('phoneInput');

    if (editableCells.length && updateBtn && fullNameInput && emailInput) {
        editableCells.forEach(cell => {
            cell.addEventListener('input', function () {
                updateBtn.classList.add('glow');
                const field = this.getAttribute('data-field');
                if (field === 'fullName') {
                    fullNameInput.value = this.textContent.trim();
                } else if (field === 'email') {
                    emailInput.value = this.textContent.trim();
                } else if (field === 'phone') { // Added phone condition
                    phoneInput.value = this.textContent.trim();
                }
            });
        });

        // Initialize hidden inputs with current values
        const fullNameCell = document.querySelector('.editable-cell[data-field="fullName"]');
        const emailCell = document.querySelector('.editable-cell[data-field="email"]');
        const phoneCell = document.querySelector('.editable-cell[data-field="phone"]');
        if (fullNameCell)
            fullNameInput.value = fullNameCell.textContent.trim();
        if (emailCell)
            emailInput.value = emailCell.textContent.trim();
        if (phoneCell)
            phoneInput.value = phoneCell.textContent.trim();
    } else {
        console.warn("Editable elements not found. 'My Info' tab may not be rendered.");
    }

    // Debug modal interactions
    const modals = document.querySelectorAll('.modal');
    if (modals.length) {
        modals.forEach(modal => {
            modal.addEventListener('show.bs.modal', function () {
                console.log(`Modal ${modal.id} is being shown`);
            });
            modal.addEventListener('hidden.bs.modal', function () {
                console.log(`Modal ${modal.id} is hidden`);
            });
        });
    } else {
        console.warn("No modals found on the page.");
    }

    // Debug modal trigger buttons
    const modalTriggers = document.querySelectorAll('[data-bs-toggle="modal"]');
    modalTriggers.forEach(button => {
        button.addEventListener('click', () => {
            const target = button.getAttribute('data-bs-target');
            console.log(`Button clicked to open modal: ${target}`);
        });
    });

    // Verify Bootstrap is loaded
    if (typeof bootstrap === 'undefined') {
        console.error("Bootstrap JS is not loaded. Modals and tabs will not work.");
    } else {
        console.log("Bootstrap JS is loaded successfully.");
    }
});