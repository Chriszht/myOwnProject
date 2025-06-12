// 这个文件可以放在 src/main/webapp/js/script.js

// 等待DOM加载完成
document.addEventListener('DOMContentLoaded', function() {
    // 首页表单提交处理
    const weatherForm = document.querySelector('form');
    if (weatherForm) {
        weatherForm.addEventListener('submit', function(e) {
            e.preventDefault();
            this.submit(); // 正常提交表单到Servlet
        });
    }

    // 从URL参数中获取数据并填充到页面
    const urlParams = new URLSearchParams(window.location.search);

    // 天气页面数据处理
    if (window.location.pathname.includes('weather.html')) {
        const city = urlParams.get('city');
        const country = urlParams.get('country');
        const temp = urlParams.get('temp');
        const description = urlParams.get('description');
        const localTime = urlParams.get('localTime');

        if (city) {
            document.querySelector('h1').textContent = `Weather in ${city}, ${country}`;
            document.querySelector('#temperature').textContent = `${temp}°C`;
            document.querySelector('#description').textContent = description;
            document.querySelector('#local-time').textContent = localTime;
        }
    }

    // 错误页面数据处理
    if (window.location.pathname.includes('error.html')) {
        const error = urlParams.get('error');
        if (error) {
            document.querySelector('#error-message').textContent = error;
        }
    }
});