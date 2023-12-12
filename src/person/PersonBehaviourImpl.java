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
package person;

import java.io.Serializable;

import action.Activity;
import asset.AssetType;
import environment.PublicLocations;
import knowledge.Knowledge;
import society.PersonFinder;
import society.PublicAssets;
import society.PublicKnowledge;
import society.PublicOrganizations;
import society.Society;

public class PersonBehaviourImpl implements PersonBehaviour, Serializable {

	private final PersonFinder personFinder;
	private final PublicOrganizations publicOrganizations;
	private final PublicAssets publicAssets;
	private final PublicKnowledge publicKnowledge;
	private final PublicLocations publicLocations;
	
	private final PersonalRelationsBehaviour personalRelationsBehaviour;
	private final SocialObligationsBehaviour socialObligationsBehaviour;
	private final AssetExtractionBehaviour assetExtractionBehaviour;
	private final TradeBehaviour tradeBehaviour;
	private final StealBehaviour stealBehaviour;
	private final DomesticationBehaviour domesticationBehaviour;
	private final CalendarSystemBehaviour calendarSystemBehaviour;
	//private final WheatFarmingBehaviour wheatFarmingBehaviour;
	private final WritingSystemBehaviour writingSystemBehaviour;
	private final GovernmentBehaviour governmentBehaviour;
	
	public PersonBehaviourImpl(Society society, PublicLocations publicLocations) {
		this(society, society, society, society, publicLocations);
	}
	
	public PersonBehaviourImpl(PersonFinder personFinder, PublicOrganizations publicOrganizations, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicLocations publicLocations) {
		super();
		this.personFinder = personFinder;
		this.publicOrganizations = publicOrganizations;
		this.publicAssets = publicAssets;
		this.publicKnowledge = publicKnowledge;
		this.publicLocations = publicLocations;
		this.personalRelationsBehaviour = new PersonalRelationsBehaviour(personFinder, publicOrganizations, publicAssets, publicKnowledge, publicLocations);
		this.socialObligationsBehaviour = new SocialObligationsBehaviour(personFinder, publicOrganizations, publicAssets, publicKnowledge, publicLocations);
		this.assetExtractionBehaviour = new AssetExtractionBehaviour(personFinder, publicOrganizations, publicAssets, publicKnowledge, publicLocations);
		this.tradeBehaviour = new TradeBehaviour(personFinder, publicOrganizations, publicAssets, publicKnowledge, publicLocations);
		this.stealBehaviour = new StealBehaviour(personFinder, publicOrganizations, publicAssets, publicKnowledge, publicLocations);
		this.domesticationBehaviour = new DomesticationBehaviour(personFinder, publicOrganizations, publicAssets, publicKnowledge, publicLocations);
		this.calendarSystemBehaviour = new CalendarSystemBehaviour(personFinder, publicOrganizations, publicAssets, publicKnowledge, publicLocations);
		//this.wheatFarmingBehaviour = new WheatFarmingBehaviour(personFinder, publicOrganizations, publicAssets, publicKnowledge, publicLocations);
		this.writingSystemBehaviour = new WritingSystemBehaviour(personFinder, publicOrganizations, publicAssets, publicKnowledge, publicLocations);
		this.governmentBehaviour = new GovernmentBehaviour(personFinder, publicOrganizations, publicAssets, publicKnowledge, publicLocations);
	}

	@Override
	public void processActions(Person person, PersonDecisions personDecisions, ProfessionStatistics professionStatistics) {
		personalRelationsBehaviour.processActions(person, personDecisions, professionStatistics);
		socialObligationsBehaviour.processActions(person, personDecisions, professionStatistics);
		person.chooseProfession(publicKnowledge, professionStatistics);
		
		extractAsset(person, AssetType.STONE_SICKLE, Activity.CREATE_STONE_SICKLE, 1);
		extractAsset(person, AssetType.FOOD, Activity.GATHER_FOOD, PersonNeeds.DEFAULT_PRODUCTION);
		extractAsset(person, AssetType.WATER, Activity.GATHER_WATER, PersonNeeds.DEFAULT_PRODUCTION);			
		tradeBehaviour.processActions(person, personDecisions, professionStatistics);
		stealBehaviour.processActions(person, personDecisions, professionStatistics);
		
		if (!person.isLightedAtNight()) {
			extractAsset(person, AssetType.CAMP_FIRE, Activity.CREATE_FIRE, 1);
		}
		//extractAsset(person, AssetType.CLAY_POT, Activity.CREATE_CLAY_POT, AssetStorage.calculateRequiredStorage(person.getAssets(), AssetTrait.PERISHABLE));
		
		domesticationBehaviour.processActionsForDog(person, personDecisions);
		calendarSystemBehaviour.processActions(person, personDecisions, professionStatistics);
		
		person.learnLearnableKnowledge(person, personFinder, publicOrganizations, publicAssets, publicKnowledge, publicLocations, personDecisions);
		person.learnKnowledgeIfNeeded(Knowledge.DIVISION_OF_LABOR, personFinder, publicAssets, publicKnowledge, publicOrganizations);
		
		//extractAsset(person, AssetType.ANIMAL_HIDE_CLOTHES, Activity.CREATE_ANIMAL_HIDE_CLOTHING, 1);
		//extractAsset(person, AssetType.COTTON_CLOTHES, Activity.CREATE_COTTON_CLOTHING, 1);
		//wheatFarmingBehaviour.processActions(person, personDecisions);
		person.getProfession().getPersonBehaviour(personFinder, publicOrganizations, publicAssets, publicKnowledge, publicLocations).processActions(person, personDecisions, professionStatistics);
		
		//extractAsset(person, AssetType.KILN, Activity.CREATE_KILN, 1);
		//extractAsset(person, AssetType.GLAZED_POT, Activity.CREATE_GLAZED_POT, AssetStorage.calculateRequiredStorage(person.getAssets(), AssetTrait.LIQUID));

		//domesticationBehaviour.processActionsForCattle(person, personDecisions);
		writingSystemBehaviour.processActions(person, personDecisions, professionStatistics);
		
		governmentBehaviour.processActions(person, personDecisions, professionStatistics);
	}
	
	private void extractAsset(Person person, AssetType neededAssetType, Activity activity, int quantityNeeded) {
		assetExtractionBehaviour.extractAsset(person, neededAssetType, activity, quantityNeeded);
	}
}
