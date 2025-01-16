package com.example.jobms.Job.mapper;

import com.example.jobms.Job.Job;
import com.example.jobms.Job.dto.JobDTO;
import com.example.jobms.Job.external.Company;
import com.example.jobms.Job.external.Review;

import java.util.List;

public class JobMapper {
public static JobDTO mapToJobWithCompanyDTO(Job job, Company company , List<Review> reviews) {
    if (job == null || company == null) {
        throw new IllegalArgumentException("Job and Company cannot be null");
    }

    JobDTO jobDTO = new JobDTO();
    jobDTO.setId(job.getId());
    jobDTO.setDescription(job.getDescription());
    jobDTO.setLocation(job.getLocation());
    jobDTO.setMaxSalary(job.getMaxSalary());
    jobDTO.setMinSalary(job.getMinSalary());
    jobDTO.setTitle(job.getTitle());
    jobDTO.setCompany(company);
    jobDTO.setReviews(reviews);

    return jobDTO;
}

}
