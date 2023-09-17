package com.kas.demo_auth.service;

import org.springframework.stereotype.Service;

import com.kas.demo_auth.model.GeneralInformation;
import com.kas.demo_auth.repository.GeneralInformationRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class GeneralInformationServiceImpl implements GeneralInformationService {
    private final GeneralInformationRepository generalInformationRepository;

    @Override
    public GeneralInformation saveGeneralInformation(GeneralInformation generalInformation) {
        if (generalInformation.getCloseTime().isBefore(generalInformation.getOpenTime()))
                    throw new IllegalArgumentException("Thời gian kết thúc phải sau thời gian bắt đầu!");
        generalInformation.setId(1L);
        return generalInformationRepository.save(generalInformation);
    }

    @Override
    public GeneralInformation getGeneralInformation() {
        return generalInformationRepository.findById(1L).orElse(new GeneralInformation());
    }

    
}
