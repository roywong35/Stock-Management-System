document.addEventListener('DOMContentLoaded', (event) => {
    // Extract URL parameters
    const urlParams = new URLSearchParams(window.location.search);
    const stockId = urlParams.get('id');
    const stockName = urlParams.get('name');
    const unit = urlParams.get('unit');
    const stockNum = urlParams.get('num');

    // Prefill the readonly fields
    document.getElementById('stockIdDisplay').value = stockId;
    document.getElementById('name').value = stockName;
    document.getElementById('unit').value = unit;
    document.getElementById('num').value = stockNum;
});


function navigateToAddForm(event) {
    event.preventDefault(); // Prevent default form submission

    // Get the form element
    const form = document.getElementById('stockIoForm');

    // Check if 入出庫数量 (ioNum) is filled out and within the specified range
    const ioNumInput = form.querySelector('#ioNum');
    const ioNum = ioNumInput.value.trim();

    // Validate if ioNum is within the specified range and format
    const min = parseFloat(ioNumInput.getAttribute('min'));
    const max = parseFloat(ioNumInput.getAttribute('max'));
    const numericValue = parseFloat(ioNum);

    // Check if ioNum is empty or not a valid number
    if (isNaN(numericValue)) {
        alert('入出庫数量を数値で入力してください。');
        return; // Exit function without submitting if ioNum is not a number
    }

    // Check if ioNum is within the specified range
    if (numericValue < min || numericValue > max) {
        alert(`入出庫数量は、${min}から${max}の間で指定してください。`);
        return; // Exit function without submitting if ioNum is out of range
    }

    // Check if ioNum has more than one decimal place
    if (!Number.isInteger(numericValue) && !/^(\d+(\.\d{1})?)$/.test(ioNum)) {
        alert('入出庫数量は整数または一桁の小数で入力してください。');
        return; // Exit function without submitting if ioNum has more than one decimal place
    }

    // Check if remarks (備考) is within the maxlength limit
    const remarksInput = form.querySelector('#remarks');
    const remarks = remarksInput.value.trim();

    if (remarks.length > 200) {
        alert('備考は200文字以内で入力してください。');
        return; // Exit function without submitting if remarks exceeds maxlength
    }

    // Create a FormData object to include the form data
    const formData = new FormData(form);

    // Submit the form data using fetch
    fetch(form.action, {
        method: 'POST',
        body: formData,
    }).then(response => {
        if (response.ok) {
            // Extract URL parameters
            const urlParams = new URLSearchParams(window.location.search);
            const stockId = urlParams.get('id');
            const stockName = urlParams.get('name');
            const unit = urlParams.get('unit');
            const stockNum = urlParams.get('num');

            // Construct the URL for redirection
            const url = `/stockio?id=${encodeURIComponent(stockId)}&name=${encodeURIComponent(stockName)}&unit=${encodeURIComponent(unit)}&num=${encodeURIComponent(stockNum)}`;
            window.location.href = url;
        } else {
            console.error('Form submission failed:', response.statusText);
        }
    }).catch(error => {
        console.error('Form submission error:', error);
    });
}


document.getElementById('closeButton').addEventListener('click', function() {
    // Extract URL parameters
    const urlParams = new URLSearchParams(window.location.search);
    const stockId = urlParams.get('id');
    const stockName = urlParams.get('name');
    const unit = urlParams.get('unit');
    const stockNum = urlParams.get('num');

    // Construct the URL for redirection
    const url = `/stockio?id=${encodeURIComponent(stockId)}&name=${encodeURIComponent(stockName)}&unit=${encodeURIComponent(unit)}&num=${encodeURIComponent(stockNum)}`;
    window.location.href = url;
});