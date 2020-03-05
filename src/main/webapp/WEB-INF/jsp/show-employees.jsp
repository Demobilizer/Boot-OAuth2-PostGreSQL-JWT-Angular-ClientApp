<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page session="false"%>
<html>
<head>
<title>Show Employees</title>
</head>
<body>

	<h3 style="color: red;">Show All Employees</h3>
	<ul>
	The resource that you need is here on client app now! <br/><br/>
		<c:forEach var="emp" items="${employees}">
			<li>${emp}</li>
		</c:forEach>
	</ul>
</body>
</html>