package org.condast.commons.persistence.clone;

import java.lang.reflect.Constructor;


public class CloneUtils<C,A extends Object> {

	@SuppressWarnings("unchecked")
	public C createObject( Class<?> clss, String className){
		Class<C> builderClass;
		C builder = null;
		try {
			builderClass = (Class<C>) clss.getClassLoader().loadClass( className );
			Constructor<?> constructor = builderClass.getConstructor();
			builder = (C) constructor.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return builder;
	}

	@SuppressWarnings("unchecked")
	public C createObject( Class<?> clss, String className, A argument){
		Class<C> builderClass;
		C builder = null;
		try {
			builderClass = (Class<C>) clss.getClassLoader().loadClass( className );
			Constructor<?> constructor = builderClass.getConstructor();
			builder = (C) constructor.newInstance( argument );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return builder;
	}

}
