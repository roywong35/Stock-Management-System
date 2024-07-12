function getUrlParameter(name) {
    name = name.replace(/\[/, '\\[').replace(/\]/, '\\]');
    var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
    var results = regex.exec(location.search);
    return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
}

function prefillSearchInputs() {
    document.getElementById('searchStockId').value = getUrlParameter('id');
    document.getElementById('searchStockName').value = getUrlParameter('name');
    document.getElementById('searchUnit').value = getUrlParameter('unit');
    document.getElementById('searchStockNum').value = getUrlParameter('num');
}

window.onload = function () {
    prefillSearchInputs();
    search(); // Trigger search once inputs are prefilled
};

function clearSearchInputs() {
    document.getElementById('searchStockId').value = '';
    document.getElementById('searchStockName').value = '';
    document.getElementById('searchUnit').value = '';
    // No need to clear searchStockNum as per your requirement
    search(); // Trigger search after clearing inputs
}

function search() {
    var inputStockId = document.getElementById("searchStockId").value.toLowerCase(); // Get 在庫ID input value
    var inputStockName = document.getElementById("searchStockName").value.toLowerCase(); // Get 在庫名称 input value
    var inputUnit = document.getElementById("searchUnit").value.toLowerCase(); // Get 単位 input value
    var selectedIoType = document.getElementById("searchIoType").value; // Get selected 入出庫タイプ value

    var table = document.querySelector(".table tbody");
    var tr = table.getElementsByTagName("tr");

    var stockNumSum = {}; // Object to store summed 入出庫数量 for each 在庫ID

    for (var i = 0; i < tr.length; i++) {
        var tdStockId = tr[i].getElementsByTagName("td")[2]; // Index 2 corresponds to 在庫ID column
        var tdStockName = tr[i].getElementsByTagName("td")[3]; // Index 3 corresponds to 在庫名称 column
        var tdUnit = tr[i].getElementsByTagName("td")[4]; // Index 4 corresponds to 単位 column
        var tdIoType = tr[i].getElementsByTagName("td")[5]; // Index 5 corresponds to 入出庫タイプ column
        var tdIoNum = tr[i].getElementsByTagName("td")[6]; // Index 6 corresponds to 入出庫数量 column

        var matchStockId = tdStockId.textContent || tdStockId.innerText;
        var matchStockName = tdStockName.textContent || tdStockName.innerText;
        var matchUnit = tdUnit.textContent || tdUnit.innerText;
        var matchIoType = tdIoType.textContent || tdIoType.innerText;

        var ioNum = parseFloat(tdIoNum.textContent.trim()); // Get 入出庫数量 and parse to float

        // Initialize sum for this 在庫ID if not already initialized
        if (!stockNumSum[matchStockId]) {
            stockNumSum[matchStockId] = 0;
        }

        // Update 入出庫数量 sum based on 入出庫タイプ
        switch (matchIoType) {
            case "購買発注入庫":
            case "仕入返品":
            case "出庫取消":
                stockNumSum[matchStockId] += ioNum;
                break;
            case "購買発注入庫取消":
            case "仕入返品取消":
            case "出庫":
                stockNumSum[matchStockId] -= ioNum;
                break;
            default:
                break;
        }
    }

    // Display the calculated 在庫数量 sum for the matching 在庫ID in the search bar
    var searchStockId = document.getElementById("searchStockId").value.trim();
    document.getElementById("searchStockNum").value = stockNumSum[searchStockId] || 0;

    // Filter and display table rows based on search criteria
    for (var i = 0; i < tr.length; i++) {
        var tdStockId = tr[i].getElementsByTagName("td")[2]; // Index 2 corresponds to 在庫ID column
        var tdStockName = tr[i].getElementsByTagName("td")[3]; // Index 3 corresponds to 在庫名称 column
        var tdUnit = tr[i].getElementsByTagName("td")[4]; // Index 4 corresponds to 単位 column
        var tdIoType = tr[i].getElementsByTagName("td")[5]; // Index 5 corresponds to 入出庫タイプ column

        var matchStockId = tdStockId.textContent || tdStockId.innerText;
        var matchStockName = tdStockName.textContent || tdStockName.innerText;
        var matchUnit = tdUnit.textContent || tdUnit.innerText;
        var matchIoType = tdIoType.textContent || tdIoType.innerText;

        var showRow = true;

        // Check if 在庫ID input matches
        if (inputStockId && matchStockId.toLowerCase().indexOf(inputStockId) === -1) {
            showRow = false;
        }

        // Check if 在庫名称 input matches
        if (inputStockName && matchStockName.toLowerCase().indexOf(inputStockName) === -1) {
            showRow = false;
        }

        // Check if 単位 input matches
        if (inputUnit && matchUnit.toLowerCase().indexOf(inputUnit) === -1) {
            showRow = false;
        }

        // Check if 入出庫タイプ is selected and matches
        if (selectedIoType && selectedIoType !== "" && matchIoType !== selectedIoType) {
            showRow = false;
        }

        // Show or hide row based on search criteria
        if (showRow) {
            tr[i].style.display = "";
        } else {
            tr[i].style.display = "none";
        }
    }
}


