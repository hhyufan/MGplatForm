document.getElementById('personal-center-link').addEventListener('click', function() {
    document.getElementById('main-content-section').style.display = 'none';
    document.getElementById('personal-center-section').style.display = 'block';

    fetch('/MGManagePlatForm_war/PersonalCenterServlet', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            document.getElementById('username').textContent = '用户名: ' + data.username;
            document.getElementById('data').textContent = '数据: ' + data.data;
        })
        .catch(error => {
            console.error('Error fetching personal center data:', error);
        });
});