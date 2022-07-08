<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
    <title>Inventory Restock Invoices</title>
    <link href="<c:url value="/css/common.css"/>" rel="stylesheet" type="text/css">
    <body>
    <c:url var="listProductsUrl" value="http://localhost:8080/pages/products/list" />
        <center><b>Restock Invoices for Low Stock Products</b></center>
        <br>
        <table align="center">
            <thead>
            <tr>
                <th>ID</th>
                <th>SKU</th>
                <th>Description</th>
                <th>Missing Stock (to reach minimum)</th>
                <th>Total Restock Price</th>
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
