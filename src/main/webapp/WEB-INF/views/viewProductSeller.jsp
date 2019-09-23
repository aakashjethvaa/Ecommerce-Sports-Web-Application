<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>   

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>   

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
	<h1>View Products</h1>
	
	<form:form action="${contextPath}/cart/addToCart"
		modelAttribute="approvedProducts" method="post">
		<table border="1">
			<tr>
				<td><b>Product Id</b></td>
				<td><b>Photo Thumb</b></td>
				<td><b>Name</b></td>
				<td><b>Category</b></td>
				<td><b>Price</b></td>
			</tr>
			<c:forEach var="approvedProducts" items="${approvedProducts}">
				<tr>
					<td><input value="${approvedProducts.id}" name="prodId"
						readonly /></td>
					<td><img height="150" width="150" name="image"
						src="${approvedProducts.filename}" /></td>
					<td><input value="${approvedProducts.name}" name="name"
						readonly /></td>
					<td><input value="${approvedProducts.category}"
						name="category" readonly /></td>
					<td><input value="${approvedProducts.price}" name="price"
						readonly /></td>
				</tr>
				 
			</c:forEach>
		</table>
		
	</form:form>
	
	<form:form action="${contextPath}/logout/" method="post">
	<button type="submit">Logout</button>
	</form:form>
</body>
</html>