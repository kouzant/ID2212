package gr.kzps.id2212.project.client.classloader;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import gr.kzps.id2212.project.client.query.QueryPlan;

/**
 * Custom class loader for dynamic loading of query plans
 * @author Antonis Kouzoupis
 *
 */
public class QueryPlanClassLoader {
	private final ClassLoader classLoader;
	
	/**
	 * @param classLoader A classloader
	 */
	public QueryPlanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
	
	/**
	 * Load a query plan class
	 * @param className The class name of the user query plan
	 * @return The query plan class of the user
	 */
	public QueryPlan loadPlan(String className) {
		try {
			Class<?> queryClass = classLoader.loadClass(className);
			Constructor<?> constructor = queryClass.getConstructor();
			
			QueryPlan queryPlan = (QueryPlan) constructor.newInstance();
			
			return queryPlan;
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException
				| InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
