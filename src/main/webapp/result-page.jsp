<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="assets.*" %>
<%@ page import="database.*" %>
<%@ page import="diversificationCriterion.*" %>
<%@ page import="utilities.*" %>
<%@ page import="java.util.*" %>

<!DOCTYPE html>
<html lang="en_US">
	<head>
		<meta charset="UTF-8">
		<title>DiversificationApp</title>
		<style><%@include file="/css/style.css"%></style>
	</head>
	<body>
		<%
		StockQuantity stockquantity = (StockQuantity) request.getAttribute("StockQuantity");
		InvestmentPortfolio investmentPortfolio = new InvestmentPortfolio(stockquantity);
		CountryDiversification countryDiversification = new CountryDiversification(investmentPortfolio); 
		%>
	
		<h1>Country Diversification</h1>
		
		Your investment portfolio:
		<table>
			<tr>
				<th>Ticker</th>
				<th>Quantity</th>
				<th>Price, ₽</th>
				<th>Total, ₽</th>
			</tr>
			<% 
			for (Ticker ticker : FinExTicker.values()) {
					if (stockquantity.get(ticker) > 0) {
						%>
						<tr>
							<td><%=ticker%></td>
							<td align="right"><%=stockquantity.get(ticker)%></td>
							<td align="right"><%=StockPrices.get(ticker)%></td>
							<td align="right"><%=Unifier.doubleToMoney(investmentPortfolio.getTotalPriceByTicker(ticker))%></td>
						</tr>
						<% 
					}
				}
				for (Ticker ticker : VTBTicker.values()) {
					if (stockquantity.get(ticker) > 0) {
						%>
						<tr>
							<td><%=ticker%></td>
							<td align="right"><%=stockquantity.get(ticker)%></td>
							<td align="right"><%=StockPrices.get(ticker)%></td>
							<td align="right"><%=Unifier.doubleToMoney(investmentPortfolio.getTotalPriceByTicker(ticker))%></td>
						</tr>
						<%
					}
				}
			 %>
			 <tr>
			 	<td><strong>Sum of all assets: </strong></td>
			 	<td colspan="3" align="right"> <%=Unifier.doubleToMoney(investmentPortfolio.getSum())%></td>
			 </tr>
		</table>
		
		<br>
		
		Diversification you have:
		<table>
			<tr>
				<th>Country</th>
				<th>Share, ₽</th>
				<th>Share, %</th>
			</tr>
			<%
			for (Map.Entry<Country, Double> entry : countryDiversification.getEntrySet()) {
				%>
				<tr>
					<td><%=entry.getKey()%></td>
					<td align="right"><%=Unifier.doubleToMoney(entry.getValue())%></td>
					<td align="right"><%=Unifier.doubleToMoney(entry.getValue() / investmentPortfolio.getSum() * 100)%></td>
				</tr>
				<%
			}
			%>
		</table>
		
		<p>
			<strong><a href="http://localhost:8080/diversification-web-project/">Back</a></strong>
		</p>
	</body>
</html>