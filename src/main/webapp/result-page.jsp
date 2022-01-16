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
		
		<style><%@include file="css/style.css"%></style>
		
		<%
		StockQuantity stockquantity = (StockQuantity) request.getAttribute("StockQuantity");
		InvestmentPortfolio investmentPortfolio = new InvestmentPortfolio(stockquantity);
		CountryDiversification countryDiversification = new CountryDiversification(investmentPortfolio);
		
		Map<Country, Double> USAmap = new LinkedHashMap<>();
		Map<Country, Double> developedMarketMap = new LinkedHashMap<>();
		Map<Country, Double> emergencyMarketMap = new LinkedHashMap<>();
		
		/* These maps include those countries whose own parts are greater than 1% */
		Map<Country, Double> marketExcludeOther = new LinkedHashMap<>();
		Map<Country, Double> developedMarketExcludeOther = new LinkedHashMap<>();
		Map<Country, Double> emergencyMarketExcludeOther = new LinkedHashMap<>();
		
		/* These maps include those countries whose own parts are less than 1% */
		Map<Country, Double> marketOther = new LinkedHashMap<>();
		Map<Country, Double> developedMarketOther = new LinkedHashMap<>();
		Map<Country, Double> emergencyMarketOther = new LinkedHashMap<>();
		
		double developedMarketSum = 0;
		double emergencyMarketSum = 0;
		
		/* These fields include those countries whose own parts are less than 1% */
		double otherMarketSum = 0;
		double otherDevelopedMarketSum = 0;
		double otherEmergencyMarketSum = 0;
		
		for (Map.Entry<Country, Double> entry : countryDiversification.getEntrySet()) {
			Country country = entry.getKey();
			Double shareRub = entry.getValue();
			if (country.equals(Country.USA)) {
				USAmap.put(country, shareRub);
				marketExcludeOther.put(country, shareRub);
			} else if (country.isDevelopedCountry()) {
				developedMarketMap.put(country, shareRub);
				developedMarketSum += shareRub;
				if (shareRub / investmentPortfolio.getSum() < 0.01) {
					otherMarketSum += shareRub;
					otherDevelopedMarketSum += shareRub;
					marketOther.put(country, shareRub);
					developedMarketOther.put(country, shareRub);
				} else {
					marketExcludeOther.put(country, shareRub);
					developedMarketExcludeOther.put(country, shareRub);
				}
			} else {
				emergencyMarketMap.put(country, shareRub);
				emergencyMarketSum += shareRub;
				if (shareRub / investmentPortfolio.getSum() < 0.01) {
					otherMarketSum += shareRub;
					otherEmergencyMarketSum += shareRub;
					marketOther.put(country, shareRub);
					emergencyMarketOther.put(country, shareRub);
				} else {
					marketExcludeOther.put(country, shareRub);
					emergencyMarketExcludeOther.put(country, shareRub);
				}
			}
		}
		
		/* If there is only 1 country in the other section then it puts into "excludeOther" map */
		if (marketOther.size() == 1) {
			for (Map.Entry<Country, Double> entry : marketOther.entrySet()) {
				marketExcludeOther.put(entry.getKey(), entry.getValue());
				otherMarketSum = 0;
			}
		}
		if (developedMarketOther.size() == 1) {
			for (Map.Entry<Country, Double> entry : developedMarketOther.entrySet()) {
				developedMarketExcludeOther.put(entry.getKey(), entry.getValue());
				developedMarketSum += entry.getValue();
				otherMarketSum -= entry.getValue();
				otherDevelopedMarketSum = 0;
			}
		}
		if (emergencyMarketOther.size() == 1) {
			for (Map.Entry<Country, Double> entry : emergencyMarketOther.entrySet()) {
				emergencyMarketExcludeOther.put(entry.getKey(), entry.getValue());
				emergencyMarketSum += entry.getValue();
				otherMarketSum -= entry.getValue();
				otherEmergencyMarketSum = 0;
			}
		}
		%>
		
		<script type="text/javascript" src="js/jquery.js"></script>
		<script type="text/javascript" src="js/jquery.canvaswrapper.js"></script>
		<script type="text/javascript" src="js/jquery.colorhelpers.js"></script>
		<script type="text/javascript" src="js/jquery.flot.js"></script>
		<script type="text/javascript" src="js/jquery.flot.saturated.js"></script>
		<script type="text/javascript" src="js/jquery.flot.browser.js"></script>
		<script type="text/javascript" src="js/jquery.flot.drawSeries.js"></script>
		<script type="text/javascript" src="js/jquery.flot.uiConstants.js"></script>
		<script type="text/javascript" src="js/jquery.flot.legend.js"></script>
		<script type="text/javascript" src="js/jquery.flot.pie.js"></script>
		<script type="text/javascript">
		$(function() {
			var standardData = [
				<%
				for (Map.Entry<Country, Double> entry : marketExcludeOther.entrySet()) {
					out.println("{ label: \"" + entry.getKey() + "\",  data: " + entry.getValue() + "},");
				}
				%>
				{ label: "Other", data: "<%=otherMarketSum%>"}
			];
			var extendedData = [
				<%
				if (!USAmap.isEmpty()) {
					out.println("{ label: \"USA\",  data: " + USAmap.get(Country.USA) + "},");
				}
				if (!developedMarketMap.isEmpty()) {
					for (Map.Entry<Country, Double> entry : developedMarketExcludeOther.entrySet()) {
						out.println("{ label: \"" + entry.getKey() + "\",  data: " + entry.getValue() + "},");
					}
					out.println("{ label: \"Other\",  data: " + otherDevelopedMarketSum + "},");
				}
				if (!emergencyMarketMap.isEmpty()) {
					for (Map.Entry<Country, Double> entry : emergencyMarketExcludeOther.entrySet()) {
						out.println("{ label: \"" + entry.getKey() + "\",  data: " + entry.getValue() + "},");
					}
					out.println("{ label: \"Other\",  data: " + otherEmergencyMarketSum + "},");
				}
				%>
			];

			var standardPiechart = $("#standardPiechart");
			var extendedPiechart = $("#extendedPiechart");
			
			$.plot(standardPiechart, standardData, {
				series: {
					pie: {
						innerRadius: 0.5,
						show: true
					}
				}
			});
			
			$.plot(extendedPiechart, extendedData, {
				series: {
					pie: {
						innerRadius: 0.5,
						show: true
					}
				}
			});

		});
		</script>
		<script type="text/javascript">
			function selectTableToShow(elementId1, elementId2, selectId) {
				document.getElementById(elementId1).style.display = selectId.value == 1 ? 'table' : 'none'; 
				document.getElementById(elementId2).style.display = selectId.value == 1 ? 'none' : 'table'; 
			}
		</script>
	</head>
	<body>
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
			 <tfoot>
				 <tr>
				 	<td>Sum of all assets:</td>
				 	<td colspan="3" align="right"> <%=Unifier.doubleToMoney(investmentPortfolio.getSum())%></td>
				 </tr>
			 </tfoot>
		</table>
		
		<br>
		
		Diversification you have:
		
		<select id="selection" onchange="selectTableToShow('standardTable', 'extendedTable', this); selectTableToShow('standardPiechart', 'extendedPiechart', this)">
			<option value="1">Standard view</option>
			<option value="2">Extended view</option>
		</select>

		<table id="standardTable">
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

		<table id="extendedTable">
			<tr>
				<th>Country</th>
				<th>Share, ₽</th>
				<th>Share, %</th>
			</tr>
			<% 
			if (!USAmap.isEmpty()) {
			%>
				<tr>
					<td>USA</td>
					<td align="right"><%=Unifier.doubleToMoney(USAmap.get(Country.USA))%></td>
					<td align="right"><%=Unifier.doubleToMoney(USAmap.get(Country.USA) / investmentPortfolio.getSum() * 100)%></td>
				</tr>
			<%
			}
			if (!developedMarketMap.isEmpty()) {%>
				<tr>	
					<th colspan="3">Developed Market</th>
				</tr>
				<%
				for (Map.Entry<Country, Double> entry : developedMarketMap.entrySet()) {
					%>
					<tr>
						<td><%=entry.getKey()%></td>
						<td align="right"><%=Unifier.doubleToMoney(entry.getValue())%></td>
						<td align="right"><%=Unifier.doubleToMoney(entry.getValue() / investmentPortfolio.getSum() * 100)%></td>
					</tr>
					<%
				}
				%>
				<tr>
					<td><strong>Total share:</strong></td>
					<td align="right"><strong><%=Unifier.doubleToMoney(developedMarketSum)%></strong></td>
					<td align="right"><strong><%=Unifier.doubleToMoney(developedMarketSum / investmentPortfolio.getSum() * 100)%></strong></td>
				</tr>
			<%
			}
			if (!emergencyMarketMap.isEmpty()) {%>
					<tr>	
					<th colspan="3" align="center">Emergency Market</th>
				</tr>
				<%
				for (Map.Entry<Country, Double> entry : emergencyMarketMap.entrySet()) {
					%>
					<tr>
						<td><%=entry.getKey()%></td>
						<td align="right"><%=Unifier.doubleToMoney(entry.getValue())%></td>
						<td align="right"><%=Unifier.doubleToMoney(entry.getValue() / investmentPortfolio.getSum() * 100)%></td>
					</tr>
					<%
				}
				%>
				<tr>
					<td><strong>Total share:</strong></td>
					<td align="right"><strong><%=Unifier.doubleToMoney(emergencyMarketSum)%></strong></td>
					<td align="right"><strong><%=Unifier.doubleToMoney(emergencyMarketSum / investmentPortfolio.getSum() * 100)%></strong></td>
				</tr>
			<%
			}
			%>
		</table>
		<br>
		<div id="standardPiechart" class="piechart"></div>
		<div id="extendedPiechart" class="piechart"></div>
		<br>
		
		<form action="http://localhost:8080/diversification-web-project/">
			<input type="submit" value="Back" class="submit">
		</form>
	</body>
</html>