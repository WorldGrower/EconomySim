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
import action.InfrastructurePersonActions;
import action.OrganizationArgs;
import action.PersonAction;
import action.PersonActionArgs;
import action.PersonActionFactory;
import action.TradeArgs;
import asset.Asset;
import asset.AssetType;
import asset.LocatableAsset;
import asset.OperationalAsset;
import environment.PublicLocations;
import knowledge.Knowledge;
import society.PersonFinder;
import society.PublicAssets;
import society.PublicKnowledge;
import society.PublicOrganizations;

class WheatFarmingBehaviour implements PersonBehaviour, Serializable {
	
	private final PersonFinder personFinder;
	private final PublicOrganizations publicOrganizations;
	private final PublicAssets publicAssets;
	private final PublicKnowledge publicKnowledge;
	private final PublicLocations publicLocations;
	private final AssetExtractionBehaviour assetExtractionBehaviour;
	
	public WheatFarmingBehaviour(PersonFinder personFinder, PublicOrganizations publicOrganizations, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicLocations publicLocations) {
		super();
		this.personFinder = personFinder;
		this.publicOrganizations = publicOrganizations;
		this.publicAssets = publicAssets;
		this.publicKnowledge = publicKnowledge;
		this.publicLocations = publicLocations;
		this.assetExtractionBehaviour = new AssetExtractionBehaviour(personFinder, publicOrganizations, publicAssets, publicKnowledge, publicLocations);
	}
	
	@Override
	public void processActions(Person person, PersonDecisions personDecisions, ProfessionStatistics professionStatistics) {
		extractAsset(person, AssetType.WHEAT, Activity.GATHER_WHEAT, PersonNeeds.DEFAULT_PRODUCTION);
		extractAsset(person, AssetType.WHEAT_FIELD, Activity.PLANT_WHEAT, person.getFamilyAssetQuantityFor(AssetType.LAND));
		extractAsset(person, AssetType.WHEAT, Activity.HARVEST_WHEAT, PersonNeeds.DEFAULT_PRODUCTION);
		extractAsset(person, AssetType.FLOUR, Activity.CREATE_FLOUR, PersonNeeds.DEFAULT_PRODUCTION);
		extractAsset(person, AssetType.BREAD, Activity.CREATE_BREAD, PersonNeeds.DEFAULT_PRODUCTION);
		extractPublicAsset(person, AssetType.IRRIGATION_CANAL, Activity.CREATE_IRRIGATION_CANAL, 1);
		person.learnKnowledgeIfNeeded(Knowledge.IRRIGATION, personFinder, publicAssets, publicKnowledge, publicOrganizations);
		buildIrrigationCanal(person);
		maintainIrrigationCanal(person);
		extractAsset(person, AssetType.PLOUGH, Activity.CREATE_PLOUGH, 1);
		irrigateWheatFields(person);
		
	}

	private void buildIrrigationCanal(Person person) {
		OperationalAsset irrigationCanal = (OperationalAsset) publicAssets.get(AssetType.IRRIGATION_CANAL);
		if (irrigationCanal != null && !irrigationCanal.getOperationalAssetAttribute(0).isFinished()) {
			PersonAction personAction = InfrastructurePersonActions.BUILD_IRRIGATION_CANAL_PERSON_ACTION;
			if (personAction.canPerform(person, person.getTimeRemaining(), publicAssets, publicOrganizations)) {
				personAction.perform(person, person.getTimeRemaining(), PersonActionArgs.NONE, publicAssets, publicKnowledge, publicOrganizations);
			}
		}
	}
	
	private void maintainIrrigationCanal(Person person) {
		OperationalAsset irrigationCanal = (OperationalAsset) publicAssets.get(AssetType.IRRIGATION_CANAL);
		if (irrigationCanal != null && irrigationCanal.getOperationalAssetAttribute(0).isFinished()) {
			PersonAction personAction = InfrastructurePersonActions.MAINTAIN_IRRIGATION_CANAL_PERSON_ACTION;
			if (personAction.canPerform(person, person.getTimeRemaining(), publicAssets, publicOrganizations)) {
				personAction.perform(person, person.getTimeRemaining(), PersonActionArgs.NONE, publicAssets, publicKnowledge, publicOrganizations);
			}
		}
	}

	private void irrigateWheatFields(Person person) {
		OperationalAsset irrigationCanal = (OperationalAsset) publicAssets.get(AssetType.IRRIGATION_CANAL);
		LocatableAsset wheatFields = (LocatableAsset) person.getAssets().get(AssetType.WHEAT_FIELD);
		
		if (irrigationCanal != null && wheatFields != null) {
			if (irrigationCanal.getOperationalAssetAttribute(0).isOperational() && wheatFields.getQuantity() > 0) {
				PersonAction personAction = InfrastructurePersonActions.IRRIGATE_WHEAT_FIELDS_CANAL_PERSON_ACTION;
				if (personAction.canPerform(person, person.getTimeRemaining(), publicAssets, publicOrganizations)) {
					personAction.perform(person, person.getTimeRemaining(), PersonActionArgs.NONE, publicAssets, publicKnowledge, publicOrganizations);
				}
			}
		}
	}
	
	private void extractAsset(Person person, AssetType neededAssetType, Activity activity, int quantityNeeded) {
		assetExtractionBehaviour.extractAsset(person, neededAssetType, activity, quantityNeeded);
	}
	
	private void extractPublicAsset(Person person, AssetType neededAssetType, Activity activity, int quantityNeeded) {
		assetExtractionBehaviour.extractPublicAsset(person, neededAssetType, activity, quantityNeeded);
	}
}
