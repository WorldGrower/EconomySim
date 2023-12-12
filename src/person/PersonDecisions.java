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

import organization.Organization;
import organization.OrganizationPolicy;

public interface PersonDecisions {

	public static final PersonDecisions AI = new PersonDecisions() {
		
		@Override
		public boolean wantsToPartnerUp(Person askedPerson, Person askingPerson) {
			return true;
		}

		@Override
		public boolean wantsToSex(Person askedPerson, Person askingPerson) {
			return true;
		}

		@Override
		public boolean personWantsToJoin(Organization organization, Person askedPerson, Person askingPerson) {
			return true;
		}

		@Override
		public boolean approvePolicyChange(Person askedPerson, Person initiator, OrganizationPolicy oldOrganizationPolicy, OrganizationPolicy newOrganizationPolicy) {
			return true;
		}

		@Override
		public boolean acceptsJob(Person askedPerson, Person askingPerson) {
			return true;
		}
	};

	public boolean wantsToPartnerUp(Person askedPerson, Person askingPerson);
	public boolean wantsToSex(Person askedPerson, Person askingPerson);
	public boolean personWantsToJoin(Organization organization, Person askedPerson, Person askingPerson);
	public boolean approvePolicyChange(Person askedPerson, Person initiator, OrganizationPolicy oldOrganizationPolicy, OrganizationPolicy newOrganizationPolicy);
	public boolean acceptsJob(Person askedPerson, Person askingPerson);
}
