<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error Page</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .error-box {
            border: 1px solid #ff6b6b;
            background-color: #fff5f5;
            padding: 15px;
            border-radius: 5px;
            margin: 20px 0;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>⚠️ Error Occurred</h1>
        <div class="error-box">
            ${error}
        </div>
        <a href="index.jsp" class="button">Return to Homepage</a>
    </div>
</body>
</html>