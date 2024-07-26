
document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('loginForm').addEventListener('submit',
        async (event) => {
        event.preventDefault();
        const loginError = document.getElementById('loginError');
        const form = event.target;
        const formData = new FormData(form);
        const username = formData.get('username').toString();
        const password = formData.get('password').toString();
        // 正则表达式匹配邮箱格式
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        const isEmailLogin = emailPattern.test(username + "");
        const actionType = "login";

        const loginData = {
            username,
            password,
            actionType,
            isEmailLogin
        };
        console.log(loginData)
        fetch('http://localhost:8080/MGManagePlatForm_war/login', {
            method: 'POST',
            body: JSON.stringify(loginData)
        }).then(async (response) => {
            if (!response.ok) {
                throw new Error('网络请求失败！');
            }
            console.log(' response.json',  JSON.stringify(response.json));
            return await response.json();
        }).then(data => {
            if (isEmailLogin ) {
                if(!data["isEmailExists"] && data["isEmailExists"] !== undefined) {
                    loginError.textContent = "邮箱不存在。"
                    return;
                }
            } else {
                if (!data["isUserExists"] && data["isUserExists"] !== undefined) {
                    loginError.textContent = "用户不存在。";
                    return;
                }
            }
            if (!data["correctPassword"]) {
                loginError.textContent = "密码输入错误。";
            } else {
                loginError.textContent = "登录成功！";
                window.location.replace('website.html');
            }

            console.log(data);
        }).catch(error => {
            console.error(error);
        })
    });
});