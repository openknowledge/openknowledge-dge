/*
 * Copyright open knowledge GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.openknowledge.util.filter.example.web.domain;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Years;

import javax.print.attribute.standard.DateTimeAtCompleted;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Marc Petersen - open knowledge GmbH
 */
public class Pet {

  private String name;

  private Date dateOfBirth;

  private Species species;

  public String getName() {
    return name;
  }

  public void setName(String aName) {
    name = aName;
  }

  public Integer getAge() {
    return Years.yearsBetween(new DateMidnight(getDateOfBirth()), new DateTime()).getYears();
  }

  public Date getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(Date aDateOfBirth) {
    dateOfBirth = aDateOfBirth;
  }

  public Species getSpecies() {
    return species;
  }

  public void setSpecies(Species aSpecies) {
    species = aSpecies;
  }
}
