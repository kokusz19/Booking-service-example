package com.kokusz19.udinfopark.service;

import com.kokusz19.udinfopark.api.CompanyApi;
import com.kokusz19.udinfopark.model.Company;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CompanyService implements CompanyApi {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public List<Company> getAll() {
        return companyRepository.findAll();
    }

    @Override
    public Company getOne(int id) {
        return companyRepository.findById(id).orElse(null);
    }

    @Override
    public int create(Company subject) {
        Optional<Company> byName = companyRepository.findByName(subject.getName());
        if(byName.isPresent()) {
            throw new RuntimeException("Company already exists!");
        }
        return companyRepository.save(subject).getCompanyId();
    }

    @Override
    public Company update(int id, Company subject) {
        subject.setCompanyId(id);
        return companyRepository.save(subject);
    }

    @Override
    public boolean delete(int id) {
        return companyRepository.findById(id).map(company -> {
            companyRepository.delete(company);
            return true;
        }).orElse(false);
    }
}
