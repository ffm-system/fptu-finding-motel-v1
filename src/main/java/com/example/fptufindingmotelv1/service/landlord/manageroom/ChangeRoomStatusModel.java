package com.example.fptufindingmotelv1.service.landlord.manageroom;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.model.*;
import com.example.fptufindingmotelv1.repository.NotificationRepository;
import com.example.fptufindingmotelv1.repository.RentalRequestRepository;
import com.example.fptufindingmotelv1.repository.RoomRepository;
import com.example.fptufindingmotelv1.untils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class ChangeRoomStatusModel implements ChangeRoomStatusService {

    @Autowired
    private RentalRequestRepository rentalRequestRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public boolean changeRoomStatus(RentalRequestDTO rentalRequestDTO) {
        if(rentalRequestDTO.getStatusId() == Constant.STATUS_ROOM_FREE){
            List<RentalRequestModel> listRequestOfRoom =
                    rentalRequestRepository.getListRequestIdByRoom(rentalRequestDTO.getRoomId(),
                            Constant.STATUS_REQUEST_PROCESSING);
            roomRepository.updateStatusRoom(rentalRequestDTO.getRoomId(), Constant.STATUS_ROOM_BE_RENTED);
            if(listRequestOfRoom != null && listRequestOfRoom.size() > 0){
                for (RentalRequestModel request:
                        listRequestOfRoom) {
                    String notificationContent = "Chủ trọ <b>" + rentalRequestDTO.getLandlordUsername() +
                            "</b> đã từ chối yêu cầu thuê trọ vào <b>" + rentalRequestDTO.getRoomName() +
                            "</b> - <b>" + rentalRequestDTO.getPostTitle() + "</b>";
                    // send notification to Renter
                    sendNotification(request, notificationContent);
                }
                rentalRequestRepository.updateStatus(null, Constant.STATUS_REQUEST_REJECTED,
                        rentalRequestDTO.getRoomId(), Constant.STATUS_REQUEST_PROCESSING);
            }
        }else if(rentalRequestDTO.getStatusId() == Constant.STATUS_ROOM_BE_RENTED){
            roomRepository.updateStatusRoom(rentalRequestDTO.getRoomId(), Constant.STATUS_ROOM_FREE);

            List<RentalRequestModel> requestAccepted = rentalRequestRepository.getListRequestIdByRoom(
                    rentalRequestDTO.getRoomId(), Constant.STATUS_REQUEST_ACCEPTED);
            if(requestAccepted != null && requestAccepted.size() > 0){
                rentalRequestRepository.updateExpireStatus(null, rentalRequestDTO.getExpireMessage(),
                        Constant.STATUS_REQUEST_ENDED, rentalRequestDTO.getRoomId(),
                        Constant.STATUS_REQUEST_ACCEPTED);
                String notificationContent = "Chủ trọ <b>" + rentalRequestDTO.getLandlordUsername() +
                        "</b> đã kết thúc cho thuê phòng tại <b>" + rentalRequestDTO.getRoomName() +
                        "</b> - <b>" + rentalRequestDTO.getPostTitle() + "</b>";
                // send notification to Renter
                sendNotification(requestAccepted.get(0), notificationContent);
            }
        }
        return true;
    }

    private NotificationModel sendNotification(RentalRequestModel requestModel, String content){
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
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
