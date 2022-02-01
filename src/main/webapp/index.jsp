<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="assets.*" %>
<%@ page import="database.*" %>
<%@ page import="java.util.*" %>

<html lang="en-US">
	<head>
		<title>DiversificationApp</title>
		<meta charset="UTF-8">
		<style><%@include file="/css/style.css"%></style>
	</head>
	<body>
		<%
		Cookie[] cookies = request.getCookies();
		Map<String, String> tickerQuantityMap = new HashMap<>();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getValue().equals("0")) {
					tickerQuantityMap.put(cookie.getName(), "");
				} else {
					tickerQuantityMap.put(cookie.getName(), cookie.getValue());
				}
			}
		}
		%>
		<h1>Diversifying Web Application</h1>
		<p>Enter the number of shares you have, then click <strong>CALCULATE</strong> button below:</p>
		<form method="get" action="http://localhost:8080/diversification-web-project/Result">
			<table>
				<tr>
					<th>Ticker</th>
					<th>Quantity</th>
					<th>Price, ₽</th>
				</tr>
				<%
				for (Ticker ticker : FinExTicker.values()) {%>
				<tr>
					<td><%=ticker%></td>
					<td><input type="number" min="0" max="2147483647" name="<%=ticker%>" placeholder="0" value="<%=tickerQuantityMap.getOrDefault(ticker.toString(), "") %>" class="quantityInput"></td>
					<td align="right"><%=StockPrices.get(ticker)%></td>
				</tr>
				<%}
				for (Ticker ticker : VTBTicker.values()) {%>
				<tr>
					<td><%=ticker%></td>
					<td><input type="number" min="0" max="2147483647" name="<%=ticker%>" placeholder="0" value="<%=tickerQuantityMap.getOrDefault(ticker.toString(), "") %>" class="quantityInput"></td>
					<td align="right"><%=StockPrices.get(ticker)%></td>
				</tr>
				<%}
				%>
			</table>
			<p><input type="submit" value="CALCULATE" class="submit"></p>
		</form>
	</body>
</html>
