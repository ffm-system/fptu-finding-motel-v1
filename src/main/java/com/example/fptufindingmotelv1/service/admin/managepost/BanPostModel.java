package com.example.fptufindingmotelv1.service.admin.managepost;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.model.*;
import com.example.fptufindingmotelv1.repository.*;
import com.example.fptufindingmotelv1.untils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class BanPostModel implements BanPostService{
    @Autowired
    PostRepository postRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    RentalRequestRepository rentalRequestRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    ReportRepository reportRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public boolean banPost(RentalRequestDTO rentalRequestDTO) {

        String notificationContent;
        boolean isExistProcessingRequest = false;
        boolean isExistAcceptedRequest = false;

        List<RentalRequestModel> listRequestOfPost =
                rentalRequestRepository.getListRequestIdByPost(rentalRequestDTO.getPostId(), 7L, 9L);

        for (RentalRequestModel request :
                listRequestOfPost) {
            if (request.getRentalStatus().getId() == Constant.STATUS_REQUEST_PROCESSING) {
                isExistProcessingRequest = true;
                notificationContent = "Yêu cầu thuê trọ vào <b>" + request.getRentalRoom().getName() +
                        "</b> - <b>" + rentalRequestDTO.getPostTitle() + "</b> đã bị từ chối do bài đăng đã bị khóa";
                // send notification to Renter
                sendNotification(request, notificationContent);
            } else if (request.getRentalStatus().getId() == Constant.STATUS_REQUEST_ACCEPTED) {
                isExistAcceptedRequest = true;

                notificationContent = "Yêu cầu thuê trọ vào <b>" + request.getRentalRoom().getName() +
                        "</b> - <b>" + rentalRequestDTO.getPostTitle() + "</b> đã kết thúc do bài đăng đã bị khóa";
                // send notification to Renter
                sendNotification(request, notificationContent);
            }
        }
        if(isExistProcessingRequest || isExistAcceptedRequest){
            rentalRequestRepository.updateStatusByPost(rentalRequestDTO.getPostId(),
                    Constant.STATUS_REQUEST_REJECTED, Constant.STATUS_REQUEST_ENDED,
                    Constant.STATUS_REQUEST_PROCESSING, Constant.STATUS_REQUEST_ACCEPTED);
        }
        roomRepository.updateStatusRoomByPost(rentalRequestDTO.getPostId(), Constant.STATUS_ROOM_FREE, Constant.STATUS_ROOM_BE_RENTED);

        reportRepository.updateStatusReportByPost(rentalRequestDTO.getPostId(),
                Constant.STATUS_REPORT_PROCESSED_POST, Constant.STATUS_REPORT_PROCESSED_ALL,
                Constant.STATUS_REPORT_PROCESSING, Constant.STATUS_REPORT_PROCESSED_USER);

        postRepository.updateBannedPost(true, rentalRequestDTO.getPostId());

        return true;
    }

    private NotificationModel sendNotification(RentalRequestModel requestModel, String content) {
        try {
            // send notification to Renter
            NotificationModel notificationModel = new NotificationModel();
            UserModel renterModel = new UserModel(requestModel.getRentalRenter().getUsername());

            StatusModel statusNotification = new StatusModel(Constant.STATUS_NOTIFICATION_NOT_SEEN);

            Date date = new Date();
            Date createdDate = new Timestamp(date.getTime());

            notificationModel.setUserNotification(renterModel);
            notificationModel.setContent(content);
            notificationModel.setStatusNotification(statusNotification);
            notificationModel.setCreatedDate(createdDate);
            notificationModel.setRentalRequestNotification(requestModel);
            return notificationRepository.save(notificationModel);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
