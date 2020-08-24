package com.example.fptufindingmotelv1.service.admin.managepaymentpackage;

import com.example.fptufindingmotelv1.model.PaymentPackageModel;
import com.example.fptufindingmotelv1.repository.PaymentPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViewListPaymentPackageServiceImpl implements ViewListPaymentPackageService {
    @Autowired
    PaymentPackageRepository paymentPackageRepository;

    @Override
    public List<PaymentPackageModel> getListPaymentPackage(Boolean available) {
        try {
            return paymentPackageRepository.getListPaymentPackage(available);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
