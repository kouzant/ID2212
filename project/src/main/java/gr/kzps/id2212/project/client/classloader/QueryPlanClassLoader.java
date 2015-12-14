package gr.kzps.id2212.project.client.classloader;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import gr.kzps.id2212.project.client.query.QueryPlan;

public class QueryPlanClassLoader {
	private final ClassLoader classLoader;
	
	public QueryPlanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
	
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
