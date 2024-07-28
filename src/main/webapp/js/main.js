const formatMilliseconds = (ms) => {
    const timeUnits = [
        { label: '年', value: 365 * 24 * 60 * 60 * 1000 },
        { label: '月', value: 30 * 24 * 60 * 60 * 1000 },
        { label: '日', value: 24 * 60 * 60 * 1000 },
        { label: '小时', value: 60 * 60 * 1000 },
        { label: '分钟', value: 60 * 1000 },
        { label: '秒', value: 1000 }
        // ,{ label: '毫秒', value: 1 }
    ];

    let formattedTime = '';
    let remainingTime = ms;

    timeUnits.forEach(unit => {
        if (remainingTime >= unit.value) {
            const num = Math.floor(remainingTime / unit.value);
            formattedTime += `${num}${unit.label} `;
            remainingTime -= num * unit.value;
        }
    });

    return formattedTime.trim();
}
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
        const lastLoginTime = JSON.parse(data.LastLoginTime);
        const h2Title = document.querySelector('h2');
        h2Title.textContent = `你好${isAdministrator ? "管理员": ""}, ${username}! 欢迎来到喵咕平台!`;
        h2Title.textContent += "距离上次登录时间：" + formatMilliseconds(lastLoginTime[1] - lastLoginTime[0]);
    }).catch(error => {
        console.error('Error fetching user info:', error);
    });
});
