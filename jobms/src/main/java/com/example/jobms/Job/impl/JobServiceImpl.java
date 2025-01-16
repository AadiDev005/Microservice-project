package com.example.jobms.Job.impl;


import com.example.jobms.Job.Clients.CompanyClient;
import com.example.jobms.Job.Clients.ReviewClient;
import com.example.jobms.Job.Job;
import com.example.jobms.Job.JobRepository;
import com.example.jobms.Job.JobService;
import com.example.jobms.Job.dto.JobDTO;
import com.example.jobms.Job.external.Company;
import com.example.jobms.Job.external.Review;
import com.example.jobms.Job.mapper.JobMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {
   // private List<Job> jobs = new ArrayList<>();

    JobRepository jobRepository;
@Autowired
    RestTemplate restTemplate;
private CompanyClient companyClient;
private ReviewClient reviewClient;
int attempt =0;
    public JobServiceImpl(JobRepository jobRepository , CompanyClient companyClient , ReviewClient reviewClient) {
        this.jobRepository = jobRepository;
        this.companyClient = companyClient;
        this.reviewClient = reviewClient;
    }


    @Override
//    @CircuitBreaker(name = "companyBreaker", fallbackMethod = "companyBreakerFallback")
//    @Retry(name = "companyBreaker", fallbackMethod = "companyBreakerFallback")
    @RateLimiter(name = "companyBreaker", fallbackMethod = "companyBreakerFallback")

    public List<JobDTO> findAll() {
        System.out.println("Attempt"+  ++attempt);
        List<Job> jobs = jobRepository.findAll();
        List<JobDTO> jobDTOS = new ArrayList<>();

        return jobs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    public List<String> companyBreakerFallback(Exception e){
        List<String> list = new ArrayList<>();
        list.add("dummy");
        return list;
    }

    private JobDTO convertToDTO(Job job){
        Long companyId = job.getCompanyId();
        if (companyId == null || companyId <= 0) {
            throw new IllegalArgumentException("Invalid company ID: " + companyId);
        }

        Company company = companyClient.getCompany(companyId);
        List<Review> reviews = reviewClient.getReviews(companyId);
        JobDTO jobDTO = JobMapper.mapToJobWithCompanyDTO(job, company, reviews);
        return jobDTO;
    }


    @Override
    public void createJob(Job job) {
        jobRepository.save(job);
    }


    @Override
    public JobDTO getJobById(Long id) {
      Job job =  jobRepository.findById(id).orElse(null);
      return convertToDTO(job);
    }

    @Override
    public Boolean deleteJobById(Long id) {
       try{
           jobRepository.deleteById(id);
           return true;
       }catch (Exception E){
           return false;
       }
    }

    @Override
    public boolean updateJobById(Long id, Job updatedJob) {
        Optional<Job> jobOptional = jobRepository.findById(id);
            if(jobOptional.isPresent()){
                Job job = jobOptional.get();
                job.setMaxSalary(updatedJob.getMaxSalary());
                job.setDescription(updatedJob.getDescription());
                job.setLocation(updatedJob.getLocation());
                job.setMinSalary(updatedJob.getMinSalary());
                job.setTitle(updatedJob.getTitle());
                jobRepository.save(job);
                return true;
            }


        return false;
    }


}

