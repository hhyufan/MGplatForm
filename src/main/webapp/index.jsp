<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Index Page</title>
    <script>
        function redirectAndChangeURL() {
            const shouldRedirect = true;
            if (shouldRedirect) {
                const newUrl = "PlatForm.html";
                history.pushState(null, null, newUrl);
                window.location.replace(newUrl);
            }
        }
    </script>
</head>
<body>
<h1>Index Page</h1>
<p>This is the index page.</p>

<script>
    redirectAndChangeURL();
</script>
</body>
</html>