package servlets;

import database.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serial;
import java.util.concurrent.*;

/**
 * Initial servlet runs scheduled stock price updating and updating information about country shares.
 * The interval between updates is 1 minute.
 * 
 * @author lennardjones
 * @version 1.3
 * @since 1.0
 */
public class InitialServlet extends HttpServlet {
	@Serial
	private static final long serialVersionUID = 4992357038668964371L;

	private static ScheduledExecutorService executor;

	@Override
	public void init() {
		executor = Executors.newScheduledThreadPool(2);
		executor.scheduleAtFixedRate(StockPrices::updateAllAsSeparateThread, 0, 1, TimeUnit.MINUTES);
		executor.scheduleAtFixedRate(CountryShares::getAllValuesFromDatabase, 0, 1, TimeUnit.MINUTES);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.sendRedirect("index.jsp");
	}

	@Override
	public void destroy() {
		executor.shutdown();
	}
}
