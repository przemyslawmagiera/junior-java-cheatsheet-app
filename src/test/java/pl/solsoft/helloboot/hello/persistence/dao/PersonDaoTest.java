package pl.solsoft.helloboot.hello.persistence.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.solsoft.helloboot.hello.enumeration.Sex;
import pl.solsoft.helloboot.hello.persistence.entity.Person;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.solsoft.helloboot.hello.factory.TestObjectFactory.nextPerson;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class PersonDaoTest {

    @Resource
    private PersonDao personDao;

    @Test
    public void shouldCreateAndNotFindPersonByEmail() {
        //given
        Person person = nextPerson("test1@test.pl");

        //when
        personDao.save(person);

        //then
        assertThat(personDao.findPersonByEmail("test@test.pl"))
                .isNull();
    }

    @Test
    public void shouldCreateAndFindPersonByEmail() {
        //given
        Person person = nextPerson("test@test.pl");

        //when
        personDao.save(person);

        //then
        assertThat(personDao.findPersonByEmail(person.getEmail()))
                .extracting(Person.FIELD_EMAIL)
                .isEqualTo("test@test.pl");
    }

    @Test
    public void shouldCreateAndFindAllPeopleByGender() {
        //given
        Person person1 = nextPerson("test");
        Person person2 = nextPerson("test2");
        Person person3 = nextPerson("test3");


        //when
        personDao.save(person1);

        //then
        assertThat(personDao.findAllByGender(Sex.F)).hasSize(15);
    }

    @Test
    public void whenNoResultsShouldReturnEmptyList() {
        //given
        List<Person> personList = new ArrayList<>();
        IntStream.range(0,15).forEach(integer -> {
            personList.add(nextPerson(Sex.M));
        });

        //when
        personList.forEach(person -> personDao.save(person));

        //then
        assertThat(personDao.findAllByGender(Sex.F)).isEmpty();
    }


}