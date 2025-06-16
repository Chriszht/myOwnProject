<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Weather Information</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">
        <h1>Weather in ${city}, ${country}</h1>
        <p>Temperature: ${temp}°C</p>
        <p>Weather description: ${description}</p>
        <p>Local time: ${localTime}</p>
        <a href="index.jsp" class="button">Return to Homepage</a>
    </div>
</body>
</html>