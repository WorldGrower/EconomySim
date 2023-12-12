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

public interface AvailableOrganizationPolicies {

	public static final AvailableOrganizationPolicies READONLY = new AvailableOrganizationPolicies() {

		@Override
		public boolean canChangeTheftPolicy() {
			return false;
		}

		@Override
		public boolean canChangeTaxesWages() {
			return false;
		}

		@Override
		public OrganizationPolicy changePolicy(OrganizationPolicy organizationPolicy, OrganizationPolicy newOrganizationPolicy) {
			throw new UnsupportedOperationException("changePolicy is unsupported");
		}		
	};
	
	public boolean canChangeTheftPolicy();
	public boolean canChangeTaxesWages();
	public OrganizationPolicy changePolicy(OrganizationPolicy oldOrganizationPolicy, OrganizationPolicy newOrganizationPolicy);
}
