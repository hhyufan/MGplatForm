document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('personal-center-link').addEventListener('click', function(event) {
        event.preventDefault(); // 防止默认行为

        // 获取用户信息的请求
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
                // 在这里处理从 userInfo 接口获取的数据
                const username = data.username;
                const paragraph = document.getElementById('con_username');
                paragraph.textContent = `${username}`;

                // 发起获取 PersonalCenterServlet 响应的请求
                return fetch('http://localhost:8080/MGManagePlatForm_war/personal', {
                    method: 'GET',
                    credentials: 'include'
                });
            })
            .then(personalCenterResponse => {
                if (!personalCenterResponse.ok) {
                    throw new Error('PersonalCenterServlet response was not ok');
                }
                return personalCenterResponse.json();
            })
            .then(personalCenterData => {
                // 处理 PersonalCenterServlet 的响应数据
                const registerTime = personalCenterData["RegisterTime"];
                console.log('PersonalCenterServlet response - registerTime:', registerTime);
                const Email = personalCenterData["Email"];
                // 更新页面的内容
                const registerTimeParagraph = document.getElementById('register_time');
                registerTimeParagraph.textContent = `${registerTime}`;

                const EmailTimeParagraph = document.getElementById('email');
                EmailTimeParagraph.textContent = `${Email}`;
            })
            .catch(error => {
                console.error('Error:', error);
                // 处理错误情况
            });
    });
});