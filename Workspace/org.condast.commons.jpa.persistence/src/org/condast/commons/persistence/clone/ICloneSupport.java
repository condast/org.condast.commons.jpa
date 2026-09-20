package org.condast.commons.persistence.clone;

public interface ICloneSupport<T extends Object> extends Cloneable {

	/**
	 * clone the given object
	 * @return
	 * @throws CloneNotSupportedException
	 */
	public T clone() throws CloneNotSupportedException;
}
