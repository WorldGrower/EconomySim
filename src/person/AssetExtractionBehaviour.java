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
import action.PersonAction;
import action.PersonActionArgs;
import action.PersonActionFactory;
import asset.Asset;
import asset.AssetType;
import environment.PublicLocations;
import knowledge.Knowledge;
import society.PersonFinder;
import society.PublicAssets;
import society.PublicKnowledge;
import society.PublicOrganizations;

class AssetExtractionBehaviour implements Serializable {

	private final PersonFinder personFinder;
	private final PublicOrganizations publicOrganizations;
	private final PublicAssets publicAssets;
	private final PublicKnowledge publicKnowledge;
	private final PublicLocations publicLocations;
	
	public AssetExtractionBehaviour(PersonFinder personFinder, PublicOrganizations publicOrganizations, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicLocations publicLocations) {
		super();
		this.personFinder = personFinder;
		this.publicOrganizations = publicOrganizations;
		this.publicAssets = publicAssets;
		this.publicKnowledge = publicKnowledge;
		this.publicLocations = publicLocations;
	}
	
	public void extractAsset(Person person, AssetType neededAssetType, Activity activity, int quantityNeeded) {
		if (person.getAssets().getQuantityFor(neededAssetType) < quantityNeeded) {
			extract(person, neededAssetType, activity);
		}
	}

	private void extract(Person person, AssetType neededAssetType, Activity activity) {
		Knowledge requiredKnowledge = activity.getRequiredKnowledge();
		if (requiredKnowledge != null && !person.hasKnowledge(requiredKnowledge)) {
			person.learnKnowledge(requiredKnowledge, personFinder, publicAssets, publicKnowledge, publicOrganizations);
		} else {
			Asset asset = person.findAsset(publicAssets, neededAssetType);
			if (asset != null) {
				PersonAction personAction = PersonActionFactory.createAssetPersonAction(asset, activity, publicAssets, publicLocations);
				if (personAction.canPerform(person, person.getTimeRemaining(), publicAssets, publicOrganizations)) {
					personAction.perform(person, person.getTimeRemaining(), PersonActionArgs.NONE, publicAssets, publicKnowledge, publicOrganizations);
				}
			}
		}
	}
	
	public void extractPublicAsset(Person person, AssetType neededAssetType, Activity activity, int quantityNeeded) {
		if (publicAssets.getQuantityFor(neededAssetType) < quantityNeeded) {
			extract(person, neededAssetType, activity);
		}
	}
}
