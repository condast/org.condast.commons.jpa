package org.condast.commons.jpa.na.questionaire;

import org.condast.commons.strings.StringStyler;

public interface IQuestion {

	public enum Categories{
		PARTICIPANT,
		GUEST,
		HOME,
		FOOD;
	}

	public enum YesNo{
		YES,
		NO;
	}

	public enum Types{
		INTAKE(0),
		UNKNOWN(999),
		FIRST_NAME(1),
		PREFIX(2),
		SURNAME(3),
		GENDER(4),
		STREET(5),
		HOUSE_NUMBER(6),
		POSTCODE(7),
		TOWN(8),
		COMMUNITY(9),
		TELEPHONE(10),
		EMAIL(11),
		BIRTH_DATE(12),
		FORMER_VOCATION(13),
		VOCATION(14),
		IS_STUDENT(15),
		ETHNIC_BACKGROUND(16),
		HOBBIES_AND_INTERESTS(17),//of the main person
		PARTNER_FIRSTNAME(18),
		PARTNER_PREFIX(19),
		PARTNER_SURNAME(20),
		PARTNER_FORMER_VOCATION(21),
		PARTNER_VOCATION(22),
		PARTNER_IS_STUDENT(23),
		PARTNER_ETHNIC_BACKGROUND(24),
		PARTNER_HOBBY(25),
		//PARTNER_SMOKES(),
		HOST_FAMILY(26),
		HOME_PETS_DESCRIPTION(27),
		HOUSE_IMPAIRMENT(28),
		HOME_SMOKING(29),
		HOME_SMOKING_INSIDE(30),
		HOME_SMOKING_OUTSIDE(31),
		GUEST_PETS_PROBLEM(32),
		GUEST_ZIMMER(33),
		GUEST_WHEELCHAIR(34),
		GUEST_IMPAIRMENT(35),
		GUEST_SMOKING_INSIDE(36),
		GUEST_SMOKING_OUTSIDE(37),

		HOST_MEAL_FISH(38),
		HOST_MEAL_MEAT(39),
		HOST_MEAL_VEGETARIAN(40),
		HOST_MEAL_VEGAN(41),
		HOST_MEAL_HALAL(42),
		HOST_MEAL_KOSHER(43),
		SPECIAL_DIET_LOW_SODIUM(44),
		SPECIAL_DIET_SUGAR_FREE(45),
		SPECIAL_DIET_GLUTEN_FREE(46),
		SPECIAL_DIET_LACTOSE_INTOLERANT(47),
		HOST_ALCOHOL(48),
		LUNCH_OR_DINNER(49),

		GUEST_MEAL_FISH(50),
		GUEST_MEAL_MEAT(51),
		GUEST_MEAL_VEGETARIAN(52),
		GUEST_MEAL_VEGAN(53),
		GUEST_MEAL_HALAL(54),
		GUEST_MEAL_KOSHER(55),
		DIET_SPECIFIC_TYPE(56),
		DIET_DONT_EAT(57),
		GUEST_ALCOHOL(58),

		CONTRIBUTION(60),//was HOST_CONTRIBUTION(60)
		HOST_COMPOSITION(61),
		MINIMUM_AGE(62),
		MAXIMUM_AGE(63),
		MY_GUEST_IS(64),
		HOST_SLIGHTLY_COGNITIVELY_IMPAIRED(65),
		HOST_HARD_OF_HEARING(66),
		//HOST_HARD_OF_SEEING // this one is missing
		HOST_SLIGHTLY_PHYSICAL_IMPAIRED(67),//was HOST_LESS_MOBILE
		HOST_MULTIPLE_GUESTS(68),
		HOST_VICINITY_GUEST(69),
		HOST_LOCALITY_GUEST(70),
		HOST_TOWN_GUEST(71),
		HOST_PARKING_VICINITY(72),
		HOST_CAN_YOU_COLLECT(73),
		HOST_TIMES_PER_MONTH(74),
		PREFERRED_DAY(75),

		INTRODUCTION(76),//was ACQUAINTANCE_VISIT
		GUEST_SLIGHTLY_COGNITIVELY_IMPAIRED(77),
		//GUEST_SLIGHTLY_PHYSICAL_IMPAIRED //this one is missing
		GUEST_HARD_OF_HEARING(78),
		GUEST_IMPAIRED_VISION(79),
		GUEST_OTHER_LIMITATIONS(80),

		GUEST_REQUIRE_TRANSPORT(81),
		GUEST_TIMES_PER_MONTH(82),
		REQUEST_CONTACT(83),
		INFO_MARKETING(84),
		INFO_JOURNALIST(85),
		REMARKS_PUBLIC(86),
		NOTES_PRIVATE(87),
		GUEST_VICINITY(88),
		GUEST_LOCALITY(89),
		GUEST_TOWN(90),
		ALLERGY(92),
		ALLERGY_DESCRIPTION(93),
		INFO_AGREEMENT(94),
		HOST_BUILDING(95),
		HOST_FLIGHT(96),
		PARTNER_GENDER(195),
		PARTNER_BIRTH_DATE(201);

		private int id = 0;

		private Types( int id ){
			this.id = id;
		}

		private int getId() {
			return id;
		}

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}

		public static Types getType( int index ) {
			for( Types type: values() ){
				if( type.getId() == index )
					return type;
			}
			return Types.UNKNOWN;
		}

		/**
		 * Returns true if the question is mandatory
		 * @return
		 */
		public boolean isMandatory(){
			boolean result = false;
			switch( this ){
			//case ETHNIC_BACKGROUND:
			case POSTCODE:
			case FIRST_NAME:
			case TOWN:
			case EMAIL:
			case GENDER:
			case PARTNER_GENDER:
			case TELEPHONE:
			case BIRTH_DATE:
			case HOUSE_NUMBER:
			case STREET:
				result = true;
				break;
			default:
				break;
			}
			return result;
		}
	}

	public String getQuestionId();

	public Types getType();

	public String getQuestion();

	public String getAnswer();

	public Categories getCategory();

	public String toFlatString();

	@Override
	public String toString();
}