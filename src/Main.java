
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        List<Patient> patients = Dump.getDump();

        collectPatientsByBirthDate(patients);
        printExpensesForEachPatient(patients);
        findMinMaxPatientByExpenses(patients);
        findFirstPatientBornInNovember1999(patients);
        checkIsAnyHealthy(patients);
        checkAllHealthy(patients);
        checkIsAnyOlderThan100(patients);
        checkNoneOlderThan100(patients);

    }

    //Метод collect()
    //**Преобразовать стрим из пациентов в Map, где ключ- номер полиса, а значение- фио без преобразований
    //В оригинале ключом была дата рождения. Изменала на номер полиса, та как есть повторяющиеся даты рождения
    private static void collectPatientsByBirthDate(List<Patient> patients) {
        Map<String, String> patientMap = patients.stream()
                .collect(Collectors.toMap(Patient::getPolicy, Patient::getFio));
        patientMap.forEach((policy, fio) -> System.out.println(policy + ": " + fio));
    }

    //-------------------------------------------
    //Метод foreach()
    //Вывести на экран только список расходов каждого пациента
    private static void printExpensesForEachPatient(List<Patient> patients) {
        patients.stream()
                .forEach(patient -> System.out.println(patient.getExpenses()));
    }

    //-------------------------------------------
    //Методы min() и max()
    //**Получить «минимального» и «максимального» пациента из потока.
    //Параметр «большести» - сумма всех расходов пациента (нужно написать компаратор – см урок)
    private static void findMinMaxPatientByExpenses(List<Patient> patients) {
        Comparator<Patient> comparator = Comparator.comparing(patient -> patient.getExpenses().stream().reduce(0, Integer::sum));
        Optional<Patient> minPatient = patients.stream().min(comparator);
        Optional<Patient> maxPatient = patients.stream().max(comparator);

        minPatient.ifPresent(min -> System.out.println("Пациент с минимальной суммой расходов: " + min));
        maxPatient.ifPresent(max -> System.out.println("Пациент с максимальной суммой расходов: " + max));
    }

    //-------------------------------------------
    //Метод findFirst()
    //Получить первого пациента, кто родился в декабре 1999 года
    //Позволила себе заменить декабрь на ноябрь, так как пациента  рожденного 12.1999 нет
    private static void findFirstPatientBornInNovember1999(List<Patient> patients) {
        Optional<Patient> december1999Patient = patients.stream()
                .filter(patient -> patient.getBirthDate().getMonth() == Month.NOVEMBER && patient.getBirthDate().getYear() == 1999)
                .findFirst();
        december1999Patient.ifPresent(p ->System.out.println("Пациент рожденный в ноябре 1999 г: " + p));
    }

    //-------------------------------------------
    //Метод allMatch()
    //allMatch(Predicate<? super T> predicate): возвращает true, если ВСЕ элементы потока удовлетворяют условию в предикате
    //**Проверить, есть ли хоть один АБСОЛЮТНО ЗДОРОВЫЙ человек
    //В этой формулировке на мой взгляд больше подходит метод anyMatch():
    private static void checkIsAnyHealthy(List<Patient> patients) {
        boolean allHealthy = patients.stream()
                .anyMatch(patient -> patient.getExpenses().isEmpty());
        System.out.println("Есть ли хотябы один абсолютно здоровый человек? " + (allHealthy ? "Да" : "Нет"));
    }

    //Чтобы использовать allMatch(), немного перефразирую вопрос:
    private static void checkAllHealthy(List<Patient> patients) {
        boolean allHealthy = patients.stream()
                .allMatch(patient -> patient.getExpenses().isEmpty());
        System.out.println("Все ли пациенты абсолютно здоровы? " + (allHealthy ? "Да" : "Нет"));
    }

    //-------------------------------------------
    //Проверить, есть ли хоть один человек, старше 100 лет

    //anyMatch()
    //anyMatch(Predicate<? super T> predicate): возвращает true, если ХОТЬ ОДИН элемент потока удовлетворяют условию в предикате
    private static void checkIsAnyOlderThan100(List<Patient> patients) {
        boolean anyOlderThan100 = patients.stream()
                .anyMatch(patient -> LocalDate.now().getYear() - patient.getBirthDate().getYear() > 100);

        System.out.println("Есть ли хоть один пациент старше 100 лет? " +
                (anyOlderThan100 ? "Есть такой пациент!" : "Нет такого пациента!"));
    }

    //noneMatch()
    //noneMatch(Predicate<? super T> predicate): возвращает true, если НИ ОДИН из элементов в потоке не удовлетворяет условию в предикате
    private static void checkNoneOlderThan100(List<Patient> patients) {
        boolean noneOlderThan100 = patients.stream()
                .noneMatch(patient -> LocalDate.now().getYear() - patient.getBirthDate().getYear() > 100);

        System.out.println("Нет ли пациента старше 100 лет? " +
                (noneOlderThan100 ? "Нет такого пациента!" : "Есть такой пациент!"));
    }
}