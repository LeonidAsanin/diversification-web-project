package servlets;

import database.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.concurrent.*;

/**
 * Initial servlet runs scheduled stock price updating and updating information about country shares.
 * The interval between updates is 1 minute.
 * 
 * @author lennardjones
 * @version 1.2
 * @since 1.0
 */
public class InitialServlet extends HttpServlet {
	private static final long serialVersionUID = 4992357038668964371L;

	@Override
	public void init() throws ServletException {
		var executor = Executors.newScheduledThreadPool(2);
		executor.scheduleAtFixedRate(() -> StockPrices.updateAllAsSeparateThread(), 0, 1, TimeUnit.MINUTES);
		executor.scheduleAtFixedRate(() -> CountryShares.getAllValuesFromDatabase(), 0, 1, TimeUnit.MINUTES);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("index.jsp");
	}
}
