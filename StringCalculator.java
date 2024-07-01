import java.util.Scanner;

public class StringCalculator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите выражение:");
        String input = scanner.nextLine();

        try {
            System.out.println("Результат: " + calculate(input));
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static String calculate(String expression) throws Exception {
    int exLength = expression.length();
    if (exLength > 27) {
        throw new Exception ("Длина задания превышает максимальный размер");
    }
    char[] exChar = new char[exLength];
    int i = 0;
    for (i = 0; i < exLength; i++) {
        exChar[i] = expression.charAt(i);
    }
    if (exChar[0] != '"') {
        throw new Exception ("Первый операнд должен быть строкой, заключенной в двойные кавычки");
    }
    i = 0;
    String operand1 = "";
    do {
        i++;
        operand1 = operand1 + Character.toString(exChar[i]);
    } while (exChar[i] != '"');
    operand1 = operand1.replace("\"", "");
    if (operand1.length() > 10) {
        throw new Exception ("Длина строки должна быть не больше 10 символов");
    }
    String operator = "";
    // Переменная для хранения индекса начала оператора
    int operatorIndex = -1;

    // Поиск оператора
    for (int k = i + 1; k < exLength; k++) {
        if (exChar[k] == '+' || exChar[k] == '-' || exChar[k] == '*' || exChar[k] == '/') {
            operatorIndex = k;
        break;
        }
    }

    // Если оператор не найден, выбрасываем исключение
    if (operatorIndex == -1) {
        throw new Exception("Оператор не найден.");
    }

    // Присваиваем оператор
    operator = Character.toString(exChar[operatorIndex]);
    
    String operand2 = "";
    // Пропускаем оператор и пробелы после него
    i = operatorIndex + 1;
    while (i < exLength && exChar[i] == ' ') {
        i++;
    }

    // Определяем второй операнд
    if (i < exLength && exChar[i] == '"') {
        // Второй операнд - строка в кавычках
        i++; // Пропускаем открывающую кавычку
        while (i < exLength && exChar[i] != '"') {
            operand2 += Character.toString(exChar[i]);
            i++;
        }
    if (i >= exLength) {
        throw new Exception("Второй операнд не заканчивается кавычкой.");
    }
    i++; // Пропускаем закрывающую кавычку
    } else {
        // Второй операнд - число
        while (i < exLength && exChar[i] != ' ') {
            operand2 += Character.toString(exChar[i]);
            i++;
        }
    }

    // Проверяем, является ли второй операнд числом
    boolean isOperand2Numeric = operand2.matches("-?\\d+(\\.\\d+)?");

    // Выполняем операцию
    String result;
    switch (operator) {
        case "+":
            result = operand1 + operand2;
            break;
        case "-":
            if (isOperand2Numeric) {
                throw new Exception("Нельзя вычитать число из строки.");
            }
            result = operand1.replace(operand2, "");
            break;
        case "*":
            if (!isOperand2Numeric) {
                throw new Exception("Нельзя умножать строку на строку.");
            }
            int times = Integer.parseInt(operand2);
            if (times < 1 || times > 10) {
                throw new Exception ("Число должно быть от 1 до 10");
            }
            result = multiplyString(operand1, times);
            break;
        case "/":
            if (!isOperand2Numeric) {
                throw new Exception("Нельзя делить строку на строку.");
            }
            int divisor = Integer.parseInt(operand2);
            if (divisor == 0) {
                throw new ArithmeticException("Деление на ноль.");
            }
            result = divideString(operand1, divisor);
            break;
        default:
            throw new Exception("Неподдерживаемый оператор.");
    }

    // Возвращаем результат в кавычках, если это строка
    if (isOperand2Numeric) result = "\"" + result + "\"";

    // Обрезаем результат, если он слишком длинный
    return result.length() > 40 ? result.substring(0, 40) + "..." : result;
    }

    // Методы для умножения и деления строк
    private static String multiplyString(String str, int times) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < times; i++) {
            result.append(str);
        }
        return result.toString();
    }

    private static String divideString(String str, int divisor) {
        if (divisor == 0) {
            throw new ArithmeticException("Деление на ноль.");
        }
        return str.substring(0, Math.max(1, str.length() / divisor));
    }
}
