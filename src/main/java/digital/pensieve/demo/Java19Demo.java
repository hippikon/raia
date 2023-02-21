package digital.pensieve.demo;

import java.util.List;

public class Java19Demo {

    public record Person(String name, int age) {}

    public static void main(String[] args) {
        Person person = new Person("Madhuri",44);

//        var (name, age) = person;
        System.out.println(List.of(person));
        System.out.println(List.of(person));
    }

}
