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

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class UTestProfessionStatistics {

	@Test
	public void testGetProfessionCount() {
		Person person1 = new Person(18, Sex.MALE, Profession.FARMER);
		Person person2 = new Person(18, Sex.FEMALE, Profession.ARTISAN);
		List<Person> persons = Arrays.asList(person1, person2);
		ProfessionStatistics professionStatistics = new ProfessionStatistics(persons);
		
		assertEquals(0, professionStatistics.getProfessionCount(Profession.GENERALIST));
		assertEquals(1, professionStatistics.getProfessionCount(Profession.FARMER));
		assertEquals(1, professionStatistics.getProfessionCount(Profession.ARTISAN));
	}
	
	@Test
	public void testUpdateInfo() {
		Person person1 = new Person(18, Sex.MALE, Profession.FARMER);
		List<Person> persons = Arrays.asList(person1);
		ProfessionStatistics professionStatistics = new ProfessionStatistics(persons);
		
		assertEquals(0, professionStatistics.getProfessionCount(Profession.GENERALIST));
		assertEquals(1, professionStatistics.getProfessionCount(Profession.FARMER));
		assertEquals(1, professionStatistics.size());
		
		Person person2 = new Person(18, Sex.FEMALE, Profession.FARMER);
		persons = Arrays.asList(person1, person2);
		professionStatistics.updateInfo(persons);
		
		assertEquals(0, professionStatistics.getProfessionCount(Profession.GENERALIST));
		assertEquals(2, professionStatistics.getProfessionCount(Profession.FARMER));
		assertEquals(2, professionStatistics.size());
	}
}
