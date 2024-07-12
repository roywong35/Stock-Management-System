function deleteSelected() {
    var selectedIds = [];
    var checkboxes = document.querySelectorAll('input[name="selectedIds"]:checked');

    if (checkboxes.length === 0) {
        alert("少なくとも1つの在庫を選択してください。");
        return;
    }

    checkboxes.forEach(function(checkbox) {
        selectedIds.push(checkbox.value);
    });

    selectedIds.forEach(function(id) {
        fetch('/deleteStock/' + id, {
            method: 'DELETE',
        })
            .then(response => {
                if (response.ok) {
                    console.log('在庫が正常に削除されました:', id);
                    var rowToRemove = document.getElementById('stockRow' + id);
                    if (rowToRemove) {
                        rowToRemove.remove();
                    } else {
                        console.warn('行要素が見つかりません:', 'stockRow' + id);
                    }
                } else {
                    response.text().then(text => {
                        console.error('在庫の削除に失敗しました:', id, text);
                        // Show error in an alert window
                        alert('在庫の削除に失敗しました: ' + id + '。' + text);
                    });
                }
            })
            .catch(error => {
                console.error('在庫を削除中にエラーが発生しました:', error);
                // Show error in an alert window
                alert('在庫を削除中にエラーが発生しました: ' + error);
            });
    });
}

function search() {
    var searchStockName = document.getElementById('searchStockName').value.trim().toLowerCase();
    var searchStockId = document.getElementById('searchStockId').value.trim().toLowerCase();
    var searchStatus = document.getElementById('searchStatus').value;

    var rows = document.querySelectorAll('tbody tr');

    rows.forEach(function (row) {
        var stockName = row.querySelector('td:nth-child(3)').innerText.trim().toLowerCase();
        var stockId = row.querySelector('td:nth-child(2)').innerText.trim().toLowerCase();
        var stockNum = parseInt(row.querySelector('td:nth-child(5)').innerText.trim());

        var nameMatch = stockName.includes(searchStockName);
        var idMatch = stockId.includes(searchStockId);
        var statusMatch = true;

        if (searchStatus) {
            if (searchStatus === "在庫あり") {
                statusMatch = stockNum > 0;
            } else if (searchStatus === "在庫なし") {
                statusMatch = stockNum === 0;
            }
        }

        if (nameMatch && idMatch && statusMatch) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });
}