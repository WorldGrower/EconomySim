/*******************************************************************************
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package knowledge;

import java.util.Collections;
import java.util.List;

public enum Knowledge {
	// Stone Age
	
	// Paleolithic
	STONE_TOOLING(16, "stone tooling", "Stone tools are any tools made either partially or entirely out of stone"),
	CONTROL_OF_FIRE(64, "control of fire", "Control of fire includes: maintaining fire over extended periods of time, the ability to make fire, using fire as a source of warmth and lighting"),
	LANGUAGE(64, "language", "Language is a structured system of communication and is the primary means by which humans convey meaning"),
	POTTERY(64, CONTROL_OF_FIRE, "pottery", "Pottery is the process and the products of forming vessels and other objects with clay and other raw materials, which are fired at high temperatures to give them a hard and durable form"),
	CLOTHING(32, "clothing", "Clothing is any item worn on the body and protects the body from the environment."),
	WEAVING(48, CLOTHING, "weaving", "Weaving is a method of textile production in which two distinct sets of yarns or threads are interlaced at right angles to form a fabric or cloth. Other methods are knitting, crocheting, felting, and braiding or plaiting"),
	COOKING(64, CONTROL_OF_FIRE, "cooking", "Cooking is the art, science and craft of using heat to make food more palatable, digestible, nutritious, or safe"),
	CELESTIAL_OBSERVATION(64, "celestial observation", "Celestial observation, which can be considered early astronomy, consisted of the observation and predictions of the motions of celestial objects visible to the naked eye"),
	DOMESTICATION_OF_DOG(48, "domestication of dog", "The domestication of the dog was the process which created the domestic dog. This included the dog's genetic divergence from the wolf, its domestication, and the emergence of the first dogs"),
	
	// what about wood resources?
	//TODO: FARMING --> CROP_FARMING, gather food --> gather food/edible plants (PLANT_FOOD)
	//TODO: GRAIN_PROCESSING in Mesolithic, COOKING get COOKED_PLANT_FOOD
	//TODO: Mesolithic gets PERMANENT_SETTLEMENTS? (why	
	
	// mathematics:  arithmetic, algebra and geometry for purposes of taxation, commerce, trade and also in the patterns in nature, the field of astronomy and to record time and formulate calendars. 
	
	// Mesolithic
	TRADE(80, LANGUAGE, "trade", "Trade involves the transfer of goods and services from one person or entity to another, often in exchange for money"),
	// land management. Swamps and wetlands were purposely burned, chipped and ground stone axes were used to cut down trees for fires, and for constructing living quarters and fishing vessels.
	// campsites & basecamp
	// domesticate dog, sheep, goats, pigs, and cattle.
	// microlithons (gr. mikros – small, lithos – stone) i.e. a product made of stone whose length ranged from 1 to 2 cm, in the form of a prism, knife or a sharp spike, which were used as inserts in the wooden or bone handrails. These products are used for making tools that necessary for breaking, cutting, chopping or scraping, as well as an obligatory bow and arrow. 
	// grain processing (flour, bread) -- see https://dawnofman.fandom.com/wiki/Grain_Processing

	DOMESTICATION_OF_CATTLE(112, DOMESTICATION_OF_DOG, "domestication of cattle", "The domestication of animals is the mutual relationship between non-human animals and the humans who have influence on their care and reproduction"),
	CALENDAR(112, CELESTIAL_OBSERVATION, "calendar", "A calendar is a system of organizing days. This is done by giving names to periods of time, typically days, weeks, months and years"),
	
	//Neolithic
	PROTO_WRITING(128, TRADE, "proto-writing", "Proto-writing consists of visible marks communicating limited information. It uses symbols to represent a limited number of concepts, in contrast to true writing systems, which record the language of the writer."),
	//TODO: used for property and accounting purposes: how to use in-game? limit on how many items one could possess, coded in assets
	FARMING(128, COOKING, "farming", "Farming is the cultivation of plants and animals to provide useful products"),
	LEAD_SMELTING(128, CONTROL_OF_FIRE, "lead smelting", "Smelting is a process of applying heat to an ore, to extract a base metal"),
	KILN(128, POTTERY, "kiln", "A kiln is a thermally insulated chamber, a type of oven, that produces temperatures sufficient to complete some process, such as hardening, drying, or chemical changes"),
	IRRIGATION(128, FARMING, "irrigation", "Irrigation is the practice of applying controlled amounts of water to land to help grow crops"),
	//centralized governments: water management
	// pictograms, ideograms
	// Cattle were probably the first object or physical thing specifically used in a way similar enough to the modern definition of money, that is, as a medium for exchange.[10]
	// first steps in division of labor
	DIVISION_OF_LABOR(128, FARMING, TRADE, "division of labor", "The division of labour is the separation of the tasks in any economic system or organisation so that participants may specialize"),
	
	CENTRALIZED_GOVERNMENT(160, LANGUAGE, IRRIGATION, "centralized government", "A government is the system or group of people governing an organized community, generally a state"),
	
	//TODO: FARMING/TRADE --> DIVISION_OF_LABOR: unlocks profession, aka specialized behaviour script, adds profession field to user interface, profession field to person details
	// LANGUAGE --> SOCIAL_ORGANIZATION: unlocks create organization
	// SOCIAL_ORGANIZATION --> RULE_OF_LAW
	// ARITHMETIC adds taxes to organization
	// IRRIGATION add water management
	//TODO: add specialization/experience bonus for activity
	
	//Bronze Age
	WRITING(256, PROTO_WRITING, "writing", "Writing is a cognitive and social activity involving neuropsychological and physical processes and the use of writing systems to structure and translate human thoughts into persistent representations of human language"),
	//TODO: removes the limits on ownership totally, ability to trade land, sharing knowledge
	//TODO: add special effects of techs in tech window, and tooltip texts for actions (Calendar --> effect on productivity) 
	ARITHMETIC(256, CALENDAR, PROTO_WRITING, "arithmetic", "Arithmetic is an elementary part of mathematics that consists of the study of the properties of the traditional operations on numbers—addition, subtraction, multiplication, division, exponentiation, and extraction of roots"),
	// TODO: GEOMETRY (depends on irrigation/construction)
	//TODO: needed for taxes (for other things like trade or calendar, basic counting is enough)
	//geometry: comes from surveying: https://www.britannica.com/science/geometry
	PLOUGHING(256, FARMING, "ploughing", "Ploughing or tilling is the process of breaking, loosening the soil, and turning it over for uprooting weeds and aerating the soil"),
	// HORSE_DOMESTICATION (able to breed horses, improves agricultural yield, takes long time to perform)
	RULE_OF_LAW(256, CENTRALIZED_GOVERNMENT, WRITING, "rule of law", "The rule of law is a political ideal that all citizens and institutions within a country, state, or community are accountable to the same laws, including lawmakers and leaders"),
	
	// BRONZE_SMELTING (create BRONZE_PLOUGH, upgrade from WOODEN_PLOUGH), 
	// THE_WHEEL (improve transportation & agricultural work)
	// WRITING (needed for trade & many government functions like legal systems, census records, contracts, deeds of ownership, taxation, trade agreements, treaties.
	//          calendric and political necessities
	//          additional capacity for the limitations of human memory[24] (e.g., to-do lists, recipes, reminders, logbooks, maps, the proper sequence for a complicated task or important ritual), dissemination of ideas and coordination (as in an essay, monograph, broadside, plans, (code) issues, petition, or manifesto), 
	
	// Iron Age
	// IRON_SMELTING
	// The main form of income in the Iron Age was farming. This is what most people did as a job. The other main jobs in these times were potters, carpenters and metalworkers
	
	//TODO: add more info to tech tree (research cost, allowed actions, description)
	;
	
	public static final Knowledge[] VALUES = values();
	public static final Knowledge NONE = null;
	
	private final int hoursRequiredToMaster;
	private final Knowledge prerequisiteKnowledge;
	private final Knowledge prerequisiteKnowledge2;
	private final String name;
	private final String description;

	private Knowledge(int hoursRequiredToMaster, String name, String description) {
		this(hoursRequiredToMaster, null, name, description);
	}
	
	private Knowledge(int hoursRequiredToMaster, Knowledge prerequisiteKnowledge, String name, String description) {
		this(hoursRequiredToMaster, prerequisiteKnowledge, null, name, description);
	}
	
	private Knowledge(int hoursRequiredToMaster, Knowledge prerequisiteKnowledge, Knowledge prerequisiteKnowledge2, String name, String description) {
		this.hoursRequiredToMaster = hoursRequiredToMaster;
		this.prerequisiteKnowledge = prerequisiteKnowledge;
		this.prerequisiteKnowledge2 = prerequisiteKnowledge2;
		this.name = name;
		this.description = description;
	}
	
	public int getHoursRequiredToMaster() {
		return hoursRequiredToMaster;
	}

	public Knowledge getPrerequisiteKnowledge() {
		return prerequisiteKnowledge;
	}

	public Knowledge getPrerequisiteKnowledge2() {
		return prerequisiteKnowledge2;
	}

	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return getName();
	}
	
	public int getPrerequisiteCount() {
		return getPrerequisiteCount(this);
	}
	
	private int getPrerequisiteCount(Knowledge knowledge) {
		if (knowledge != null) {
			final int prerequisiteCount;
			if (knowledge.getPrerequisiteKnowledge() != null) {
				prerequisiteCount = getPrerequisiteCount(knowledge.getPrerequisiteKnowledge()) + 1;
			} else {
				prerequisiteCount = 0;
			}
			final int prerequisiteCount2;
			if (knowledge.getPrerequisiteKnowledge2() != null) {
				prerequisiteCount2 = getPrerequisiteCount(knowledge.getPrerequisiteKnowledge2()) + 1;
			} else {
				prerequisiteCount2 = 0;
			}
		
			return Math.max(prerequisiteCount, prerequisiteCount2);
		} else {
			return 0;
		}
	}

	public static void sortOnHoursToMaster(List<Knowledge> KnowledgeList) {
		Collections.sort(KnowledgeList, (o1, o2) -> o1.getHoursRequiredToMaster() - o2.getHoursRequiredToMaster());
	}
	
	public static void sortOnName(List<Knowledge> KnowledgeList) {
		Collections.sort(KnowledgeList, (o1, o2) -> o1.getName().compareTo(o2.getName()));
	}
}
