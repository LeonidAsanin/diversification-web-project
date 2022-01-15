package servlets;

import java.io.IOException;
import java.util.Optional;

import assets.FinExTicker;
import assets.Ticker;
import assets.VTBTicker;
import database.StockQuantity;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet to process and calculate country diversification of user's investment portfolio.
 * 
 * @author lennardjones
 * @version 1.3
 * @since 1.0
 */
public class ProcessingServlet extends HttpServlet {
	private static final long serialVersionUID = -3515658310223927361L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		var stockQuantity = new StockQuantity();
		
		for (Ticker ticker : FinExTicker.values()) {
			var optionalQuantity = tryToGetStockQuantity(request, response, ticker);
			
			if (optionalQuantity.isEmpty())
				return;
			
			optionalQuantity.ifPresent((quantity) -> {
				stockQuantity.put(ticker, quantity);
				var cookie = new Cookie(ticker.toString(), String.valueOf(quantity)); 
				response.addCookie(cookie);
			});
		}
		
		for (Ticker ticker : VTBTicker.values()) {
			var optionalQuantity = tryToGetStockQuantity(request, response, ticker);
			
			if (optionalQuantity.isEmpty())
				return;
			
			optionalQuantity.ifPresent((quantity) -> {
				stockQuantity.put(ticker, quantity);
				var cookie = new Cookie(ticker.toString(), String.valueOf(quantity)); 
				response.addCookie(cookie);
			});
		}
		
		request.setAttribute("StockQuantity", stockQuantity);
		request.getRequestDispatcher("result-page.jsp").forward(request, response);
	}
	
	/**
	 * Tries to get stock quantity by Ticker (which user entered) from HttpRequest.
	 * Gives an error response if the entered data is incorrect.
	 * 
	 * @param request To get entered data by user 
	 * @param response To dispatch error message if data is incorrect
	 * @param ticker 
	 * @return Optional<Integer> which may contain desired value
	 * @throws IOException 
	 */
	private Optional<Integer> tryToGetStockQuantity(HttpServletRequest request, HttpServletResponse response, Ticker ticker) throws IOException {
		Integer quantity = null;
		var out = response.getWriter();
		
		try {
			var parameter = request.getParameter(ticker.toString());
			if (parameter.equals("")) parameter = "0";
			quantity = Integer.parseInt(parameter);
			if (quantity < 0) {
				out.println("<strong>Error while entering ticker's quantity!");
				out.println("<br>Please, enter a positive numbers</strong>");
				out.println("<p>");
					out.println("<strong><a href=\"http://localhost:8080/diversification-web-project/\">Back</a></strong>");
				out.println("</p>");
				out.close();
            }
		} catch (NumberFormatException e) {
			out.println("<strong>Error while entering ticker's quantity!<strong>");
			out.println("<br>Please, enter a proper integer numbers</strong>");
			out.println("<p>");
				out.println("<strong><a href=\"http://localhost:8080/diversification-web-project/\">Back</a></strong>");
			out.println("</p>");
			out.close();
		}
		
		return Optional.of(quantity);
	}
}
