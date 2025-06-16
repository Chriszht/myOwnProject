<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Weather App</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="container">
    <h1>Local Weather and Time</h1>
    <form action="weather" method="post">
        <input type="text" name="location" placeholder="Please enter city name" required>
        <button type="submit">Get information</button>
    </form>
</div>
</body>
</html>