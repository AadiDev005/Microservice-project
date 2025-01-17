package com.example.jobms.Job;

import com.example.jobms.Job.dto.JobDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/jobs")
public class JobController {
    private JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }
private Long nextId = 1L;

    @GetMapping
public ResponseEntity<List<JobDTO>> findAll(){
    return  ResponseEntity.ok(jobService.findAll());
}
@PostMapping
    public ResponseEntity<String> addJob( @RequestBody Job job){
        job.setId(nextId++);
        jobService.createJob (job);
        return new ResponseEntity<>("Job added Successfully",HttpStatus.CREATED);
}

@GetMapping("/{id}")

    public ResponseEntity<JobDTO> getJobById(@PathVariable Long id){
    JobDTO jobDTO = jobService.getJobById(id);
        if (jobDTO != null){
            return new ResponseEntity<>(jobDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND) ;
}
@DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id){
      boolean deleted =  jobService.deleteJobById(id);
       if (deleted ){
           return new ResponseEntity<>("Job deleted successfully", HttpStatus.OK);
       }
       return new ResponseEntity<>(HttpStatus.NOT_FOUND);

}
@PutMapping("/{id}")
public ResponseEntity<String> updateJob(@PathVariable Long id , @RequestBody Job updatedJob){
        boolean updated = jobService.updateJobById(id,updatedJob);
        if(updated){
            return new ResponseEntity<>("Job updated successfully",HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
}

}
