document.addEventListener('DOMContentLoaded', function() {
  const homeLink = document.getElementById('home-link'); // 首页链接
  const personalCenterLink = document.getElementById('personal-center-link'); // 个人中心链接
  const mainContentSection = document.getElementById('main-content-section'); // 首页 section
  const personalCenterSection = document.getElementById('personal-center-section'); // 个人中心 section
  const chartLine = document.getElementById('chart-line');
  const users = document.getElementById('users');
  const envelope = document.getElementById('envelope');
  // 设定当前主题，true为黄色，false为紫色
  let theme = true;
  let g_activeLinkColor = 'rgba(198,78,249,0.59)'
  fetch('http://localhost:8080/MGManagePlatForm_war/userInfo', {
    method: 'GET',
    credentials: 'include'
  }).then(async response => {
    if (!response.ok) {
      throw new Error('Network response was not ok');
    }
    return await response.json();
  }).then(data => {
    chartLine.style.display = data.isAdministrator ? 'block' : 'none';
    users.style.display = data.isAdministrator ? 'block' : 'none';
  });

  // 点击首页链接时显示首页 section
  homeLink.addEventListener('click', function(event) {
    event.preventDefault(); // 阻止默认行为
    mainContentSection.style.display = 'flex';
    personalCenterSection.style.display = 'none';
  });

  // 点击个人中心链接时显示个人中心 section
  personalCenterLink.addEventListener('click', function(event) {
    event.preventDefault(); // 阻止默认行为
    mainContentSection.style.display = 'none';
    personalCenterSection.style.display = 'block';
  });

  // 处理点击事件时的页面跳转
  const hrefLoading = () => {
    window.location.href = 'http://localhost:8080/MGManagePlatForm_war/loading.html';
  }
  chartLine.addEventListener('click', hrefLoading)
  users.addEventListener('click', hrefLoading)
  envelope.addEventListener('click', hrefLoading)

  // 设置导航项的 active 状态
  const navItems = document.querySelectorAll('.nav-item');
  navItems.forEach(item => {
    item.addEventListener('click', function() {
      navItems.forEach(navItem => {
        navItem.classList.remove('active')
        navItem.querySelector('a').style.color = '';
      });
      item.classList.add('active');
      item.querySelector('a').style.color = g_activeLinkColor;
    });
  });

  // 初始时移除所有的 active 类
  navItems.forEach(item => item.classList.remove('active'));
  // 将第一个导航项设置为 active
  navItems[0].classList.add('active');

  // 处理切换主题的 toggle
  const toggle = document.getElementById('toggle');
  toggle.addEventListener('change', () => {
    const icons = document.querySelectorAll('.card i');
    const sidebar = document.querySelector('.sidebar');

    const setStyles = (bodyBg_left, bodyBg_right, activeLinkColor, sidebarBg, icons) => {
      document.body.style.background = `linear-gradient(200deg, ${bodyBg_left}, ${bodyBg_right})`;
      sidebar.style.backgroundColor = sidebarBg;
      theme = !toggle.checked
      g_activeLinkColor = activeLinkColor;
      navItems.forEach(item => {
        if (item.classList.contains('active')) {
          item.querySelector('a').style.color = g_activeLinkColor;
        }
      });
      icons.forEach(icon => {
        icon.style.color = sidebarBg;
      });
    }

    if (toggle.checked) {
      setStyles('rgba(227,197,235,0.8)','rgba(192,102,229,0.6)', 'rgba(251,251,15,0.82)', 'rgb(213, 133, 248)', icons);
    } else {
      setStyles('rgba(239,249,79,0.8)', 'rgba(231,215,86,0.6)','rgba(198,78,249,0.59)', 'rgb(235,199,119)', icons);
    }
  });
});