package com.example.simple_table.controller;

import com.example.simple_table.model.Employee;
import com.example.simple_table.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Отображение списка сотрудников
    @GetMapping
    public String getEmployees(Model model) {
        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "index"; // Возвращает шаблон index.html
    }

    // Переход на страницу добавления сотрудника
    @GetMapping("/add")
    public String showAddEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "add"; // Возвращает шаблон add.html
    }

    // Добавление нового сотрудника
    @PostMapping
    public String addEmployee(@ModelAttribute Employee employee) {
        employeeService.saveEmployee(employee);
        return "redirect:/employees";
    }

    // Удаление сотрудника
    @PostMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return "redirect:/employees";
    }

    // Обновление данных сотрудника
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateEmployee(@PathVariable Long id, @RequestBody Map<String, String> updates) {
        System.out.println("Received updates: " + updates); // Логирование
        Employee employee = employeeService.getEmployeeById(id);

        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    employee.setName(value);
                    break;
                case "dateOfBirth":
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    employee.setDateOfBirth(LocalDate.parse(value, formatter));
                    break;
                case "salary":
                    employee.setSalary(value);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid field: " + key);
            }
        });

        employeeService.saveEmployee(employee);
        return ResponseEntity.ok().build();
    }
}
