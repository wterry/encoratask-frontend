<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html>
    <title>Inventory Restock Invoices</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/js/bootstrap.bundle.min.js"></script>
    <body>
    <c:url var="listProductsUrl" value="http://localhost:8080/pages/products/list" />
        <h2 class="text-center">Existing Catalog</h2>
        <br>
        <table align="center" class="table table-striped">
            <thead>
            <tr>
                <th scope="col">ID</th>
                <th scope="col">SKU</th>
                <th scope="col">Description</th>
                <th scope="col">Missing Stock (to reach minimum)</th>
                <th scope="col">Total Restock Price</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${invoiceSearch}" var="invoice">
                <tr>
                    <td>${invoice.id}</td>
                    <td>${invoice.sku}</td>
                    <td>${invoice.description}</td>
                    <td>${invoice.missingStock}</td>
                    <td>${invoice.restockPrice}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <br>
        <center><a href=${listProductsUrl}>Return to Product Listing</a></center>
    </body>
</html>
