<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <title>View Products</title>
    <link href="<c:url value="/css/common.css"/>" rel="stylesheet" type="text/css">
</head>
<body>
    <c:url var="addProductUrl" value="http://localhost:8080/pages/products/add" />
    <c:url var ="editProductUrl" value="http://localhost:8080/pages/products/edit" />
    <c:url var ="deleteProductUrl" value="http://localhost:8080/pages/products/delete" />
    <c:url var ="generateInvoicesUrl" value="http://localhost:8080/pages/inventory/invoices" />

    <center><b>Existing Catalog</b></center>
    <br>

    <c:if test="${success}">
        <center>Operation Successful! Correlational Id: ${correlationalId}</center>
    </c:if>
    <c:if test="${validationError}">
        <center>Invalid Input! Please, review the following:</center>
        <br>
        <table align="center">
            <tbody>
            <c:forEach items="${errorContent}" var="error">
                <tr>
                    <td><b>${error.error}</b></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>
    <c:if test="${genericError}">
        <center>Operation Failed with status ${operationStatus}!</center>
        <br>
        <center>The following error occurred:  ${errorContent}</center>
    </c:if>

<br>
<table align="center">
    <thead>
    <tr>
        <th>ID</th>
        <th>SKU</th>
        <th>Description</th>
        <th><a href=${addProductUrl}>Add Product</a></th>
        <th><a href=${generateInvoicesUrl}>List Restock Invoices</a></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${productSearch}" var="product">
        <tr>
            <td>${product.id}</td>
            <td>${product.sku}</td>
            <td>${product.description}</td>
            <td><a href="${deleteProductUrl}/${product.id}">Delete</a></td>
            <td><a href="${editProductUrl}/${product.id}">Edit</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<br>
<br>

<!-- forms and such go here, they should appear only when needed -->
    <c:if test="${editing}">

        <div align="center"><b>Product Details</b></div>
        <br>
        <form:form action="${editProductUrl}" method="post" modelAttribute="changingProduct">
            <table align="center">
                <tbody>
                    <tr hidden="true"><td align="right"><form:label path="id">ID: </form:label></td> <td><form:input path="id" type="number" /></td></tr>
                    <tr><td align="right"><form:label path="sku">SKU: </form:label></td> <td><form:input path="sku" type="text" /></td></tr>
                    <tr><td align="right"><form:label path="description">Description: </form:label></td> <td><form:input path="description" type="text" /></td></tr>
                    <tr><td align="right"><form:label path="price">Price: </form:label></td> <td><form:input path="price" type="number" /></td></tr>
                    <tr><td align="right"><form:label path="inventoryStock">Stock: </form:label></td> <td><form:input path="inventoryStock" type="number" /></td></tr>
                    <tr><td/><td><input type="submit" value="submit" /></td></tr>
                </tbody>
            </table>
        </form:form>
    </c:if>

    <!-- add new product form -->
    <c:if test="${adding}">

        <div align="center"><b>New Product</b></div>
        <br>
        <form:form action="${addProductUrl}" method="post" modelAttribute="newProduct">
            <table align="center">
                <tbody>
                <tr><td align="right"><form:label path="sku">SKU: </form:label></td> <td><form:input path="sku" type="text" /></td></tr>
                <tr><td align="right"><form:label path="description">Description: </form:label></td> <td><form:input path="description" type="text" /></td></tr>
                <tr><td align="right"><form:label path="price">Price: </form:label></td> <td><form:input  path="price" type="number" step=".01"/></td></tr>
                <tr><td align="right"><form:label path="inventoryStock">Stock: </form:label></td> <td><form:input path="inventoryStock" type="number"  /></td></tr>
                <tr><td/><td><input type="submit" value="submit" /></td></tr>
                </tbody>
            </table>
        </form:form>
    </c:if>
</body>
</html>