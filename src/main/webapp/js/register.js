// register.js
const validatePassword = (password) => {

    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[$@!%*#?&])[A-Za-z\d$@!%*#?&']{8,24}$/;
    return passwordRegex.test(password);
}

document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('registerForm').addEventListener('submit',
        async (event) => {
            event.preventDefault();
            const registerError = document.getElementById('registerError');
            const form = event.target;
            const formData = new FormData(form);
            const username = formData.get('username').toString();
            const password = formData.get('password').toString();
            const email = formData.get('email').toString();
            const confirmPassword = formData.get('confirmPassword').toString();
            const actionType = "register";
            if (!validatePassword(password)) {
                registerError.textContent = "密码不符合要求，请重新输入。";
                form.reset()
                return;
            }
            if (password !== confirmPassword) {
                registerError.textContent = "密码不一致。";
                form.reset()
                return;
            }

            const registerData = {
                username,
                password,
                actionType,
                email
            };
            console.log(registerData)
            fetch('http://localhost:8080/MGManagePlatForm_war/register', {
                method: 'POST',
                body: JSON.stringify(registerData)
            }).then(async (response) => {
                if (!response.ok) {
                    throw new Error('网络请求失败！');
                }
                console.log('response.json',  JSON.stringify(response.json));
                return await response.json();
            }).then(data => {
                if(data["isEmailExists"]) {
                    registerError.textContent = "邮箱已存在。"
                    form.reset()
                    return;
                }
                if (data["isUserExists"]) {
                    registerError.textContent = "用户已存在。";
                    form.reset()
                    return;
                }
                alert('register successful!');
                registerError.textContent = "注册成功！";
                form.reset()
                window.location.replace('website.html');
                console.log(data);
            }).catch(error => {
                console.error(error);
            })
        });
});