function handleSaveResponse(response) {
    if (response.startsWith('重複エラー')) {
        alert(response);
    } else {
        // Handle other success scenarios if needed
        window.location.href = '/stock'; // Redirect to stock page on success
    }
}

function submitForm(event) {
    event.preventDefault(); // Prevent the default form submission

    var form = document.getElementById('saveForm');
    if (form) {
        // Validate 在庫名称 (stockName)
        var stockNameInput = form.querySelector('#stockName');
        var stockName = stockNameInput.value.trim();

        if (stockName === '') {
            alert('在庫名称を入力してください。');
            return;
        }

        if (stockName.length > 50) {
            alert('在庫名称は50文字以内で入力してください。');
            return;
        }

        // Validate 備考 (remarks)
        var remarksInput = form.querySelector('#remarks');
        var remarks = remarksInput.value.trim();

        if (remarks.length > 200) {
            alert('備考は200文字以内で入力してください。');
            return;
        }

        var formData = new FormData(form);

        fetch('/saveStock', {
            method: 'POST',
            body: formData
        })
            .then(response => response.text())
            .then(data => handleSaveResponse(data))
            .catch(error => console.error('Error:', error));
    } else {
        console.error('Form element not found');
    }
}