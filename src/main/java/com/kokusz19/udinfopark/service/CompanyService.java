package com.kokusz19.udinfopark.service;

import com.google.common.base.Preconditions;
import com.kokusz19.udinfopark.api.CompanyApi;
import com.kokusz19.udinfopark.model.dto.Company;
import com.kokusz19.udinfopark.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CompanyService implements CompanyApi {

    private final CompanyRepository companyRepository;
    private final ModelConverter modelConverter;

    public CompanyService(CompanyRepository companyRepository, ModelConverter modelConverter) {
        this.companyRepository = companyRepository;
        this.modelConverter = modelConverter;
    }

    @Override
    public List<Company> getAll() {
        return companyRepository.findAll().stream().map(modelConverter::convert).toList();
    }

    @Override
    public Company getOne(int id) {
        return companyRepository.findById(id).map(modelConverter::convert).orElse(null);
    }

    @Override
    public int create(Company subject) {
        Optional<com.kokusz19.udinfopark.model.dao.Company> byName = companyRepository.findByName(subject.getName());
        Preconditions.checkArgument(byName.isEmpty(), "Company already exists!");
        return companyRepository.save(modelConverter.convert(subject)).getCompanyId();
    }

    @Override
    public Company update(int id, Company subject) {
        subject.setCompanyId(id);
        return modelConverter.convert(companyRepository.save(modelConverter.convert(subject)));
    }

    @Override
    public boolean delete(int id) {
        return companyRepository.findById(id).map(company -> {
            companyRepository.delete(company);
            return true;
        }).orElse(false);
    }
}
