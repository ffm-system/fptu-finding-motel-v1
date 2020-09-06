package com.example.fptufindingmotelv1.service.renter.report;

import com.example.fptufindingmotelv1.dto.ReportRequestDTO;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.RenterModel;
import com.example.fptufindingmotelv1.model.ReportModel;
import com.example.fptufindingmotelv1.model.StatusModel;
import com.example.fptufindingmotelv1.repository.PostRepository;
import com.example.fptufindingmotelv1.repository.RenterRepository;
import com.example.fptufindingmotelv1.repository.ReportRepository;
import com.example.fptufindingmotelv1.untils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportPostModel implements ReportPostService {

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    RenterRepository renterRepository;

    @Autowired
    PostRepository postRepository;

    @Override
    public void sendReport(ReportRequestDTO reportRequestDTO) {
        try {
            ReportModel reportModel = null;
            PostModel postModel = new PostModel(reportRequestDTO.getPostId());
            RenterModel renterModel = new RenterModel(reportRequestDTO.getRenterId());
            StatusModel statusModel = new StatusModel(Constant.STATUS_REPORT_PROCESSING);
            reportModel = new ReportModel(renterModel, postModel, statusModel, reportRequestDTO);
            reportRepository.save(reportModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
