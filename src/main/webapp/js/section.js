document.addEventListener('DOMContentLoaded', function() {
  const homeLink = document.querySelector('.nav a[href="#"]'); // 首页链接
  const personalCenterLink = document.getElementById('personal-center-link'); // 个人中心链接
  const mainContentSection = document.getElementById('main-content-section'); // 首页 section
  const personalCenterSection = document.getElementById('personal-center-section'); // 个人中心 section
  const chartLine  = document.getElementById('chart-line');
  const users = document.getElementById('users');
  const envelope = document.getElementById('envelope');
  // 点击首页链接时显示首页 section
  homeLink.addEventListener('click', function(event) {
    event.preventDefault(); // 阻止默认行为
    mainContentSection.style.display = 'block';
    //window.location.href = "http://localhost:8080/MGManagePlatForm_war/website.html";
    personalCenterSection.style.display = 'none';
  });

  // 点击个人中心链接时显示个人中心 section
  personalCenterLink.addEventListener('click', function(event) {
    event.preventDefault(); // 阻止默认行为
    mainContentSection.style.display = 'none';
    personalCenterSection.style.display = 'block';
  });
  const hrefLoading = () => {
    window.location.href = 'http://localhost:8080/MGManagePlatForm_war/loading.html';
  }
  chartLine.addEventListener('click', hrefLoading)
  users.addEventListener('click', hrefLoading)
  envelope.addEventListener('click', hrefLoading)
});