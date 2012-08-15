package de.openknowledge.util.dge.sample.pet.domain;

import de.openknowledge.util.dge.filter.annotation.FilterChoice;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Bartug Bölükemini - open knowledge GmbH
 * @version $Revision$
 */
public class PetLineChoices implements Serializable {

  private Set<String> species;

    public PetLineChoices() {
      init();
    }

    public void init() {
      species = new HashSet<String>();
      species.add("Cat");
      species.add("Dog");
      species.add("Bird");
      species.add("Rabbit");
    }

    @FilterChoice("SPECIES")
    public Set<String> getSpecies() {
      return species;
    }

}
