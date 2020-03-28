package main;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {

    List<Person> people = new ArrayList<>();

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        int selection;
        while ((selection = menu()) != 0) {
            switch (selection) {
                case 1:
                    people = readFromFile();
                    break;
                case 2:
                    writeToFile(people);
                    break;
                case 3:
                    printList(people);
                    break;
                case 4:
                    writeToBinaryFile("file.dat");
                    break;
                case 5:
                    people = readFromBinaryFile("file.dat");
                    break;
                case 6:
                    System.out.println("Year: ");
                    int year = new Scanner(System.in).nextInt();
                    List<Person> find = findByYear(people,year);
                    printList(find);
                    break;
                case 7:
                    System.out.println("id: ");
                    int id = new Scanner(System.in).nextInt();
                    Person p = findById(people, id);
                    if (p!=null) deleteFromList(people, p);
                    break;
                case 8:
                    List<String> cities = findAllCities(people);
                    printCities(cities);
                    break;
                case 9:
                    Map<String,Integer> map = countPeopleByCity(people);
                    printMap(map);
                    break;
                case 10:
                    Map<String, List<Person>> map1 = findPeopleByCities(people);
                    printMap1(map1);
                    break;
            }
        }
    }

    private Map<String, List<Person>> findPeopleByCities(List<Person> people) {
        Map<String, List<Person>> map = new HashMap<>();
        for (Person person : people) {
            String city = person.getCity();
            List<Person> list = map.get(city);
            if (list == null) {
                list = new ArrayList<>();
                map.put(city, list);
            }
            list.add(person);
        }
        return map;
    }

    private void printMap1(Map<String, List<Person>> map1) {
        for (Map.Entry<String, List<Person>> entry : map1.entrySet()) {
            System.out.println(entry.getKey());
            for (Person person : entry.getValue()) {
                System.out.println(person);
            }
            System.out.println("------------------");
        }
    }

    private Map<String, Integer> countPeopleByCity(List<Person> people) {
        Map<String, Integer> map = new HashMap<>();
        for (Person person : people) {
            String city = person.getCity();
//            if (map.containsKey(city)) {
//                Integer k = map.get(city);
//                map.put(city, k+1);
//            } else {
//                map.put(city, 1);
//            }
            Integer k = map.get(city);
            if (k==null) k = 0;
            map.put(city, k+1);
        }
        return map;
    }

    private void printMap(Map<String, Integer> map) {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    private List<String> findAllCities(List<Person> people) {
        Set<String> cities = new HashSet<>();
        for (Person person : people) {
            cities.add(person.getCity());
        }
        return new ArrayList<>(cities);
    }

    private void printCities(List<String> cities) {
        for (String city : cities) {
            System.out.println(city);
        }
    }

    private void deleteFromList(List<Person> people, Person p) {
        people.remove(p);
    }

    private Person findById(List<Person> people, int id) {
        for (Person person : people) {
            if (person.getId() == id) {
                return person;
            }
        }
        return null;
    }

    private List<Person> findByYear(List<Person> people, int year) {
        List<Person> list = new ArrayList<>();
        for (Person person : people) {
            if (person.getBirthday().isAfter(LocalDate.of(year, 12, 31))) {
                list.add(person);
            }
        }
        return list;
    }

    private List<Person> readFromBinaryFile(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (List<Person>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private void writeToBinaryFile(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(people);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printList(List<Person> people) {
        System.out.println("--- People ---");
        for (Person person : people) {
            System.out.println(person);
        }
        System.out.println("--------------");
    }

    private List<Person> readFromFile() {
        List<Person> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("people.txt"))) {
            String line;
            while ((line = reader.readLine())!=null) {
                String[] s = line.split(" ");
                int id = Integer.parseInt(s[0]);
                String name = s[1];
                int day = Integer.parseInt(s[2]);
                int month = Integer.parseInt(s[3]);
                int year = Integer.parseInt(s[4]);
                double rating = Double.parseDouble(s[5]);
                String city = s[6];
                Person p = new Person(id, name, LocalDate.of(year, month, day), rating, city);
                list.add(p);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void writeToFile(List<Person> people) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d M yyyy");
        try (PrintWriter out = new PrintWriter("people1.txt")) {
            for (Person person : people) {
                out.print(person.getId()+" "+person.getName()+" ");
                out.print(formatter.format(person.getBirthday()));
                out.println(" "+person.getRating()+" "+person.getCity());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private int menu() {
        System.out.println("1. Чтение из файла");
        System.out.println("2. Запись в файл");
        System.out.println("3. Вывод списка на экран");
        System.out.println("4. Запись в бинарный файл");
        System.out.println("5. Чтение из бинарного файла");
        System.out.println("6. Родившиеся после заданного года");
        System.out.println("7. Удалить по id");
        System.out.println("8. Список городов без повторов");
        System.out.println("9. Количество людей по городам");
        System.out.println("10. Списки людей по городам");
        System.out.println("--------------------------");
        System.out.println("0. Выход");
        return new Scanner(System.in).nextInt();
    }
}
