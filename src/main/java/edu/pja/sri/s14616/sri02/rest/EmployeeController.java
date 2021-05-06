package edu.pja.sri.s14616.sri02.rest;

import edu.pja.sri.s14616.sri02.dto.EmployeeDto;
import edu.pja.sri.s14616.sri02.model.Employee;
import edu.pja.sri.s14616.sri02.repo.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private EmployeeRepository employeeRepository;
    private ModelMapper modelMapper;

    public EmployeeController(EmployeeRepository employeeRepository, ModelMapper modelMapper){
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }
    private EmployeeDto convertToDto(Employee e){
        return modelMapper.map(e, EmployeeDto.class);
    }
    private Employee convertToEntity(EmployeeDto dto){
        return modelMapper.map(dto, Employee.class);
    }

    @GetMapping
    public ResponseEntity<Collection<EmployeeDto>> getEmployees(){
        List<Employee> allEmployees = employeeRepository.findAll();
        List<EmployeeDto> result = allEmployees.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @GetMapping("/{empId}")
    public ResponseEntity<EmployeeDto>
    getEmployeeById(@PathVariable Long empId){
        Optional<Employee> emp =
                employeeRepository.findById(empId);
        if(emp.isPresent()){
            EmployeeDto employeeDto = convertToDto(emp.get());
            return new ResponseEntity<>(employeeDto, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}
