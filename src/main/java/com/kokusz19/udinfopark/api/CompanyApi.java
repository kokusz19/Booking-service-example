package com.kokusz19.udinfopark.api;

import com.kokusz19.udinfopark.model.dto.Company;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("companies")
public interface CompanyApi extends CrudApi<Company> {
}
