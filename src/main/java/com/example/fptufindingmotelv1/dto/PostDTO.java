package com.example.fptufindingmotelv1.dto;

import com.example.fptufindingmotelv1.model.ImageModel;
import com.example.fptufindingmotelv1.model.PaymentPostModel;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.untils.Constant;
import lombok.Data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Data
public class PostDTO {
    private String id;
    private String type;
    private String landlord;
    private String landlordDisplayName;
    private String phoneNumber;
    private double price;
    private double distance;
    private double square;
    private int roomNUmber;
    private String createDate;
    private String description;
    private Date expireDate;
    private boolean isVisible;
    private String title;
    private String color;
    private String isLord;
    private List<String> images;
    private List<RoomDTO> listRoom;

    public PostDTO(PostModel postModel) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        this.id = postModel.getId();
        this.type = postModel.getType().getName();
        this.landlord = postModel.getLandlord().getUsername();
        this.landlordDisplayName = postModel.getLandlord().getDisplayName();
        this.phoneNumber = postModel.getLandlord().getPhoneNumber();
        this.price = postModel.getPrice();
        this.distance = postModel.getDistance();
        this.square = postModel.getSquare();
        this.roomNUmber = postModel.getRoomNumber();
        this.createDate = sdf.format(postModel.getCreateDate());
        this.description = postModel.getDescription();
        this.expireDate = postModel.getExpireDate();
        this.isVisible = postModel.isVisible();
        this.title = postModel.getTitle();
        this.images = new ArrayList<>();
        for (ImageModel image:
                postModel.getImages()) {
            String imageUrl = "data:image/"+ image.getFileType()+";base64,"
                    + Base64.getEncoder().encodeToString(image.getFileContent());
            images.add(imageUrl);
        }
        this.listRoom = new ArrayList<>();
        for (int i = 0; i < postModel.getRooms().size(); i++) {
            listRoom.add(new RoomDTO(i + 1, postModel.getRooms().get(i)));
        }

    }

}
