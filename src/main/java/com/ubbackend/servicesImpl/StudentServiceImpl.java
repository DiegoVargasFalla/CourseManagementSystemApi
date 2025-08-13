package com.ubbackend.servicesImpl;

import com.ubbackend.services.StudentService;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    @Override
    public String proof() {
        return "This is a proof";
    }
}
