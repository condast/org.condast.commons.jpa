package org.condast.commons.jpa.na.questionaire;


import org.condast.commons.jpa.na.questionaire.IQuestion.Types;

public interface IQuestionnaire{

	public static final String NONE = "geen";
	public static final String NIKS = "niks";

	public enum YesNo{
		YES,
		NO;
	}

	String getId();

	String getName();

	IQuestion[] getQuestions();

	@Override
	String toString();

	IQuestion getQuestion(Types type);

	String getAnswer(Types type);

	public boolean isHost();

	/**
	 * returns true when the question retus a TRUE or FALSE answer
	 * @param question
	 * @return
	 */
	boolean isChoice( IQuestion question );

	boolean isOptional(IQuestion question);

	/**
	 * Return true if the question is undefined
	 * @param question
	 * @return
	 */
	boolean isUndefined(IQuestion.Types type );

}