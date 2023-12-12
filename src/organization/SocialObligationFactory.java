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
package organization;

import java.io.Serializable;

import action.PersonAction;
import action.PersonActionArgs;
import asset.Asset;
import asset.AssetContainer;
import asset.AssetType;
import person.Person;
import society.PublicAssets;
import society.PublicOrganizations;

class SocialObligationFactory {

	public static SocialObligation create(SocialObligationType type, AssetType assetType, int quantity, AssetContainer assetContainer) {
		return new PaymentSocialObligation(type, assetType, quantity, assetContainer);
	}

	public static SocialObligation create(SocialObligationType type, PersonAction personAction, int numberOfHours, int numberOfTurns, PublicAssets publicAssets, PublicOrganizations publicOrganizations) {
		return new CommunityServiceSocialObligation(type, personAction, numberOfHours, numberOfTurns, publicAssets, publicOrganizations);
	}
	
	public static SocialObligation create(SocialObligationType type, PersonAction personAction, int numberOfHours, PublicAssets publicAssets, PublicOrganizations publicOrganizations) {
		return new EmploymentSocialObligation(type, personAction, numberOfHours, publicAssets, publicOrganizations);
	}
	
	private static abstract class AbstractSocialObligation implements SocialObligation, Serializable {
		
		private final SocialObligationType type;

		public AbstractSocialObligation(SocialObligationType type) {
			super();
			this.type = type;
		}
		
		@Override
		public final SocialObligationType getType() {
			return type;
		}	
	}

	private static class PaymentSocialObligation extends AbstractSocialObligation {

		private final AssetType assetType;
		private final int quantity;
		private final AssetContainer assetContainer;
		private boolean fullfilled = false;

		public PaymentSocialObligation(SocialObligationType type, AssetType assetType, int quantity, AssetContainer assetContainer) {
			super(type);
			this.assetType = assetType;
			this.quantity = quantity;
			this.assetContainer = assetContainer;
		}

		@Override
		public boolean canFullfill(Person person) {
			return person.getAssets().getQuantityFor(assetType) >= quantity;
		}

		@Override
		public void fullfill(Person person) {
			Asset removedAsset = person.getAssets().retrieve(assetType, quantity);
			assetContainer.addAsset(removedAsset);
			fullfilled = true;
		}

		@Override
		public void endTurn() {
		}

		@Override
		public boolean isDone() {
			return fullfilled;
		}

		@Override
		public String getDescription() {
			return "taxes (" + quantity + " " + assetType + ")";
		}
	}
	
	private static class CommunityServiceSocialObligation extends AbstractSocialObligation {

		private final PersonAction personAction;
		private final int numberOfHours;
		private int numberOfTurns;
		private final PublicAssets publicAssets;
		private final PublicOrganizations publicOrganizations;

		public CommunityServiceSocialObligation(SocialObligationType type, PersonAction personAction, int numberOfHours, int numberOfTurns, PublicAssets publicAssets, PublicOrganizations publicOrganizations) {
			super(type);
			this.personAction = personAction;
			this.numberOfHours = numberOfHours;
			this.numberOfTurns = numberOfTurns;
			this.publicAssets = publicAssets;
			this.publicOrganizations = publicOrganizations;
		}

		@Override
		public boolean canFullfill(Person person) {
			return personAction.canPerform(person, person.getTimeRemaining(), publicAssets, publicOrganizations) 
					&& person.getTimeRemaining().hasRemainingTime(numberOfHours);
		}

		@Override
		public void fullfill(Person person) {
			int numberOfPerforms = numberOfHours / personAction.getTimeRequired();
			for (int i=0; i<numberOfPerforms; i++) {
				personAction.perform(person, person.getTimeRemaining(), PersonActionArgs.NONE, publicAssets, null, publicOrganizations);
			}
		}

		@Override
		public void endTurn() {
			numberOfTurns--;
		}

		@Override
		public boolean isDone() {
			return numberOfTurns <= 0;
		}

		@Override
		public String getDescription() {
			return "community service (" + numberOfHours + " hours)";
		}
	}
	
	private static class EmploymentSocialObligation extends AbstractSocialObligation {

		private final PersonAction personAction;
		private final int numberOfHours;
		private final PublicAssets publicAssets;
		private final PublicOrganizations publicOrganizations;
		private boolean fullfilled = false;

		public EmploymentSocialObligation(SocialObligationType type, PersonAction personAction, int numberOfHours, PublicAssets publicAssets, PublicOrganizations publicOrganizations) {
			super(type);
			this.personAction = personAction;
			this.numberOfHours = numberOfHours;
			this.publicAssets = publicAssets;
			this.publicOrganizations = publicOrganizations;
		}

		@Override
		public boolean canFullfill(Person person) {
			return personAction.canPerform(person, person.getTimeRemaining(), publicAssets, publicOrganizations) 
					&& person.getTimeRemaining().hasRemainingTime(numberOfHours);
		}

		@Override
		public void fullfill(Person person) {
			int numberOfPerforms = numberOfHours / personAction.getTimeRequired();
			for (int i=0; i<numberOfPerforms; i++) {
				personAction.perform(person, person.getTimeRemaining(), PersonActionArgs.NONE, publicAssets, null, publicOrganizations);
			}
			fullfilled = true;
		}

		@Override
		public void endTurn() {
		}

		@Override
		public boolean isDone() {
			return fullfilled;
		}

		@Override
		public String getDescription() {
			return "employment (" + numberOfHours + " hours)";
		}
	}
}
