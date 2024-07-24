document.addEventListener('DOMContentLoaded', function() {
fetch('http://localhost:8080/MGManagePlatForm_war/userInfo', {
    method: 'GET',
    credentials: 'include'
})
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        // 假设后端返回的数据结构包含用户名字段
        const username = data.username;
        const isAdministrator = data.isAdministrator;
        // 选择页面中的h2元素，并将用户名设置为其文本内容
        const h2Title = document.querySelector('h2');
        h2Title.textContent = `你好${isAdministrator ? "管理员": ""}, ${username}! 欢迎来到喵咕平台`;
    })
    .catch(error => {
        console.error('Error fetching user info:', error);
    });
});