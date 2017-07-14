package io.vavr.hibernate;

import static org.assertj.core.api.Assertions.assertThat;

import io.vavr.collection.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CollectionType;
import org.hibernate.annotations.Type;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
public class ListUserTypeTest {

  @Autowired
  private PersonRepository people;

  @SpringBootApplication
  static class Configuration {

  }


  @Test
  public void readsPersonIntoJavaslangOptionById() {

    Person adrian = new Person("Adrian", "Bongiorno");
    Person maya = new Person("Maya", "Bongiorno");
    Person viola = new Person("Viola", "Bongiorno", List.of(adrian, maya));
    Person me = new Person("Christian", "Bongiorno", List.of(adrian, maya));

    Iterable<Person> people = this.people.save(List.of(adrian, maya, viola, me));

    Person result = this.people.findOne(me.getId());

    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(me);
    assertThat(result.getChildren()).containsExactly(adrian, maya);
  }

  @Getter
  @Entity
  @NoArgsConstructor
  public class Person {

    private @GeneratedValue
    @Id
    Long id;
    private String firstname;

    private String lastname;

    @ManyToMany
    @CollectionType(type = "io.vavr.hibernate.userstype.ListUserType")
    private List<Person> children;

    public Person(String firstname, String lastname, List<Person> children) {
      this.firstname = firstname;
      this.lastname = lastname;
      this.children = children;
    }

    public Person(String firstname, String lastname) {
      this(firstname, lastname, List.empty());
    }
  }

  public interface PersonRepository extends CrudRepository<Person, Long> {

  }
}
