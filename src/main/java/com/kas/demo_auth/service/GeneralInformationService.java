package com.kas.demo_auth.service;

import com.kas.demo_auth.model.GeneralInformation;

public interface GeneralInformationService {
    GeneralInformation saveGeneralInformation(GeneralInformation generalInformation);
    GeneralInformation getGeneralInformation();
}
