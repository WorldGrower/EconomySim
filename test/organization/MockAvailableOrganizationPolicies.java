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

public class MockAvailableOrganizationPolicies implements AvailableOrganizationPolicies {

	private final boolean canChangeTheftPolicy;
	private final boolean canChangeTaxesWages;

	public MockAvailableOrganizationPolicies(boolean canChangeTheftPolicy, boolean canChangeTaxesWages) {
		super();
		this.canChangeTheftPolicy = canChangeTheftPolicy;
		this.canChangeTaxesWages = canChangeTaxesWages;
	}

	@Override
	public boolean canChangeTheftPolicy() {
		return canChangeTheftPolicy;
	}

	@Override
	public boolean canChangeTaxesWages() {
		return canChangeTaxesWages;
	}

	@Override
	public OrganizationPolicy changePolicy(OrganizationPolicy organizationPolicy, OrganizationPolicy newOrganizationPolicy) {
		return newOrganizationPolicy;
	}
}