function deleteSelected() {
    var selectedIds = [];
    var checkboxes = document.querySelectorAll('input[name="selectedIds"]:checked');

    if (checkboxes.length === 0) {
        alert("少なくとも1つの在庫を選択してください。");
        return;
    }

    checkboxes.forEach(function (checkbox) {
        selectedIds.push(checkbox.value);
    });

    selectedIds.forEach(function (id) {
        fetch('/deleteStockIo/' + id, {
            method: 'DELETE', // Keep as DELETE method
        })
            .then(response => {
                if (response.ok) {
                    console.log('Entry marked as deleted successfully:', id);
                    document.getElementById('stockioRow' + id).remove();
                    search(); // Trigger search again after successful deletion
                } else {
                    response.text().then(text => {
                        console.error('Failed to mark entry as deleted:', id, text);
                    });
                }
            })
            .catch(error => {
                console.error('Error while marking entry as deleted:', error);
            });
    });
}

function navigateToAddForm() {
    const stockId = document.getElementById('searchStockId').value.trim();
    const stockName = document.getElementById('searchStockName').value.trim();
    const unit = document.getElementById('searchUnit').value.trim();
    const stockNum = document.getElementById('searchStockNum').value.trim();

    // Basic validation for 在庫ID
    if (stockId === '') {
        alert('在庫IDを入力してください。');
        return;
    }

    // Check if stock ID exists
    const url = '/checkStockId/' + stockId;
    fetch(url)
        .then(response => {
            if (!response.ok) {
                console.error('Stock ID check failed:', response.statusText);
                alert('指定された在庫IDが存在しません。');
                throw new Error('Stock ID check failed');
            }
            return response.json();
        })
        .then(data => {
            if (!data) {
                alert('指定された在庫IDが存在しません。');
                return;
            }
            console.log('Stock ID exists:', data);
            // Proceed to check stockName and unit only if provided
            if (stockName !== '') {
                const nameUrl = `/checkStockName/${stockId}/${stockName}`;
                fetch(nameUrl)
                    .then(response => {
                        if (!response.ok) {
                            console.error('Stock Name check failed:', response.statusText);
                            alert('指定された在庫名称が存在しません。');
                            throw new Error('Stock Name check failed');
                        }
                        return response.json();
                    })
                    .then(data => {
                        if (!data) {
                            alert('指定された在庫名称が在庫IDと一致しません。');
                            return;
                        }
                        console.log('Stock Name exists:', data);
                        checkUnit(unit);
                    })
                    .catch(error => {
                        console.error('Error checking stock name:', error);
                    });
            } else {
                checkUnit(unit);
            }
        })
        .catch(error => {
            console.error('Error checking stock ID:', error);
        });

    function checkUnit(unit) {
        if (unit !== '') {
            const unitIdUrl = `/getUnitIdByName/${encodeURIComponent(unit)}`;
            fetch(unitIdUrl)
                .then(response => {
                    if (!response.ok) {
                        console.error('Unit ID fetch failed:', response.statusText);
                        alert('指定された単位が存在しません。');
                        throw new Error('Unit ID fetch failed');
                    }
                    return response.json();
                })
                .then(unitId => {
                    if (!unitId) {
                        alert('指定された単位が存在しません。');
                        return;
                    }
                    const unitCheckUrl = `/checkStockIdAndUnit/${stockId}/${unitId}`;
                    return fetch(unitCheckUrl);
                })
                .then(response => {
                    if (!response.ok) {
                        console.error('Unit check failed:', response.statusText);
                        alert('指定された単位が在庫IDと一致しません。');
                        throw new Error('Unit check failed');
                    }
                    return response.json();
                })
                .then(data => {
                    if (!data) {
                        alert('指定された単位が在庫IDと一致しません。');
                        return;
                    }
                    console.log('Unit exists:', data);
                    navigateToEditForm();
                })
                .catch(error => {
                    console.error('Error checking unit:', error);
                });
        } else {
            navigateToEditForm();
        }
    }

    function navigateToEditForm() {
        const editUrl = `/showEditStockIoForm?id=${encodeURIComponent(stockId)}&name=${encodeURIComponent(stockName)}&unit=${encodeURIComponent(unit)}&num=${encodeURIComponent(stockNum)}`;
        window.location.href = editUrl;
    }
}


function getIoTypeText(ioType) {
    switch (ioType) {
        case 1:
            return "購買発注入庫";
        case 2:
            return "購買発注入庫取消";
        case 3:
            return "仕入返品";
        case 4: return "仕入返品取消";
        case 5: return "出庫";
        case 6: return "出庫取消";
        default: return "Unknown";
    }
}

function formatIoTypes() {
    const ioTypeElements = document.querySelectorAll('[data-io-type]');
    ioTypeElements.forEach(element => {
        const ioType = parseInt(element.textContent.trim(), 10); // Parse content as integer

        // Set text content based on Io Type
        element.textContent = getIoTypeText(ioType);

        // Apply row color based on Io Type
        switch (ioType) {
            case 1:
            case 3:
            case 6:
                element.closest('tr').classList.add('table-in');
                break;
            case 2:
            case 4:
            case 5:
                element.closest('tr').classList.add('table-out');
                break;
        }
    });
}

document.addEventListener('DOMContentLoaded', formatIoTypes);
