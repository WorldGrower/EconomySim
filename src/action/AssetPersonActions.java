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
package action;

import java.util.HashSet;
import java.util.Set;

import asset.Asset;
import asset.AssetType;
import asset.SimpleAsset;
import asset.Tools;
import environment.PublicLocations;
import knowledge.Knowledge;
import person.Person;
import society.PublicAssets;
import society.PublicKnowledge;
import society.PublicOrganizations;

class AssetPersonActions {

	public static PersonActions createAssetPersonActions(Person person, PublicAssets publicAssets, PublicLocations publicLocations) {
		PersonActions personActions = new PersonActions();
		Set<AssetType> allAssetTypes = new HashSet<>();
		allAssetTypes.addAll(person.getAssets().keys());
		allAssetTypes.addAll(publicAssets.getAssetTypes());
		
		for(AssetType assetType : allAssetTypes) {
			Asset asset = person.getAsset(publicAssets, assetType);
			for(Activity activity : Activity.VALUES) {
				if (activity.isApplicableTo(asset)) {
					personActions.add(new AssetPersonAction(asset, activity, publicAssets, publicLocations));
				}
			}
		}

		return personActions;
	}
	public static PersonActions createAllAssetPersonActions() {
		PersonActions personActions = new PersonActions();
		for(Activity activity : Activity.VALUES) {
			personActions.add(new AssetPersonAction(new SimpleAsset(AssetType.FOOD, 1), activity, null, null));
		}
		return personActions;
	}
	
	public static PersonAction createAssetPersonAction(Asset asset, Activity activity, PublicAssets publicAssets, PublicLocations publicLocations) {
		return new AssetPersonAction(asset, activity, publicAssets, publicLocations);
	}
	
	private static class AssetPersonAction extends AbstractPersonAction {

		private final Asset asset;
		private final Activity activity;
		private final PublicAssets publicAssets;
		private final PublicLocations publicLocations;
		
		private AssetPersonAction(Asset asset, Activity activity, PublicAssets publicAssets, PublicLocations publicLocations) {
			super();
			if (asset == null) { throw new IllegalArgumentException("asset is null"); }
			if (activity == null) { throw new IllegalArgumentException("activity is null"); }
			this.asset = asset;
			this.activity = activity;
			this.publicAssets = publicAssets;
			this.publicLocations = publicLocations;
		}

		@Override
		public void performInternal(Person person, PersonActionArgs args, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicOrganizations publicOrganizations) {
			
			boolean assetsProduced = asset.work(activity.getProvidedAssetType());
			if (assetsProduced) {
				Tools tools = person.getAssets().findToolsForAsset(asset.getAssetType(), activity.getProvidedAssetType());
				activity.perform(person.getAssets(), tools, publicAssets, publicLocations);
				tools.checkRemainingProduce(person.getAssets());
			}
		}

		@Override
		public boolean canPerformInternal(Person person, PublicAssets publicAssets, PublicOrganizations publicOrganizations) {
			return asset.hasRemainingProduce(activity.getProvidedAssetType()) 
					&& activity.canPerformAssetAction(person.getAssets(), publicLocations);
		}

		@Override
		public Knowledge getRequiredKnowledge() {
			return activity.getRequiredKnowledge();
		}

		@Override
		public int getTimeRequired() {
			return activity.getTimeRequired();
		}

		@Override
		public String getName() {
			return activity.getName();
		}
	}
}
