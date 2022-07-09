<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!doctype html>

<html>
<head>
    <title>View Products</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
    <c:url var="addProductUrl" value="http://localhost:8080/pages/products/add" />
    <c:url var ="editProductUrl" value="http://localhost:8080/pages/products/edit" />
    <c:url var ="deleteProductUrl" value="http://localhost:8080/pages/products/delete" />
    <c:url var ="generateInvoicesUrl" value="http://localhost:8080/pages/inventory/invoices" />

    <h2 class="text-center">Existing Catalog</h2>
    <br>

    <c:if test="${success}">
        <h4 class="text-center">Operation Successful! Correlational Id: ${correlationalId}</h4>
    </c:if>
    <c:if test="${validationError}">
        <h4 class="text-center">Invalid Input! Please, review the following:</h4>
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
<table align="center" class="table table-striped">
    <thead>
    <tr>
        <th scope="col">ID</th>
        <th scope="col">SKU</th>
        <th scope="col">Description</th>
        <th scope="col"><a href=${addProductUrl}>Add Product</a></th>
        <th scope="col"><a href=${generateInvoicesUrl}>List Restock Invoices</a></th>
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

        <h5 class="text-center">Product Details</h5>
        <br>
        <form:form action="${editProductUrl}" method="post" modelAttribute="changingProduct">
            <table align="center">
                <tbody>
                    <tr><td align="right"><form:label path="id">ID: </form:label></td> <td><form:input readonly="true" class="form-control-plaintext" path="id" type="number" /></td></tr>
                    <tr><td align="right"><form:label path="sku">SKU: </form:label></td> <td><form:input class="form-control form-control-sm" path="sku" type="text" /></td></tr>
                    <tr><td align="right"><form:label path="description">Description: </form:label></td> <td><form:input class="form-control form-control-sm" path="description" type="text" /></td></tr>
                    <tr><td align="right"><form:label path="price">Price: </form:label></td> <td><form:input path="price" class="form-control form-control-sm" type="number" /></td></tr>
                    <tr><td align="right"><form:label path="inventoryStock">Stock: </form:label></td> <td><form:input class="form-control form-control-sm" path="inventoryStock" type="number" /></td></tr>
                    <tr><td/><td><button class="btn btn-primary" type="submit" value="submit">Update</button></td></tr>
                </tbody>
            </table>
        </form:form>
    </c:if>

    <!-- add new product form -->
    <c:if test="${adding}">

        <h5 class="text-center">New Product</h5>
        <br>
        <form:form action="${addProductUrl}" method="post" modelAttribute="newProduct">
            <table align="center">
                <tbody>
                <tr><td align="right"><form:label path="sku">SKU: </form:label></td> <td><form:input  class="form-control form-control-sm" path="sku" type="text" /></td></tr>
                <tr><td align="right"><form:label path="description">Description: </form:label></td> <td><form:input class="form-control form-control-sm" path="description" type="text" /></td></tr>
                <tr><td align="right"><form:label path="price">Price: </form:label></td> <td><form:input  class="form-control form-control-sm" path="price" type="number" step=".01"/></td></tr>
                <tr><td align="right"><form:label path="inventoryStock">Stock: </form:label></td> <td><form:input class="form-control form-control-sm" path="inventoryStock" type="number"  /></td></tr>
                <tr><td/><td><button class="btn btn-primary" type="submit" value="submit">Create</button></td></tr>
                </tbody>
            </table>
        </form:form>
    </c:if>
</body>
</html>