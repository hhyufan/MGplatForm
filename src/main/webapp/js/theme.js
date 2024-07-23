// 要操作到的元素
let login=document.getElementById('login');
let register=document.getElementById('register');
let form_box=document.getElementsByClassName('form-box')[0];
let register_box=document.getElementsByClassName('register-box')[0];
let login_box=document.getElementsByClassName('login-box')[0];
// 去注册按钮点击事件
register.addEventListener('click', () => {
    form_box.style.transform = 'translateX(80%)';
    form_box.style.background = '#d3b7d8';
    login_box.classList.add('hidden');
    register_box.classList.remove('hidden');
    document.body.style.background = 'linear-gradient(200deg,#e3c5eb,#a9c1ed)';
});

login.addEventListener('click', () => {
    form_box.style.transform = 'translateX(0%)';
    form_box.style.background = '#ebc777';
    register_box.classList.add('hidden');
    login_box.classList.remove('hidden');
    document.body.style.background = 'linear-gradient(200deg, #fbed77, #d8b376)';
});