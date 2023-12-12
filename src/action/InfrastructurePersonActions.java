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

import asset.Asset;
import asset.AssetType;
import asset.LocatableAsset;
import asset.OperationalAsset;
import asset.OperationalAssetAttribute;
import knowledge.Knowledge;
import person.Person;
import society.PublicAssets;
import society.PublicKnowledge;
import society.PublicOrganizations;

public class InfrastructurePersonActions {

	public static final PersonAction BUILD_IRRIGATION_CANAL_PERSON_ACTION = new BuildIrrigationCanalPersonAction();
	public static final PersonAction MAINTAIN_IRRIGATION_CANAL_PERSON_ACTION = new MaintainIrrigationCanalPersonAction();
	public static final PersonAction IRRIGATE_WHEAT_FIELDS_CANAL_PERSON_ACTION = new IrrigateWheatFieldsPersonAction();
	
	public static PersonActions createInfrastructurePersonActions() {
		PersonActions personActions = new PersonActions();
		addIrrigationPersonActions(personActions);
		
		return personActions;
	}

	private static void addIrrigationPersonActions(PersonActions personActions) {
		personActions.add(BUILD_IRRIGATION_CANAL_PERSON_ACTION);
		personActions.add(MAINTAIN_IRRIGATION_CANAL_PERSON_ACTION);
		personActions.add(IRRIGATE_WHEAT_FIELDS_CANAL_PERSON_ACTION);
	}
	
	private static class BuildIrrigationCanalPersonAction extends AbstractPersonAction {

		@Override
		public void performInternal(Person person, PersonActionArgs args, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicOrganizations publicOrganizations) {
			OperationalAsset irrigationCanalAsset = (OperationalAsset) publicAssets.get(AssetType.IRRIGATION_CANAL);
			irrigationCanalAsset.getOperationalAssetAttribute(0).increaseOperational();
		}

		@Override
		public boolean canPerformInternal(Person person, PublicAssets publicAssets, PublicOrganizations publicOrganizations) {
			OperationalAsset irrigationCanalAsset = (OperationalAsset) publicAssets.get(AssetType.IRRIGATION_CANAL);
			if (irrigationCanalAsset != null) {
				OperationalAssetAttribute attribute = irrigationCanalAsset.getOperationalAssetAttribute(0);
				return !attribute.isFinished();
			} else {
				return false;
			}
		}

		@Override
		public Knowledge getRequiredKnowledge() {
			return Knowledge.IRRIGATION;
		}

		@Override
		public int getTimeRequired() {
			return 1;
		}

		@Override
		public String getName() {
			return "build irrigation canal";
		}
	}
	
	private static class MaintainIrrigationCanalPersonAction extends AbstractPersonAction {

		@Override
		public void performInternal(Person person, PersonActionArgs args, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicOrganizations publicOrganizations) {
			OperationalAsset irrigationCanalAsset = (OperationalAsset) publicAssets.get(AssetType.IRRIGATION_CANAL);
			irrigationCanalAsset.getOperationalAssetAttribute(0).increaseOperational();
		}

		@Override
		public boolean canPerformInternal(Person person, PublicAssets publicAssets, PublicOrganizations publicOrganizations) {
			OperationalAsset irrigationCanalAsset = (OperationalAsset) publicAssets.get(AssetType.IRRIGATION_CANAL);
			if (irrigationCanalAsset != null) {
				OperationalAssetAttribute attribute = irrigationCanalAsset.getOperationalAssetAttribute(0);
				return attribute.isFinished();
			} else {
				return false;
			}
		}

		@Override
		public Knowledge getRequiredKnowledge() {
			return Knowledge.IRRIGATION;
		}

		@Override
		public int getTimeRequired() {
			return 1;
		}

		@Override
		public String getName() {
			return "maintain irrigation canal";
		}
	}
	
	private static class IrrigateWheatFieldsPersonAction extends AbstractPersonAction {

		@Override
		public void performInternal(Person person, PersonActionArgs args, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicOrganizations publicOrganizations) {
			OperationalAsset irrigationCanalAsset = (OperationalAsset) publicAssets.get(AssetType.IRRIGATION_CANAL);
			LocatableAsset wheatFields = (LocatableAsset) person.getAssets().get(AssetType.WHEAT_FIELD);
			Asset river = publicAssets.get(AssetType.RIVER);
			
			if (irrigationCanalAsset != null && river != null && wheatFields != null) {
				int wheatFieldsQuantity = wheatFields.getQuantity();
				for (int i=0; i<wheatFieldsQuantity; i++) {
					if (wheatFields.canSetCapacity(i)) {
						int quantityUsed = river.useRemainingProduce(AssetType.WATER, 100);
						
						int wheatFieldCapacity = wheatFields.getCapacity(i);
						wheatFields.setCapacity(i, wheatFieldCapacity + (quantityUsed / 2));
					}
				}
			}
		}

		@Override
		public boolean canPerformInternal(Person person, PublicAssets publicAssets, PublicOrganizations publicOrganizations) {
			Asset river = publicAssets.get(AssetType.RIVER);
			LocatableAsset wheatFields = (LocatableAsset) person.getAssets().get(AssetType.WHEAT_FIELD);
			OperationalAsset irrigationCanalAsset = (OperationalAsset) publicAssets.get(AssetType.IRRIGATION_CANAL);
			if (river != null && irrigationCanalAsset != null && wheatFields != null && wheatFields.canSetCapacity()) {
				OperationalAssetAttribute attribute = irrigationCanalAsset.getOperationalAssetAttribute(0);
				return attribute.isOperational();
			} else {
				return false;
			}
		}

		@Override
		public Knowledge getRequiredKnowledge() {
			return Knowledge.IRRIGATION;
		}

		@Override
		public int getTimeRequired() {
			return 1;
		}

		@Override
		public String getName() {
			return "irrigate wheat fields";
		}
	}
}
