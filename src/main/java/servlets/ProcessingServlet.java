package servlets;

import java.io.IOException;
import java.io.Serial;
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
	@Serial
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
	 * Tries to get stock quantity by ticker.
	 * Gives an error response if the entered data is incorrect.
	 * 
	 * @param request To get entered data by user 
	 * @param response To dispatch error message if data is incorrect
	 * @param ticker Ticker (which user entered) from HttpRequest
	 * @return Optional<Integer> which may contain desired value
	 */
	private Optional<Integer> tryToGetStockQuantity(HttpServletRequest request, HttpServletResponse response, Ticker ticker) {
		int quantity;

		try {
			var parameter = request.getParameter(ticker.toString());
			if (parameter == null || parameter.equals("")) parameter = "0";
			quantity = Integer.parseInt(parameter);
		} catch (NumberFormatException e) {
			quantity = 0;
		}
		
		return Optional.of(quantity);
	}
}
