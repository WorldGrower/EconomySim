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
import society.PersonFinder;
import society.PublicAssets;
import society.PublicKnowledge;
import society.PublicOrganizations;

class CreateClothingBehaviour implements PersonBehaviour, Serializable {
	
	private final PersonFinder personFinder;
	private final PublicOrganizations publicOrganizations;
	private final PublicAssets publicAssets;
	private final PublicKnowledge publicKnowledge;
	private final PublicLocations publicLocations;
	private final AssetExtractionBehaviour assetExtractionBehaviour;
	private final int quantityProduced;
	
	public CreateClothingBehaviour(PersonFinder personFinder, PublicOrganizations publicOrganizations, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicLocations publicLocations, int quantityProduced) {
		super();
		this.personFinder = personFinder;
		this.publicOrganizations = publicOrganizations;
		this.publicAssets = publicAssets;
		this.publicKnowledge = publicKnowledge;
		this.publicLocations = publicLocations;
		this.assetExtractionBehaviour = new AssetExtractionBehaviour(personFinder, publicOrganizations, publicAssets, publicKnowledge, publicLocations);
		this.quantityProduced = quantityProduced;
	}
	
	@Override
	public void processActions(Person person, PersonDecisions personDecisions, ProfessionStatistics professionStatistics) {
		extractAsset(person, AssetType.ANIMAL_HIDE_CLOTHES, Activity.CREATE_ANIMAL_HIDE_CLOTHING, quantityProduced);
		extractAsset(person, AssetType.COTTON_CLOTHES, Activity.CREATE_COTTON_CLOTHING, quantityProduced);
		
	}
	
	private void extractAsset(Person person, AssetType neededAssetType, Activity activity, int quantityNeeded) {
		assetExtractionBehaviour.extractAsset(person, neededAssetType, activity, quantityNeeded);
	}
}
