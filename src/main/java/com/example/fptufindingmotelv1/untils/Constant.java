package com.example.fptufindingmotelv1.untils;

import net.minidev.json.JSONObject;
import org.springframework.data.domain.Page;

public class Constant {
    public static final int BUTTONS_TO_SHOW = 9;
    public static final int INITIAL_PAGE = 0;
    public static final int INITIAL_PAGE_SIZE = 9;
    //public static final int[] PAGE_SIZES = {3, 6};
    public static final String DATE_FORMAT = "HH:mm:ss dd/MM/yyyy";
    public static final int NUMBER_OF_BAN_DATE_POST = 3;
    public static final int NUMBER_OF_BAN_DATE_USER = 10;
    public static final String DATE_FORMAT_ONLY_DATE = "dd/MM/yyyy";
    public static final Integer EXPIRE_MIN = 1;
    public static final Integer OTP_LENGTH = 6;

    public static JSONObject responseMsg(String code, String message, Object data) {
        JSONObject msg = new JSONObject();
        msg.put("code", code);
        msg.put("message", message);
        msg.put("data", data);
        return msg;
    }

    public static JSONObject paginationModel(Page page) {
        JSONObject msg = new JSONObject();
        msg.put("totalPages", page.getTotalPages());
        msg.put("sizePage", page.getSize());
        msg.put("currentPage", page.getNumber());
        msg.put("totalItems", page.getTotalElements());
        msg.put("hasNext", page.hasNext());
        msg.put("hasPrevious", page.hasPrevious());
        return msg;
    }
}
