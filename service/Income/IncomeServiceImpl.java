package com.codeera.expensetracker.service.Income;

import com.codeera.expensetracker.dto.Income.IncomeRequestDto;
import com.codeera.expensetracker.dto.Income.IncomeResponseDto;
import com.codeera.expensetracker.entity.Income;
import com.codeera.expensetracker.entity.User;
import com.codeera.expensetracker.exception.ExceptionUtil;
import com.codeera.expensetracker.mapper.IncomeMapper;
import com.codeera.expensetracker.repository.IncomeRepository;
import com.codeera.expensetracker.util.SessionUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Getter
@AllArgsConstructor
public class IncomeServiceImpl  implements IncomeService {

    private final IncomeRepository incomeRepository;

    private final SessionUtil sessionUtil;

    @Override
    public IncomeResponseDto addIncome(IncomeRequestDto incomeRequestDto) {

        User user = sessionUtil.getCurrentUserOrThrow();

       boolean isFound =incomeRepository.existsByTitleAndUser(incomeRequestDto.getTitle(), user);
       if(isFound)
           ExceptionUtil.duplicate("Income", incomeRequestDto.getTitle());

        Income  income = IncomeMapper.mapToIncomeEntity(incomeRequestDto);
        income.setUser(user);
        Income saveIncome = incomeRepository.save(income);

        return IncomeMapper.mapToIncomeResponseDto(saveIncome);
    }

    @Override
    public List<IncomeResponseDto> getAllIncomesList() {
        User user = sessionUtil.getCurrentUserOrThrow();

        return incomeRepository.findAllByUser(user)
                .stream()
                .map(IncomeMapper::mapToIncomeResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public IncomeResponseDto getIncomeById(Long id) {

        Income income = incomeRepository.findById(id)
                .orElseThrow(()-> ExceptionUtil.throwResourceNotFound("Income", String.valueOf(id)));

       if(!income.getUser().getEmail().equals(sessionUtil.getCurrentUserEmail())){
           throw ExceptionUtil.throwAccessDenied("You are not authorized to view this income");
       }
       return IncomeMapper.mapToIncomeResponseDto(income);
    }

    @Override
    public IncomeResponseDto updateIncome(Long id, IncomeRequestDto incomeRequestDto) {

        Income income = incomeRepository.findById(id)
                .orElseThrow(()-> ExceptionUtil.throwResourceNotFound("Income", String.valueOf(id)));

        if(!income.getUser().getEmail().equals(sessionUtil.getCurrentUserEmail())){
            throw ExceptionUtil.throwAccessDenied("You are not authorized to view this income");
        }

        income.setTitle(incomeRequestDto.getTitle());
        income.setAmount(incomeRequestDto.getAmount());
        income.setDescription(incomeRequestDto.getDescription());
        income.setCategory(incomeRequestDto.getCategory());

       Income income1= incomeRepository.save(income);

        return IncomeMapper.mapToIncomeResponseDto(income1);

    }

    @Override
    public long deleteIncome(Long id) {
        Income income = incomeRepository.findById(id)
                .orElseThrow(()-> ExceptionUtil.throwResourceNotFound("Income", String.valueOf(id)));

        if(!income.getUser().getEmail().equals(sessionUtil.getCurrentUserEmail())){
            throw ExceptionUtil.throwAccessDenied("You are not authorized to view this income");
        }

        incomeRepository.deleteById(id);
        return id;
    }
}
