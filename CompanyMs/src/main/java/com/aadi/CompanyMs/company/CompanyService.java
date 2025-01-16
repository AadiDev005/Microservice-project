package com.aadi.CompanyMs.company;

import com.aadi.CompanyMs.company.dto.ReviewMessage;

import java.util.List;

public interface CompanyService {
    List<Company> getAllCompanies();
   boolean updateCompany(Long id ,Company company);
   void createCompany(Company company);
boolean deleteCompanyById(Long id);
Company getCompanyById(Long id);
public void updateCompanyRating(ReviewMessage reviewMessage);
}
