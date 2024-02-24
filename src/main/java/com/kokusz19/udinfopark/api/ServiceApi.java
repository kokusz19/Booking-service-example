package com.kokusz19.udinfopark.api;

import com.kokusz19.udinfopark.model.Service;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("services")
public interface ServiceApi extends CrudApi<Service> {
}
